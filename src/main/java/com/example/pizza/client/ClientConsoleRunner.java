package com.example.pizza.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pizza.bom.Ingredient;
import com.example.pizza.bom.Order;
import com.example.pizza.bom.Pizza;

@Component
public class ClientConsoleRunner {
	private List<Pizza> pizzas = new ArrayList<Pizza>();
	private List<Integer> orderIds = new ArrayList<Integer>();
	private List<Order> orders = new ArrayList<Order>();
	
	@Autowired
	private RequestSender requestSender;
	@Autowired
	private ResponseAnalyzer responseAnalyzer;
	
	public void runClientApp() {
		String option = "";
		Scanner in = new Scanner(System.in);
		
		while(!option.equals("6")) {
			System.out.println("Created order ids: " + printActiveOrders());
			System.out.println("1. 5 mushrooms, 10 cheese, 2 sausages");
			System.out.println("2. 1 + 3 pineapples, 10 cheese, 2 sausages");
			System.out.println("3. EXTRA HOT - 10 pepperoni, 20 jalapeno, 2 sausages");
			System.out.println("4. 2 x pineapple from option 2 + 2 hams, 5 jalapenos, 2 sausages");
			System.out.println("5. Check order status using above ids");
			System.out.println("6. Exit app");
			System.out.print("What do you want to do: ");
			
			try {
				String input = in.nextLine();
				int inputOption = Integer.parseInt(input);
				
				if(inputOption == 6) {
					option = "6";
					break;
				}
				
				if(inputOption > 4) {
					System.out.print("Order id: ");
					String wantedId = in.nextLine();
					int id = Integer.parseInt(wantedId);
					System.out.println(requestSender.getOrderInfo(id));
				} else if (inputOption < 4) {
					Order o = createOrder(inputOption);
					List<String> messages = requestSender.createOrder(o);
					Integer orderId = responseAnalyzer.getOrderIdFromCreateOrderResponse(messages);
					
					if(orderId != null) {
						orderIds.add(orderId);
						o.setId(orderId);
						orders.add(o);
						createOrderTimer(orderId);
					}else {
						System.out.println("\n Something wrong with your Order number.\n");
					}
					
					
				} else {
					System.out.println("\n\n\nOnly numbers [1,4] are acceptable at the moment\n\n\n");
				}		
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createOrderTimer(Integer orderId) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			Optional<Order> order = orders.stream().filter(o -> o.getId() == orderId.intValue()).findFirst();
			
			@Override
			public void run() {
				try {
					if(order.isEmpty() || requestSender.isOrderComplete(orderId)) {
						cancel();
					} else {
						List<Pizza> pizzas = requestSender.getBakedPizzas(orderId);
						responseAnalyzer.printBakedPizzas(pizzas);
					}
				} catch(Exception e) {
					System.out.println("\n Theres an error while we were creating your order and had to cancel it.\n"+e.getMessage());
					cancel();
				}
			}
		}, 100);
	}

	private String printActiveOrders() {
		StringBuilder builder = new StringBuilder();
		
		orderIds.stream().forEach(id -> builder.append(id));
		
		return builder.toString();
	}

	private Order createOrder(int orderType) {
		switch(orderType) {
			case 1: {
				Pizza pizza = new Pizza(pizzas.size() + 1, List.of(new Ingredient("mushroom", 5), new Ingredient("cheese", 10), new Ingredient("sausage", 2)));
				pizzas.add(pizza);
				return new Order(List.of(pizza));
			}
			case 2: {
				Pizza pizza = new Pizza(pizzas.size() + 1, List.of(new Ingredient("mushroom", 5), new Ingredient("cheese", 10), new Ingredient("sausage", 2)));
				Pizza pizza2 = new Pizza(pizzas.size() + 1, List.of(new Ingredient("pineapple", 3), new Ingredient("cheese", 10), new Ingredient("sausage", 2)));
				pizzas.add(pizza);
				pizzas.add(pizza2);
				return new Order(List.of(pizza, pizza2));
			}
			case 3: {
				Pizza pizza = new Pizza(pizzas.size() + 1, List.of(new Ingredient("pepperoni", 10), new Ingredient("jalapeno", 20), new Ingredient("sausage", 2)));
				pizzas.add(pizza);
				return new Order(List.of(pizza));
			} 
			case 4: {
				Pizza pizza = new Pizza(pizzas.size() + 1, List.of(new Ingredient("ham", 2), new Ingredient("jalapeno", 5), new Ingredient("mushroom", 1)));
				Pizza pizza2 = new Pizza(pizzas.size() + 1, List.of(new Ingredient("pineapple", 3), new Ingredient("cheese", 10), new Ingredient("sausage", 2)));
				Pizza pizza3 = new Pizza(pizzas.size() + 1, List.of(new Ingredient("pineapple", 3), new Ingredient("cheese", 10), new Ingredient("sausage", 2)));
				pizzas.add(pizza);
				pizzas.add(pizza2);
				pizzas.add(pizza3);
				return new Order(List.of(pizza, pizza2, pizza3));
			}
			default: {
				return null;
			}
		}
	}
}
