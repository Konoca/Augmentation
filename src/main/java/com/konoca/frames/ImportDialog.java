package com.konoca.frames;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.konoca.objs.Augment;
import com.konoca.objs.ModObj;
import com.konoca.objs.URLObj;
import com.konoca.utils.OSUtils;
import com.konoca.utils.PrismUtils;

public class ImportDialog extends JDialog
{
    private static Logger logger = Logger.getLogger(ImportDialog.class.getName());

    private JLabel label;
    private JPanel actionPanel;
    private JButton proceedBtn;
    private JButton cancelBtn;


    public ImportDialog(MainFrame parent, Augment aug)
    {
        logger.info("Initializing");
        this.setLocationRelativeTo(parent);

        this.setTitle("Warning");
        this.setSize(300, 100);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModal(true);

        this.label = new JLabel("Are you sure you would like to continue?");
        this.label.setHorizontalAlignment(SwingConstants.CENTER);
        this.label.setHorizontalTextPosition(SwingConstants.CENTER);
        this.label.setVerticalAlignment(SwingConstants.CENTER);
        this.label.setVerticalTextPosition(SwingConstants.CENTER);
        this.add(this.label);

        this.actionPanel = new JPanel();
        this.proceedBtn = new JButton("Yes");
        this.proceedBtn.addActionListener(e -> this.handleImport(parent, aug));
        this.actionPanel.add(this.proceedBtn);

        this.cancelBtn = new JButton("No");
        this.cancelBtn.addActionListener(e -> this.dispose());
        this.actionPanel.add(this.cancelBtn);

        this.add(this.actionPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void handleImport(MainFrame parent, Augment aug)
    {
        this.setTitle("Importing");
        this.remove(this.actionPanel);
        this.label.setText("Loading Augment...");

        ArrayList<URLObj> urls = UrlDialog.create(parent, aug.urls);

        Path instancePath = parent.getInstancePath();
        urls.forEach(url -> OSUtils.downloadFile(url, instancePath));

        Augment currAug = parent.getAugment();

        ArrayList<ModObj> addMods = new ArrayList<>(aug.mods);
        addMods.removeAll(currAug.mods);

        ArrayList<ModObj> deleteMods = new ArrayList<>(currAug.mods);
        deleteMods.removeAll(aug.mods);

        Path modsPath = instancePath.resolve(".minecraft", "mods");
        deleteMods.forEach(mod -> {
            this.label.setText("Deleting " + mod.name);
            String filename = mod.enabled ? mod.filename : mod.filename + ".disabled";
            Path path = modsPath.resolve(filename);
            Path tomlPath = modsPath.resolve(".index", mod.tomlName);

            logger.info("Deleting " + path.toString());
            logger.info("Deleting " + tomlPath.toString());

            OSUtils.deleteFile(path);
            OSUtils.deleteFile(tomlPath);
        });

        addMods.forEach(mod -> {
            this.label.setText("Downloading " + mod.name);

            Path path = modsPath.resolve(mod.filename);
            logger.info("Downloading " + path.toString());
            mod.download(instancePath);

            Path tomlPath = modsPath.resolve(".index", mod.tomlName);
            logger.info("Writing TOML: " + tomlPath.toString());
            OSUtils.writeFile(tomlPath.toString(), mod.toTOML());
        });

        aug.mods.forEach(mod -> {
            boolean isEnabled = PrismUtils.isModEnabled(instancePath, mod.filename);
            boolean shouldBeEnabled = mod.enabled;

            if (isEnabled && !shouldBeEnabled)
            {
                logger.info("Disabling " + mod.filename);
                this.label.setText("Disabling " + mod.filename);

                Path path = modsPath.resolve(mod.filename);
                Path newPath = modsPath.resolve(mod.filename + ".disabled");

                OSUtils.moveFile(path, newPath);
                return;
            }

            if (!isEnabled && shouldBeEnabled)
            {
                logger.info("Enabling " + mod.filename);
                this.label.setText("Enabling " + mod.filename);

                Path path = modsPath.resolve(mod.filename + ".disabled");
                Path newPath = modsPath.resolve(mod.filename);

                OSUtils.moveFile(path, newPath);
                return;
            }
        });

        this.label.setText("Done!");
        this.actionPanel.remove(this.proceedBtn);
        this.cancelBtn.setText("Close");
        this.add(this.actionPanel, BorderLayout.SOUTH);
    }
}
