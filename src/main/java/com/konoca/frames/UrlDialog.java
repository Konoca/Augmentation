package com.konoca.frames;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.konoca.objs.URLObj;
import com.konoca.utils.OSUtils;

public class UrlDialog extends JDialog
{
    private static Logger logger = Logger.getLogger(UrlDialog.class.getName());

    public static final String[] cols = {"URL", "Path from Instance", "Is ZIP"};
    private Object[] defaultRow = {"https://download.url", "", "Y"};

    private JTable table;
    private JScrollPane scrollPane;

    private JPanel actionPanel;
    private JButton addBtn;
    private JButton removeBtn;
    private JButton okBtn;

    private DefaultTableModel model;

    public UrlDialog(MainFrame parent, DefaultTableModel model)
    {
        logger.info("Initializing");
        this.model = model;
        this.model.setColumnIdentifiers(cols);

        this.setLocationRelativeTo(parent);
        this.setTitle("Add Download URLs");
        this.setSize(500, 400);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setModal(true);

        String mcDir = OSUtils.getMCdir(parent.getInstancePath());
        this.defaultRow[1] = mcDir + "/";

        this.table = new JTable(model);
        this.scrollPane = new JScrollPane(this.table);
        this.add(this.scrollPane, BorderLayout.CENTER);

        this.actionPanel = new JPanel();

        this.addBtn = new JButton("Add");
        this.addBtn.addActionListener(e -> this.model.addRow(defaultRow));
        this.actionPanel.add(this.addBtn);

        this.removeBtn = new JButton("Remove");
        this.removeBtn.addActionListener(e -> this.model.removeRow(this.table.getSelectedRow()));
        this.actionPanel.add(this.removeBtn);

        this.okBtn = new JButton("OK");
        this.okBtn.addActionListener(e -> this.dispose());
        this.actionPanel.add(this.okBtn);

        this.add(this.actionPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    public static ArrayList<URLObj> create(MainFrame parent, ArrayList<URLObj> startingURLs)
    {
        Object[][] rows = new Object[startingURLs.size()][];
        for (int i = 0; i < startingURLs.size(); i++) {
            URLObj obj = startingURLs.get(i);
            rows[i] = new Object[3];
            rows[i][0] = obj.getURL();
            rows[i][1] = obj.getRelativePath();
            rows[i][2] = obj.getIsZip();
        }

        DefaultTableModel model = new DefaultTableModel(rows, UrlDialog.cols);
        new UrlDialog(parent, model);

        ArrayList<URLObj> urls = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++)
        {
            String url = (String) model.getValueAt(i, 0);
            String path = (String) model.getValueAt(i, 1);
            String isZip = (String) model.getValueAt(i, 2);

            logger.info("ZIP="+isZip+" URL="+url+" Path="+path);
            URLObj dl = new URLObj(url, path, isZip.equals("Y"));
            urls.add(dl);
        }
        return urls;
    }
}
