package com.konoca.frames.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.konoca.frames.MainFrame;
import com.konoca.objs.ModObj;
import com.konoca.utils.PrismUtils;

public class TablePanel extends JPanel
{
    private static Logger logger = Logger.getLogger(InputPanel.class.getName());
    private static String[] modColumns = {"Enabled", "Mod Name", "Mod Version", "Provider", "Loaders", "MC Versions"};

    private MainFrame parent;

    private JTable modTable;
    private JScrollPane modScrollPane;

    private JTable versionTable;
    private JScrollPane versionSP;

    public TablePanel(MainFrame parent)
    {
        this.parent = parent;
        this.setLayout(new BorderLayout());

        this.addVersionsTable();
        this.addModTable();
    }

    private void addVersionsTable()
    {
        String[] cols = {"Minecraft"};
        String[][] data = {{""}};

        this.versionTable = new JTable(data, cols);
        this.versionSP = new JScrollPane(this.versionTable);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        this.versionTable.setDefaultRenderer(Object.class, centerRenderer);
        this.versionTable.setDefaultEditor(Object.class, null);

        int headerHeight = versionTable.getTableHeader().getPreferredSize().height;
        int rowHeight = versionTable.getRowHeight();
        versionSP.setPreferredSize(new Dimension(400, headerHeight + rowHeight + 2));

        this.add(this.versionSP, BorderLayout.NORTH);
    }

    private void addModTable()
    {
        String[][] data = {{"", "", "", "", "", ""}};

        this.modTable = new JTable(data, modColumns);
        this.modTable.setDefaultEditor(Object.class, null);
        this.modScrollPane = new JScrollPane(this.modTable);

        this.add(this.modScrollPane, BorderLayout.CENTER);
    }

    public void reloadData(Path instancePath)
    {
        this.reloadVersionsTable(instancePath);
        this.reloadModTable(instancePath);
    }

    private void reloadVersionsTable(Path instancePath)
    {
        Map<String, String> versions = PrismUtils.getVersionInfo(instancePath);
        Object[] columns = versions.keySet().toArray();
        Object[][] data = {versions.values().toArray()};

        DefaultTableModel model = new DefaultTableModel(data, columns);
        this.versionTable.setModel(model);
    }

    private void reloadModTable(Path instancePath)
    {
        ArrayList<ModObj> mods = PrismUtils.getModInfo(instancePath);

        ArrayList<String[]> data = new ArrayList<>();
        mods.forEach((ModObj mod) -> {
            String[] row = {
                mod.getIsEnabled(),
                mod.name,
                mod.provider.getVersionID(),
                mod.provider.getName(),
                mod.loaders,
                mod.mcversions
            };
            data.add(row);
        });

        Object[][] rows = new Object[data.size()][];

        for (int i = 0; i < data.size(); i++) {
            rows[i] = data.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(rows, modColumns);
        this.modTable.setModel(model);
    }
}
