package hjem.is.ui;

import hjem.is.controller.StoragePlanController;

import javax.swing.*;

public class StoragePlanListUI extends MyFrame {

    StoragePlanController controller;

    public StoragePlanListUI() {
        controller = new StoragePlanController();
        JPanel panel = makeMainPanel();
        JButton generateNew = new JButton("Generer ny lagerplan");
        panel.add(generateNew);
        generateNew.addActionListener(e -> {
            controller.generateNew("navn");
            new StoragePlanUI(controller);
        });
        JLabel label = new JLabel("Nuværende lagerplaner");
        panel.add(label);
        for (String name : controller.getNames()) {
            JButton button = new JButton(name);
            panel.add(button);
        }

        setSize(400, 400);

        setLayout(null);

        setVisible(true);
    }


}

