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
		scrollPane.setBounds(0, 53, 492, 510);
		storagePlansPanel.add(scrollPane);
		
		JPanel scrollPlansPanel = new JPanel();
		scrollPane.setViewportView(scrollPlansPanel);
		GridBagLayout gbl_scrollPlansPanel = new GridBagLayout();
		gbl_scrollPlansPanel.columnWidths = new int[]{0};
		gbl_scrollPlansPanel.rowHeights = new int[]{0};
		gbl_scrollPlansPanel.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_scrollPlansPanel.rowWeights = new double[]{Double.MIN_VALUE};
		scrollPlansPanel.setLayout(gbl_scrollPlansPanel);
		
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
            scrollPane.add(savedPlan);
        }
		
		/* This thing doesn't work... 
		controller.addOnSaveListener(x -> {
            JButton button = new JButton(x.getName());
            scrollPlansPanel.add(button);
            validate();
            repaint();
        });
		*/
		
		setVisible(true);
	}
}
