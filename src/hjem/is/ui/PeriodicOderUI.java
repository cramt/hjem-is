package hjem.is.ui;

import hjem.is.controller.PeriodicPlanController;

import javax.swing.*;

public class PeriodicOderUI extends JFrame {
    public PeriodicOderUI(PeriodicPlanController plan) {
        setSize(400, 400);
        setContentPane(new JPanel());
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 400);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel);

        setLayout(null);

        setVisible(true);
    }
}
