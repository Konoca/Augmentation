const fs = require('fs');
const j2t = require('json2toml');
const { pipeline } = require('stream');
const { promisify } = require('util');

const modrinth = require('../utils/modrinth');
const curseforge = require('../utils/curseforge');


const pipelineAsync = promisify(pipeline);

const files = (path) => {
    if (!path) {
        console.error('Missing parameter in os.files');
        return [];
    }

    return fs.readdirSync(path);
};

const download = async (path, modinfo, gameversion, loaders) => {
    if (!path || !modinfo || !gameversion || !loaders) {
        console.error('Missing parameter in os.download');
        return;
    }

    let data;
    const pId = modinfo.url.split('/').at(-1);
    if (modinfo.url.search('modrinth') > 0) {
        data = await modrinth.getInfo(
            pId,
            modinfo.filename,
            modinfo.version,
            gameversion,
            loaders
        );
    }
    if (modinfo.url.search('curseforge') > 0) {
        data = await curseforge.getInfo(
            pId,
            modinfo.filename,
            modinfo.version,
            gameversion,
            loaders
        );
    }

    if (!data) {
        console.error(`Failed to get info for ${modinfo.filename} using`, path, modinfo, gameversion, loaders)
        return;
    }

    const filestream = fs.createWriteStream(`${path}/${data.filename}`);
    const download = await fetch(
        data.download.url, {
            method: 'GET'
        }
    );

    await pipelineAsync(download.body, filestream)
    .then(() => {
        console.log(`Successfully downloaded ${data.filename}`);
        filestream.close();
        fs.writeFileSync(
            `${path}/.index/${data.slug}.pw.toml`,
            j2t(data, {newlineAfterSection: true})
        );
        console.log(`Created ${data.slug}.pw.toml`);
    })
    .catch((err) => console.error(`Error when downloading ${data.filename}: ${err}`));
};

const deleteFiles = async (path, filename, tomlname) => {
    if (!path || !filename || !tomlname) {
        console.error('Missing parameter in os.deleteFiles');
        return;
    }

    fs.rm(`${path}/${filename}`, (err) => {
        if (err) {
            console.error(`Error when deleting: ${e}`);
            return;
        }

        console.log(`Successfully deleted ${path}/${filename}`);

        fs.rm(`${path}/.index/${tomlname}`, (err) => {
            if (err) {
                console.error(`Error when deleting toml: ${e}`);
                return;
            }
            console.log(`Successfully deleted ${tomlname}`);
        })

    })
};

module.exports = {
    files,
    download,
    deleteFiles,
};
