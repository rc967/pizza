package com.example.pizza.utility;

public class ValueChecker {
	public static boolean isValueNotEmpty(String val) {
		return !val.equals("") 
				&& !val.equalsIgnoreCase("\\N") 
				&& !val.equals(null) 
				&& !val.equalsIgnoreCase("\"\"")
				&& !val.equals("");
	}
}
