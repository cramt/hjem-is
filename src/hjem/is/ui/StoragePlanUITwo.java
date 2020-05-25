package hjem.is.ui;

import javax.swing.JPanel;
import hjem.is.model.time.Period;
import java.util.List;

import hjem.is.controller.StoragePlanController;
import java.awt.Font;
import javax.swing.*;

public class StoragePlanUITwo extends MyFrame {
	private JPanel contentPane;
	private StoragePlanController controller;
	private JTextField nameField;
	
	public StoragePlanUITwo(StoragePlanController controller) {
        this.controller = controller;
        List<Period> periods = controller.getPeriods();
        
        //Create main window
        //setDefaultCloseOperation()
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Big container
		JPanel periodicPlanFullPanel = new JPanel();
		periodicPlanFullPanel.setBounds(10, 11, 964, 639);
		contentPane.add(periodicPlanFullPanel);
		periodicPlanFullPanel.setLayout(null);
		
		//Scroll pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 37, 521, 563);
		periodicPlanFullPanel.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		JLabel periodPlansLabel = new JLabel("Periode planer:");
		periodPlansLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		periodPlansLabel.setBounds(0, 0, 167, 32);
		periodicPlanFullPanel.add(periodPlansLabel);
		
		//Right side of UI
		JPanel savePanel = new JPanel();
		savePanel.setBounds(531, 0, 433, 598);
		periodicPlanFullPanel.add(savePanel);
		savePanel.setLayout(null);
		
		//Name of plan field
		nameField = new JTextField();
		nameField.setBounds(10, 6, 416, 32);
		savePanel.add(nameField);
		nameField.setText("indtast navn...");
		nameField.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		nameField.setColumns(10);
		nameField.addActionListener(e -> controller.setName(nameField.getText()));
		
		//save button
		JButton save = new JButton("Gem");
		save.setBounds(219, 49, 117, 35);
		savePanel.add(save);
		save.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		save.addActionListener(e -> {
            controller.setName(nameField.getText());
            controller.save();
            close();
        });
		
		JToggleButton active = new JToggleButton("Aktiv");
		active.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		active.setBounds(10, 49, 127, 35);
		savePanel.add(active);
		active.setSelected(controller.isActive());
		active.addActionListener(e -> controller.setActive(active.isSelected()));
        
		for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            JButton button = new JButton("Periode: " + (period.getStart() + 1) + " - " + period.getEnd());
            scrollPane.add(button);
            final int finalI = i;
            button.addActionListener(e -> {
            	//new PeriodicPlanUI(controller.getPeriodicPlanController(finalI));
                new PeriodicPlanUITwo(controller.getPeriodicPlanController(finalI));
            });
        }
		
        setVisible(true);
	}
}
