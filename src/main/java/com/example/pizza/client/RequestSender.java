package com.example.pizza.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.pizza.bom.Order;
import com.example.pizza.bom.Pizza;
import com.example.pizza.paths.Paths;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RequestSender {
	private static final String DOMAIN = "http://localhost:8080";
	
	private CloseableHttpClient client = HttpClients.createDefault();
	
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ResponseAnalyzer analyzer;
	
	public List<String> createOrder(Order order) throws ParseException, IOException {
		String url = DOMAIN + Paths.CREATE_ORDER_URL;
		HttpPost post = createPost(url, mapper.writeValueAsString(order));
		CloseableHttpResponse response = client.execute(post);
		String respAsString = analyzer.responseToString(response);
		List<String> messages = mapper.readValue(respAsString, new TypeReference<List<String>>(){});
		
		return messages;
	}
	
	public String getOrderInfo(int orderId) throws ClientProtocolException, IOException {
		String url = DOMAIN + Paths.GET_ORDER_DETAILS_URL + "/" + orderId;
		HttpGet get = createGet(url);
		CloseableHttpResponse response = client.execute(get);
		String respAsString = analyzer.responseToString(response);
		String message = mapper.readValue(respAsString, String.class);
		
		return message;
	}
	
	public boolean isOrderComplete(int orderId) throws ParseException, IOException {
		String url = DOMAIN + Paths.SPAM_UNTIL_ORDER_COMPLETE + "/" + orderId;
		HttpGet get = createGet(url);
		CloseableHttpResponse response = client.execute(get);
		String respAsString = analyzer.responseToString(response);
		Boolean isComplete = mapper.readValue(respAsString, Boolean.class);
		
		return isComplete;
	}
	
	public List<Pizza> getBakedPizzas(int orderId) throws ParseException, IOException {
		String url = DOMAIN + Paths.BAKED_PIZZAS + "/" + orderId;
		HttpGet post = createGet(url);
		CloseableHttpResponse response = client.execute(post);
		String respAsString = analyzer.responseToString(response);
		List<Pizza> pizzas = mapper.readValue(respAsString, new TypeReference<List<Pizza>>(){});
		
		return pizzas;
	}
	
	private HttpPost createPost(String url, String objectAsJson) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(url);
		
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");
		StringEntity stringEntity = new StringEntity(objectAsJson);
		post.setEntity(stringEntity);
		
		return post;
	}
	
	private HttpGet createGet(String url) {
		HttpGet get = new HttpGet(url);
		
		get.setHeader("Accept", "application/json");
		get.setHeader("Content-type", "application/json");
		
		return get;
	}
}
