package com.uvp.openapi;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.uvp.openapi.client.UvpClient;
import com.uvp.openapi.client.UvpClientConfig;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;
import java.util.regex.Pattern;

/** Verifies every generated method against the independent, authoritative YAML contract. */
class ContractSurfaceTest {
    @SuppressWarnings("unchecked")
    private static Map<String, Object> map(Object value) {
        return value instanceof Map ? (Map<String, Object>) value : Collections.emptyMap();
    }

    private static Map<String, Object> contract() throws Exception {
        Path current = Paths.get("").toAbsolutePath();
        while (current != null && !Files.exists(current.resolve("contract/openapi-v1.yaml"))) {
            current = current.getParent();
        }
        assertNotNull(current, "find contract/openapi-v1.yaml from Maven module");
        try (InputStream stream = Files.newInputStream(current.resolve("contract/openapi-v1.yaml"))) {
            DumperOptions options = new DumperOptions();
            LoaderOptions loader = new LoaderOptions();
            loader.setMaxAliasesForCollections(10000);
            Resolver resolver = new Resolver() {
                @Override protected void addImplicitResolvers() {
                    addImplicitResolver(Tag.BOOL, Pattern.compile("^(?:true|false)$"), "tf");
                    addImplicitResolver(Tag.INT, Resolver.INT, "-+0123456789");
                    addImplicitResolver(Tag.FLOAT, Resolver.FLOAT, "-+0123456789.");
                    addImplicitResolver(Tag.NULL, Resolver.NULL, "~nN\0");
                }
            };
            return new Yaml(new Constructor(loader), new Representer(options), options, loader, resolver).load(stream);
        }
    }

    private static Map<String, Object> resolve(Map<String, Object> document, Object raw) {
        Map<String, Object> schema = map(raw);
        if (schema.containsKey("$ref")) {
            Object result = document;
            for (String part : schema.get("$ref").toString().substring(2).split("/")) {
                result = map(result).get(part);
            }
            return resolve(document, result);
        }
        return schema;
    }

    private static Object sample(Map<String, Object> document, Object raw, boolean nullables) {
        Map<String, Object> schema = resolve(document, raw);
        if (nullables && Boolean.TRUE.equals(schema.get("nullable"))) return null;
        if (schema.containsKey("example")) return schema.get("example");
        if (schema.containsKey("enum")) return ((List<?>) schema.get("enum")).get(0);
        String type = String.valueOf(schema.get("type"));
        if ("object".equals(type) || schema.containsKey("properties")) {
            Map<String, Object> result = new LinkedHashMap<>();
            for (Map.Entry<String, Object> property : map(schema.get("properties")).entrySet()) {
                result.put(property.getKey(), sample(document, property.getValue(), nullables));
            }
            if (result.isEmpty() && schema.get("additionalProperties") instanceof Map) {
                result.put("protocol", sample(document, schema.get("additionalProperties"), nullables));
            }
            return result;
        }
        if ("array".equals(type)) return Collections.singletonList(sample(document, schema.get("items"), nullables));
        if ("boolean".equals(type)) return false;
        if ("integer".equals(type)) return schema.getOrDefault("minimum", 0);
        if ("number".equals(type)) return schema.getOrDefault("minimum", 0.5);
        if ("uuid".equals(schema.get("format"))) return "a7e5bf43-560c-455d-a8ef-38dbd628c6f4";
        if ("date-time".equals(schema.get("format"))) return "2026-11-09T12:00:00Z";
        if ("date".equals(schema.get("format"))) return "2026-11-09";
        return "测试 value";
    }

    private static Field property(Class<?> type, String name) {
        for (Field field : type.getDeclaredFields()) {
            SerializedName serialized = field.getAnnotation(SerializedName.class);
            if (serialized != null && name.equals(serialized.value())) return field;
        }
        throw new AssertionError(type.getName() + " missing typed contract field " + name);
    }

    private static void assertModel(Map<String, Object> document, Object raw, Type javaType) {
        Map<String, Object> schema = resolve(document, raw);
        Class<?> type = javaType instanceof Class ? (Class<?>) javaType
                : (Class<?>) ((ParameterizedType) javaType).getRawType();
        assertNotEquals(Object.class, type, "contract shapes must retain their types");
        if (Boolean.TRUE.equals(schema.get("nullable"))) assertFalse(type.isPrimitive(), "nullable must be boxed");
        if ("array".equals(schema.get("type"))) {
            assertTrue(javaType instanceof ParameterizedType, "array items must be typed");
            assertModel(document, schema.get("items"), ((ParameterizedType) javaType).getActualTypeArguments()[0]);
        }
        for (Map.Entry<String, Object> entry : map(schema.get("properties")).entrySet()) {
            assertModel(document, entry.getValue(), property(type, entry.getKey()).getGenericType());
        }
        if (schema.get("additionalProperties") instanceof Map) {
            assertTrue(javaType instanceof ParameterizedType, "dynamic maps must be typed: " + javaType);
            assertModel(document, schema.get("additionalProperties"), ((ParameterizedType) javaType).getActualTypeArguments()[1]);
        }
    }

    private static void assertValues(JsonElement expected, JsonElement actual, String path) {
        if (expected == null || expected.isJsonNull()) {
            assertTrue(actual == null || actual.isJsonNull(), path + " retains null");
        } else if (expected.isJsonObject()) {
            assertNotNull(actual, path);
            assertTrue(actual.isJsonObject(), path);
            for (Map.Entry<String, JsonElement> entry : expected.getAsJsonObject().entrySet()) {
                assertValues(entry.getValue(), actual.getAsJsonObject().get(entry.getKey()), path + "." + entry.getKey());
            }
        } else if (expected.isJsonArray()) {
            assertTrue(actual.isJsonArray(), path);
            assertEquals(expected.getAsJsonArray().size(), actual.getAsJsonArray().size(), path);
            for (int i = 0; i < expected.getAsJsonArray().size(); i++) {
                assertValues(expected.getAsJsonArray().get(i), actual.getAsJsonArray().get(i), path + "[" + i + "]");
            }
        } else {
            assertEquals(expected, actual, path);
        }
    }

    @Test
    void all106MethodsSendContractRequestsAndDecodeEveryResponseField() throws Exception {
        Map<String, Object> document = contract();
        try (MockWebServer server = new MockWebServer()) {
            server.start();
            String secret = Base64.getUrlEncoder().withoutPadding().encodeToString(new byte[32]);
            try (UvpClient client = new UvpClient(UvpClientConfig.of(server.url("/").toString(),
                    "uvp_000102030405060708090a0b0c0d0e0f", secret, "contract-test"))) {
                Map<String, Method> methods = new HashMap<>();
                Map<String, Object> apis = new HashMap<>();
                Object[] groups = {client.devices(), client.live(), client.playback(), client.ptz(),
                        client.talk(), client.alarms(), client.configs(), client.control()};
                for (Object api : groups) {
                    for (Method method : api.getClass().getDeclaredMethods()) {
                        Method prior = methods.get(method.getName());
                        if (prior == null || prior.getParameterCount() < method.getParameterCount()) {
                            methods.put(method.getName(), method);
                            apis.put(method.getName(), api);
                        }
                    }
                }
                int count = 0;
                for (Map.Entry<String, Object> path : map(document.get("paths")).entrySet()) {
                    for (Map.Entry<String, Object> verb : map(path.getValue()).entrySet()) {
                        if (!Arrays.asList("get", "post").contains(verb.getKey())) continue;
                        Map<String, Object> operation = map(verb.getValue());
                        String id = operation.get("operationId").toString();
                        Method method = methods.get(id);
                        assertNotNull(method, "missing SDK method " + id);
                        Object responseSchema = map(map(map(map(operation.get("responses")).get("200"))
                                .get("content")).get("application/json")).get("schema");
                        assertModel(document, responseSchema, method.getGenericReturnType());
                        JsonElement response = UvpJson.getGson().toJsonTree(sample(document, responseSchema, true));
                        response.getAsJsonObject().addProperty("code", "OK");
                        response.getAsJsonObject().addProperty("message", "success");
                        response.getAsJsonObject().addProperty("requestId", id);
                        server.enqueue(new MockResponse().setHeader("Content-Type", "application/json")
                                .setBody(response.toString()));

                        List<Object> args = new ArrayList<>();
                        Map<String, Object> query = new LinkedHashMap<>();
                        for (Object raw : (List<?>) operation.getOrDefault("parameters", Collections.emptyList())) {
                            Map<String, Object> parameter = resolve(document, raw);
                            if (!"query".equals(parameter.get("in"))) continue;
                            Object value = sample(document, parameter.get("schema"), false);
                            query.put(parameter.get("name").toString(), value);
                            args.add(UvpJson.getGson().fromJson(UvpJson.getGson().toJsonTree(value),
                                    method.getGenericParameterTypes()[args.size()]));
                        }
                        JsonElement expectedBody = null;
                        if (operation.containsKey("requestBody")) {
                            Object schema = map(map(map(operation.get("requestBody")).get("content"))
                                    .get("application/json")).get("schema");
                            assertModel(document, schema, method.getGenericParameterTypes()[args.size()]);
                            expectedBody = UvpJson.getGson().toJsonTree(sample(document, schema, false));
                            args.add(UvpJson.getGson().fromJson(expectedBody,
                                    method.getGenericParameterTypes()[args.size()]));
                        }
                        assertEquals(args.size(), method.getParameterCount(), id);
                        Object result = method.invoke(apis.get(id), args.toArray());
                        RecordedRequest request = server.takeRequest(2, TimeUnit.SECONDS);
                        assertNotNull(request, id);
                        assertEquals(verb.getKey().toUpperCase(Locale.ROOT), request.getMethod(), id);
                        assertEquals(path.getKey(), request.getRequestUrl().encodedPath(), id);
                        assertEquals(query.keySet(), request.getRequestUrl().queryParameterNames(), id);
                        for (Map.Entry<String, Object> parameter : query.entrySet()) {
                            assertEquals(String.valueOf(parameter.getValue()),
                                    request.getRequestUrl().queryParameter(parameter.getKey()), id);
                        }
                        if (expectedBody != null) {
                            assertEquals("application/json", request.getHeader("Content-Type"), id);
                            assertValues(expectedBody, com.google.gson.JsonParser.parseString(request.getBody().readUtf8()), id);
                        } else {
                            assertEquals(0, request.getBodySize(), id);
                        }
                        if ("required".equals(operation.get("x-uvp-idempotency-mode"))) {
                            assertNotNull(request.getHeader("Idempotency-Key"), id);
                        }
                        assertValues(response, UvpJson.getGson().toJsonTree(result), id);
                        count++;
                    }
                }
                assertEquals(106, count);
                assertEquals(106, methods.size(), "no retired API method remains");
            }
        }
    }
}
