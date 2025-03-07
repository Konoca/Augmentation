const express = require('express');
const router = express.Router();

const fs = require('fs');
const toml = require('toml');

router.get('/', (req, res, next) => {
    res.send('ping');
});

router.post('/info', (req, res, next) => {
    const { path } = req.body;

    if (!path) {
        res.statusCode = 400;
        res.json({ error: 'Missing path' });
        return;
    }

    if (!path.endsWith('.index/')) {
        res.statusCode = 400;
        res.json({ error: 'Not .index/' })
        return;
    }

    fs.readdir(path, (err, files) => {
        if (err) {
            res.statusCode = 400;
            res.json(err);
            return;
        }

        //const data = {};
        const data = [];
        files.forEach((file) => {
            if (!file.endsWith('.toml')) return;

            //data[file] = toml.parse(fs.readFileSync(`${path}/${file}`, 'utf8'));
            const d = toml.parse(fs.readFileSync(`${path}/${file}`, 'utf8'));
            d['toml'] = file;

            //if (name && d['name'] !== name) return;
            //data[d['name']] = d;
            data.push(d);
        })

        res.json(data);
    });
});

module.exports = router;
