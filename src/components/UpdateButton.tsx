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
    const [, forceUpdate] = React.useReducer(x => x + 1, 0);

    React.useEffect(() => {
        const tmp = getChanges(props.files, props.modpack);
        if (tmp)
            setChanges(tmp);

        //const mcVersion = props.modinfo
    }, [props.files, props.modpack]);

    const handleClick = async () => {
        changes.forEach(async (change) => {
            if (change.toRemove) {
                fetch('http://localhost:3001/os/delete', {
                    headers: {'Content-Type': 'application/json'},
                    method: 'POST',
                    mode: 'cors',
                    body: JSON.stringify({path: props.path, filename: change.name}),
                });
            }

            if (change.toAdd) {
                const mod = props.modpack.find((mod) => change.name === mod.filename);
                console.log(mod);
            }
        });
        forceUpdate();
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
