package com.konoca.frames.panels;

import com.konoca.Constants;
import com.konoca.frames.MainFrame;
import com.konoca.utils.OSUtils;

import java.util.function.Predicate;
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

    private MainFrame parent;
    private JTextField textField;
    private JButton browseBtn;
    private JButton searchBtn;

    private String errorMsg;
    private Predicate<String> searchValidation;
    private Predicate<String> postSearch;

    public InputPanel(
            MainFrame parent,
            String placeholderTxt, String errorMsg,
            Predicate<String> searchValidation,
            Predicate<String> postSearch
        )
    {
        this.parent = parent;
        this.errorMsg = errorMsg;
        this.searchValidation = searchValidation;
        this.postSearch = postSearch;

        this.addPathTextField(placeholderTxt);
        this.addBrowseBtn();
        this.addSearchBtn();
    }

    private void addPathTextField(String placeholderTxt)
    {
        this.textField = new JTextField(placeholderTxt);
        Dimension size = new Dimension(Constants.WindowWidth / 2, 25);

        this.textField.setPreferredSize(size);
        this.add(this.textField);
    }
    public void setTextFieldText(String text)
    {
        this.textField.setText(text);
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

        boolean isValid = this.searchValidation.test(path);
        logger.info("IsValid: " + isValid);
        if (!isValid)
        {
            errorPopup();
            return;
        }

        this.postSearch.test(path);
    }

    private void errorPopup()
    {
        JOptionPane.showMessageDialog(
            this.parent,
            this.errorMsg,
            "Invalid path",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
