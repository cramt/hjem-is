package hjem.is.ui;

import hjem.is.controller.StoragePlanController;
import hjem.is.model.time.Period;

import javax.swing.*;
import java.util.List;

public class StoragePlanUI extends MyFrame {
    public StoragePlanUI(StoragePlanController controller) {
        setSize(400, 400);
        setContentPane(new JPanel());
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 400);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel);
        JButton save = new JButton("save");
        panel.add(save);
        save.addActionListener(e->{
            controller.save();
            close();
        });
        JTextField name = new JTextField(controller.getName());
        panel.add(name);
        name.addActionListener(e -> controller.setName(name.getText()));
        JToggleButton active = new JToggleButton("active");
        panel.add(active);
        active.setSelected(controller.isActive());
        active.addActionListener(e -> controller.setActive(active.isSelected()));
        List<Period> periods = controller.getPeriods();
        JPanel scroll = new JPanel();
        scroll.setAutoscrolls(true);
        for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            JButton button = new JButton("period: " + period.getStart() + " - " + period.getEnd());
            panel.add(button);
            final int finalI = i;
            button.addActionListener(e -> {
                new PeriodicPlanUI(controller.getPeriodicPlanController(finalI));
            });
        }
        panel.add(new JScrollPane(scroll));

        setLayout(null);

        setVisible(true);
    }
}
