package hjem.is.ui;

import hjem.is.controller.StoragePlanController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StoragePlanListUITwo extends MyFrame {

	private JPanel contentPane;
	private JPanel scrollPlansPanel = new JPanel();
	StoragePlanController controller;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY");

	/**
	 * Create the frame.
	 */
	public StoragePlanListUITwo() {
		controller = new StoragePlanController();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel fullPlansPanel = new JPanel();
		fullPlansPanel.setBounds(0, 11, 964, 639);
		contentPane.add(fullPlansPanel);
		fullPlansPanel.setLayout(null);

		JPanel storagePlansPanel = new JPanel();
		storagePlansPanel.setBounds(0, 0, 502, 628);
		fullPlansPanel.add(storagePlansPanel);
		storagePlansPanel.setLayout(null);

		JPanel currentPlansTextPanel = new JPanel();
		currentPlansTextPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		currentPlansTextPanel.setBounds(10, 11, 482, 32);
		storagePlansPanel.add(currentPlansTextPanel);

		JLabel currentPlansLabel = new JLabel("Nuværende lagerplaner:");
		currentPlansTextPanel.add(currentPlansLabel);
		currentPlansLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 54, 482, 509);
		storagePlansPanel.add(scrollPane);
		
		scrollPane.setViewportView(scrollPlansPanel);
		scrollPlansPanel.setLayout(new BoxLayout(scrollPlansPanel, BoxLayout.Y_AXIS));

		JButton generateNew = new JButton("Generer ny lagerplan");
		generateNew.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		generateNew.setBounds(597, 11, 278, 32);
		fullPlansPanel.add(generateNew);
		generateNew.addActionListener(e -> {
            controller.generateNew("Lagerplan " + formatter.format(java.time.LocalDate.now()));
            //new StoragePlanUI(controller);
            new StoragePlanUITwo(controller, this);
        });

		//generate buttons for each already saved StoragePlan
		//paintStoragePlans(controller, scrollPlansPanel);
		for (String name : controller.getNames()) {
			JButton savedPlan = new JButton(name);
			savedPlan.addActionListener(e -> {
				controller.select(savedPlan.getText());
				new StoragePlanUITwo(controller, this);
			});
			SwingUtilities.invokeLater(() -> {
	            validate();
	            repaint();
			});
			savedPlan.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
    		scrollPlansPanel.add(savedPlan);
		}

		//When save is chosen from StoragePlanUI
		controller.addOnSaveListener(x -> {
			//Remove all buttons
			scrollPlansPanel.removeAll();
			makeStoragePlanButton(x.getName());
			//repaint all buttons
			paintStoragePlans(controller, scrollPlansPanel);
			//update UI (Doesn't work)
			scrollPlansPanel.updateUI();
            validate();
            repaint();
            setVisible(false);
            setVisible(true);
        });

		setTitle("Lagerplaner");

		setVisible(true);
	}

	public void paintPlans() {
		paintStoragePlans(controller, scrollPlansPanel);
	}
	public void paintStoragePlans(StoragePlanController controller, JPanel panel) {
		if(panel.getComponentCount() > 0) {
			panel.removeAll();
		}
		for (String name : controller.getNames()) {
			JButton savedPlan = new JButton(name);
			savedPlan.addActionListener(e -> {
				controller.select(savedPlan.getText());
				new StoragePlanUITwo(controller, this);
			});
			SwingUtilities.invokeLater(() -> {
	            validate();
	            repaint();
			});
			savedPlan.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
 			savedPlan.setSize(200, 40);
    		panel.add(savedPlan);
		}
	}

	public JButton makeStoragePlanButton(String name) {
		JButton button = new JButton(name);
        button.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
        button.setSize(200, 40);
        return button;
	}
}
