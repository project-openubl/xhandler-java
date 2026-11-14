#!/usr/bin/env bash
# The xhandler core (xhandler.wasm, its models and the conformance corpus) comes from the
# releases of xhandler-rust, which attach xhandler-dist-<version>.tar.gz, xhandler.wasm and
# SHA256SUMS.
#
#   core.sh download <version> <dir>      extracts the release's dist into <dir>
#   core.sh verify <wasm> <version-file>  checks that <wasm> is the xhandler.wasm released as the
#                                         version in <version-file>; a version that was never
#                                         released (a core built from a checkout) only warns,
#                                         unless STRICT=true
set -euo pipefail

releases=${XHANDLER_RUST_RELEASES:-https://github.com/project-openubl/xhandler-rust/releases/download}

sha256() { if command -v sha256sum >/dev/null; then sha256sum "$1"; else shasum -a 256 "$1"; fi | cut -d' ' -f1; }
# The checksum of <asset> in the SHA256SUMS of release <version>; empty if there is none
released_sha256() { curl -fsSL "$releases/v$1/SHA256SUMS" 2>/dev/null | awk -v asset="$2" '$2 == asset {print $1}' || true; }

case "${1:-}" in
download)
  version=$2 dir=$3 asset="xhandler-dist-$2.tar.gz"
  tmp=$(mktemp -d)
  trap 'rm -rf "$tmp"' EXIT
  curl -fsSL -o "$tmp/$asset" "$releases/v$version/$asset"
  if [ "$(sha256 "$tmp/$asset")" != "$(released_sha256 "$version" "$asset")" ]; then
    echo "error: $asset does not match the SHA256SUMS of xhandler-rust v$version" >&2
    exit 1
  fi
  rm -rf "$dir" && mkdir -p "$dir" && tar -xzf "$tmp/$asset" -C "$dir"
  ;;
verify)
  wasm=$2 version=$(cat "$3")
  expected=$(released_sha256 "$version" xhandler.wasm)
  if [ -z "$expected" ]; then
    message="the core $version ($wasm) is not an xhandler-rust release: sync a release with 'make sync CORE=<version>'"
    if [ "${STRICT:-}" = true ]; then
      echo "error: $message" >&2
      exit 1
    fi
    echo "warning: $message" >&2
  elif [ "$(sha256 "$wasm")" != "$expected" ]; then
    echo "error: $wasm is not the xhandler.wasm of xhandler-rust v$version" >&2
    exit 1
  else
    echo "$wasm is the xhandler.wasm of xhandler-rust v$version"
  fi
  ;;
*)
  echo "usage: core.sh download <version> <dir> | verify <wasm> <version-file>" >&2
  exit 2
  ;;
esac
