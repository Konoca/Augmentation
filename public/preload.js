const { contextBridge } = require('electron');
const { info } = require('./routes/mc');
const { files, download, deleteFiles } = require('./routes/os');

const api = {
    mc: {
        info: async (path) => info(path),
    },
    os: {
        files: (path) => files(path),

        download: async (path, modinfo, gameversions, loaders) =>
            download(path, modinfo, gameversions, loaders),

        delete: (path, filename, tomlname) => deleteFiles(path, filename, tomlname),
    },
}

contextBridge.exposeInMainWorld('api', api);

module.exports = {
    api,
}
