# RUST_REPO is a checkout of xhandler-rust; `make sync` copies what its
# `make -C xhandler-wasm dist` produced: the WebAssembly module, the generated
# models and the conformance corpus.
RUST_REPO ?= ../xhandler-rust
DIST := $(RUST_REPO)/dist
CORE := xhandler-core/src/main
MODELS := xbuilder/core/src/main/java/io/github/project/openubl/xbuilder/models

.PHONY: sync dist test

dist:
	$(MAKE) -C $(RUST_REPO)/xhandler-wasm dist

sync: dist
	cp $(DIST)/xhandler.wasm $(CORE)/wasm/
	cp $(DIST)/VERSION $(CORE)/resources/io/github/project/openubl/xhandler/core/
	rm -rf $(MODELS) && mkdir -p $(MODELS) && cp $(DIST)/models/java/*.java $(MODELS)/
	rm -rf testdata/conformance && cp -r $(DIST)/conformance testdata/conformance

test:
	mvn verify
