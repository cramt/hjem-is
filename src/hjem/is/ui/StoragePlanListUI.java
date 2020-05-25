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
        JPanel storagePlans = new JPanel();
        panel.add(storagePlans);
        for (String name : controller.getNames()) {
            JButton button = new JButton(name);
            storagePlans.add(button);
        }
        controller.addOnSaveListener(x -> {
            JButton button = new JButton(x.getName());
            storagePlans.add(button);
            validate();
            repaint();
        });

        setLayout(null);

        setVisible(true);
    }


}

