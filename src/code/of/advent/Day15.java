package code.of.advent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {
	
	private static class Memory {
		
		boolean wasNew;
		int turn = 1;
		
		int diff;
		
		private Map<Integer, Integer> memory = new HashMap<>();
		
		public Memory(List<Integer> startingNumbers) {
			for (int i = 0; i < startingNumbers.size(); i++) {
				store(startingNumbers.get(i));
			}		
		}
		
		public int getTurn() {
			return turn;
		}
		
		public void store(int n) {
			wasNew = isNew(n);
			if (!wasNew) {
				int previous = memory.get(n);
				diff = turn - previous;
			}
			
			memory.put(n, turn);
			turn++;
		}
		
		public int getNumTurnsSinceSpoken(int n) {
			int last = memory.get(n);
			return diff;
		}
		
		public boolean lastNumberWasNew() {
			return wasNew;
		}
		
		public boolean isNew(Integer n) {
			return !memory.keySet().contains(n);
		}
	}

	public static void main(String[] args) {

		List<Integer> startingNums = List.of(0,1,5,10,3,12,19);
		Memory mem = new Memory(startingNums);  
		
		int numTurns = 30000000;
		
		int spoken = startingNums.get(startingNums.size()-1);
		
		while (mem.getTurn() <= numTurns) {
			
			if (mem.lastNumberWasNew()) {
				spoken = 0;
			}
			else {
				spoken = mem.getNumTurnsSinceSpoken(spoken);
			}
			
			mem.store(spoken);
			
			//System.out.println(spoken);
		}
		
		System.out.println(spoken);
		

	}

}
