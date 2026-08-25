package com.konoca.frames.dialogs;

import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.konoca.Updater;
import com.konoca.frames.MainFrame;

public class UpdateDialog extends JDialog
{
    private static Logger logger = Logger.getLogger(UpdateDialog.class.getName());

    private JLabel label;

    private JPanel actionPanel;
    private JButton updateBtn;
    private JButton okBtn;

    public UpdateDialog(MainFrame parent)
    {
        logger.info("Initializing");

        this.setLocationRelativeTo(parent);
        this.setTitle("Updater");
        this.setSize(300, 100);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModal(true);

        this.label = new JLabel("There is an update available");
        this.label.setHorizontalAlignment(SwingConstants.CENTER);
        this.label.setHorizontalTextPosition(SwingConstants.CENTER);
        this.label.setVerticalAlignment(SwingConstants.CENTER);
        this.label.setVerticalTextPosition(SwingConstants.CENTER);
        this.add(this.label);

        this.actionPanel = new JPanel();

        this.updateBtn = new JButton("Update");
        this.updateBtn.addActionListener(e -> this.update());
        this.actionPanel.add(this.updateBtn);

        this.okBtn = new JButton("No");
        this.okBtn.addActionListener(e -> this.dispose());
        this.actionPanel.add(this.okBtn);

        this.add(this.actionPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public void drawError(String errMsg) {
        this.setVisible(false);
        this.remove(this.label);
        this.remove(this.actionPanel);

        this.label = new JLabel(errMsg);
        this.label.setHorizontalAlignment(SwingConstants.CENTER);
        this.label.setHorizontalTextPosition(SwingConstants.CENTER);
        this.label.setVerticalAlignment(SwingConstants.CENTER);
        this.label.setVerticalTextPosition(SwingConstants.CENTER);
        this.add(this.label);

        this.actionPanel = new JPanel();
        this.okBtn = new JButton("Ok");
        this.okBtn.addActionListener(e -> this.dispose());
        this.actionPanel.add(this.okBtn);
        this.add(this.actionPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private void update() {
        String error = Updater.downloadLatest();
        if (error.isEmpty()) {
            this.dispose();
            System.exit(0);
        }

        drawError(error);
    }
}
