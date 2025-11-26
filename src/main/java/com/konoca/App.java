package com.konoca;

import javax.swing.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.konoca.frames.MainFrame;

import java.util.logging.Logger;

public class App
{
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args)
    {
        logger.info("Starting up Augmentation...");

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
