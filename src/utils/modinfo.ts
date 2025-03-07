import ModInfo from "../interfaces/modinfo";

export const getModProvider = (mod: ModInfo) => {
    if (mod.update.modrinth) return 'Modrinth';
    if (mod.update.curseforge) return 'CurseForge';

    return 'Unknown';
}

export const getModID = (mod: ModInfo) => {
    if (mod.update.modrinth) return mod.update.modrinth["mod-id"];
    if (mod.update.curseforge) return mod.update.curseforge["project-id"];

    return '';
}

export const getModVersion = (mod: ModInfo) => {
    if (mod.update.modrinth) return mod.update.modrinth.version;
    if (mod.update.curseforge) return mod.update.curseforge["file-id"];

    return '';
}
