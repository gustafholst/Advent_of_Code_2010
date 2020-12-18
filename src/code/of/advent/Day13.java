package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
	
	private static class Bus {
		public long id;
		public int index;
		
		public static Bus parseBus(String s, int index) {
			Bus b = new Bus();
			
			if (s.equals("x"))
				b.id = -1L;
			else
				b.id = Long.parseLong(s);
			
			b.index = index;
			
			return b;
		}
	}
	

	public static void main(String[] args) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("buses.txt"));
			
			
			long startTime = Long.parseLong(br.readLine());
			
			String secondline = br.readLine();
			
			
			String[] busLines = secondline.split(",");
				
			
			/* part 1 
			List<Long> busIds = Arrays.stream(busLines)
					.filter(s -> !s.equals("x"))
					.map(Long::valueOf).collect(Collectors.toList());
					
			List<Long> nextDepartures = busIds.stream().map(i -> {
				long factor = (startTime)/i;
				long nextDep = i * (factor + 1);
				return nextDep;
			}).map(i -> i - startTime).collect(Collectors.toList());
			
			
			long earliest = Long.MAX_VALUE;
			int index = 0;
			for (int i = 0; i < nextDepartures.size(); i++) {
				//if (nextDepartures.get(i) < 0) continue;
				
				if (nextDepartures.get(i) < earliest) {
					earliest = nextDepartures.get(i);
					index = i;
				}		
			}
			
			Long id = busIds.get(index);
			
			System.out.println("Bus id: " + id + "\twait time: " + earliest + "\tmult: " + id * earliest);
			*/
			
			
			/* part 2 */
				
			List<Bus> buses = new ArrayList<>();
			for (int i = 0; i < busLines.length; i++) {
				buses.add(Bus.parseBus(busLines[i], i));
			}
			
			
			long t = 0;
			long period = 1;
		
			for (Bus b : buses) {
				if (b.id == -1) {
					continue;
				}
				
				while(((t + b.index) % b.id) > 0) {
					t += period;
				}
				
				period *= b.id;
			}
			
			System.out.println("t = " + t);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static long lcm(long number1, long number2) {
	    if (number1 == 0 || number2 == 0) {
	        return 0;
	    }
	    long  absNumber1 = Math.abs(number1);
	    long  absNumber2 = Math.abs(number2);
	    long  absHigherNumber = Math.max(absNumber1, absNumber2);
	    long  absLowerNumber = Math.min(absNumber1, absNumber2);
	    long  lcm = absHigherNumber;
	    while (lcm % absLowerNumber != 0) {
	        lcm += absHigherNumber;
	    }
	    return lcm;
	}

}
