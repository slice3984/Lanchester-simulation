package utils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PanelUpdater implements ActionListener {
    private ArrayList<JPanel> panels;
    private Timer timer;
    private int updateTime = 0;

    public PanelUpdater(int fps) {
        panels = new ArrayList<>();
        updateTime = 1000 / fps;
        timer = new Timer(updateTime, this);
    }

    public void addPanel(JPanel panel) {
        panels.add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JPanel panel : panels) {
            panel.repaint();
            panel.getTopLevelAncestor().repaint();
        }
    }
    public void run() {
        timer.start();
    }
}
