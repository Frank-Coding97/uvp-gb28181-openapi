"""交付检查允许标准文档地址，同时仍拦截真实平台地址。"""
import contextlib
import importlib.util
import io
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch


def load_script(name):
    spec = importlib.util.spec_from_file_location(name, Path(__file__).with_name(name + ".py"))
    module = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(module)
    return module


checker = load_script("check-no-endpoints")


class DeliveryAddressCheckTest(unittest.TestCase):
    def test_rfc5737_addresses_are_examples_not_deployment_endpoints(self):
        for address in ["192.0.2.10", "198.51.100.20", "203.0.113.30"]:
            with self.subTest(address=address):
                self.assertTrue(checker.allowed_address(address))

    def test_actual_network_addresses_are_still_rejected(self):
        for parts in [(10, 20, 30, 40), (172, 16, 2, 3), (192, 168, 11, 20), (8, 8, 8, 8)]:
            with self.subTest(parts=parts):
                self.assertFalse(checker.allowed_address(".".join(map(str, parts))))
        self.assertTrue(checker.allowed_address("127.0.0.1"))

    def test_repository_scan_accepts_documentation_but_rejects_private_endpoint(self):
        with tempfile.TemporaryDirectory() as directory:
            sample = Path(directory, "example.json")
            with patch.object(checker, "SDK_ROOT", directory), contextlib.redirect_stdout(io.StringIO()), contextlib.redirect_stderr(io.StringIO()):
                sample.write_text('{"ip":"192.0.2.10"}')
                self.assertEqual(checker.main(), 0)
                sample.write_text('{"ip":"%s"}' % ".".join(map(str, (10, 20, 30, 40))))
                self.assertEqual(checker.main(), 1)


if __name__ == "__main__":
    unittest.main()
