package com.konoca.frames.panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.konoca.frames.MainFrame;
import com.konoca.frames.PrismFrame;
import com.konoca.utils.OSUtils;
import com.konoca.utils.PrismUtils;

public class InstancesGrid extends JPanel
{
    private static Logger logger = Logger.getLogger(InstancesGrid.class.getName());

    private MainFrame mainFrame;
    private PrismFrame funcs;

    public InstancesGrid(MainFrame m, PrismFrame funcs)
    {
        this.mainFrame = m;
        this.funcs = funcs;
        this.setLayout(new GridLayout(0, 2));
        this.loadInstances();
    }

    public void loadInstances()
    {
        this.removeAll();
        OSUtils.getAllInstances(funcs.getPrismPath()).forEach(instance -> {
            String path = instance.getPath();
            // String name = instance.getName() + "/ " + PrismUtils.getInstanceName(path);
            String name = PrismUtils.getInstanceName(path);

            JButton btn = new JButton(name);
            btn.addActionListener(e -> this.mainFrame.drawInstanceFrame(path));

            btn.setPreferredSize(new Dimension(5, 40));

            this.add(btn);
        });
        this.revalidate();
        this.repaint();
    }
}
