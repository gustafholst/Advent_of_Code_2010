package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
	
	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("input_day1.txt"));
			
			List<Integer> entries = br.lines().map(l -> Integer.parseInt(l)).collect(Collectors.toList());
			
			
			int A = 0;
			int B = 0;
			int C = 0;
			
			outerloop:
			for (Integer a : entries) {
				for (Integer b : entries) {
					for (Integer c : entries) {
						if (a + b + c == 2020) {
							A = a;
							B = b;
							C = c;
							break outerloop;
						}
					}
				}
			}
			

			System.out.println("Multiplied: " + A * B * C);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
