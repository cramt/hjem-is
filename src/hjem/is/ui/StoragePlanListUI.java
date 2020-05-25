package hjem.is.ui;

import hjem.is.controller.StoragePlanController;

import javax.swing.*;

public class StoragePlanListUI extends MyFrame {

    StoragePlanController controller;

    public StoragePlanListUI() {
        controller = new StoragePlanController();
        JPanel panel = makeMainPanel();
        JButton generateNew = new JButton("Generate New");
        panel.add(generateNew);
        generateNew.addActionListener(e -> {
            controller.generateNew("new name");
            new StoragePlanUI(controller);
        });
        JLabel label = new JLabel("current storage plans");
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

