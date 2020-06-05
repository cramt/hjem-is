package hjem.is.ui;

import hjem.is.model.time.Period;
import java.util.List;

import hjem.is.controller.StoragePlanController;
import java.awt.Font;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;

public class StoragePlanUITwo extends JDialog {
	private JPanel contentPane;
	private StoragePlanController controller;
	private JTextField nameField;
	private List<Period> periods;
	
	public StoragePlanUITwo(StoragePlanController controller, Frame owner) {
		super(owner); //sets the owner of this window, so we can update data on the "owner" window
		this.controller = controller;
        periods = controller.getPeriods();
        this.setModal(false);
        
        System.out.println("Owner of StoragePlanUITwo JDialog: " + getOwner());
        
        //Create main window
        //setDefaultCloseOperation()
		setBounds(110, 110, 1000, 700);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		//Big container
		JPanel periodicPlanFullPanel = new JPanel();
		periodicPlanFullPanel.setBounds(10, 11, 964, 639);
		contentPane.add(periodicPlanFullPanel);
		periodicPlanFullPanel.setLayout(null);

		JPanel buttonPanelContainer = new JPanel();
		buttonPanelContainer.setBounds(20, 21, 900, 60);
		buttonPanelContainer.setLayout(null);
	    
		//create Period buttons
		int buttonCount = createPeriodButtons(periods, buttonPanelContainer);
		
		//Update size of the panel so scrolling works
		buttonPanelContainer.setPreferredSize(new Dimension(520, getPeriodPanelSize(buttonCount)));

		//Scroll pane
		JScrollPane scrollPane = new JScrollPane(buttonPanelContainer);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 37, 520, 602);
		periodicPlanFullPanel.add(scrollPane);
		
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
            JOptionPane.showMessageDialog(null, "Lagerplan gemt.");
            setEnabled(false);
        });

		JButton delete = new JButton("Slet");
		delete.setBounds(219, 90, 117, 35);
		delete.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		delete.addActionListener(e -> {
			controller.delete();
			JOptionPane.showMessageDialog(null, "Lagerplan slettet.");
			setEnabled(false);
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
        setVisible(true);
	}
	
	public int getButtonYPlacement(int i) {
		//return Y coordinate depending on button numbers
		//Every new button must be 45 pixels below the one before
		return i * 45;
	}
	
	public int getPeriodPanelSize(int buttonAmount) {
		//The first button start with a 10 pixel offset in Y coordinate
		//Each new button is 45 pixels below the other.
		//Each button has a height of 38 pixels
		//We'll add 10 pixels at the bottom for neatness
		//This means the total panel size would be:
		//10 + (buttonAmount * 45) + 38 + 10
		return (10 + (getButtonYPlacement(buttonAmount)) + 38 + 10);
	}
	
	public int createPeriodButtons(List<Period> periods, JPanel container) {
		int buttonCount = 0;
		if (container.getComponentCount() > 0) {
			container.removeAll();
		}
		for (int i = 0; i < periods.size(); i++) {
	        Period period = periods.get(i);
            JButton button = new JButton("Periode: " + (period.getStart() + 1) + " - " + period.getEnd());
            button.setBounds(10, 10 + getButtonYPlacement(i), 480, 38);
            button.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
            container.add(button);
            final int finalI = i;
            buttonCount = i;
            button.addActionListener(e -> {
            	//new PeriodicPlanUI(controller.getPeriodicPlanController(finalI));
                new PeriodicPlanUITwo(controller.getPeriodicPlanController(finalI), this);
            });
        }
		return buttonCount;
	}
}
