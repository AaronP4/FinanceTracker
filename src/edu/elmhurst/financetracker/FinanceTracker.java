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
		String[] categories = {"Salary", "Food", "Car", "Other"};
		JComboBox<String> category = new JComboBox(categories);
		JButton addButton = new JButton("Add");
		
		panelInput.add(new JLabel("Description:"));
		panelInput.add(new JLabel("Amount:"));
		panelInput.add(new JLabel("Category:"));
		panelInput.add(new JLabel("Type:"));
		panelInput.add(new JLabel(""));
		panelInput.add(description);
		panelInput.add(amount);
		panelInput.add(category);
		panelInput.add(type);
		panelInput.add(addButton);
		
		String[] columns = {"Description", "Amount", "Category", "Type"};
		model = new DefaultTableModel(columns, 0);
		table = new JTable(model);
		JScrollPane scrollTable = new JScrollPane(table);
		
		JPanel panelDisplay = new JPanel(new GridLayout(1, 4));
		incomeLabel = new JLabel("Income: $0.00");
		expenseLabel = new JLabel("Expenses: $0.00");
		balanceLabel = new JLabel("Balance: $0.00");
		JButton clearButton = new JButton("Clear All");
		
		panelDisplay.add(incomeLabel);
		panelDisplay.add(expenseLabel);
		panelDisplay.add(balanceLabel);
		panelDisplay.add(clearButton);
		
		addButton.addActionListener(e -> {
			try { 
				String desc = description.getText();
				double amnt = Double.parseDouble(amount.getText());
				String cat = (String) category.getSelectedItem();
				String typ = (String) type.getSelectedItem();
			
				Transaction t = new Transaction(desc, amnt, typ, cat);
				manager.addTransaction(t);
			
				Object[] row = {desc, amnt, cat, typ};
				model.addRow(row);
				update();
				manager.saveToFile("transactions.txt");
			
				description.setText("");
				amount.setText("");
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Invalid input.");
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
