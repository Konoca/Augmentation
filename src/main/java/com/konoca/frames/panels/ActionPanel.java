package com.konoca.frames.panels;

import java.util.ArrayList;
import java.util.logging.Logger;

import java.io.File;
import java.lang.reflect.Array;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.konoca.frames.UrlDialog;
import com.konoca.frames.ImportDialog;
import com.konoca.frames.MainFrame;
import com.konoca.objs.Augment;
import com.konoca.objs.URLObj;
import com.konoca.utils.AugUtils;
import com.konoca.utils.OSUtils;

public class ActionPanel extends JPanel
{
    private static Logger logger = Logger.getLogger(ActionPanel.class.getName());

    private MainFrame parent;
    private JButton exportBtn;
    private JButton importBtn;
    private JButton urlsBtn;

    public ActionPanel(MainFrame parent)
    {
        this.parent = parent;

        this.addUrlsBtn();
        this.addExportBtn();
        this.addImportBtn();
    }

    private void addExportBtn()
    {
        this.exportBtn = new JButton("Export");
        this.exportBtn.addActionListener(e -> handleExport());
        this.exportBtn.setEnabled(false);
        this.add(this.exportBtn);
    }

    private void addImportBtn()
    {
        this.importBtn = new JButton("Import");
        this.importBtn.addActionListener(e -> handleImport());
        this.importBtn.setEnabled(false);
        this.add(this.importBtn);
    }

    private void addUrlsBtn()
    {
        this.urlsBtn = new JButton("Add URLs");
        this.urlsBtn.addActionListener(e -> handleURLs());
        this.urlsBtn.setEnabled(false);
        this.add(this.urlsBtn);
    }

    public void enable()
    {
        this.exportBtn.setEnabled(true);
        this.importBtn.setEnabled(true);
        this.urlsBtn.setEnabled(true);
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
        ArrayList<URLObj> urls = UrlDialog.create(this.parent, this.parent.getURLs());
        this.parent.setURLs(urls);
    }

    private void handleExport()
    {
        logger.info("Export pressed");
        JFileChooser fc = new JFileChooser();

        String filename = AugUtils.getNewFilename(this.parent.getInstancePath());
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

        Augment aug = this.parent.getAugment();
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
        Augment aug = Augment.fromString(rawFile);

        if (aug == null)
        {
            errorPopup("Bad version");
            logger.severe("Bad file selected");
            return;
        }

        new ImportDialog(this.parent, aug);
    }
}
