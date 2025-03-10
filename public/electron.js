const { app, BrowserWindow } = require('electron');
const path = require('node:path');

function createWindow() {
    const win = new BrowserWindow({
        //width: 800,
        //height: 600,
         webPreferences: {
             preload: path.join(__dirname, 'preload.js'),
             nodeIntegration: true,
             sandbox: false
        },
        frame: false,
        useContentSize: true,
        darkTheme: true,
    });

    //express();
    win.loadURL(
        app.isPackaged
          ? `file://${path.join(__dirname, 'index.html')}`
          : 'http://localhost:3000'
      );
}

app.whenReady().then(createWindow)
