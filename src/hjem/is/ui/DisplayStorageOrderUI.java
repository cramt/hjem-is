package hjem.is.ui;

import hjem.is.controller.StoragePlanController;
import hjem.is.model.StorageOrder;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;
import javafx.scene.layout.FlowPane;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DisplayStorageOrderUI extends MyFrame {
    public DisplayStorageOrderUI(StoragePlanController controller) {
        setSize(400, 400);
        setContentPane(new JPanel());
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 400);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel);
        JTextField name = new JTextField(controller.getName());
        panel.add(name);
        JToggleButton active = new JToggleButton("active");
        panel.add(active);
        List<Period> periods = controller.getPeriods();
        JPanel scroll = new JPanel();
        scroll.setAutoscrolls(true);
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            JButton button = new JButton("period: " + period.getStart() + " - " + period.getEnd());
            panel.add(button);
            final int finalI = i;
            button.addActionListener(e -> {
                new PeriodicOderUI(controller.getPeriodicPlan(finalI));
            });
        }
        panel.add(new JScrollPane(scroll));

        setLayout(null);

        setVisible(true);
    }
}
