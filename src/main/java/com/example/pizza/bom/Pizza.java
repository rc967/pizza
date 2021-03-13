package com.example.pizza.bom;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
	private int id;
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	private int bakingCountdown;
	
	public Pizza(int id, List<Ingredient> ingredients) {
		super();
		this.id = id;
		this.ingredients = ingredients;
	}
	public Pizza() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	public boolean isBaked() {
		return bakingCountdown <= 0;
	}
	public int getBakingCountdown() {
		return this.bakingCountdown;
	}
	public void setBakingCountdown(int bakingTime) {
		this.bakingCountdown = bakingTime;
	}
}
