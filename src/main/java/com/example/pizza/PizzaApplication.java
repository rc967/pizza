package com.example.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class PizzaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("If application fails with 'Port already in use, please use this: java -Dserver.port=[X] --mode='server/client' --ovenBakingTime=[Y]");
		if(args.length < 1) {
			System.out.println("App should be run with at least --mode='server/client' argument. Example: java x.jar --mode='server' --ovenBakingTime=50 ");
		} else if(!args[0].contains("--mode") && !args[0].contains("Dserver.port")) {
			System.out.println(args[0]);
			System.out.println("App must have a mode argument. Example: java x.jar (--mode=server --ovenBakingTime=50 | --mode=client)");
		} else {
			SpringApplication.run(PizzaApplication.class, args);	
		}
	}
	
}
