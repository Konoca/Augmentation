const { app, BrowserWindow } = require('electron');
const path = require('path');

function createWindow() {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            preload: path.join(__dirname, 'preload.js'),
            nodeIntegration: false,
            sandbox: false,
            contextIsolation: true,
        },
        useContentSize: true,
        darkTheme: true,
    });

    win.loadURL(
        app.isPackaged
            ? `file://${path.join(__dirname, 'index.html')}`
            : 'http://localhost:3000'
    );
}

app.whenReady().then(createWindow);
