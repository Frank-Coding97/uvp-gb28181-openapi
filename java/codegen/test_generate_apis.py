"""Contract checks for the SDK generator (run with python -m unittest discover)."""
import unittest
import contextlib
import io
import os
import tempfile
from unittest.mock import patch
import contract_yaml

import generate_apis as generator


class GeneratorContractTest(unittest.TestCase):
    def test_all_operations_use_short_action_routes_and_typed_payloads(self):
        operations = generator.load_operations()
        self.assertEqual(106, len(operations))
        self.assertEqual(106, len({operation['id'] for operation in operations}))
        for operation in operations:
            with self.subTest(operation=operation['id']):
                self.assertIn(operation['method'], ('GET', 'POST'))
                self.assertNotIn('{', operation['path'])
                self.assertNotEqual('Object', operation['response_type'])
                self.assertNotEqual('Object', operation['body_type'])
                self.assertTrue(all(parameter['in'] == 'query' for parameter in operation['params']))
                if operation['method'] == 'POST':
                    self.assertFalse(operation['params'])
                    self.assertTrue(operation['body_required'])
                    self.assertIsNotNone(operation['body_type'])

    def test_action_routes_stay_in_their_business_domain(self):
        expected = {
            '/openapi/v1/record/start': 'ControlApi',
            '/openapi/v1/record/search': 'PlaybackApi',
            '/openapi/v1/storage/card/list': 'ControlApi',
            '/openapi/v1/config/info': 'ConfigApi',
            '/openapi/v1/firmware/info': 'ConfigApi',
            '/openapi/v1/subscription/info': 'AlarmApi',
            '/openapi/v1/event/list': 'AlarmApi',
            '/openapi/v1/position/latest': 'AlarmApi',
            '/openapi/v1/alarm/guard/set': 'ControlApi',
            '/openapi/v1/alarm/info': 'AlarmApi',
            '/openapi/v1/cloud/record/list': 'PlaybackApi',
            '/openapi/v1/snapshot/start': 'DeviceApi',
        }
        for path, domain in expected.items():
            with self.subTest(path=path):
                self.assertEqual(domain, generator.domain_of(path))

    def test_check_detects_outdated_generated_api_without_writing(self):
        with tempfile.TemporaryDirectory() as output, patch.object(generator, 'OUT_DIR', output):
            with contextlib.redirect_stdout(io.StringIO()), contextlib.redirect_stderr(io.StringIO()):
                with patch('sys.argv', ['generate_apis.py']):
                    self.assertEqual(0, generator.main())
                with patch('sys.argv', ['generate_apis.py', '--check']):
                    self.assertEqual(0, generator.main())
                    path = os.path.join(output, 'LiveApi.java')
                    with open(path, 'a', encoding='utf-8') as handle:
                        handle.write('// stale\n')
                    self.assertEqual(1, generator.main())
                    with open(path, encoding='utf-8') as handle:
                        self.assertTrue(handle.read().endswith('// stale\n'))

    def test_yaml12_preserves_device_action_and_time_wire_values(self):
        self.assertEqual({'action': 'off', 'enabled': False, 'time': '2026-11-09T12:00:00Z'},
                         contract_yaml.load('action: off\nenabled: false\ntime: 2026-11-09T12:00:00Z\n'))


if __name__ == '__main__':
    unittest.main()
