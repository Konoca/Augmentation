package com.konoca;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.konoca.frames.MainFrame;
import com.konoca.providers.Curseforge;
import com.konoca.providers.Provider;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class App
{
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args)
    {
        logger.info("Starting up Augmentation...");
        logger.info("Using version " + Constants.VERSION);

        if (args.length > 0) {
            logger.info("Entering DEBUG mode");
            Constants.DEBUG = true;

            System.out.println("Passed args: ");
            for (int i = 0; i < args.length; i++) System.out.printf("[%d] %s\n", i, args[i]);

            if (args[0].equalsIgnoreCase("-cf")) {
                logger.info("CurseForge debugging");
                Provider cf = new Curseforge();
                cf.setModVersionIDs(args[1], args[2]);

                String userHome = System.getProperty("user.home");
                Path downloads = Paths.get(userHome, "Downloads");

                cf.download(downloads, "", "");
                return;
            }
        }

        try {
            logger.info("Using FlatDark Laf");
            FlatDarkLaf.setup();
        } catch (Exception e) {
            logger.severe("Error setting Laf: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
