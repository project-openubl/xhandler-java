# The core (xhandler.wasm and its VERSION in xhandler-core, the xbuilder models and
# testdata/conformance) comes from xhandler-rust:
#
#   make sync CORE=<version>   the xhandler-rust v<version> release
#   make sync                  a checkout of xhandler-rust (RUST_REPO), for development
#   make verify-core           checks that the committed core is the one released as its VERSION
#   make verify-release TAG=v<version>
#                              what releasing <version> requires: the core of the xhandler-rust
#                              release of the same version
RUST_REPO ?= ../xhandler-rust
CORE ?=
DIST := $(if $(CORE),.dist,$(RUST_REPO)/dist)
WASM := xhandler-core/src/main/wasm/xhandler.wasm
VERSION_FILE := xhandler-core/src/main/resources/io/github/project/openubl/xhandler/core/VERSION
MODELS := xbuilder/core/src/main/java/io/github/project/openubl/xbuilder/models

.PHONY: sync dist verify-core verify-release test

sync: dist
	cp $(DIST)/xhandler.wasm $(WASM)
	cp $(DIST)/VERSION $(VERSION_FILE)
	rm -rf $(MODELS) && mkdir -p $(MODELS) && cp $(DIST)/models/java/*.java $(MODELS)/
	rm -rf testdata/conformance && cp -r $(DIST)/conformance testdata/conformance
	$(if $(CORE),rm -rf $(DIST))

dist:
ifeq ($(CORE),)
	$(MAKE) -C $(RUST_REPO)/xhandler-wasm dist
else
	./hack/core.sh download $(CORE) $(DIST)
endif

verify-core:
	./hack/core.sh verify $(WASM) $(VERSION_FILE)

verify-release:
	@test "v$$(cat $(VERSION_FILE))" = "$(TAG)" || { echo "error: the core is $$(cat $(VERSION_FILE)), not $(TAG:v%=%): run make sync CORE=$(TAG:v%=%)" >&2; exit 1; }
	STRICT=true ./hack/core.sh verify $(WASM) $(VERSION_FILE)

test:
	mvn verify
