package com.konoca.frames;

import java.util.ArrayList;
import java.util.logging.Logger;
import com.konoca.Constants;
import com.konoca.objs.Augment;
import com.konoca.objs.URLObj;
import com.konoca.utils.PrismUtils;

import java.nio.file.Path;

import javax.swing.JFrame;

public class MainFrame extends JFrame
{
    private static Logger logger = Logger.getLogger(MainFrame.class.getName());

    public MainFrame()
    {
        logger.info("Initializing");

        this.setTitle(Constants.WindowTitle);
        this.setSize(Constants.WindowWidth, Constants.WindowHeight);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPrismFrame();
        // drawInstanceFrame(null);
    }

    public void drawPrismFrame()
    {
        logger.info("Drawing Prism Frame");
        new PrismFrame(this);
    }
    public void drawInstanceFrame(String instancePath)
    {
        logger.info("Drawing Instance Frame : " + instancePath);
        new InstanceFrame(this, instancePath);
    }

    public Augment getAugment(Path instancePath, ArrayList<URLObj> urls)
    {
        Augment aug = new Augment();
        aug.versions = PrismUtils.getVersionInfo(instancePath);
        aug.urls = urls;
        aug.mods = PrismUtils.getModInfo(instancePath);
        aug.mmcPackJson = PrismUtils.getVersionFile(instancePath);

        logger.info("-- Augment --");
        logger.info(aug.toString());

        return aug;
    }
}
