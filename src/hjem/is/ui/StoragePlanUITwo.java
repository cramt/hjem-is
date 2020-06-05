package hjem.is.ui;

import hjem.is.model.time.Period;
import java.util.List;

import hjem.is.controller.StoragePlanController;
import java.awt.Font;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;

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

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		//create Period buttons
		for (int i = 0; i < periods.size(); i++) {
            Period period = periods.get(i);
            JButton button = new JButton("Periode: " + (period.getStart() + 1) + " - " + period.getEnd());
            
            button.setPreferredSize(new Dimension(250, 40));
            button.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
            panel.add(button);
            final int finalI = i;
            button.addActionListener(e -> {
            	//new PeriodicPlanUI(controller.getPeriodicPlanController(finalI));
                new PeriodicPlanUITwo(controller.getPeriodicPlanController(finalI));
            });
        }

		JPanel buttonPanelContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		buttonPanelContainer.setBounds(20, 21, 900, 60);
		buttonPanelContainer.add(panel);

		//Scroll pane
		JScrollPane scrollPane = new JScrollPane(buttonPanelContainer);
		scrollPane.setBounds(0, 37, 520, 602);
		periodicPlanFullPanel.add(scrollPane);
		//getContentPane().add(scrollPane);
		
		JLabel periodPlansLabel = new JLabel("Periode planer for " + controller.getName() + ":");
		periodPlansLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		periodPlansLabel.setBounds(0, 0, 360, 32);
		periodicPlanFullPanel.add(periodPlansLabel);

		//Right side of UI
		JPanel savePanel = new JPanel();
		savePanel.setBounds(531, 0, 433, 639);
		periodicPlanFullPanel.add(savePanel);
		savePanel.setLayout(null);

		//'Name of plan' field
		nameField = new JTextField();
		nameField.setToolTipText("Indtast navn");
		if (controller.getName() != null) {
			nameField.setText(controller.getName());
		}
		nameField.setBounds(10, 6, 416, 32);
		savePanel.add(nameField);
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

		JButton delete = new JButton("Slet");
		delete.setBounds(219, 90, 117, 35);
		delete.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		delete.addActionListener(e -> {
			
			controller.delete();
			close();
		});
		savePanel.add(delete);

		JToggleButton active = new JToggleButton("Aktiv");
		active.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		active.setBounds(10, 49, 127, 35);
		savePanel.add(active);
		active.setSelected(controller.isActive());
		
		JLabel giveNameLabel = new JLabel("Navngiv plan:");
		giveNameLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 17));
		giveNameLabel.setBounds(391, 0, 129, 32);
		periodicPlanFullPanel.add(giveNameLabel);
		active.addActionListener(e -> controller.setActive(active.isSelected()));

		setTitle("Periodeplaner");
		repaint();

		//make sure the StoragePlanListUI is updated when this window is closed
		/*addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame,
		            "Are you sure you want to close this window?", "Close Window?",
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            System.exit(0);
		        }
		    }
		});
		*/
        setVisible(true);
	}
}
