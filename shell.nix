{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
    buildInputs = with pkgs; [
        javaPackages.compiler.openjdk25
        maven
        python311 # needed for jdtls lsp
    ];
}
