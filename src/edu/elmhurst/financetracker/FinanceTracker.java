package edu.elmhurst.financetracker;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FinanceTracker {
	private TransactionManager manager = new TransactionManager();
	private JTable table;
	private DefaultTableModel model;
	private JLabel incomeLabel;
	private JLabel expenseLabel;
	private JLabel balanceLabel;
	public FinanceTracker() {
		JFrame frame = new JFrame("Finance Tracker");
		frame.setSize(700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelInput = new JPanel(new GridLayout(2, 5));
		JTextField description = new JTextField(20);
		JTextField amount = new JTextField(20);
		String[] types = {"Income", "Expense"};
		JComboBox<String> type = new JComboBox(types);
		String[] categories = {"Pay Check", "Food", "Car", "Other"};
		JComboBox<String> category = new JComboBox(categories);
		JButton addButton = new JButton("Add");
		
		panelInput.add(new JLabel("Description:"));
		panelInput.add(new JLabel("Amount:"));
		panelInput.add(new JLabel("Category:"));
		panelInput.add(new JLabel("Type:"));
		panelInput.add(new JLabel(""));
		panelInput.add(description);
		panelInput.add(amount);
		panelInput.add(type);
		panelInput.add(category);
		panelInput.add(addButton);
		
		String[] columns = {"Description", "Amount", "Category", "Type"};
		model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		JScrollPane scrollTable = new JScrollPane(table);
		
		JPanel panelDisplay = new JPanel(new GridLayout(1, 5));
		incomeLabel = new JLabel("Income: $0.00");
		expenseLabel = new JLabel("Expenses: $0.00");
		balanceLabel = new JLabel("Balance: $0.00");
		JButton deletePreviousButton = new JButton("Delete Last Line");
		JButton clearButton = new JButton("Clear All");
		
		panelDisplay.add(incomeLabel);
		panelDisplay.add(expenseLabel);
		panelDisplay.add(balanceLabel);
		panelDisplay.add(deletePreviousButton);
		panelDisplay.add(clearButton);
		
		addButton.addActionListener(e -> {
			try { 
				String desc = description.getText().trim();
				String amntTxt = amount.getText().trim();
				String cat = (String) category.getSelectedItem();
				String typ = (String) type.getSelectedItem();
				
				if (desc.equals(null) || desc.equals("")) {
					throw new IllegalArgumentException("Description cannot be empty.");
				}
				if (!amntTxt.matches("\\d+(\\.\\d{1,2})?")) {
		            throw new IllegalArgumentException("Invalid amount input. Enter amount without '$' or ','");
		        }
				double amnt = Double.parseDouble(amntTxt);
				if (typ.equals("Income") && !(cat.equals("Pay Check")) && !(cat.equals("Other"))) {
					throw new IllegalArgumentException("Invalid category for Income.");
				}
				if (typ.equals("Expense") && cat.equals("Pay Check")) {
					throw new IllegalArgumentException("Invalid category for Expense.");
				}
				Transaction t = new Transaction(desc, amnt, typ, cat);
				manager.addTransaction(t);
			
				Object[] row = {desc, amnt, cat, typ};
				model.addRow(row);
				update();
				manager.saveToFile("transactions.txt");
			
				description.setText("");
				amount.setText("");
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(frame, ex.getMessage());
				description.setText("");
				amount.setText("");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Invalid input.");
				description.setText("");
				amount.setText("");
			}
		});
		
		clearButton.addActionListener(e -> {
			model.setRowCount(0);
			manager.clearTransactions();
			update();
			try {
				manager.clearFile("transactions.txt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		deletePreviousButton.addActionListener(e -> {
			if (model.getRowCount() > 0) {
			    model.removeRow(model.getRowCount() - 1);
			}
			try {
			manager.removeLast();
			update();
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(frame, "Table is already empty.");
			}
			try {
				manager.removeLastLine("transactions.txt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		try {
			manager.loadFromFile("transactions.txt");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "File Failed to Load.");
		}
		for (Transaction t : manager.getTransactions()) {
		    Object[] row = { t.getDescription(), t.getAmount(), t.getCategory(), t.getType() };
		    model.addRow(row);
		}
		update();
		
		frame.setLayout(new BorderLayout());
		frame.add(panelInput, BorderLayout.NORTH);
		frame.add(panelDisplay, BorderLayout.SOUTH);
		frame.add(scrollTable, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	private void update() {
		double income = manager.getTotalIncome();
		double expenses = manager.getTotalExpenses();
		
		incomeLabel.setText("Income: $" + String.format("%.2f", income));
		expenseLabel.setText("Expenses: $" + String.format("%.2f", expenses));
		balanceLabel.setText("Balance: $" + String.format("%.2f", income - expenses));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(FinanceTracker::new);
	}

}
