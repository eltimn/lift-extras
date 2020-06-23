# This pins the packages to certain versions
with import (builtins.fetchTarball {
  # Descriptive name to make the store path easier to identify
  name = "nixos-unstable-2020-03-18";
  # Commit hash for nixos-unstable as of 2020-03-18
  url = https://github.com/NixOS/nixpkgs/archive/053ad4e0db7241ae6a02394d62750fdc5d64aa9f.tar.gz;
  # Hash obtained using `nix-prefetch-url --unpack <url>`
  sha256 = "11l9sr8zg8j1n5p43zjkqwpj59gn8c84z1kf16icnsbnv2smzqdc";
}) { };

stdenv.mkDerivation rec {
  name = "env";
  env = buildEnv { name = name; paths = buildInputs; };
  buildInputs = [
    openjdk
    sbt
    # nodejs-8_x
    # yarn
  ];
}