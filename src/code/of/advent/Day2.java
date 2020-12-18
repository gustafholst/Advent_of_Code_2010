package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day2 {
	
	
	private static boolean checkPassword(String line) {
		//example line
		//1-3 a: abcde
		
		String[] tokens = line.split(" ");
		
		String[] occurrences = tokens[0].split("-");
		int atLeast = Integer.valueOf(occurrences[0]);
		int atMost = Integer.valueOf(occurrences[1]);
		
		char letter = tokens[1].charAt(0);
		
		String password = tokens[2];
		
		return isValidPart2(password, letter, atLeast, atMost);
	}
	
	private static boolean isValidPart1(String pwd, char letter, int least, int most) {
		int occurrences = (int) pwd.chars().filter(c -> c == letter).count();
		return occurrences >= least && occurrences <= most;
	}
	
	private static boolean isValidPart2(String pwd, char letter, int a, int b) {
		return pwd.charAt(a-1) == letter ^ pwd.charAt(b-1) == letter;
	}

	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("passwords.txt"));
			
			long numCorrect = br.lines().filter(Day2::checkPassword).count();
			
			System.out.println("Number of correct passwords: " + numCorrect);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
