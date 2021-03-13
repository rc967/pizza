package com.example.pizza.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pizza.bom.Order;
import com.example.pizza.bom.Pizza;
import com.example.pizza.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/create")
	public List<String> hello(@RequestBody Order order) {
		return orderService.processOrder(order);
	}
	
	@GetMapping("/get/{id}")
	public String getOrderDetails(@PathVariable("id") int id) {
		return orderService.getOrderDetails(id);
	}
	
	@GetMapping("/baked/{id}")
	public List<Pizza> getBakedPizzas(@PathVariable("id") int id) {
		return orderService.getBakedPizzas(id);
	}
	
	@GetMapping("/checkOrderComplete/{id}")
	public boolean checkOrderComplete(@PathVariable("id") int id) {
		return orderService.checkOrderCompletion(id);
	}
}
