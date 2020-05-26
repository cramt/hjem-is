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

public class PeriodicPlanUI extends MyFrame {
    private PeriodicPlanController controller;
    private Map<String, JTextField> productMap;
    private JPanel products;

    public PeriodicPlanUI(PeriodicPlanController controller) {
        this.controller = controller;
        JPanel panel = makeMainPanel();
        JButton save = new JButton("save");
        panel.add(save);
        JLabel startPeriodLabel = new JLabel("period start");
        panel.add(startPeriodLabel);
        JTextField startPeriod = new JTextField(controller.getStartPeriod() + "");
        panel.add(startPeriod);
        JLabel endPeriodLabel = new JLabel("period end");
        panel.add(endPeriodLabel);
        JTextField endPeriod = new JTextField(controller.getEndPeriod() + "");
        panel.add(endPeriod);
        products = new JPanel();
        products.setLayout(new BoxLayout(products, BoxLayout.Y_AXIS));
        panel.add(products);
        productMap = new HashMap<>();
        for (String productName : controller.getUsedNames()) {
            addProduct(productName);
        }
        JButton add = new JButton("add product");
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
        JButton remove = new JButton("remove product");
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
        save.addActionListener(e -> {
            Integer start = Parse.integer32(startPeriodLabel.getText());
            Integer end = Parse.integer32(endPeriodLabel.getText());

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


        setLayout(null);

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
