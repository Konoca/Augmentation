const { app, BrowserWindow } = require('electron');
const { spawn } = require('child_process');
const path = require('path');

let apiProcess;

function createWindow() {
    const win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: false,
            sandbox: false
        },
        useContentSize: true,
        darkTheme: true,
    });

    win.loadURL(
        app.isPackaged
            ? `file://${path.join(__dirname, 'index.html')}`
            : 'http://localhost:3000'
    );

    const apiPath = path.join(process.resourcesPath, 'api', 'app.js');
    //const spawnOpts = { stdio: 'inherit', shell: true, windowsHide: true };
    const spawnOpts = { stdio: 'inherit', shell: true }; // TODO temporary workaround

    apiProcess = process.platform === 'win32'
        ? spawn('node', [apiPath], spawnOpts)
        : spawn('npm', ['run', 'api'], spawnOpts);

    apiProcess.on('exit', (code) => {
        console.log(`API exited with code ${code}`);
    });

    apiProcess.on('error', (err) => {
        console.error(`Failed to start API: ${err}`);
    });
}

app.whenReady().then(createWindow);

app.on('before-quit', () => {
    if (apiProcess) {
        console.log(`Killing API process (PID: ${apiProcess.pid})`);
        apiProcess.kill();
    }
});
