package edu.elmhurst.financetracker;

public class Transaction {
	private String description;
	private double amount;
	private String type;
	private String category;
	
	public Transaction(String description, double amount, String type, String category) {
		this.description = description;
		this.amount = amount;
		this.type = type;
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
