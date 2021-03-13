package com.example.pizza.service;

import java.util.List;

import com.example.pizza.bom.Order;
import com.example.pizza.bom.Pizza;

public interface OrderService {
	List<String> processOrder(Order order);
	
	String getOrderDetails(int orderId);
	
	boolean checkOrderCompletion(int orderId);

	List<Pizza> getBakedPizzas(int id);
}
