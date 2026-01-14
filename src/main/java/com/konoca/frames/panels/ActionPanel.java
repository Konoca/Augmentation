package com.konoca.frames.panels;

import java.util.ArrayList;
import java.util.logging.Logger;

import java.io.File;
import java.nio.file.Path;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.konoca.frames.dialogs.UrlDialog;
import com.konoca.frames.dialogs.ImportDialog;
import com.konoca.frames.FrameFuncs;
import com.konoca.frames.InstanceFrame;
import com.konoca.frames.MainFrame;
import com.konoca.frames.PrismFrame;
import com.konoca.objs.Augment;
import com.konoca.objs.URLObj;
import com.konoca.utils.AugUtils;
import com.konoca.utils.OSUtils;
import com.konoca.utils.PrismUtils;

public class ActionPanel extends JPanel
{
    private static Logger logger = Logger.getLogger(ActionPanel.class.getName());

    private MainFrame parent;
    private FrameFuncs funcs;

    private JButton exportBtn;
    private JButton importBtn;
    private JButton urlsBtn;
    private JButton refreshBtn;

    public ActionPanel(MainFrame parent, FrameFuncs funcs)
    {
        this.parent = parent;
        this.funcs = funcs;
    }

    public void addExportBtn()
    {
        this.exportBtn = new JButton("Export");
        this.exportBtn.addActionListener(e -> handleExport());
        this.exportBtn.setEnabled(false);
        this.add(this.exportBtn);
    }

    public void addImportBtn()
    {
        this.importBtn = new JButton("Import");
        this.importBtn.addActionListener(e -> handleImport());
        this.importBtn.setEnabled(false);
        this.add(this.importBtn);
    }

    public void addUrlsBtn()
    {
        this.urlsBtn = new JButton("Add URLs");
        this.urlsBtn.addActionListener(e -> handleURLs());
        this.urlsBtn.setEnabled(false);
        this.add(this.urlsBtn);
    }

    public void addRefreshBtn()
    {
        this.refreshBtn = new JButton("Refresh");
        this.refreshBtn.addActionListener(e -> this.funcs.reload());
        this.refreshBtn.setEnabled(false);
        this.add(this.refreshBtn);
    }

    public void addTestBtn()
    {
        JButton testBtn = new JButton("Test");
        testBtn.addActionListener(e -> this.parent.drawInstanceFrame(null));
        this.add(testBtn);
    }
    public void addBackBtn()
    {
        JButton backBtn = new JButton("Back to All Instances");
        backBtn.addActionListener(e -> this.parent.drawPrismFrame());
        this.add(backBtn);
    }

    public void enable()
    {
        if (this.exportBtn != null) this.exportBtn.setEnabled(true);
        if (this.importBtn != null) this.importBtn.setEnabled(true);
        if (this.urlsBtn != null) this.urlsBtn.setEnabled(true);
        if (this.refreshBtn != null) this.refreshBtn.setEnabled(true);
    }

    private void errorPopup(String msg)
    {
        JOptionPane.showMessageDialog(
            this.parent,
            msg,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void handleURLs()
    {
        logger.info("URLs pressed");
        InstanceFrame inst = (InstanceFrame) this.funcs;

        ArrayList<URLObj> urls = UrlDialog.create(
            this.parent, this.funcs.getURLs(),
            inst.getInstancePath().toString()
        );
        this.funcs.setURLs(urls);
    }

    private void handleExport()
    {
        logger.info("Export pressed");
        InstanceFrame inst = (InstanceFrame) this.funcs;

        JFileChooser fc = new JFileChooser();
        String filename = AugUtils.getNewFilename(inst.getInstancePath());
        fc.setSelectedFile(new File(filename));

        int option = fc.showSaveDialog(this.parent);
        if (option != JFileChooser.APPROVE_OPTION) {
            logger.info("Selection canceled");
            return;
        }

        File file = fc.getSelectedFile();
        String path = file.getPath();

        logger.info("Selected: " + path);

        if (!path.endsWith(AugUtils.fileExt))
            path = path + AugUtils.fileExt;

        Augment aug = this.funcs.getAugment();
        OSUtils.writeFile(path, aug.toString());
    }

    private void handleImport()
    {
        logger.info("Import pressed");
        JFileChooser fc = new JFileChooser();
        int option = fc.showOpenDialog(this.parent);
        if (option != JFileChooser.APPROVE_OPTION) {
            logger.info("Selection canceled");
            return;
        }

        File file = fc.getSelectedFile();
        String path = file.getPath();
        logger.info("Selected: " + path);

        if (!path.endsWith(AugUtils.fileExt))
        {
            errorPopup("Select a " + AugUtils.fileExt + " file");
            logger.severe("Bad file selected");
            return;
        }

        String rawFile = OSUtils.readFile(path);
        Augment augToImport = Augment.fromString(rawFile);

        if (augToImport == null)
        {
            errorPopup("Bad version of Augment file, please use a new one");
            logger.severe("Bad file selected: Augment versions do not match");
            return;
        }

        Augment currAug = this.funcs.getAugment();
        Path instancePath = null;
        if (this.funcs instanceof InstanceFrame)
        {
            if (!augToImport.versions.equals(currAug.versions))
            {
               errorPopup("Versions do not match");
               logger.severe("Bad file selected: Loaders do not match");
               return;
            }
            instancePath = ((InstanceFrame) this.funcs).getInstancePath();
        }

        if (this.funcs instanceof PrismFrame)
        {
            String filename = file.getName().substring(0, file.getName().length() - AugUtils.fileExt.length());

            // Create Empty Directory
            instancePath = ((PrismFrame) this.funcs).getPrismPath();
            instancePath = instancePath.resolve(filename);
            OSUtils.createDirectories(instancePath);

            // Create mmc-pack.json from Augment file being imported
            String mmcPackStr = augToImport.mmcPackJson;
            Path mmcPackPath = instancePath.resolve(PrismUtils.versionsFile);
            OSUtils.writeFile(mmcPackPath.toString(), mmcPackStr + "\n");

            // Create instance.cfg
            Path instanceCfgPath = instancePath.resolve(PrismUtils.instanceCfgFile);
            OSUtils.writeFile(instanceCfgPath.toString(), "[General]\nConfigVersion=1.2\nInstanceType=OneSix\niconKey=default\nname=" + filename + "\n");
        }

        new ImportDialog(this.parent, augToImport, currAug, instancePath);

        this.funcs.reload();
    }
}
