package com.example.pizza.client;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pizza.bom.Pizza;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResponseAnalyzer {
	private static final String SUCCESS_MESSAGE_TEXT_PART = "Order accepted. Order id: ";
	private static final String PIZZA = "Pizza";
	private static final String PIZZA_BAKED = "is done!";
	
	@Autowired
	private ObjectMapper mapper;
	
	public void printResponse(List<String> response) {
		response.stream().forEach(msg -> System.out.println(msg));
	}
	
	public void printBakedPizzas(List<Pizza> response) {
		response.stream().forEach(p -> {
			if(p.getBakingCountdown() == 0) {
				System.out.println(PIZZA + p.getId() + PIZZA_BAKED);
			}
		});
	}
	
	public Integer getOrderIdFromCreateOrderResponse(List<String> response) {
		String resp = response.get(0);
		
		if(resp.contains(SUCCESS_MESSAGE_TEXT_PART)) {
			return Integer.parseInt(resp.replace(SUCCESS_MESSAGE_TEXT_PART, ""));
		}
		
		return null;
	}
	
	public String responseToString(HttpResponse response) throws ParseException, IOException {
		int status = response.getStatusLine().getStatusCode();
		
		if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
	}
}
