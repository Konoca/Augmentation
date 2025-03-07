import { styled, Tab, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tabs } from "@mui/material";
import { Box } from "@mui/system";
import React from "react";
import { colors } from "../constants";
import Mod from "../interfaces/mod";
import ModInfo from "../interfaces/modinfo";
import ModStatus from "../interfaces/modstatus";
import { getChanges, getModID, getModProvider, getModVersion } from "../utils/modinfo";

interface ModGridProps {
    files: string[];
    modinfo: ModInfo[];
    modpack: Mod[];
}

const StyledTab = styled(Tab)({
    '&.MuiTab-root': {
        color: colors.text.hex,
    },
    '&.Mui-selected': {
        color: colors.blue.hex,
    },
});

const StyledTabs = styled(Tabs)({
    '& .MuiTabs-indicator': {
        backgroundColor: colors.blue.hex,
    },
});

const StyledTCell = styled(TableCell)({
    color: colors.text.hex,
});

const ColoredSTCell = styled(TableCell)({
    color: colors.surface1.hex,
    opacity: 1,
});

function FileTable({files}: {files: string[]}) {
    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <StyledTCell>Filename</StyledTCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {files.map((file) => <TableRow>
                        <StyledTCell>{file}</StyledTCell>
                    </TableRow>)}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

function ModInfoTable({modinfo}: {modinfo: ModInfo[]}) {
    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <StyledTCell>Name</StyledTCell>
                        <StyledTCell>Filename</StyledTCell>
                        <StyledTCell>Provider</StyledTCell>
                        <StyledTCell>Mod ID</StyledTCell>
                        <StyledTCell>Version ID</StyledTCell>
                        <StyledTCell>Loader</StyledTCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {modinfo.map((mod) => <TableRow>
                        <StyledTCell>{mod.name}</StyledTCell>
                        <StyledTCell>{mod.filename}</StyledTCell>
                        <StyledTCell>{getModProvider(mod)}</StyledTCell>
                        <StyledTCell>{getModID(mod)}</StyledTCell>
                        <StyledTCell>{getModVersion(mod)}</StyledTCell>
                        <StyledTCell>{mod["x-prismlauncher-loaders"]}</StyledTCell>
                    </TableRow>)}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

function ModPackTable({modpack}: {modpack: Mod[]}) {
    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <StyledTCell>Name</StyledTCell>
                        <StyledTCell>Filename</StyledTCell>
                        <StyledTCell>Version</StyledTCell>
                        <StyledTCell>URL</StyledTCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {modpack.map((mod) => <TableRow>
                        <StyledTCell>{mod.name}</StyledTCell>
                        <StyledTCell>{mod.filename}</StyledTCell>
                        <StyledTCell>{mod.version}</StyledTCell>
                        <StyledTCell>{mod.url}</StyledTCell>
                    </TableRow>)}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

function ChangesTable(props: ModGridProps) {
    const [changes, setChanges] = React.useState<ModStatus[]>([]);

    React.useEffect(() => {
        const tmp = getChanges(props.files, props.modpack);
        if (tmp)
            setChanges(tmp);
    }, [props.files, props.modpack]);

    return (
        <TableContainer>
            <Table>
                <TableHead>
                    <TableRow>
                        <StyledTCell>Filename</StyledTCell>
                        <StyledTCell>Operation</StyledTCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {changes.map((mod) =>
                        ((mod.toAdd || mod.toRemove) &&
                            <TableRow sx={{
                                backgroundColor: mod.toAdd ? colors.green.hex : colors.red.hex,
                                opacity: 0.75,
                            }}>
                                <ColoredSTCell>{mod.name}</ColoredSTCell>
                                <ColoredSTCell>{(mod.toAdd && 'Add') || (mod.toRemove && 'Remove')}</ColoredSTCell>
                            </TableRow>
                        )
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    )
}

function ModGrid(props: ModGridProps) {
    const [tab, setTab] = React.useState<number>(0);

    return (
        <Box
            sx={{
                height: '100%',
                width: '100%'
            }}
            display={'flex'}
            flexDirection={'column'}
        >
            <Box>
                <StyledTabs value={tab} onChange={(_, newTab) => setTab(newTab)}>
                    <StyledTab label={'Local Files'} sx={{ width: '25%' }} />
                    <StyledTab label={'Local Mod Info'} sx={{ width: '25%' }} />
                    <StyledTab label={'Modpack Info'} sx={{ width: '25%' }} />
                    <StyledTab label={'Differences'} sx={{ width: '25%' }} />
                </StyledTabs>
            </Box>

            <Box>
                {tab === 0 &&
                    <FileTable files={props.files} />
                }
                {tab === 1 &&
                    <ModInfoTable modinfo={props.modinfo} />
                }
                {tab === 2 &&
                    <ModPackTable modpack={props.modpack} />
                }
                {tab === 3 &&
                    <ChangesTable {...props} />
                }
            </Box>
        </Box>
    )
}

export default ModGrid;
