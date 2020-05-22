package hjem.is.ui;

import hjem.is.controller.PeriodicPlanController;

import javax.swing.*;

public class PeriodicPlanUI extends MyFrame {
    public PeriodicPlanUI(PeriodicPlanController plan) {
        JPanel panel = makeMainPanel();

        setLayout(null);

        setVisible(true);
    }
}
