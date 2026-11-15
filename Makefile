# The core (xhandler.wasm and its VERSION in xhandler-core, the xbuilder models and
# testdata/conformance) comes from xhandler-rust:
#
#   make sync CORE=<version>   the xhandler-rust v<version> release
#   make sync                  a checkout of xhandler-rust (RUST_REPO), for development
#   make verify-core           checks that the committed core is the one released as its VERSION
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

# core.sh syncs and verifies a released core. It lives in xhandler-rust, next to the release
# format it reads, and is downloaded from the tag of the core at hand (from main for a core
# released before it was there, or not released). CORE_SH=<path> runs a local copy instead, e.g.
# while changing it in xhandler-rust.
CORE_SH ?=
CORE_SH_URL := https://raw.githubusercontent.com/project-openubl/xhandler-rust
# $(call core_sh,<core version>,<core.sh arguments>)
core_sh = $(if $(CORE_SH),bash $(CORE_SH) $(2),script=$$(mktemp) && trap 'rm -f "$$script"' EXIT && \
	{ curl -fsSL -o "$$script" $(CORE_SH_URL)/v$(1)/xhandler-wasm/core.sh 2>/dev/null || \
	curl -fsSL -o "$$script" $(CORE_SH_URL)/main/xhandler-wasm/core.sh; } && bash "$$script" $(2))

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
	$(call core_sh,$(CORE:v%=%),download $(CORE) $(DIST))
endif

verify-core:
	$(call core_sh,$$(tr -d '\r\n' <$(VERSION_FILE)),verify $(WASM) $(VERSION_FILE))

verify-release:
	$(call core_sh,$(TAG:v%=%),verify-release $(TAG) $(WASM) $(VERSION_FILE))
	$(MAKE) --no-print-directory sync CORE=$(TAG:v%=%)
	@if [ -n "$$(git status --porcelain -- $(SYNCED))" ]; then \
		echo "error: the core differs from the xhandler-rust $(TAG) release (now synced here):" >&2; \
		git status --short -- $(SYNCED) >&2; \
		exit 1; \
	fi

test:
	mvn verify
