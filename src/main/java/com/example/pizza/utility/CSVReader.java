package com.example.pizza.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {
	private static String CSV_SEPARATOR = ",";
	
	public static List<List<String>> readCSVTokens(String filePath) throws Exception {
		List<List<String>> tokens = null;
		File file = new File(filePath);
		
		if(file.exists()) {
			try(InputStream is = new FileInputStream(file)) {
				tokens = readCSVTokens(is);
			} catch(Exception e) {
				throw e;
			}
			
		} else {
			throw new FileNotFoundException("File not found on path: " + filePath);
		}

		return tokens;
	}

	public static List<List<String>> readCSVTokens(InputStream is) throws Exception {
		List<List<String>> tokens = new ArrayList<>();
		
		try(InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr)) {
			br.lines()
				.map(l -> Arrays.asList(l.replaceAll("\"", "").split(CSV_SEPARATOR)))
				.forEach(tokenList -> tokens.add(tokenList));
		}
		
		return tokens;
	}
}
