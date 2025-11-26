{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
    buildInputs = with pkgs; [
        jdk24
        maven
        python311 # needed for jdtls lsp
    ];
}
