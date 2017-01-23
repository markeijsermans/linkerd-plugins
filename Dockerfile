FROM buoyantio/linkerd:0.8.6-32b
MAINTAINER Mark Eijsermans <mark.eijsermas@gmail.com>

ADD k8s-normalize/target/scala-2.11/k8s-normalize-assembly-1.0.1.jar /io.buoyant/linkerd/0.8.6/plugins/
