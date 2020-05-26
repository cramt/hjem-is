package hjem.is.ui;

import hjem.is.controller.PeriodicPlanController;
import hjem.is.model.Product;
import hjem.is.utilities.Parse;

import javax.swing.*;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class PeriodicPlanUITwo extends MyFrame {
	 private PeriodicPlanController controller;
	 private Map<String, JTextField> productMap;
	 private JPanel contentPane;
	 private JPanel products;
	 private JTextField startPeriod;
	 private JTextField endPeriod;

	 public PeriodicPlanUITwo(PeriodicPlanController controller) {
         this.controller = controller;
         productMap = new HashMap<>();

         //make panel
	   	 contentPane = new JPanel();
	   	 setBounds(0, 0, 500, 500);
		 setContentPane(contentPane);
		 contentPane.setLayout(null);

		 JPanel panel = new JPanel();
		 panel.setBounds(10, 11, 484, 461);
		 contentPane.add(panel);
		 panel.setLayout(null);

		 JScrollPane scrollPane = new JScrollPane();
		 scrollPane.setBounds(10, 145, 464, 305);
		 panel.add(scrollPane);

		 products = new JPanel();
		 products.setBorder(new LineBorder(new Color(0, 0, 0), 5));
		 scrollPane.setViewportView(products);
		 products.setLayout(null);
		 JLabel startPeriodLabel = new JLabel("Periode start:");
		 startPeriodLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		 startPeriodLabel.setBounds(10, 11, 121, 23);
		 panel.add(startPeriodLabel);

		 JLabel endPeriodLabel = new JLabel("Periode slut:");
		 endPeriodLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		 endPeriodLabel.setBounds(156, 11, 121, 23);
		 panel.add(endPeriodLabel);

		 startPeriod = new JTextField(controller.getStartPeriod() + "");
		 startPeriod.setBounds(10, 45, 106, 28);
		 panel.add(startPeriod);
		 startPeriod.setColumns(10);

		 endPeriod = new JTextField(controller.getEndPeriod() + "");
		 endPeriod.setColumns(10);
		 endPeriod.setBounds(156, 45, 106, 28);
		 panel.add(endPeriod);

		 JLabel productsLabel = new JLabel("Produkter:");
		 productsLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
		 productsLabel.setBounds(10, 106, 106, 28);
		 panel.add(productsLabel);
		 panel.setBounds(0, 0, 484, 461);

		 JButton add = new JButton("Tilføj produkt");
		 add.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		 add.setBounds(156, 101, 140, 28);
		 panel.add(add);
		 add.addActionListener(e -> {
	         List<String> possibleValues = controller.getUnusedNames();
	         final String selectedValue = (String) JOptionPane.showInputDialog(null,
	        	     "Choose one", "Input",
	                 JOptionPane.INFORMATION_MESSAGE, null,
	                 possibleValues.toArray(), possibleValues.get(0));
	         controller.addProduct(selectedValue);
	         SwingUtilities.invokeLater(() -> {
	             addProduct(selectedValue);
                 validate();
                 repaint();
	         });
	     });
		 JButton remove = new JButton("Fjern produkt");
		 remove.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		 remove.setBounds(339, 101, 135, 28);
		 panel.add(remove);
		 remove.addActionListener(e -> {
	         List<String> possibleValues = controller.getUsedNames();
	         final String selectedValue = (String) JOptionPane.showInputDialog(null,
	                 "Choose one", "Input",
	                 JOptionPane.INFORMATION_MESSAGE, null,
                     possibleValues.toArray(), possibleValues.get(0));
	         controller.addProduct(selectedValue);
	         SwingUtilities.invokeLater(() -> {
	             controller.removeProduct(selectedValue);
	             products.remove(productMap.get(selectedValue));
	             revalidate();
	             repaint();
	         });
	     });

		 JButton save = new JButton("Gem");
		 save.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
		 save.setBounds(343, 14, 131, 41);
		 panel.add(save);
		 save.addActionListener(e -> {
	            Integer start = Parse.integer32(startPeriod.getText());
	            Integer end = Parse.integer32(endPeriod.getText());

	            Map<String, Integer> parsedProducts = productMap.entrySet().stream().collect(Collectors.toMap(
	                    Map.Entry::getKey,
	                    v -> Parse.integer32(v.getValue().getText())
	            ));
	            if (start == null) {
	                showError("your start period cannot be parsed");
	                return;
	            }
	            if (end == null) {
	                showError("your end period cannot be parsed");
	                return;
	            }
	            for (Map.Entry<String, Integer> productIntegerEntry : parsedProducts.entrySet()) {
	                if (productIntegerEntry.getValue() == null) {
	                    showError(productIntegerEntry.getKey() + " has an unparseable amount");
	                    return;
	                }
	            }
	            controller.setStartPeriod(start);
	            controller.setEndPeriod(end);
	            for (Map.Entry<String, Integer> stringIntegerEntry : parsedProducts.entrySet()) {
	                controller.setProductAmount(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
	            }
	            controller.save();
	        });

		 //add
		 for (String productName : controller.getUsedNames()) {
	            addProduct(productName);
	     }

		 setTitle("Periodeplan for " + (controller.getStartPeriod() + 1) + "-" + controller.getEndPeriod());

		 setVisible(true);
	 }

	 private void addProduct(String name) {
	        JPanel productPanel = new JPanel();
	        products.add(productPanel);
	        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.X_AXIS));
	        productPanel.add(new JLabel(name));
	        JTextField amount = new JTextField(controller.getAmountByName(name));
	        productPanel.add(amount);
	        productMap.put(name, amount);
	 }

	 private void showError(String message) {
	        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
	                JOptionPane.ERROR_MESSAGE);
	    }
}
