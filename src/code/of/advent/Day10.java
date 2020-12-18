package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day10 {

	private static List<Adapter> adapters;

	private static class Adapter {

		public final int jolt;

		public Adapter(int j) {
			this.jolt = j;
		}

		public int getJoltageDiffWith(Adapter other) {
			return other.jolt - this.jolt;
		}

		public boolean canConnectTo(Adapter other) {
			int diff = getJoltageDiffWith(other);
			return diff > 0 && diff <= 3;
		}
		
		@Override
		public int hashCode() {
			return jolt;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof Adapter) {
				return ((Adapter)other).jolt == this.jolt;
			}
			return false;
		}

	}

	public static boolean isPossibleSequence(List<Adapter> list) {
		for (int i = 0; i < list.size() -1; i++) {		
			Adapter a = list.get(i);
			Adapter b = list.get(i+1);

			if (!a.canConnectTo(b)) {
				return false;
			}		
		}
		return true;
	}
	
	public static boolean isGoal(Adapter a) {
		return a.jolt == adapters.get(adapters.size()-1).jolt;
	}
	
	public static List<Adapter> getNextClosestAdaptersFrom(Adapter adapter) {
		return adapters.stream()
				.dropWhile(a -> a.jolt <= adapter.jolt)
				.limit(3)
				.collect(Collectors.toList());
	}
	
	public static Map<Adapter, Long> memo = new HashMap<>();
	
	public static long recurse(Adapter adapter) {
		if (isGoal(adapter)) {
			return 1;
		}
		else if (memo.keySet().contains(adapter)) {
			return memo.get(adapter);
		}
		else {
			//calculate it	
			long possiblePaths = 0;
			List<Adapter> nextClosest = getNextClosestAdaptersFrom(adapter);
			
			System.out.println("calculating");
			for (Adapter a : nextClosest) {
				if ((a.jolt - adapter.jolt) <= 3) {
					possiblePaths += recurse(a);
				}	
			}
			
			memo.put(adapter, possiblePaths);
			return possiblePaths;
		}		
	}

	public static void main(String[] args) {

		try {
			BufferedReader br = new BufferedReader(new FileReader("adapters.txt"));

			adapters = br.lines().map(Integer::parseInt).sorted().map(Adapter::new).collect(Collectors.toList());

			adapters.add(new Adapter(0));
			adapters = adapters.stream().sorted((a, b) -> Integer.compare(a.jolt, b.jolt)).collect(Collectors.toList());
			adapters.add(new Adapter(adapters.get(adapters.size() -1).jolt + 3));

			/* Part 1 */
			adapters.forEach(a -> System.out.println(a.jolt));

			int ones = 0;
			int twos = 0;
			int threes = 0;

			for (int i = 0; i < adapters.size() -1; i++) {

				Adapter a = adapters.get(i);
				Adapter b = adapters.get(i+1);

				if (!a.canConnectTo(b)) {
					System.err.println("Impossible connection");
					break;
				}

				int diff = a.getJoltageDiffWith(b);

				if (diff == 1) ones++;
				if (diff == 2) twos++;
				if (diff == 3) threes++;		

			}

			System.out.println("ones: " + ones + "\nthrees: " + threes);

			System.out.println("answer: " + ones * threes);

			/* Part 2 */

			System.out.println("possibilities: " + recurse(adapters.get(0)));
			
			//memo.entrySet().stream().forEach(es -> System.out.println(es.getKey() + ": " + es.getValue()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
