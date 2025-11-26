package com.konoca.frames;

import java.util.ArrayList;
import java.util.logging.Logger;
import com.konoca.Constants;
import com.konoca.frames.panels.ActionPanel;
import com.konoca.frames.panels.InputPanel;
import com.konoca.frames.panels.TablePanel;
import com.konoca.objs.Augment;
import com.konoca.objs.URLObj;
import com.konoca.utils.OSUtils;
import com.konoca.utils.PrismUtils;

import java.awt.BorderLayout;
import java.nio.file.Path;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
    private static Logger logger = Logger.getLogger(MainFrame.class.getName());

    private InputPanel inputPanel;
    private TablePanel tablePanel;
    private ActionPanel actionPanel;

    private Path instancePath;
    private ArrayList<URLObj> urls;

    public MainFrame()
    {
        logger.info("Initializing");

        this.setTitle(Constants.WindowTitle);
        this.setSize(Constants.WindowWidth, Constants.WindowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.urls = new ArrayList<>();

        this.inputPanel = new InputPanel(this);
        this.add(this.inputPanel, BorderLayout.NORTH);
        this.tablePanel = new TablePanel(this);
        this.add(this.tablePanel, BorderLayout.CENTER);
        this.actionPanel = new ActionPanel(this);
        this.add(this.actionPanel, BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    public void setInstancePath(String pathString)
    {
        Path path = OSUtils.getPath(pathString);
        this.setInstancePath(path);
    }
    public void setInstancePath(Path path)
    {
        this.instancePath = path;
        this.tablePanel.reloadData(path);
        this.actionPanel.enable();
    }

    public void reload()
    {
        this.tablePanel.reloadData(this.instancePath);
    }

    public Path getInstancePath()
    {
        return this.instancePath;
    }

    public void setURLs(ArrayList<URLObj> urls)
    {
        this.urls = urls;
    }
    public ArrayList<URLObj> getURLs()
    {
        return this.urls;
    }

    public Augment getAugment()
    {
        Augment aug = new Augment();
        aug.urls = this.urls;
        aug.mods = PrismUtils.getModInfo(this.instancePath);

        logger.info("-- Augment --");
        logger.info(aug.toString());

        return aug;
    }
}
