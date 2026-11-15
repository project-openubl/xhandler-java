# The core (xhandler.wasm and its VERSION in xhandler-core, the xbuilder models and
# testdata/conformance) comes from xhandler-rust:
#
#   make sync CORE=<version>   the xhandler-rust v<version> release
#   make sync                  a checkout of xhandler-rust (RUST_REPO), for development
#   make verify-core           checks that the committed core is the one released as its VERSION
#   make verify-core-script    checks that hack/core.sh is release-tools' xhandler/core.sh
#   make verify-release TAG=v<version>
#                              what releasing <version> requires: syncing the xhandler-rust
#                              release of the same version changes nothing
RUST_REPO ?= ../xhandler-rust
CORE ?=
DIST := $(if $(CORE),.dist,$(RUST_REPO)/dist)
WASM := xhandler-core/src/main/wasm/xhandler.wasm
VERSION_FILE := xhandler-core/src/main/resources/io/github/project/openubl/xhandler/core/VERSION
MODELS := xbuilder/core/src/main/java/io/github/project/openubl/xbuilder/models

SYNCED := $(WASM) $(VERSION_FILE) $(MODELS) testdata/conformance

.PHONY: sync dist verify-core verify-core-script verify-release test

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

# hack/core.sh is a copy: change release-tools' xhandler/core.sh, then copy it here
CORE_SCRIPT := https://raw.githubusercontent.com/project-openubl/release-tools/main/xhandler/core.sh

verify-core-script:
	@original=$$(mktemp) && trap 'rm -f "$$original"' EXIT && \
	curl -fsSL -o "$$original" $(CORE_SCRIPT) && \
	{ diff -u "$$original" hack/core.sh || { echo "error: hack/core.sh differs from $(CORE_SCRIPT): change it there first, then copy it" >&2; exit 1; }; }

verify-release:
	./hack/core.sh verify-release $(TAG) $(WASM) $(VERSION_FILE)
	$(MAKE) --no-print-directory sync CORE=$(TAG:v%=%)
	@if [ -n "$$(git status --porcelain -- $(SYNCED))" ]; then \
		echo "error: the core differs from the xhandler-rust $(TAG) release (now synced here):" >&2; \
		git status --short -- $(SYNCED) >&2; \
		exit 1; \
	fi

test:
	mvn verify
