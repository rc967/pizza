package com.example.pizza.bom;

public class Ingredient {
	private String name;
	private int quantity;
	public Ingredient(String name, int quantity) {
		super();
		this.name = name;
		this.quantity = quantity;
	}
	public Ingredient() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void subtract(int quantity) {
		this.quantity -= quantity;
	}
	
}
