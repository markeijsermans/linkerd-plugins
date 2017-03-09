Linkerd Plugins
===============

Plugins for [linkerd](https://linkerd.io).

## K8sNormalize Namer

K8sNormalize namer plugin rewrites service names to the accepted kubernetes format: `[a-z]([-a-z0-9]*[a-z0-9])?` (e.g. 'my-name' or 'abc-123'). This is particularly useful for gRPC services running in kubernetes. On requests, gRPC encodes the http/2 `:path` as `/packageName.ServiceName/methodName`.  

Given the following proto3 definition:
```protobuf
package helloworld;                                                            
service Greeter {
  rpc SayHello (Hello) returns (Hello) {}                          
}
```

GRPC will make a request with `:path` set to `/helloworld.Greeter/SayHello`. This namer will rename it to `/greeter/SayHello`

### Config

| key | default | description |
| --- | ------- | ----------- |
| kind | com.markeijsermans.k8s.normalize | The plugin's id |
| segment | 2 | The path segment to normalize |
| serviceOnly | false | Drop the package and only use the service name. True rewrites `helloworld.Greeter` to `greeter`, false rewrites to `helloworld-greeter` |



### Example

```yaml
namers:
- kind: com.markeijsermans.k8s.normalize
  segment: 2
  serviceOnly: false
- kind: io.l5d.k8s
  host: localhost
  port: 8001

routers:
- protocol: h2
  experimental: true
  label: grpc
  identifier:
    kind: io.l5d.headerPath
    segments: 2
  baseDtab: |
    /rw => /#/io.l5d.k8s/default/grpc;
    /srv=> /#/com.markeijsermans.k8s.normalize/rw;
    /h2 => /srv;
  servers:
  - port: 5001
    ip: 0.0.0.0
```


## Building

```bash
make all

# Or build directly with sbt
./sbt k8s-normalize:assembly
```

## Running

#### Plugin jar
Download from releases
Refer to the [plugin docs](https://linkerd.io/in-depth/plugin/) on how run linkerd with plugins 

#### Docker
Refer to linkerd's docker image for full docs and run args, as this image inherits from it 
```bash
docker pull markeijsermans/linkerd:0.9.0 
```
 
#### Sanity test
```bash
make run
```
This will run the container with `test-conf.yaml` and debug logging. Check for output:
```
LoadService: loaded instance of class com.markeijsermans.linkerd.namers.K8sNormalizeInitializer for requested service io.buoyant.namer.NamerInitializer
```
