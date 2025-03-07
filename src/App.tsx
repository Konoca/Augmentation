import React from 'react';
import { Box, Button, GlobalStyles } from '@mui/material';

import CustomTextField from './components/CustomTextField';
import Mod from './interfaces/mod';
import ModInfo from './interfaces/modinfo';
import { CustomSubTitle, CustomTitle } from './components/CustomTypography';
import { colors } from './constants';
import ModGrid from './components/ModGrid';
import ExportButton from './components/UpdateButton';
//import { changeTheme } from './utils/theme';

function App() {
    const [modsPath, setModsPath] = React.useState<string>('');
    const [files, setFiles] = React.useState<string[]>([]);
    const [modInfo, setModInfo] = React.useState<ModInfo[]>([]);
    const [pathError, setPathError] = React.useState<boolean>(false);
    const [jsonError, setJsonError] = React.useState<boolean>(false);
    const [modList, setModList] = React.useState<Mod[]>([]);

    //const [, forceUpdate] = React.useReducer(x => x + 1, 0);

    const handleSetPath = (path: string) => {
        setModsPath(path);

        if (!path.endsWith('.minecraft/mods')) return;
        fetch('http://localhost:3001/os/files', {
            headers: {'Content-Type': 'application/json'},
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify({path: path}),
        })
        .then(res => {
            if (res.status !== 200) {
                setPathError(true);
                setFiles([]);
                throw new Error('Bad Response in handleSetPath() fetch(files)');
            }

            return res;
        })
        .then(res => res.json())
        .then(files => {
            setPathError(false);
            setFiles(files);
            getModInfo(path, files);
        })
        .catch((e) => console.log(e));
    };

    const getModInfo = (path: string, f: string[]) => {
        if (!f.includes('.index')) return;

        fetch('http://localhost:3001/mc/info', {
            headers: {'Content-Type': 'application/json'},
            method: 'POST',
            mode: 'cors',
            body: JSON.stringify({path: path + '/.index/'}),
        })
        .then(res => {
            if (res.status !== 200) {
                setPathError(true);
                setFiles([]);
                throw new Error('Bad Response in getModInfo() fetch(info)');
            }

            return res;
        })
        .then(res => res.json())
        .then((info: ModInfo[]) => setModInfo(info))
    }

    const handleSetModList = (rawJson: string) => {
        try {
            const list = JSON.parse(rawJson);
            if (!list) return;
            setModList(list);
            setJsonError(false);
        } catch {
            setJsonError(true);
        }
    }

    return (
        <Box
            sx={{ width: '100dvw', height: '100dvh', overflowX: 'hidden' }}
            display={'flex'}
            flexDirection={'column'}
        >
            <GlobalStyles styles={{
                html: { backgroundColor: colors.base.hex },
                body: { margin: 0 }
            }}
            />

            <CustomTitle variant='h1' align='center' /* onClick={() => {changeTheme(); forceUpdate();}} */ >
                Augmentation
            </CustomTitle>
            <CustomSubTitle variant='h5' align='center' gutterBottom>
                A Prism Launcher Custom Modpack Updater
            </CustomSubTitle>

            <CustomTextField
                required
                error={pathError}
                label="Path/To/.minecraft/mods"
                sx={{ my: 2 }}
                fullWidth
                value={modsPath}
                onChange={(event) => handleSetPath(event.target.value)}
            />

            <CustomTextField
                required
                id="textfield"
                label="Modpack JSON String"
                fullWidth
                onChange={(event) => handleSetModList(event.target.value)}
                error={jsonError}
            />

            <ExportButton
                path={modsPath}
                files={files}
                modinfo={modInfo}
                modpack={modList}
            />

            <ModGrid
                files={files}
                modinfo={modInfo}
                modpack={modList}
            />
        </Box>
    );
}

export default App;
