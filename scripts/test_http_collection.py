import importlib.util
import json
import unittest
from pathlib import Path

loader = importlib.util.spec_from_file_location('http_collection', Path(__file__).with_name('gen-http-collection.py'))
gen = importlib.util.module_from_spec(loader)
loader.loader.exec_module(gen)


def fixture():
    return {
        'components': {'examples': {'continue': {'summary': '还有下一页', 'value': {'items': [], 'nextCursor': 'opaque-next'}}}},
        'paths': {
            '/openapi/v1/event/list': {'get': {
                'operationId': 'eventList', 'summary': '事件续读',
                'parameters': [
                    {'name': 'deviceId', 'in': 'query', 'required': True, 'schema': {'type': 'string', 'pattern': '^[0-9]{20}$'}},
                    {'name': 'cursor', 'in': 'query', 'schema': {'type': 'string'}},
                    {'name': 'limit', 'in': 'query', 'schema': {'type': 'integer', 'default': 50}}
                ],
                'responses': {'200': {'description': '已读取', 'content': {'application/json': {
                    'schema': {'type': 'object'}, 'examples': {
                        'continue': {'$ref': '#/components/examples/continue'},
                        'complete': {'summary': '末页', 'value': {'items': [], 'nextCursor': ''}}
                    }
                }}}}
            }},
            '/openapi/v1/ptz/control': {'post': {
                'operationId': 'movePTZ', 'summary': '云台控制', 'x-uvp-idempotency-mode': 'required',
                'requestBody': {'required': True, 'content': {'application/json': {
                    'schema': {'type': 'object', 'additionalProperties': False,
                        'required': ['deviceId', 'channelId', 'action', 'speed'],
                        'properties': {'deviceId': {'type': 'string'}, 'channelId': {'type': 'string'}, 'action': {'type': 'string', 'enum': ['up', 'stop']}, 'speed': {'type': 'integer'}}},
                    'examples': {
                        'stop': {'summary': '停止', 'value': {'deviceId': '34020000002000000001', 'channelId': '34020000001320000001', 'action': 'stop', 'speed': 0}},
                        'up': {'summary': '向上', 'value': {'deviceId': '34020000002000000001', 'channelId': '34020000001320000001', 'action': 'up', 'speed': 30}}
                    }
                }}},
                'responses': {'202': {'description': '已受理', 'content': {'application/json': {'examples': {'accepted': {'value': {'accepted': False, 'count': 0, 'observed': None}}}}}}}
            }}
        }
    }


class HttpCollectionTest(unittest.TestCase):
    def test_yaml_preserves_action_names_and_date_strings(self):
        import yaml
        value = yaml.load('action: off\nenabled: false\nat: 2026-11-09T00:00:00Z\n', Loader=gen.OpenAPILoader)
        self.assertEqual('off', value['action'])
        self.assertIs(False, value['enabled'])
        self.assertEqual('2026-11-09T00:00:00Z', value['at'])

    def test_required_targets_and_optional_cursor_are_editable_query(self):
        spec = fixture()
        query = gen.postman_query(spec, spec['paths']['/openapi/v1/event/list']['get'])
        by_name = {p['key']: p for p in query}
        self.assertEqual('{{deviceId}}', by_name['deviceId']['value'])
        self.assertFalse(by_name['deviceId'].get('disabled', False))
        self.assertTrue(by_name['cursor']['disabled'])
        self.assertEqual('50', by_name['limit']['value'])
        url = gen.postman_url('/openapi/v1/event/list', query)
        self.assertNotIn('cursor=', url['raw'])

    def test_request_uses_real_first_named_example_and_documents_all(self):
        spec = fixture()
        op = spec['paths']['/openapi/v1/ptz/control']['post']
        body = json.loads(gen.request_body_text(spec, op))
        self.assertEqual('stop', body['action'])
        self.assertEqual(0, body['speed'])
        self.assertEqual('{{deviceId}}', body['deviceId'])
        text = gen.describe_operation(spec, op, '/openapi/v1/ptz/control', 'POST')
        self.assertIn('停止', text)
        self.assertIn('向上', text)

    def test_success_examples_preserve_cursor_terminal_values_and_refs(self):
        spec = fixture()
        postman = gen.build_postman(spec, gen.collect_operations(spec))
        items = [item for group in postman['item'] for item in group['item']]
        event = next(item for item in items if item['request']['method'] == 'GET')
        self.assertEqual(2, len(event['response']))
        self.assertEqual(['opaque-next', ''], [json.loads(r['body'])['nextCursor'] for r in event['response']])
        control = next(item for item in items if item['request']['method'] == 'POST')
        self.assertEqual(202, control['response'][0]['code'])
        self.assertEqual({'accepted': False, 'count': 0, 'observed': None}, json.loads(control['response'][0]['body']))
        apifox = gen.build_apifox(spec, gen.collect_operations(spec))
        event_api = next(item['api'] for group in apifox['apiCollection'] for item in group['items'] if item['api']['operationId'] == 'eventList')
        self.assertEqual(2, len(event_api['responseExamples']))
        self.assertEqual(200, event_api['responses'][0]['code'])
        self.assertEqual(gen.PATH_DEFAULTS['deviceId'], next(parameter['example'] for parameter in event_api['parameters']['query'] if parameter['name'] == 'deviceId'))
        control_api = next(item['api'] for group in apifox['apiCollection'] for item in group['items'] if item['api']['operationId'] == 'movePTZ')
        self.assertEqual(gen.PATH_DEFAULTS['channelId'], json.loads(control_api['requestBody']['example'])['channelId'])

    def test_literal_success_values_do_not_fall_back_to_schema(self):
        spec = fixture()
        for value in [False, 0, None]:
            self.assertEqual(value, gen.media_examples(spec, {'example': value, 'schema': {'type': 'string'}})[0]['value'])

    def test_current_contract_has_all_106_request_and_success_examples(self):
        spec, _ = gen.load_spec()
        operations = gen.collect_operations(spec)
        self.assertEqual(106, len(operations))
        postman = gen.build_postman(spec, operations)
        items = {item['id']: item for group in postman['item'] for item in group['item']}
        apifox = gen.build_apifox(spec, operations)
        apis = {item['api']['operationId']: item['api'] for group in apifox['apiCollection'] for item in group['items']}
        self.assertEqual(106, len(items))
        self.assertEqual(106, len(apis))
        for operation in operations:
            op = operation['op']
            item = items[op['operationId']]
            self.assertIn(operation['method'], ('GET', 'POST'))
            self.assertNotIn('{', operation['path'])
            if operation['method'] == 'GET':
                self.assertNotIn('body', item['request'])
            else:
                body = json.loads(item['request']['body']['raw'])
                schema = gen.resolve(spec, gen.resolve(spec, op['requestBody'])['content']['application/json']['schema'])
                for name in schema.get('required', []):
                    self.assertIn(name, body, op['operationId'] + ': ' + name)
            expected = gen.success_examples(spec, op)
            self.assertTrue(expected, op['operationId'])
            self.assertEqual([example['value'] for example in expected], [json.loads(example['body']) for example in item['response']], op['operationId'])
            self.assertEqual([example['value'] for example in expected], [json.loads(example['data']) for example in apis[op['operationId']]['responseExamples']], op['operationId'])


if __name__ == '__main__':
    unittest.main()
