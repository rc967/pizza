package com.example.pizza.bom;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private int id;
	private List<Pizza> pizzas = new ArrayList<Pizza>();
	private String status;

	public List<Pizza> getPizzas() {
		return pizzas;
	}

	public void setPizzas(List<Pizza> pizzas) {
		this.pizzas = pizzas;
	}

	public Order(List<Pizza> pizzas) {
		super();
		this.pizzas = pizzas;
	}

	public Order() {
		super();
	}

	@Override
	public String toString() {
		return "Order [pizzas=" + pizzas.size() + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean areAllPizzasBaked() {
		return pizzas.stream().filter(p -> p.isBaked()).count() == pizzas.size();
	}
}
