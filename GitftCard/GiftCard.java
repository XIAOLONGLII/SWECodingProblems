package com.xiaolong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class GiftCard {
	private static Scanner keyboard;
	public static void main(String[] args) {
		BufferedReader reader; 
		try {
			keyboard = new Scanner(System.in);
			String filename = keyboard.next();
			int gifeCardValue = keyboard.nextInt();
			
			reader = new BufferedReader(new FileReader(filename));//1.read the text file
			List<String> words = new ArrayList<>();
			
			String line = reader.readLine();
			while(line != null) {
				String[]  wordsArray = line.split("\t");//2. split by tab
				for(String each: wordsArray) {
					words.add(each);
				}
				line = reader.readLine();	
			}
			
			String[] arr = new String[words.size()];
			LinkedHashMap<String, Integer> map = new LinkedHashMap<>(); //3. LinkedHashMap ensure the order
			for(int i = 0; i < words.size(); i++) {
				arr[i] = words.get(i);
			}	
			for(String s: arr) {
				int index = s.indexOf(',');
				map.put(s.substring(0, index), Integer.valueOf(s.substring(index + 2)));
			}
			List<Integer> list = new ArrayList<Integer>();
			for(int i: map.values()) {
				list.add(i);
			}	
			List<Integer> result = find(list, gifeCardValue);
			for(int i = 0; i < result.size(); i++) {
				for(Map.Entry<String, Integer> entry: map.entrySet()) {
					if(entry.getValue().equals(result.get(i))) {
						System.out.print(entry.getKey() + " "+result.get(i) + " ");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static List<Integer> find(List<Integer> sortedPricesArr, int giftCardValue) {

		List<Integer> result = new ArrayList<>();
		int leftPointer = 0; 
		int rightPointer = sortedPricesArr.size() - 1;
		int currentMax = 0;
		Integer[] res = new Integer[2];
		while (leftPointer < rightPointer) {
			int currentTotalValue = sortedPricesArr.get(leftPointer) + sortedPricesArr.get(rightPointer);

			if(currentTotalValue > giftCardValue) {
				rightPointer--;
			}
			else {
				if(currentTotalValue > currentMax) {
					currentMax = currentTotalValue;
					res[0] = sortedPricesArr.get(leftPointer);
					res[1] = sortedPricesArr.get(rightPointer);
				}
				leftPointer++;	
			}
		}
		for(int i = 0; i < 2; i++) {
			result.add(res[i]);
		}	
		return result;
	}
}
