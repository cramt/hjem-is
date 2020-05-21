package hjem.is.ui;

import hjem.is.controller.StoragePlanController;
import hjem.is.model.StorageOrder;
import hjem.is.model.StoragePlan;
import hjem.is.model.time.Period;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DisplayStorageOrderUI extends MyFrame {
    public DisplayStorageOrderUI(StoragePlanController controller) {
        List<JComponent> components = new ArrayList<>();
        JTextField name = new JTextField(controller.getName());
        components.add(name);
        JToggleButton active = new JToggleButton("active");
        components.add(active);
        List<Period> periods = controller.getPeriods();
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            JButton button = new JButton("period: " + period.getStart() + " - " + period.getEnd());
            components.add(button);
            final int finalI = i;
            button.addActionListener(e -> {
                new PeriodicOderUI(controller.getPeriodicPlan(finalI));
            });
        }

        order(components);

        setSize(400, 400);

        setLayout(null);

        setVisible(true);
    }
}
