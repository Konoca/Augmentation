const express = require('express');
const router = express.Router();

const fs = require('fs');

router.get('/', (req, res, next) => {
    res.send('ping');
});

router.post('/files', (req, res, next) => {
    const body = req.body;
    const path = body['path'];

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

module.exports = router;
