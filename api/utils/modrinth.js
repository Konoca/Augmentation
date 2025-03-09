const fetch = require('fetch-retry')(global.fetch);

const API = 'https://api.modrinth.com/v2';

const getSideBool = (value) => {
    if (value === 'unsupported') return false;
    return true;
}

const getSideString = (isClient, isServer) => {
    if (isClient && isServer) return 'both';
    if (isClient) return 'client';
    if (isServer) return 'server';
    return '';
}

const getInfo = async (projectId, filename, modversion, gameversion, loaders) => {
    const projecturl = API + `/project/${projectId}`;

    const versionurl = `${projecturl}/version?` + new URLSearchParams({
        game_versions: `["${gameversion.join('", "')}"]`,
        loaders: `["${loaders.join('", "')}"]`,
    });

    //console.log(projectId, filename, modversion, gameversions, loaders, url);

    try {
        const preq = await fetch(projecturl);
        const project = await preq.json();

        const isClient = getSideBool(project.client_side);
        const isServer = getSideBool(project.server_side);
        const side = getSideString(isClient, isServer);

        const vreq = await fetch(versionurl);
        const vres = await vreq.json();

        let version, file;
        vres.every((data) => {
            if (data.version_number.substring(modversion) < 0) return true;
            file = data.files.find((f) => f.filename === filename);
            if (file) {
                version = data;
                return false;
            }
            return true;
        });

        // if file wasnt found using version number as filter, just seach for the filename directly
        // this is for mods like Joy of Painting that for some reason are registed as NONE for version
        if (!file) {
            vres.every((data) => {
                file = data.files.find((f) => f.filename === filename);
                if (file) {
                    version = data;
                    return false;
                }
                return true;
            });
        }

        if (!file || !version || !project) {
            console.log('MISSING INFO', filename, projectId, file, version, project);
            console.log(projecturl, versionurl);
            return;
        }

        const info = {
            filename: filename,
            name: project.title,
            side: side,
            'x-prismlauncher-loaders': version.loaders ?? loaders,
            'x-prismlauncher-mc-versions': version.game_versions,
            'x-prismlauncher-release-type': version.version_type,
            slug: project.slug,

            download: {
                hash: file['hashes']['sha512'],
                'hash-format': 'sha512',
                'mode': 'url',
                'url': file.url,
            },

            update: {
                modrinth: {
                    'mod-id': projectId,
                    'version': version.id,
                },
            },
        }

        return info;
    } catch (err) {
        console.log(`Error downloading from Modrinth: ${filename} ${err}`);
        return null;
    }
}

module.exports = {
   getInfo
}
