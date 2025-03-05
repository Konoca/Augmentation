const { app, BrowserWindow } = require('electron');
const path = require('node:path')

// TODO
// https://github.com/miloman23/ReactJS-ElectronJS-ExpressJS-App/

let win;
function createWindow() {
    win = new BrowserWindow({
        width: 800,
        height: 600,
        // webPreferences: {
        //     preload: path.join(__dirname, 'preload.js'),
        //     nodeIntegration: true,
        //     sandbox: false
        // }
    });

    win.loadURL(
        app.isPackaged
          ? `file://${path.join(__dirname, '../build/index.html')}`
          : 'http://localhost:3000'
      );
}

app.whenReady().then(createWindow)