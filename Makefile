IMAGE := markeijsermans/linkerd:1.0.0

all: clean build container

clean:
	./sbt clean

build:
	./sbt k8s-normalize:assembly

container:
	docker build -t $(IMAGE) .

push:
	docker push $(IMAGE)

# check for output:
# LoadService: loaded instance of class com.markeijsermans.linkerd.namers.K8sNormalizeInitializer for requested service io.buoyant.namer.NamerInitializer
run:
	docker run -ti --rm -p 5001 -p 9990 -v "$(shell pwd)/test-conf.yaml:/test-conf.yaml" $(IMAGE) /test-conf.yaml -log.level=DEBUG
