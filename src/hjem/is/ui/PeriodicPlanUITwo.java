package hjem.is.ui;

import hjem.is.controller.PeriodicPlanController;
import hjem.is.controller.StorageOrderController;
import hjem.is.model.StorageOrder;
import hjem.is.utilities.Parse;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class PeriodicPlanUITwo extends JDialog {
    private PeriodicPlanController controller;
    private Map<String, Integer> productMap;
    private StoragePlanUITwo parentUI;
    private StorageOrderController sOController;
    private JPanel contentPane;
    private JTable products;
    private JScrollPane scrollPane;
    private JTextField startPeriod;
    private JTextField endPeriod;
    private JTextField amountField;
    private String[] array;
    private JTable productsTable;
    
    public PeriodicPlanUITwo(PeriodicPlanController controller, StoragePlanUITwo parentUI) {
    	this.parentUI = parentUI;
        this.controller = controller;
        productMap = new HashMap<>();
        sOController = new StorageOrderController();
        this.setModal(true);
        
        //make panel
        contentPane = new JPanel();
        setBounds(120, 120, 504, 590);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 484, 546);
        contentPane.add(panel);
        panel.setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 240, 464, 305);
        panel.add(scrollPane);
        
        
        products = new JTable();
        products.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableModel model = new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Produkt", "Antal"
        	}
        ) {
        	@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalTextPosition(SwingConstants.CENTER);
        products.setDefaultRenderer(String.class, centerRenderer);
        products.setModel(model);
        products.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        products.setFillsViewportHeight(true);
        products.setBorder(new LineBorder(new Color(0, 0, 0), 2));
        products.setRowHeight(25);
        scrollPane.setViewportView(products);
        
        //add products already in period to table 
        for (String productName : controller.getUsedNames()) {
        	Integer productAmount = controller.getAmountByName(productName);
        	productMap.put(productName, productAmount);
        	String[] productData = {productName, Integer.toString(productAmount)};
        	model.addRow(productData);
        }
        
        JLabel startPeriodLabel = new JLabel("Periode start:");
        startPeriodLabel.setBounds(10, 11, 121, 23);
        startPeriodLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        panel.add(startPeriodLabel);

        JLabel endPeriodLabel = new JLabel("Periode slut:");
        endPeriodLabel.setBounds(175, 11, 121, 23);
        endPeriodLabel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        panel.add(endPeriodLabel);

        startPeriod = new JTextField(controller.getStartPeriod() + "");
        startPeriod.setBounds(10, 40, 106, 28);
        panel.add(startPeriod);
        startPeriod.setColumns(10);

        endPeriod = new JTextField(controller.getEndPeriod() + "");
        endPeriod.setBounds(175, 40, 106, 28);
        endPeriod.setColumns(10);
        panel.add(endPeriod);

        JButton save = new JButton("Gem");
        save.setBounds(343, 14, 131, 41);
        save.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
        panel.add(save);
        
        JLabel lblProducts = new JLabel("Produkter:");
        lblProducts.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        lblProducts.setBounds(10, 215, 163, 14);
        panel.add(lblProducts);
        
        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        amountField.setToolTipText("Indtast antal...");
        amountField.setBounds(180, 130, 145, 22);
        panel.add(amountField);
        amountField.setColumns(10);
        
        //List of products that can be added
        List<String> possibleProducts = controller.getUnusedNames();
        array = possibleProducts.toArray(new String[0]);
        JComboBox comboBox = new JComboBox(array);
        comboBox.setToolTipText("Vælg produkt...");
        comboBox.setFont(new Font("Segoe UI Black", Font.PLAIN, 12));
        comboBox.setBounds(10, 128, 145, 22);
        panel.add(comboBox);
        
        JLabel lblProduktNavn = new JLabel("Produkt navn:");
        lblProduktNavn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        lblProduktNavn.setLabelFor(comboBox);
        lblProduktNavn.setBounds(10, 94, 145, 23);
        panel.add(lblProduktNavn);
        
        JLabel lblAntal = new JLabel("Antal:");
        lblAntal.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        lblAntal.setBounds(180, 94, 145, 23);
        panel.add(lblAntal);
        
        JButton add = new JButton("Tilføj produkt");
        add.setBounds(10, 174, 145, 28);
        add.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        panel.add(add);
        add.addActionListener(e -> {
            //check values are valid
        	int rowCount = products.getRowCount();
        	String productAmount = amountField.getText();
        	//display error if amountField contains anything but whole numbers above 0
        	if(comboBox.getSelectedIndex() < 0 || !productAmount.matches("[0-9]*$") || productAmount.isBlank()) {
        		JOptionPane.showMessageDialog(null, "Sørg for at du har valgt et produkt og at der kun bruges hele tal under Antal.");
        		amountField.setText("");
        	} else if (Integer.parseInt(productAmount) < 1) {
        		JOptionPane.showMessageDialog(null, "Sørg for at du har valgt et produkt og at der kun bruges hele tal under Antal.");
        		amountField.setText("");
        	} else {
        		//add product and amount to products table
        		Object selectedProduct = comboBox.getSelectedItem();
        		String data[] = {selectedProduct.toString(), productAmount};
        		Integer intProdAmount = Integer.parseInt(productAmount);
        		//if there are no products already, add the new product
    			if(rowCount == 0) {
    				SwingUtilities.invokeLater(() -> {
            			model.addRow(data);
            			controller.addProduct(selectedProduct.toString());
            			productMap.put(data[0], intProdAmount);
            			JOptionPane.showMessageDialog(null, "Produkt tilføjet");
            			//reset boxes for next entry
            			comboBox.setSelectedIndex(-1);
            			amountField.setText("");
                    });
    				return;
    			}
        		//if table already contains selected product, add the amount only
    			boolean exists = false;
        		for (int i = 0; i < rowCount; i++) {
        			String rowEntry = "";
        			//if product is already in 	the table, add the amount to that.
        			if (products.getValueAt(i, 0).toString().equalsIgnoreCase(data[0])) {
        				exists = true;
        				final int finalI = i;
        				SwingUtilities.invokeLater(() -> {
        					Integer amount = productMap.get(data[0]);
        					productMap.put(data[0], intProdAmount + amount);
        					controller.setProductAmount(data[0], intProdAmount + amount);
        					products.setValueAt(intProdAmount + amount, finalI, 1);
        				});
        			}
        		}
       			//if product is not already in the table, add the new product
       			if (!exists){
       				SwingUtilities.invokeLater(() -> {
               			model.addRow(data);
               			controller.addProduct(selectedProduct.toString());
               			productMap.put(data[0], intProdAmount);
               			JOptionPane.showMessageDialog(null, "Produkt tilføjet");
               			//reset boxes for next entry
               			comboBox.setSelectedIndex(-1);
               			amountField.setText("");
                    });
       			}
        	}
        });
        
        //Remove a product from the plan
        JButton remove = new JButton("Fjern produkt");
        remove.setBounds(178, 174, 145, 28);
        remove.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        panel.add(remove);
        remove.addActionListener(e -> {
            //remove selected product from table
            if(products.getSelectedRowCount() != 1) {
            	//display error message
            	JOptionPane.showMessageDialog(null, "Du har ikke valgt et produkt du vil fjerne.");
            } else if (products.getRowCount() == 0) {
            	JOptionPane.showMessageDialog(null, "Der er ingen produkter der kan fjernes.");
            } else {
            	int row = products.getSelectedRow();
                int columnCount = products.getColumnCount();
                final String selectedValue = products.getModel().getValueAt(row, 0).toString();
                SwingUtilities.invokeLater(() -> {
                	model.removeRow(products.getSelectedRow());
                    controller.removeProduct(selectedValue);
                    productMap.remove(selectedValue);
                });
            }
        });
        
        save.addActionListener(e -> {
        	//update information
            Integer start = Parse.integer32(startPeriod.getText());
            Integer end = Parse.integer32(endPeriod.getText());
            Map<String, Integer> parsedProducts = productMap;
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
            parentUI.createButtons();
            JOptionPane.showMessageDialog(null, "Perioden er gemt.");
            this.dispose();
        });
        
        JButton btnCreateOrder = new JButton("Bestil");
        btnCreateOrder.setFont(new Font("Segoe UI Black", Font.PLAIN, 13));
        btnCreateOrder.setBounds(343, 174, 131, 28);
        panel.add(btnCreateOrder);
        btnCreateOrder.addActionListener(e -> {
        	//If table is empty, don't create an order
        	if(model.getRowCount() > 0) {
	        	//For each entry in productmap, create new storageOrder
	        	List<StorageOrder> storageOrders = sOController.createOrders(controller.getCurrent());
	        	JOptionPane.showMessageDialog(null, "Bestilling oprettet.");
	        	showStorageOrders(storageOrders, controller, model);
        	} else {
        		JOptionPane.showMessageDialog(null, "Tilføj produkter så der kan oprettes en bestilling.");
        	}
        });
        
        setTitle("Periodeplan for " + (controller.getStartPeriod() + 1) + "-" + controller.getEndPeriod());
        setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
                JOptionPane.ERROR_MESSAGE);
    }
    
    private void showStorageOrder(StorageOrder storageOrder, PeriodicPlanController controller, DefaultTableModel model) {
    	//create a GUI representation of the StorageOrder
    	JDialog storageOrderUI = new JDialog();
    	JPanel container = new JPanel();
    	setBounds(120, 120, 504, 590);
        setContentPane(container);
        container.setLayout(null);
        
        JLabel lblHjemis = new JLabel("Hjem-IS");
        lblHjemis.setFont(new Font("Segoe UI Black", Font.PLAIN, 18));
        lblHjemis.setHorizontalAlignment(SwingConstants.CENTER);
        lblHjemis.setBounds(10, 0, 468, 33);
        container.add(lblHjemis);
        
        JLabel lblAalborgDepot = new JLabel("Aalborg depot");
        lblAalborgDepot.setHorizontalAlignment(SwingConstants.CENTER);
        lblAalborgDepot.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        lblAalborgDepot.setBounds(10, 31, 468, 23);
        container.add(lblAalborgDepot);
        
        JLabel lblTarmvej = new JLabel("Tarmvej 6, 9220 Aalborg");
        lblTarmvej.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblTarmvej.setHorizontalAlignment(SwingConstants.CENTER);
        lblTarmvej.setBounds(10, 59, 468, 23);
        container.add(lblTarmvej);
        
        //Create the table which shows products, amounts and price
        productsTable = new JTable();
        productsTable.setFont(new Font("Consolas", Font.PLAIN, 14));
        productsTable.setShowVerticalLines(false);
        productsTable.setShowHorizontalLines(false);
        productsTable.setShowGrid(false);
        productsTable.setRowSelectionAllowed(false);
        productsTable.setEnabled(false);
        productsTable.setBounds(10, 113, 468, 317);
        DefaultTableModel tableModel = new DefaultTableModel(
           	new Object[][][][] {
           	},
           	new String[] {
           		"Produkt", "Antal", "Stk. Pris", "Pris"
           	}
        ) {
           	@Override
           	public boolean isCellEditable(int row, int column) {
           		return false;
           	}
        };
        productsTable.setModel(tableModel);
        productsTable.setRowHeight(25);
        container.add(productsTable);
        
        //add each product name, amount, price, and total price to tableModel
        int subTotal = 0;
        int productCost;
        int productAmount;
        String productName;
        for(int i = 0; i < model.getRowCount(); i++) {
        	productName = model.getValueAt(i, 0).toString();
        	productAmount = controller.getAmountByName(productName);
        	productCost = controller.getCostByName(productName);
        	String[] tableData = {productName, "" + productAmount, "" + productCost, "" + productAmount * productCost};
        	tableModel.addRow(tableData);
        	subTotal += Integer.parseInt(tableData[3]);
        }
        double moms = subTotal * 0.25;
        double totalDKK = subTotal + moms;
        
        JPanel totalPricePanel = new JPanel();
        totalPricePanel.setBounds(208, 441, 270, 99);
        container.add(totalPricePanel);
        totalPricePanel.setLayout(null);
        
        JLabel lblSubtotal = new JLabel("Subtotal");
        lblSubtotal.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblSubtotal.setBounds(10, 11, 105, 14);
        totalPricePanel.add(lblSubtotal);
        
        JLabel lblMoms = new JLabel("Moms (25,00%)");
        lblMoms.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblMoms.setBounds(10, 36, 105, 14);
        totalPricePanel.add(lblMoms);
        
        JLabel lblTotalDkk = new JLabel("Total DKK");
        lblTotalDkk.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTotalDkk.setBounds(10, 61, 105, 14);
        totalPricePanel.add(lblTotalDkk);
        
        JLabel lblTotalmoney = new JLabel("" + subTotal + ",00");
        lblTotalmoney.setFont(new Font("Consolas", Font.PLAIN, 14));
        lblTotalmoney.setHorizontalAlignment(SwingConstants.TRAILING);
        lblTotalmoney.setBounds(171, 12, 89, 14);
        totalPricePanel.add(lblTotalmoney);
        
        JLabel lblMomsmoney = new JLabel("" + String.format("%.2f", moms));
        lblMomsmoney.setFont(new Font("Consolas", Font.PLAIN, 14));
        lblMomsmoney.setHorizontalAlignment(SwingConstants.TRAILING);
        lblMomsmoney.setBounds(171, 37, 89, 14);
        totalPricePanel.add(lblMomsmoney);
        
        JLabel lblTotaldkkmoney = new JLabel("" + String.format("%.2f", totalDKK));
        lblTotaldkkmoney.setHorizontalAlignment(SwingConstants.TRAILING);
        lblTotaldkkmoney.setFont(new Font("Consolas", Font.BOLD, 14));
        lblTotaldkkmoney.setBounds(171, 62, 89, 14);
        totalPricePanel.add(lblTotaldkkmoney);
        
        JLabel lblProduktAntalStk = new JLabel("Produkt        Antal           Stk. Pris          Pris");
        lblProduktAntalStk.setFont(new Font("Consolas", Font.PLAIN, 15));
        lblProduktAntalStk.setBounds(10, 88, 468, 23);
        container.add(lblProduktAntalStk);
        
        
    }
    
    private void showStorageOrders(List<StorageOrder> storageOrders, PeriodicPlanController controller, DefaultTableModel model) {
    	for (int i = 0; i < storageOrders.size(); i++) {
    		showStorageOrder(storageOrders.get(i), controller, model);
    	}
    }
}
