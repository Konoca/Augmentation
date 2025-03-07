export default interface ModInfo {
    "filename": string;
    "name": string;
    "side": string;
    "x-prismlauncher-loaders": string[];
    "x-prismlauncher-mc-versions": string[];
    "x-prismlauncher-release-type": string;
    "download": {
        "hash": string;
        "hash-format": string;
        "mode": string;
        "url": string;
    };
    "update": {
        "modrinth": {
            "mod-id": string;
            "version": string;
        };
        "curseforge": {
            "file-id": number;
            "project-id": number;
        }
    };
    "toml": string;
}
