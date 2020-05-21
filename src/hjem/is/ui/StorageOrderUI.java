package hjem.is.ui;

import hjem.is.controller.StorageOrderController;
import hjem.is.controller.StoragePlanController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StorageOrderUI extends JFrame {
    StoragePlanController controller;

    public StorageOrderUI() {
        controller = new StoragePlanController();
        List<JComponent> components = new ArrayList<>();
        JButton generateNew = new JButton("Generate New");
        components.add(generateNew);
        JLabel label = new JLabel("current storage plans");
        components.add(label);
        var names = controller.getNames();
        System.out.println(names);
        for (String name : names) {
            JButton button = new JButton(name);
            components.add(button);
        }

        order(components);

        setSize(400, 400);

        setLayout(null);

        setVisible(true);
    }

    private void order(List<JComponent> components) {
        int i = 0;
        for (JComponent component : components) {
            add(component);
            component.setBounds(0, i, 150, 30);
            i += 30;
        }
    }
}

