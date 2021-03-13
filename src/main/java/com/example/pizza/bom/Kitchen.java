package com.example.pizza.bom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Kitchen {
	private List<Oven> ovens = new ArrayList<Oven>();
	private List<Ingredient> ingredients = new ArrayList<Ingredient>();
	private List<Order> orders = new ArrayList<Order>();
	
	public Kitchen() {
		super();
	}
	
	public Kitchen(List<Oven> ovens, List<Ingredient> ingredients) {
		super();
		this.ovens = ovens;
		this.ingredients = ingredients;
	}
	public List<Oven> getOvens() {
		return ovens;
	}
	public void setOvens(List<Oven> ovens) {
		this.ovens = ovens;
	}
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	public void addIngredient(Ingredient ingredient) {
		ingredients.add(ingredient);
	}
	
	@Override
	public String toString() {
		return "Kitchen [ovens=" + ovens + ", ingredients=[" + ingredients.size() + "]";
	}
	
	public String getOrderDetails(int orderId) {
		StringBuilder builder = new StringBuilder();
		builder.append("Order details: \n");
		
		Optional<Order> orderOptional = orders.parallelStream().filter(o -> o.getId() == orderId).findFirst();
		
		if(orderOptional.isEmpty()) {
			return "Order does not exist";
		}
		
		for(Pizza p : orderOptional.get().getPizzas()) {
			builder.append("Pizza " + p.getId());
			
			if(p.isBaked()) {
				builder.append(" is done!");
			} else {
				builder.append(" is still baking...");
			}
			
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
	public boolean checkOrderComplete(int orderId) {
		Optional<Order> orderOptional = orders.parallelStream().filter(o -> o.getId() == orderId).findFirst();
		
		return orderOptional.isPresent() && orderOptional.get().areAllPizzasBaked();
	}
	
	public List<Pizza> getBakedPizzas(int orderId) {
		Optional<Order> orderOptional = orders.parallelStream().filter(o -> o.getId() == orderId).findFirst();
		
		if(orderOptional.isPresent()) {
			return orderOptional.get().getPizzas().parallelStream().filter(p -> p.getBakingCountdown() == 0).collect(Collectors.toList());
		}
		
		return null;
	}
	
	public String acceptOrder(Order order) throws InterruptedException {
		for(Pizza pizza : order.getPizzas()) {
			boolean pizzaBaking = false;
			
			for(Oven oven : ovens) {
				if(oven.getBakingPizza() == null) {
					oven.bakePizza(pizza);
					pizzaBaking = true;
					break;
				}
			}
			
			if(!pizzaBaking) {
				Oven smallestQueueOven = determineSmallestQueueOven();
				smallestQueueOven.bakePizza(pizza);	
			}
		}
		
		order.setId(orders.size() + 1);
		orders.add(order);
		
		return "Order accepted. Order id: " + order.getId();
	}
	
	public void depledeIngredient(Ingredient i) {
		ingredients.stream().filter(ingredient -> ingredient.getName().toLowerCase().equals(i.getName().toLowerCase()))
			.findFirst().get().subtract(i.getQuantity());
	}
	
	private Oven determineSmallestQueueOven() {
		Oven oven = null;
		
		for(Oven o : ovens) {
			if(oven == null) {
				oven = o;
			} if(o.waitingPizzasSize() < oven.waitingPizzasSize()) {
				oven = o;
			}
		}
		
		return oven;
	}
}
