import React from "react";
import { Button } from "@mui/material"

import { colors } from "../constants";

import Mod from "../interfaces/mod";
import ModInfo from "../interfaces/modinfo";
import ModStatus from "../interfaces/modstatus";

import { getChanges } from "../utils/modinfo";


interface BtnProps {
    path: string;
    files: string[];
    modinfo: ModInfo[];
    modpack: Mod[];
}

function ExportButton(props: BtnProps) {
    const [changes, setChanges] = React.useState<ModStatus[]>([]);
    //const [, forceUpdate] = React.useReducer(x => x + 1, 0);

    const [gameVersions, setGameVersions] = React.useState<string[]>([]);
    const [loaders, setLoaders] = React.useState<string[]>([]);

    React.useEffect(() => {
        const tmp = getChanges(props.files, props.modpack);
        if (tmp)
            setChanges(tmp);

        if (props.modinfo.length > 0) {
            const allVersions: string[][] = props.modinfo.map((mod) =>
                mod["x-prismlauncher-mc-versions"]).filter((mod) => mod);

            const commonVersions = allVersions.reduce((pre, curr) =>
                curr.filter((item) => pre.indexOf(item) !== -1));

            setGameVersions(commonVersions);

            const allLoaders: string[][] = props.modinfo.map((mod) =>
                mod["x-prismlauncher-loaders"]).filter((mod) => mod);

            const commonLoaders = allLoaders.reduce((pre, curr) =>
                curr.filter((item) => pre.indexOf(item) !== -1));

            setLoaders(commonLoaders);
        }

    }, [props.path, props.files, props.modinfo, props.modpack]);

    const handleClick = async () => {
        changes.forEach(async (change) => {
            const toml = props.modinfo.find((mod) => change.name === mod.filename);
            if (change.toRemove) {
                await fetch('http://localhost:3001/os/delete', {
                    headers: {'Content-Type': 'application/json'},
                    method: 'POST',
                    mode: 'cors',
                    body: JSON.stringify({path: props.path, filename: change.name, tomlname: toml?.toml}),
                });
            }

            if (change.toAdd) {
                const mod = props.modpack.find((mod) => change.name === mod.filename);

                fetch('http://localhost:3001/os/download', {
                    headers: {'Content-Type': 'application/json'},
                    method: 'POST',
                    mode: 'cors',
                    body: JSON.stringify({
                        path: props.path,
                        modinfo: mod,
                        gameversion: gameVersions,
                        loaders: loaders,
                    }),
                });
            }
        });
        //forceUpdate();
    }

    return (
        <Button
            variant={'contained'}
            style={{
                backgroundColor: colors.blue.hex,
                color: colors.surface1.hex
            }}
            fullWidth
            sx={{ my: 1 }}
            onClick={handleClick}
        >
            Update Mods
        </Button>
    )
}

export default ExportButton;
