#!/usr/bin/env bash
# The xhandler core (xhandler.wasm, its models and the conformance corpus) comes from the
# releases of xhandler-rust, which attach xhandler-dist-<version>.tar.gz, xhandler.wasm and
# SHA256SUMS. The original is project-openubl/release-tools' xhandler/core.sh: change it there,
# then copy it to xhandler-go, xhandler-python and xhandler-java (`make verify-core-script` there
# checks the copy).
#
#   core.sh download <version> <dir>
#       extracts the release's dist into <dir>
#   core.sh verify <wasm> <version-file>
#       checks that <wasm> is the xhandler.wasm released as the version in <version-file>; a
#       version that was never released (a core built from a checkout) only warns
#   core.sh verify-release <tag> <wasm> <version-file>
#       what releasing <tag> requires: the version in <version-file> is the tag's and <wasm> is
#       the xhandler.wasm of that xhandler-rust release
#
# A release that can't be read (network, GitHub errors) is an error, never "not released".
# XHANDLER_RUST_RELEASES replaces the releases URL (for tests).
set -euo pipefail

releases=${XHANDLER_RUST_RELEASES:-https://github.com/project-openubl/xhandler-rust/releases/download}
# The versions release-tools releases
version_re='^[0-9]+\.[0-9]+\.[0-9]+(-(alpha|beta|rc)\.[0-9]+)?$'

die() {
  echo "error: $*" >&2
  exit 1
}
usage() {
  echo "usage: core.sh download <version> <dir> | verify <wasm> <version-file> | verify-release <tag> <wasm> <version-file>" >&2
  exit 2
}
sha256() { if command -v sha256sum >/dev/null; then sha256sum "$1"; else shasum -a 256 "$1"; fi | cut -d' ' -f1; }
# The version in a VERSION file, without a Windows line ending
read_version() { tr -d '\r\n' <"$1"; }

# fetch <url> <file>: 0 when downloaded, 1 when the release has no such file (404); any other
# failure stops the script
fetch() {
  local status
  status=$(curl -sSL -o "$2" -w '%{http_code}' "$1") || die "cannot download $1"
  case "$status" in
  200) return 0 ;;
  404) return 1 ;;
  *) die "cannot download $1: HTTP $status" ;;
  esac
}

# The checksum of <asset> in the SHA256SUMS of release <version>; empty if it was never released
# (or not sealed yet: SHA256SUMS goes last)
released_sha256() {
  local sums
  sums=$(mktemp)
  if fetch "$releases/v$1/SHA256SUMS" "$sums"; then
    awk -v asset="$2" '$2 == asset {print $1}' "$sums"
  fi
  rm -f "$sums"
}

case "${1:-}" in
download)
  [ $# -eq 3 ] || usage
  version=${2#v} dir=$3 asset="xhandler-dist-${2#v}.tar.gz"
  [[ "$version" =~ $version_re ]] || die "$2 is not a release version (X.Y.Z or X.Y.Z-alpha.N, -beta.N, -rc.N)"
  expected=$(released_sha256 "$version" "$asset")
  [ -n "$expected" ] || die "xhandler-rust v$version is not released (no SHA256SUMS with $asset)"
  tmp=$(mktemp -d)
  trap 'rm -rf "$tmp"' EXIT
  fetch "$releases/v$version/$asset" "$tmp/$asset" || die "xhandler-rust v$version has no $asset"
  [ "$(sha256 "$tmp/$asset")" = "$expected" ] || die "$asset does not match the SHA256SUMS of xhandler-rust v$version"
  rm -rf "$dir" && mkdir -p "$dir" && tar -xzf "$tmp/$asset" -C "$dir"
  ;;
verify | verify-release)
  if [ "$1" = verify-release ]; then
    [ $# -eq 4 ] || usage
    tag=$2
    shift
  else
    [ $# -eq 3 ] || usage
    tag=""
  fi
  wasm=$2 version=$(read_version "$3")
  if [ -n "$tag" ]; then
    [ "v$version" = "$tag" ] || die "the core is $version, not ${tag#v}: run make sync CORE=${tag#v}"
    [[ "$version" =~ $version_re ]] || die "$tag is not a release version (vX.Y.Z or vX.Y.Z-alpha.N, -beta.N, -rc.N)"
  fi
  expected=$(released_sha256 "$version" xhandler.wasm)
  message="the core $version ($wasm) is not an xhandler-rust release: sync a release with 'make sync CORE=<version>'"
  if [ -z "$expected" ]; then
    [ -z "$tag" ] || die "$message"
    echo "warning: $message" >&2
  elif [ "$(sha256 "$wasm")" != "$expected" ]; then
    die "$wasm is not the xhandler.wasm of xhandler-rust v$version"
  else
    echo "$wasm is the xhandler.wasm of xhandler-rust v$version"
  fi
  ;;
*) usage ;;
esac
