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


        JTextField name = new JTextField(controller.getName());
        add(name);
        JToggleButton active = new JToggleButton("active");
        add(active);
        List<Period> periods = controller.getPeriods();
        JScrollPane scroll = new JScrollPane();
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            JButton button = new JButton("period: " + period.getStart() + " - " + period.getEnd());
            scroll.add(button);
            final int finalI = i;
            button.addActionListener(e -> {
                new PeriodicOderUI(controller.getPeriodicPlan(finalI));
            });
        }
        add(scroll);

        setSize(400, 400);

        setLayout(null);

        setVisible(true);
    }
}
