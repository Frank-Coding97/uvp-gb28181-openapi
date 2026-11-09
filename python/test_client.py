"""以本地 HTTP 服务验证当前动作接口的实际请求与 HMAC，不访问真实平台。"""
import base64
import contextlib
import hashlib
import hmac
import io
import json
import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from threading import Thread
import unittest
from unittest.mock import patch
from urllib.parse import parse_qsl, quote, urlencode, urlsplit

import demo_devices
from uvp_openapi import UvpClient

DEVICE = "34020000002000000001"
CHANNEL = "34020000001320000001"
KEY = bytes(range(32))
AK = "uvp_000102030405060708090a0b0c0d0e0f"
SK = base64.urlsafe_b64encode(KEY).decode().rstrip("=")
AUDIENCE = "uvp-sdk-test"


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.respond()

    def do_POST(self):
        self.respond()

    def respond(self):
        raw = self.rfile.read(int(self.headers.get("Content-Length", "0")))
        self.server.records.append((self.command, self.path, dict(self.headers), raw))
        path = urlsplit(self.path).path
        status = 200
        if path == "/openapi/v1/device/list":
            data = {"items": [{"deviceId": DEVICE, "name": "园区设备", "channelCount": 2}],
                    "page": 1, "pageSize": 20, "total": 1}
        elif path == "/openapi/v1/channel/list":
            data = {"items": [{"channelId": CHANNEL, "audioEnabled": False,
                               "longitude": 0, "lastOnlineAt": None}], "total": 1}
        elif path in ("/openapi/v1/channel/update", "/openapi/v1/record/search"):
            data = json.loads(raw)
        else:
            status, data = 404, None
        payload = json.dumps({"code": "OK" if status == 200 else "RESOURCE_NOT_FOUND",
                              "message": "success", "requestId": "req_test", "data": data}).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(payload)))
        self.end_headers()
        self.wfile.write(payload)

    def log_message(self, *args):
        pass


class ActionClientTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.server = ThreadingHTTPServer(("127.0.0.1", 0), Handler)
        cls.server.records = []
        cls.thread = Thread(target=cls.server.serve_forever, daemon=True)
        cls.thread.start()
        cls.base = "http://127.0.0.1:%d" % cls.server.server_port

    @classmethod
    def tearDownClass(cls):
        cls.server.shutdown()
        cls.server.server_close()
        cls.thread.join()

    def setUp(self):
        self.server.records.clear()
        self.client = UvpClient(self.base, AK, SK, AUDIENCE)

    def assert_signed(self):
        method, target, headers, body = self.server.records[-1]
        parts = urlsplit(target)
        query = urlencode(sorted(parse_qsl(parts.query, keep_blank_values=True)),
                          quote_via=quote, safe="")
        canonical = "\n".join([
            "UVP-HMAC-SHA256/1", AK, headers["X-UVP-Timestamp"], headers["X-UVP-Nonce"],
            method, parts.path, query, headers.get("Content-Type", ""),
            hashlib.sha256(body).hexdigest(), headers.get("Idempotency-Key", ""), AUDIENCE,
        ])
        self.assertEqual(headers["X-UVP-Signature"],
                         hmac.new(KEY, canonical.encode(), hashlib.sha256).hexdigest())
        return method, target, headers, body

    def test_get_filters_targets_and_rich_response(self):
        data = self.client.get("/openapi/v1/channel/list", query={
            "deviceId": DEVICE, "keyword": "东门 A+1", "audioEnabled": False,
            "ptzType": 0, "manufacturer": None,
        })
        method, target, headers, body = self.assert_signed()
        query = dict(parse_qsl(urlsplit(target).query))
        self.assertEqual(query["audioEnabled"], "false")
        self.assertEqual(query["ptzType"], "0")
        self.assertEqual(query["deviceId"], DEVICE)
        self.assertIn("%20A%2B1", target)
        self.assertNotIn("manufacturer", query)
        self.assertNotIn("Content-Type", headers)
        self.assertEqual(body, b"")
        self.assertIsNone(data["data"]["items"][0]["lastOnlineAt"])
        self.assertIs(data["data"]["items"][0]["audioEnabled"], False)

    def test_post_mapping_preserves_false_zero_and_unicode(self):
        value = {"deviceId": DEVICE, "channelId": CHANNEL, "alias": "东门",
                 "audioEnabled": False, "longitude": 0, "latitude": 0}
        response = self.client.post("/openapi/v1/channel/update", value)
        method, target, headers, body = self.assert_signed()
        self.assertEqual(target, "/openapi/v1/channel/update")
        self.assertEqual(method, "POST")
        self.assertEqual(headers["Content-Type"], "application/json")
        self.assertEqual(json.loads(body), value)
        self.assertIn("东门".encode(), body)
        self.assertEqual(response["data"], value)

    def test_raw_body_is_signed_and_sent_byte_for_byte(self):
        body = ('{ "deviceId" : "%s", "channelId" : "%s", "audioEnabled":false }'
                % (DEVICE, CHANNEL)).encode()
        self.client.post("/openapi/v1/channel/update", body, idempotency_key="update-0001")
        _, _, headers, sent = self.assert_signed()
        self.assertEqual(sent, body)
        self.assertEqual(headers["Idempotency-Key"], "update-0001")

    def test_record_search_continuation_only_sends_cursor_and_targets(self):
        body = {"deviceId": DEVICE, "channelId": CHANNEL, "cursor": "opaque_cursor_2"}
        self.client.post("/openapi/v1/record/search", body)
        self.assertEqual(json.loads(self.assert_signed()[3]), body)
        self.assertNotIn("startTime", json.loads(self.server.records[-1][3]))

    def test_device_demo_uses_current_device_and_channel_queries(self):
        env = {"UVP_BASE_URL": self.base, "UVP_ACCESS_KEY": AK,
               "UVP_SECRET_KEY": SK, "UVP_AUDIENCE": AUDIENCE}
        with patch.dict(os.environ, env), contextlib.redirect_stdout(io.StringIO()):
            self.assertEqual(demo_devices.main(), 0)
        self.assertEqual([urlsplit(r[1]).path for r in self.server.records],
                         ["/openapi/v1/device/list", "/openapi/v1/channel/list"])
        self.assertEqual(dict(parse_qsl(urlsplit(self.server.records[1][1]).query))["deviceId"], DEVICE)


if __name__ == "__main__":
    unittest.main()
