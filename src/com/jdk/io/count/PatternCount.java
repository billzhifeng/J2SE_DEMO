package com.jdk.io.count;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PatternCount {

	public static void main(String[] args) {
		
		File file = new File("test.txt");		
		String s = "";		
		StringBuffer buffer = new StringBuffer();
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		try {			
			BufferedReader br = new BufferedReader(new FileReader(file));
			try {				
				while ((s = br.readLine()) != null) {
					buffer.append(s + '\n');
				}				
				br.close();			
			} catch (IOException e) {
				e.printStackTrace();			
			}	
		} catch (FileNotFoundException e) {
			e.printStackTrace();		
		}
		
		Pattern p = Pattern.compile("[A-Za-z]+");
		Matcher m = p.matcher(buffer.toString());
		while(m.find()) {			
			 String word = m.group();
			 if (!map.containsKey(word)) {
				 map.put(word, 0);
			 }
			 map.put(word, map.get(word) + 1);
		}
		for (String word : map.keySet()) {
			System.out.println(word + " : " + map.get(word));
		}
	}
}
