"""Read OpenAPI as YAML 1.2: on/off remain strings and timestamps remain wire strings."""
import re
import yaml


class OpenAPILoader(yaml.SafeLoader):
    pass


OpenAPILoader.yaml_implicit_resolvers = {
    key: [(tag, pattern) for tag, pattern in values
          if tag not in ('tag:yaml.org,2002:bool', 'tag:yaml.org,2002:timestamp')]
    for key, values in yaml.SafeLoader.yaml_implicit_resolvers.items()
}
OpenAPILoader.add_implicit_resolver('tag:yaml.org,2002:bool', re.compile(r'^(?:true|false)$'), list('tf'))


def load(handle):
    return yaml.load(handle, Loader=OpenAPILoader)
