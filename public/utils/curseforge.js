const fetch = require('fetch-retry')(global.fetch);

const API = 'https://api.curse.tools/v1/cf';

const getReleaseString = (releaseType) => {
    switch (releaseType) {
        case 1: return 'release';
        case 2: return 'beta';
        case 3: return 'alpha';
        default: return '';
    }
}

const getModLoaderString = (modLoader) => {
    switch (modLoader) {
        case 0: return 'any';
        case 1: return 'forge';
        case 2: return 'cauldron';
        case 3: return 'liteloader';
        case 4: return 'fabric';
        case 5: return 'quilt';
        case 6: return 'neoforge';
        default: return '';
    }
}

const getInfo = async (projectId, filename, modversion, gameversion, loaders) => {
    const url = API + `/mods/${projectId}`;

    try {
        const req = await fetch(url);
        const res = await req.json();
        const data = await res.data;

        const fileIdx = data.latestFilesIndexes.find(f => f.filename === filename);

        const fReq = await fetch(url + '/files/' + fileIdx.fileId);
        const fRes = await fReq.json();
        const file = await fRes.data;

        if (!file) {
            console.log('MISSING FILE', filename, projectId, url, file, fileIdx);
            return;
        }

        const info = {
            filename: filename,
            name: data.name,
            side: '', // TODO figure out how CurseForge tracks this
            'x-prismlauncher-loaders': [getModLoaderString(fileIdx.modLoader)],
            'x-prismlauncher-mc-versions': file.gameVersions,
            'x-prismlauncher-release-type': getReleaseString(file.releaseType),
            slug: data.slug,

            download: {
                hash: file.hashes.find(f => f.algo === 1).value,
                'hash-format': 'sha1',
                'mode': 'url',
                'url': file.downloadUrl,
            },

            update: {
                curseforge: {
                    'file-id': file.id,
                    'project-id': projectId,
                },
            },
        }

        return info;
    } catch (err) {
        console.log(`Error downloading from CurseForge: ${filename} ${err}`);
        return null;
    }
}

module.exports = {
   getInfo
}
