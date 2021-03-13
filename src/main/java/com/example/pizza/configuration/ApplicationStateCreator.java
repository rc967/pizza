package com.example.pizza.configuration;

import java.io.InputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import com.example.pizza.applicationStateHolder.ApplicationStateHolder;
import com.example.pizza.bom.Ingredient;
import com.example.pizza.client.ClientConsoleRunner;
import com.example.pizza.utility.CSVReader;

@Component
public class ApplicationStateCreator {
	private static final Logger logger = LogManager.getLogger(ApplicationStateCreator.class);
	private static final String fileName = "ingredients.csv";
	private static int bakingTime;
	private static boolean isServer = false;
	
	@Autowired
	private ClientConsoleRunner runner;
	
	@Autowired
	public ApplicationStateCreator(ApplicationArguments args) throws Exception {
		if(args.containsOption("mode")) {
			if(args.getOptionValues("mode").get(0).equals("server")) {
				if(args.containsOption("ovenBakingTime")) {
					bakingTime = Integer.parseInt(args.getOptionValues("ovenBakingTime").get(0));
				} else {
					bakingTime = 30;
				}
				isServer = true;
				createAppState();
			}	
		} else {
			System.exit(1);
		}
	 }
	
	@PostConstruct
	private void runConsole() {
		if(!isServer) {
			runner.runClientApp();	
		}
	}
	
	@SuppressWarnings("unused")
	private void runClientConsole() {
		runner.runClientApp();
	}

	public void createAppState() throws Exception {
		InputStream is = getFileFromResourceAsStream(fileName);
		List<List<String>> tokens = CSVReader.readCSVTokens(is);
		createApplicationState(tokens);
		logger.info("Application started from runner!");
		logger.info(ApplicationStateHolder.getState().getKitchen().toString());
	}
	
	private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }
	
	private void createApplicationState(List<List<String>> tokens) {
		tokens.parallelStream()
			.forEach(line -> {
				try {
					ApplicationStateHolder.getState().getKitchen().addIngredient(new Ingredient(line.get(0), Integer.parseInt(line.get(1))));
				} catch(NumberFormatException e) {
					logger.error("Header ommited while reading CSV");
				}
			});
	}
	
	public static int getBakingTime() {
		return bakingTime;
	}
}
