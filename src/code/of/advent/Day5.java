package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 {

	private static class BoardingPass {
		
		public int row;
		public int col;
		
		public int getSeatId() {
			return this.row * 8 + this.col;
		}
		
		private static boolean isUpper(char c) {
			return c == 'B' || c == 'R';
		}
		
		private static int recurse(String str, int upperLimit, int lowerLimit) {
			if (str.length() == 1) {
				return isUpper(str.charAt(0)) ? upperLimit : lowerLimit;
			}
			
			int upper, lower;
			
			if (isUpper(str.charAt(0))) {  //upper half
				upper = upperLimit;
				lower = lowerLimit + (upperLimit - lowerLimit) / 2 + 1;
			}
			else {   //lower half
				upper = lowerLimit + (upperLimit - lowerLimit) / 2;
				lower = lowerLimit;
			}
			
			return recurse(str.substring(1), upper, lower);
		}
		
		private static int getNumber(String str) {
			int upperLimit = (int) (Math.pow(2,str.length()) - 1);
			int lowerLimit = 0;
		
			
			return recurse(str, upperLimit, lowerLimit);
		}
		
		public static BoardingPass parseString(String str) {
			
			String rowString = str.substring(0,7);
			String colString = str.substring(7, str.length());
			
			BoardingPass bPass = new BoardingPass();
			
			bPass.row = getNumber(rowString);
			bPass.col = getNumber(colString);
			
			return bPass;
		}
	}
	
	public static void main(String[] args) {
	
		try {
			BufferedReader br = new BufferedReader(new FileReader("boarding_passes.txt"));
			
			//Part 1
			//Optional<Integer> maxId = br.lines().map(line -> BoardingPass.parseString(line))
			//.map(bp -> bp.getSeatId()).max(Integer::compare);		
			//.forEach(bp -> System.out.println(String.format("row: %d\t\tcol:%s\t\tseatId: %d\n", bp.row, bp.col, bp.getSeatId())));
					
			//System.out.println("Max id: " + maxId.get());
			
			//Part 2
			List<Integer> seatIds = br.lines().map(line -> BoardingPass.parseString(line))
					.map(bp -> bp.getSeatId()).sorted().collect(Collectors.toList());	
			
			int mySeatId = 0;
			for (int i = 1; i < seatIds.size() - 1; i++) {
				int id = seatIds.get(i);
				
				if (seatIds.get(i+1) != id + 1) {  //if next seat is not next consecutive number
					mySeatId = id + 1;  //then my seat is that missing number
					break;
				}
			}
			
			System.out.println("My seat id: " + mySeatId);
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
