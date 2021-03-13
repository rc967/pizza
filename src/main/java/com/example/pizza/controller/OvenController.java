package com.example.pizza.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pizza.applicationStateHolder.ApplicationStateHolder;
import com.example.pizza.bom.Oven;

@RestController
@RequestMapping("/api/oven")
public class OvenController {

	@PostMapping("/create")
	public String createOven(@RequestBody Oven oven) {
		ApplicationStateHolder.getState().addOven(oven);
		return "Oven added: " + ApplicationStateHolder.getState().getKitchen().getOvens().size();
	}
}
