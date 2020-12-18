package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Day14 {
	
	private static class Device {
		
		public static final int size = 36;
		
		Map<Long, Long> memory = new HashMap<>();
		
		char[] mask = new char[size];   // X 0 1
		
		
		public void doInstruction(String s) {
			if (s.startsWith("mask =")) {
				setMask(s.substring(7));   //indexof '=' + 1
			}
			else {
				String addrStr = s.substring(s.indexOf('[')+1, s.indexOf(']'));
				int memAddr = Integer.parseInt(addrStr);
				
				System.out.println(s);
				long value = Long.parseLong(s.substring(s.indexOf('=')+2));
				
				storeValue(memAddr, value);
 			}			
		}
		
		public void doPart2Instr(String s) {
			if (s.startsWith("mask =")) {
				setMask(s.substring(7));   //indexof '=' + 1
			}
			else {
				String addrStr = s.substring(s.indexOf('[')+1, s.indexOf(']'));
				char[] addrBytes = longTo36(Long.parseLong(addrStr));
				
				//System.out.println(s);
				long value = Long.parseLong(s.substring(s.indexOf('=')+2));
				
				//apply mask			
				for (int i = size - 1; i >= 0; i--) {
					if (mask[(size-1)-i] == '0') {
						continue;
					}
					else
						addrBytes[i] = mask[(size-1)-i];
				}
				
				memAddresses = new HashSet<>();
				
				recurse(addrBytes, size-1);
				
				for (long addr : memAddresses)
					memory.put(addr, value);
 			}		
		}
		
		static Set<Long> memAddresses;
		
		public void recurse(char[] addresses, int i) {		
			if (i < 0) {
				return;
			}
			else if (addresses[i] != 'X'){
				recurse(addresses, i - 1);
			}
			else {
				if (addresses[i] == 'X') {
					
					char[] copy = new char[size];					
					System.arraycopy(addresses, 0,copy, 0, size);
					
					copy[i] = '0';
					recurse(copy, i-1);
					if (!containsX(copy)) {
						memAddresses.add(arrayToLong(copy));					
						//printByteArray(copy);
					}
									
					copy[i] = '1';	
					recurse(copy, i-1);
					if (!containsX(copy)) {
						memAddresses.add(arrayToLong(copy));					
						//printByteArray(copy);
					}
				}					
			}	
		}
		
		public void storeValue(long address, long value) {
			char[] v = longTo36(value);
			
			for (int i = size - 1; i >= 0; i--) {
				if (mask[(size-1)-i] == 'X') {
					continue;
				}
				else
					v[i] = mask[(size-1)-i];
			}
			
			long valueToStore = arrayToLong(v);
			System.out.println("Storing " + valueToStore);
			
			memory.put(address, valueToStore);
			
			printByteArray(v);
		}
		
		public long arrayToLong(char[] array) {
			long v = 0;
			for (int i = size - 1; i >= 0; i--) {
				if (array[i] == '1') {
					v += Math.pow(2, i);
				}
			}
			return v;
		}
		
		public char[] longTo36(Long value) {		
			char[] b = new char[size];
			
			for (int i = size - 1; i >= 0; i--) 
				b[i] = '0';
			
			for (int i = size - 1; i >= 0; i--) {
				long bit = (long) Math.pow(2, i);
				
				if (bit > value)
					continue;
				
				value = value - bit;
				
				if (value >= 0) {
					b[i] = '1';
				}						
			}
			return b;
		}
		
		public void setMask(String s) {
			mask = s.toCharArray();
		}
		
		public long sumAllMemory() {
			return  memory.values().stream().mapToLong(Long::longValue).sum();
		}
	}
	
	public static void printByteArray(char[] b) {
		for (int i = b.length - 1; i >= 0; i--) 
			System.out.print(b[i]);
		System.out.println();
	}
	
	public static boolean containsX(char[] b) {
		for (int i = b.length - 1; i >= 0; i--) 
			if (b[i] == 'X')
				return true;
		return false;
	}

	public static void main(String[] args) {

		Device d = new Device();
			
		try {
			BufferedReader br = new BufferedReader(new FileReader("init_program.txt"));
			
			/* part 1*/
			//br.lines().forEach(d::doInstruction);
			
			/* PART 2 */
			br.lines().forEach(d::doPart2Instr);
			
			System.out.println("Sum: " + d.sumAllMemory());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}



