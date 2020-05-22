package hjem.is.ui;

import hjem.is.controller.StoragePlanController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StoragePlanListUI extends MyFrame {

    StoragePlanController controller;

    public StoragePlanListUI() {
        controller = new StoragePlanController();
        List<JComponent> components = new ArrayList<>();
        JButton generateNew = new JButton("Generate New");
        components.add(generateNew);
        generateNew.addActionListener(e -> {
            controller.generateNew("new name");
            new StoragePlanUI(controller);
        });
        JLabel label = new JLabel("current storage plans");
        components.add(label);
        for (String name : controller.getNames()) {
            JButton button = new JButton(name);
            components.add(button);
        }

        order(components);

        setSize(400, 400);

        setLayout(null);

        setVisible(true);
    }


}

