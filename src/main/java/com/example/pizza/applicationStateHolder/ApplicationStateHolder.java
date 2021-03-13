package com.example.pizza.applicationStateHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.pizza.bom.Ingredient;
import com.example.pizza.bom.Kitchen;
import com.example.pizza.bom.Oven;
import com.example.pizza.configuration.ApplicationStateCreator;

public class ApplicationStateHolder {
	private static ApplicationStateHolder appStateHolder = new ApplicationStateHolder();
	private Kitchen kitchen;
	
	private ApplicationStateHolder() {
		kitchen = new Kitchen();
		Oven oven1 = new Oven(1, null, ApplicationStateCreator.getBakingTime());
		Oven oven2 = new Oven(2, null, ApplicationStateCreator.getBakingTime());
		
		kitchen.setOvens(new ArrayList<Oven>(Arrays.asList(oven1, oven2)));
	}
	
	public synchronized static ApplicationStateHolder getState() {
		return appStateHolder;
	}

	public Kitchen getKitchen() {
		return kitchen;
	}	
	
	public synchronized List<Ingredient> getIngredients() {
		return kitchen.getIngredients();
	}

	public void depledeIngredient(Ingredient i) {
		kitchen.depledeIngredient(i);
	}
	
	public void addOven(Oven oven) {
		kitchen.getOvens().add(oven);
	}
}
