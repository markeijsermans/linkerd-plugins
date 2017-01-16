FROM buoyantio/linkerd:0.8.5
MAINTAINER Mark Eijsermans <mark.eijsermas@gmail.com>

ADD k8s-normalize/target/scala-2.11/k8s-normalize-assembly-0.1-SNAPSHOT.jar /io.buoyant/linkerd/0.8.5/plugins/
