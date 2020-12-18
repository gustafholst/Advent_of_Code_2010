package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day7 {
	
	private List<Bag> bags = new ArrayList<>();
	
	private static class Bag {
		
		public String adjective;
		public String color;
		
		public Map<Bag, Integer> canCarryMap = new HashMap<>();
		
		public Bag(String adj, String col) {
			this.adjective = adj;
			this.color = col;
		}
		
		public long getBagCount() {
			return canCarryMap.values().stream().mapToInt(Integer::valueOf).sum();
		}
		
		public int canCarry(Bag bag) {
			Integer number = canCarryMap.get(bag);
			return number == null ? 0 : number;
		}
		
		public boolean canCarryNoBag() {
			return canCarryMap.size() == 0;
		}
		
		public void addCarryBag(Bag bag, int amount) {
			if (amount > 0)
				canCarryMap.put(bag, amount);
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof Bag) {
				Bag otherBag = (Bag)other;
				return this.adjective.equals(otherBag.adjective) && this.color.equals(otherBag.color);
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.adjective.hashCode() & this.color.hashCode();
		}
	}
	
	public Bag findBag(Bag b) {
		if (bags.contains(b)) {
			return bags.get(bags.indexOf(b));
		}
		return b;
	}
	
	public void addBag(Bag b) {
		if (!bags.contains(b)) {
			bags.add(b);
		}
	}
	
	private void parseAndStoreBag(String line) {
		String bagStr = line.substring(0, line.indexOf("bags"));
		String[] tokens = bagStr.split(" ");
		String adj = tokens[0];
		String col = tokens[1];
		
		Bag b = findBag(new Bag(adj, col));
		
		String contain = "contain";
		String carryBagsSubstring = line.substring(line.indexOf(contain) + contain.length() + 1);
		String[] carryBags = carryBagsSubstring.split(",");
		
		Arrays.stream(carryBags).map(String::trim).forEach(str -> {
			String[] t = str.split(" ");
			
			int amount = 0;
			if (!t[0].equals("no")) {
				amount = Integer.valueOf(t[0]);
				
				String a = t[1];
				String c = t[2];
				
				Bag carryBag = findBag(new Bag(a,c));
				b.addCarryBag(carryBag, amount);
				addBag(carryBag);
			}	
		});
		
		addBag(b);
	}
	
	public boolean recursePart1(Bag searched, Bag b) {
		if (b.canCarryNoBag()) {
			return false;
		}
		else if (b.canCarry(searched) > 0) {
			return true;
		}
		else {
			boolean canCarry = false;
			for (Bag carryBag : b.canCarryMap.keySet()) {
				if (recursePart1(searched, carryBag))
					canCarry = true;
			}
			return canCarry;
		}
	}
	
	public long recursePart2(Bag b, long total) {
		if (b.canCarryNoBag()) {
			return 0;
		}
		else {
			long totalThisBag = b.getBagCount();
			for (Bag carryBag : b.canCarryMap.keySet()) {
				totalThisBag += recursePart2(carryBag, totalThisBag) * b.canCarryMap.get(carryBag);  //multiply with amount of bags of that type
			}
			return totalThisBag; //+ ;
		}
	}
	
	
	
	public long getNumberOfBags(Bag searched) {
		/* Part 1*/
//		return bags.stream().filter(b -> !b.equals(searched))
//				.map(b -> recursePart1(searched, b))  //part1
//				.filter(b -> b)
//				.count();
		
		/* Part 2 */
		return recursePart2(findBag(searched), 0);  //start with count 0
	}

	public static void main(String[] args) {
		
		Day7 day7 = new Day7();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("luggage_rules.txt"));
			
			br.lines().forEach(day7::parseAndStoreBag);
			
			System.out.println(day7.bags.size());
			
			System.out.println("num bags: " + day7.getNumberOfBags(new Bag("shiny", "gold")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
