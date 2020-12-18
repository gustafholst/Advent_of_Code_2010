package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Day16 {
	
	private static class Rule {
		
		public final String name;
		public final int br1, er1,br2,er2;
		
		public Rule(String n, int b1, int e1, int b2, int e2) {
			name = n;
			br1 = b1;
			er1 = e1;
			br2 = b2;
			er2 = e2;
		}
		
		public boolean satisfiesRanges(int n) {
			return (n >= br1 && n <= er1) || (n >= br2 && n <= er2);
		}
		
		public int getErrorCount(Ticket t) {
			int errorCount = 0;
			for (Integer n : t.numbers) {
				if (!satisfiesRanges(n)) {
					errorCount += n;
				}
			}
			return errorCount;
		}
		
		public String toString() {
			return name + " " + br1 + "-" + er1 + " or " + br2 + "-" + er2;
		}

		public boolean couldBeIndex(int t, List<Ticket> tickets) {

			for (int i = 0; i < tickets.size(); i++) {
				if (!satisfiesRanges(tickets.get(i).numbers.get(t)))
					return false;
					
			}
			return true;
		}
		
	}
	
	private static class Ticket {
		public boolean valid = true;
		
		public List<Integer> numbers = new ArrayList<>();
		
	}

	public static void main(String[] args) {
		
		List<Rule> rules = new ArrayList<>();
		List<Ticket> tickets = new ArrayList<>();

		Ticket myTicket = new Ticket();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("tickets.txt"));
			
			/*
			class: 1-3 or 5-7
			row: 6-11 or 33-44
			seat: 13-40 or 45-50

			your ticket:
			7,1,14

			nearby tickets:
			7,3,47
			40,4,50
			55,2,20
			38,6,12
			*/
			
			int emptyLineCount = 0;
			
			String line = br.readLine();
			while (line != null) {
				if (line.equals("")) {
					emptyLineCount++;			
				}			
				else if (emptyLineCount == 0) {
					String afterColon = line.substring(line.indexOf(":")+2);
					
					String[] tokens = afterColon.split(" ");
					
					String ruleName = line.substring(0, line.indexOf(":"));
				
					String[] range1 = tokens[0].split("-");
				    int startRange1 = Integer.valueOf(range1[0]);
				    int endRange1 = Integer.valueOf(range1[1]);
				    
				    String[] range2 = tokens[2].split("-");
				    int startRange2 = Integer.valueOf(range2[0]);
				    int endRange2 = Integer.valueOf(range2[1]);
				    
				    rules.add(new Rule(ruleName, startRange1, endRange1, startRange2, endRange2));
				}
				
				else if (emptyLineCount == 1) {
					if (!line.startsWith("y")) {
						String[] tokens = line.split(",");
						for (String s : tokens)
							myTicket.numbers.add(Integer.valueOf(s));
					}
				}
				
				else if (emptyLineCount == 2) {
					if (!line.startsWith("n")) {
						String[] tokens = line.split(",");
						Ticket t = new Ticket();
						for (String s : tokens)
							t.numbers.add(Integer.valueOf(s));
						
						tickets.add(t);
					}
				}
				
				
				line = br.readLine();
			}
			
			int errorRate = 0;
			ticketLoop:
			for (Ticket t : tickets) {				
				int rError = 0;
				
				for (int n : t.numbers) {								
					boolean satisfiesAtLeastOneRule = false;
					for (Rule r : rules) {
						if (r.satisfiesRanges(n)) {
							satisfiesAtLeastOneRule = true;
						}
					}
					
					if (!satisfiesAtLeastOneRule) {
						rError += n;
						t.valid = false;
						continue ticketLoop;
					}
				}
				errorRate += rError;
			}
			
			/* part 1*/
			//System.out.println("Error rate: " + errorRate);
			
			/* Part 2 */
			System.out.println("num tickets: " + tickets.size());
			tickets.removeIf(t -> !t.valid);
			System.out.println("num valid tickets: " + tickets.size());
			

			List<Rule> solution = solveAllConstraints(rules, tickets);

			displayRules(solution);
			
			long prod = 1;
			for (int i = 0; i < solution.size(); i++) {
				if (solution.get(i).name.startsWith("departure")) {
					prod *= myTicket.numbers.get(i);
				}
			}
			
			System.out.println("Product of departure rules: " + prod);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	private static List<Rule> solveAllConstraints(List<Rule> rules, List<Ticket> tickets) {
		List<Rule> solution = new ArrayList<>();
				
		Map<Rule, List<Integer>> possibilities = new HashMap<>();
		
		for (int r = 0; r < rules.size(); r++) {
			Rule rule = rules.get(r);
			List<Integer> p = new ArrayList<>();
			
			
			for (int t = 0; t < 20; t++) {				
				if (rule.couldBeIndex(t, tickets)) {
					p.add(t);
				}
			}
			
			possibilities.put(rule, p);
		}	
		
		while (!allSolved(possibilities)) {
			
			for (int r = 0; r < rules.size(); r++) {
				Rule rule = rules.get(r);
				
				List<Integer> p = possibilities.get(rule);
				if (p.size() == 1) {
					int index = p.get(0);
					
					removeFromAllOther(possibilities, rule, index);
				}
				
			}			
		}
		
		for (int i = 0; i < possibilities.size(); i++) {
			for (Entry<Rule, List<Integer>> e : possibilities.entrySet() ) {
				if (e.getValue().get(0) == i) {
					solution.add(e.getKey());
				}		
			}	
		}
		
		return solution;
	}
	
	private static void removeFromAllOther(Map<Rule, List<Integer>> possibilities, Rule r,  int index) {
		for (Entry<Rule, List<Integer>> e : possibilities.entrySet() ) {
			if (!e.getKey().equals(r)) {
				List<Integer> list = e.getValue();
				Iterator<Integer> it = list.iterator();	
				while(it.hasNext()) {
					Integer n = it.next();
					if (n == index) {
						it.remove();
						return;
					}
				}
			}		
		}	
	}

	private static boolean allSolved(Map<Rule, List<Integer>> map) {
		long numUnsolved = map.entrySet().stream().filter(es -> es.getValue().size() != 1).count();
		return numUnsolved == 0;
	}
	
	public static void displayRules(List<Rule> rules) {
		System.out.println("-----------RULES--------------------");
		rules.forEach(System.out::println);
		System.out.println("------------------------------------");
	}
}
