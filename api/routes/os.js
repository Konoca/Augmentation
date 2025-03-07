const express = require('express');
const router = express.Router();

const fs = require('fs');

router.get('/', (req, res, next) => {
    res.send('ping');
});

router.post('/files', (req, res, next) => {
    const { path } = req.body;

    if (!path) {
        res.statusCode = 400;
        res.json({error: 'Missing path'});
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

router.post('/download', (req, res, next) => {
    const { path, download } = req.body;
});

router.post('/delete', (req, res, next) => {
    const { path, filename } = req.body;

    if (!path) {
        res.statusCode = 400;
        res.json({error: 'Missing path'});
        return;
    }

    fs.rm(`${path}/${filename}`, (err) => {
        if (err) {
            res.statusCode = 400;
            res.json(err);
            return;
        }

        res.json({ msg: 'Success' });
    })
});

module.exports = router;
