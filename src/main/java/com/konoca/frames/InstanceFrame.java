package com.konoca.frames;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;
import com.konoca.frames.panels.ActionPanel;
import com.konoca.frames.panels.InputPanel;
import com.konoca.frames.panels.ModsTable;
import com.konoca.objs.Augment;
import com.konoca.utils.OSUtils;

import java.awt.BorderLayout;
import java.nio.file.Path;

public class InstanceFrame extends FrameFuncs
{
    private static Logger logger = Logger.getLogger(InstanceFrame.class.getName());
    private static String placeholderTxt = "Path/To/Instance";
    private static String errorMsg = "Please select a valid Prism Instance folder";

    private MainFrame mainFrame;

    private InputPanel inputPanel;
    private ModsTable modsTable;
    private ActionPanel actionPanel;

    private Path instancePath;

    public Predicate<String> searchValidation = (String path) -> {
        String mcDir = OSUtils.getMCdir(path);
        return mcDir != null;
    };

    public Predicate<String> postSearch = (String path) -> {
        this.setInstancePath(path);
        return true;
    };

    public InstanceFrame(MainFrame m)
    {
        new InstanceFrame(m, null);
    }

    public InstanceFrame(MainFrame m, String instancePath)
    {
        logger.info("Initializing");
        m.getContentPane().removeAll();

        this.mainFrame = m;
        this.urls = new ArrayList<>();

        this.inputPanel = new InputPanel(m, placeholderTxt, errorMsg, searchValidation, postSearch);
        m.add(this.inputPanel, BorderLayout.NORTH);

        this.modsTable = new ModsTable();
        m.add(this.modsTable, BorderLayout.CENTER);

        this.actionPanel = new ActionPanel(m, this);
        this.actionPanel.addUrlsBtn();
        this.actionPanel.addExportBtn();
        this.actionPanel.addImportBtn();
        this.actionPanel.addRefreshBtn();
        this.actionPanel.addBackBtn();
        m.add(this.actionPanel, BorderLayout.SOUTH);

        m.pack();
        m.setVisible(true);

        if (instancePath != null)
        {
            this.inputPanel.setTextFieldText(instancePath);
            this.setInstancePath(instancePath);
        }
    }

    public void setInstancePath(String pathString)
    {
        Path path = OSUtils.getPath(pathString);

        this.instancePath = path;
        this.modsTable.reloadData(path);
        this.actionPanel.enable();
    }

    public void reload()
    {
        this.modsTable.reloadData(this.instancePath);
    }

    public Path getInstancePath()
    {
        return this.instancePath;
    }

    public Augment getAugment()
    {
        return this.mainFrame.getAugment(this.instancePath, this.urls);
    }
}
