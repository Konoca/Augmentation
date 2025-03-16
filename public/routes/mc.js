const fs = require('fs');
const toml = require('toml');

const info = async (path) => {
    if (!path.endsWith('.index/')) {
        return [];
    }

    const files = await new Promise((resolve, reject) => {
        fs.readdir(path, (err, files) => {
            if (err) {
                console.error(`Error reading .index/: ${err}`);
                reject([]);
                return;
            }

            const data = [];
            files.forEach((file) => {
                if (!file.endsWith('.toml')) return;
                const d = toml.parse(fs.readFileSync(`${path}/${file}`, 'utf8'));
                d['toml'] = file;
                data.push(d);
            })
            resolve(data);
        });
    });

    return files;
};

module.exports = {
    info,
};
