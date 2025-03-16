import React from 'react';
import { Box, GlobalStyles } from '@mui/material';

import CustomTextField from './components/CustomTextField';
import Mod from './interfaces/mod';
import ModInfo from './interfaces/modinfo';
import { CustomSubTitle, CustomTitle } from './components/CustomTypography';
import { colors } from './constants';
import ModGrid from './components/ModGrid';
import ExportButton from './components/UpdateButton';

function App() {
    const [modsPath, setModsPath] = React.useState<string>('');

    const [files, setFiles] = React.useState<string[]>([]);
    const [modInfo, setModInfo] = React.useState<ModInfo[]>([]);
    const [modList, setModList] = React.useState<Mod[]>([]);

    const [pathError, setPathError] = React.useState<boolean>(false);
    const [jsonError, setJsonError] = React.useState<boolean>(false);

    const handleSetPath = (path: string) => {
        setModsPath(path);

        const files = window.api.os.files(path);
        setPathError(false);
        setFiles(files);
        getModInfo(path, files);
    };

    const getModInfo = (path: string, f: string[]) => {
        if (!f.includes('.index')) return;

        window.api.mc.info(path + '/.index/')
        .then((info: ModInfo[]) => setModInfo(info))
        .catch((err) => {
            setPathError(true);
            setFiles([]);
            console.error('Bad Response in getModInfo():', err);
        })
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

            <CustomTitle variant='h1' align='center' >
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
                forceUpdate={() => handleSetPath(modsPath)}
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
