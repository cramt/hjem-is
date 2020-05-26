package hjem.is.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import hjem.is.controller.StoragePlanController;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import java.awt.GridBagLayout;

public class StoragePlanListUITwo extends MyFrame {

	private JPanel contentPane;
	StoragePlanController controller;
	

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
		
		JPanel scrollPlansPanel = new JPanel();
		scrollPane.setViewportView(scrollPlansPanel);
		scrollPlansPanel.setLayout(new BoxLayout(scrollPlansPanel, BoxLayout.Y_AXIS));
		
		JButton generateNew = new JButton("Generer ny lagerplan");
		generateNew.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		generateNew.setBounds(597, 11, 278, 32);
		fullPlansPanel.add(generateNew);
		generateNew.addActionListener(e -> {
            controller.generateNew("navn");
            //new StoragePlanUI(controller);
            new StoragePlanUITwo(controller);
        });
		
		
		for (String name : controller.getNames()) {
			JButton savedPlan = new JButton(name);
			savedPlan.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
 			savedPlan.setSize(200, 40);
    		scrollPlansPanel.add(savedPlan);
		}
		
		/* This thing doesn't work...
		controller.addOnSaveListener(x -> {
            JButton button = new JButton(x.getName());
            scrollPlansPanel.add(button);
            validate();
            repaint();
        });
        */
		setTitle("Lagerplaner");
		
		setVisible(true);
	}
}
