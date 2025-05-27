package edu.elmhurst.financetracker;

import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
	private List<Transaction> transactions = new ArrayList<>();
	
	public void addTransaction(Transaction t) {
		transactions.add(t);
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	public double getTotalIncome() {
		double sum = 0.0;
		for(int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).getType().equals("Income")) {
				sum += transactions.get(i).getAmount();
			}
		}
		return sum;
	}
	
	public double getTotalExpenses() {
		double sum = 0.0;
		for(int i = 0; i < transactions.size(); i++) {
			if (transactions.get(i).getType().equals("Expense")) {
				sum += transactions.get(i).getAmount();
			}
		}
		return sum;
	}
	
	public void clearTransactions() {
		transactions.clear();
	}
	
	public void removeLast() {
		transactions.remove(transactions.size() - 1);
	}
	
	public void saveToFile(String filename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for (Transaction t : transactions) {
			writer.write(t.getDescription() + "," + t.getAmount() + "," + t.getType() + "," + t.getCategory() + "\n");
		}
		writer.close();
	}
	
	public void loadFromFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String transaction;
		while((transaction = reader.readLine()) != null) {
			String[] t = transaction.split(",");
			double amount = Double.parseDouble(t[1]);
			transactions.add(new Transaction(t[0], amount, t[2], t[3]));
		}
		reader.close();
	}
	
	public void clearFile(String filename) throws IOException {
		new BufferedWriter(new FileWriter(filename)).close();
	}
	
	public void removeLastLine(String filename) throws IOException {
		List<String> lines = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
		
		if(!lines.isEmpty()) {
			lines.remove(lines.size() - 1);
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		for (String linet : lines) {
			writer.write(linet + "\n");
		}
		writer.close();
	}
}
