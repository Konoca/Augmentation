package com.konoca.frames;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.konoca.frames.panels.ActionPanel;
import com.konoca.frames.panels.InputPanel;
import com.konoca.frames.panels.InstancesGrid;
import com.konoca.objs.Augment;
import com.konoca.utils.OSUtils;

import java.awt.BorderLayout;
import java.nio.file.Path;

public class PrismFrame extends FrameFuncs
{
    private static Logger logger = Logger.getLogger(PrismFrame.class.getName());
    private static String placeholderTxt = "Path/To/Prism/Instances";
    private static String errorMsg = "Please select a valid folder";

    private MainFrame mainFrame;

    protected InputPanel inputPanel;
    private InstancesGrid instGrid;
    private ActionPanel actionPanel;

    private Path prismPath;

    public Predicate<String> searchValidation = (String path) -> {
        return true;
    };

    public Predicate<String> postSearch = (String path) -> {
        this.setPrismPath(path);
        return true;
    };

    public PrismFrame(MainFrame m)
    {
        this.mainFrame = m;
        this.urls = new ArrayList<>();

        String path = OSUtils.getSetting("PrismPath");
        logger.info("Prism path: " + path);
        prismPath = OSUtils.getPath(path);

        m.getContentPane().removeAll();
        this.inputPanel = new InputPanel(m, placeholderTxt, errorMsg, searchValidation, postSearch);
        m.add(this.inputPanel, BorderLayout.NORTH);

        this.instGrid = new InstancesGrid(m, this);
        m.add(this.instGrid, BorderLayout.CENTER);

        this.actionPanel = new ActionPanel(m, this);
        this.actionPanel.addImportBtn();
        this.actionPanel.addRefreshBtn();
        // this.actionPanel.addTestBtn();
        m.add(this.actionPanel, BorderLayout.SOUTH);

        m.pack();
        m.setVisible(true);

        if (prismPath != null)
        {
            this.actionPanel.enable();
            this.inputPanel.setTextFieldText(path);
        }
    }

    public void setPrismPath(String pathString)
    {
        Path path = OSUtils.getPath(pathString);
        if (!OSUtils.pathExists(path)) return;

        this.prismPath = path;
        OSUtils.setSetting("PrismPath", path.toString());
        this.actionPanel.enable();
    }

    public void reload()
    {
        this.instGrid.loadInstances();
    }

    public Augment getAugment()
    {
        return Augment.blank();
    }

    public Path getPrismPath()
    {
        return this.prismPath;
    }
}
