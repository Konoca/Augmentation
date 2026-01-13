package com.konoca.frames.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.konoca.objs.ModObj;
import com.konoca.objs.VersionObj;
import com.konoca.utils.PrismUtils;

public class ModsTable extends JPanel
{
    private static Logger logger = Logger.getLogger(InputPanel.class.getName());
    private static String[] modColumns = {"Enabled", "Mod Name", "Mod Version", "Provider", "Loaders", "MC Versions"};


    private JTable modTable;
    private JScrollPane modScrollPane;

    private JTable versionTable;
    private JScrollPane versionSP;

    public ModsTable()
    {
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
        Object[][] data = {{"", "", "", "", "", ""}};

        this.modTable = new JTable(data, modColumns);
        this.modTable.setDefaultEditor(Object.class, null);
        this.modScrollPane = new JScrollPane(this.modTable);

        // this.modTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
        //     @Override
        //     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //         if (value instanceof String) return null;
        //         JCheckBox cbx = new JCheckBox();
        //         cbx.setSelected((Boolean) value);
        //         return cbx;
        //     }
        // });

        this.add(this.modScrollPane, BorderLayout.CENTER);
    }

    public void reloadData(Path instancePath)
    {
        this.reloadVersionsTable(instancePath);
        this.reloadModTable(instancePath);
    }

    private void reloadVersionsTable(Path instancePath)
    {
        ArrayList<VersionObj> versions = PrismUtils.getVersionInfo(instancePath);
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> rows = new ArrayList<>();

        versions.forEach((VersionObj v) -> {
            columns.add(v.getName());
            rows.add(v.getValue());
        });

        Object[][] data = { rows.toArray() };

        DefaultTableModel model = new DefaultTableModel(data, columns.toArray());
        this.versionTable.setModel(model);
    }

    private void reloadModTable(Path instancePath)
    {
        ArrayList<ModObj> mods = PrismUtils.getModInfo(instancePath);

        ArrayList<Object[]> data = new ArrayList<>();
        mods.forEach((ModObj mod) -> {
            Object[] row = {
                mod.getIsEnabled(),
                // mod.enabled,
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
