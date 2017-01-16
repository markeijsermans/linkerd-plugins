IMAGE := markeijsermans/linkerd:0.8.5

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
	docker run -ti --rm -p 5001 -p 9990 -v "$(shell pwd)/test.conf:/test.conf" $(IMAGE) /test.conf -log.level=DEBUG
