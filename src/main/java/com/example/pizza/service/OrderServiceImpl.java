package com.example.pizza.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.pizza.applicationStateHolder.ApplicationStateHolder;
import com.example.pizza.bom.Ingredient;
import com.example.pizza.bom.Order;
import com.example.pizza.bom.Pizza;

@Service
public class OrderServiceImpl implements OrderService {
	private static final String ingredientDeplededMessage = "Ingredient {0} not available at the moment.";
	private static final String ingredientTooLow = "Not enough {0} ingredient.";
	
	@Override
	public List<String> processOrder(Order order) {
		List<String> ingredientDepleededMessageOptional = checkIngredientAvailability(order);
		
		if(!ingredientDepleededMessageOptional.isEmpty()) {
			return ingredientDepleededMessageOptional;
		} else {
			return bakePizzas(order);
		}
	}
	
	private List<String> bakePizzas(Order order) {
		try {
			return List.of(ApplicationStateHolder.getState().getKitchen().acceptOrder(order));
		} catch (InterruptedException e) {
			return List.of(e.getMessage());
		}
	}

	private List<String> checkIngredientAvailability(Order order) {
		List<String> messages = new ArrayList<String>();
		
		for(Pizza pizza : order.getPizzas()) {
			List<Ingredient> pizzaIngredients = pizza.getIngredients();
			List<Ingredient> kitchenIngredients = ApplicationStateHolder.getState().getIngredients();
			
			for(Ingredient i : pizzaIngredients) {
				boolean ingredientAdded = true;
				Optional<Ingredient> kitchenIngredientOptional = kitchenIngredients.stream()
							.filter(ingredient -> ingredient.getName().equals(i.getName())).findFirst();
				
				if(kitchenIngredientOptional.isPresent()) {
					Ingredient kitchenIngredient = kitchenIngredientOptional.get();
					
					if(kitchenIngredient.getQuantity() < 0) {
						messages.add(ingredientDeplededMessage.replace("{0}", kitchenIngredient.getName()));
						ingredientAdded = false;
					}
					
					if(kitchenIngredient.getQuantity() < i.getQuantity()) {
						messages.add(ingredientTooLow.replace("{0}", kitchenIngredient.getName()));
						ingredientAdded = false;
					}
					
					if(ingredientAdded) {
						ApplicationStateHolder.getState().depledeIngredient(i);
					}
				}
			}
		}
		
		return messages;
	}

	@Override
	public String getOrderDetails(int orderId) {
		return ApplicationStateHolder.getState().getKitchen().getOrderDetails(orderId);
	}

	@Override
	public boolean checkOrderCompletion(int orderId) {
		return ApplicationStateHolder.getState().getKitchen().checkOrderComplete(orderId);
	}

	@Override
	public List<Pizza> getBakedPizzas(int id) {
		return ApplicationStateHolder.getState().getKitchen().getBakedPizzas(id);
	}
}
