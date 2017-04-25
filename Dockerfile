FROM buoyantio/linkerd:1.0.0
MAINTAINER Mark Eijsermans <mark.eijsermas@gmail.com>

ADD k8s-normalize/target/scala-2.11/k8s-normalize-assembly-1.0.4.jar /io.buoyant/linkerd/1.0.0/plugins/
