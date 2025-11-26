package com.konoca.frames.panels;

import com.konoca.Constants;
import com.konoca.frames.MainFrame;
import com.konoca.utils.OSUtils;

import java.util.logging.Logger;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputPanel extends JPanel
{
    private static Logger logger = Logger.getLogger(InputPanel.class.getName());
    private static String placeholderTxt = "Path/To/Instance";

    private MainFrame parent;
    private JTextField textField;
    private JButton browseBtn;
    private JButton searchBtn;

    public InputPanel(MainFrame parent)
    {
        this.parent = parent;

        this.addPathTextField();
        this.addBrowseBtn();
        this.addSearchBtn();
    }

    private void addPathTextField()
    {
        this.textField = new JTextField(placeholderTxt);
        Dimension size = new Dimension(Constants.WindowWidth / 2, 25);

        this.textField.setPreferredSize(size);
        this.add(this.textField);
    }

    private void addBrowseBtn()
    {
        this.browseBtn = new JButton("Browse");
        this.browseBtn.addActionListener(e -> handleBrowse());
        this.add(this.browseBtn);
    }

    private void addSearchBtn()
    {
        this.searchBtn = new JButton("Search");
        this.searchBtn.addActionListener(e -> search());
        this.add(this.searchBtn);
    }

    private void handleBrowse()
    {
        logger.info("Browse pressed");
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fc.showOpenDialog(this.parent);

        if (option != JFileChooser.APPROVE_OPTION) {
            logger.info("Selection canceled");
            return;
        }

        File file = fc.getSelectedFile();
        String path = file.getPath();

        logger.info("Selected: " + path);
        this.textField.setText(path);
        search();
    }

    private void search()
    {
        String path = this.textField.getText();
        boolean pathExists = OSUtils.pathExists(path);

        logger.info("Path: " + path);
        logger.info("Exists: " + pathExists);

        if (!pathExists)
        {
            errorPopup();
            return;
        }

        boolean isValid = OSUtils.constainsDotMC(path);
        logger.info("IsValid: " + isValid);

        if (!isValid)
        {
            errorPopup();
            return;
        }

        this.parent.setInstancePath(path);
    }

    private void errorPopup()
    {
        JOptionPane.showMessageDialog(
            this.parent,
            "Please select a valid Prism Instance folder",
            "Invalid path",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
