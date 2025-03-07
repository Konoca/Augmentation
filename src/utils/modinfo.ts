import Mod from "../interfaces/mod";
import ModInfo from "../interfaces/modinfo";
import ModStatus from "../interfaces/modstatus";

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

export const getChanges = (files: string[], modpack: Mod[]) => {
    if (files.length === 0 || modpack.length === 0) return;
    const tmp1: ModStatus[] = files.map((file) => {
        if (modpack.find((mod) => mod.filename === file))
            return {name: file};

        return {name: file, toRemove: true};
    })
    .filter((mod) => mod.name !== '.index')

    const tmp2: ModStatus[] = modpack.map((mod) => {
        if (files.find((file) => mod.filename === file))
            return { name: mod.filename };

        return { name: mod.filename, toAdd: true };
    })

    const sorted = [...tmp1, ...tmp2].sort((a, b) => a.name.localeCompare(b.name));
    const filtered = sorted.filter((mod) => mod.toAdd || mod.toRemove);
    return filtered;
}
