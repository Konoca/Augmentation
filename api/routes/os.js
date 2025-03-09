const express = require('express');
const router = express.Router();

const fs = require('fs');
const j2t = require('json2toml');
const { Readable } = require('stream');
const { finished } = require('stream/promises');

const modrinth = require('../utils/modrinth');
const curseforge = require('../utils/curseforge');

router.get('/', (req, res, next) => {
    res.send('ping');
});

router.post('/files', (req, res, next) => {
    const { path } = req.body;

    if (!path) {
        res.statusCode = 400;
        res.json({ error: 'Missing path' });
        return;
    }

    fs.readdir(path, (err, files) => {
        if (err) {
            res.statusCode = 400;
            res.json(err);
            return;
        }

        res.json(files);
    });
});

router.post('/download', async (req, res, next) => {
    const { path, modinfo, gameversion, loaders } = req.body;

    if (!path || !modinfo) {
        res.statusCode = 400;
        res.json({ error: 'Bad Request' });
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
        res.statusCode = 400;
        res.json({ error: `${modinfo.filename} failed`});
        return;
    }

    const filestream = fs.createWriteStream(`${path}/${data.filename}`);
    const download = await fetch(data.download.url, {method: 'GET'});
    finished(Readable.fromWeb(download.body).pipe(filestream));

    filestream.on('finish', () => {
        filestream.close();
        fs.writeFileSync(`${path}/.index/${data.slug}.pw.toml`, j2t(data, {newlineAfterSection: true}));
        res.json({ msg: 'Success', data: data });
    })

});

router.post('/delete', (req, res, next) => {
    const { path, filename, tomlname } = req.body;

    if (!path || !filename) {
        res.statusCode = 400;
        res.json({error: 'Bad Request'});
        return;
    }

    // this is so stupid but i stopped caring
    fs.rm(`${path}/${filename}`, (err) => {
        if (err) {
            res.statusCode = 400;
            res.json(err);
            return;
        }

        fs.rm(`${path}/.index/${tomlname}`, (err) => {
            if (err) {
                res.statusCode = 400;
                res.json(err);
                return;
            }

            res.json({ msg: 'Success' });
        })

    })
});

module.exports = router;
