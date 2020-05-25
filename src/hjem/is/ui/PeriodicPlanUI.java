package hjem.is.ui;

import hjem.is.controller.PeriodicPlanController;

import javax.swing.*;

public class PeriodicPlanUI extends MyFrame {
    private PeriodicPlanController controller;
    public PeriodicPlanUI(PeriodicPlanController controller) {
        this.controller = controller;
        JPanel panel = makeMainPanel();
        JTextField startPeriod = new JTextField("Periode start");



        setLayout(null);

        setVisible(true);
    }
}
