FROM buoyantio/linkerd:0.9.0
MAINTAINER Mark Eijsermans <mark.eijsermas@gmail.com>

ADD k8s-normalize/target/scala-2.11/k8s-normalize-assembly-1.0.2.jar /io.buoyant/linkerd/0.9.0/plugins/
