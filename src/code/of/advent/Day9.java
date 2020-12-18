package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day9 {
	
	private static class Buffer<T> {
		
		private int limit;
		private List<T> data;
		
		public Buffer(int size) {
			this.limit = size;
			data = new ArrayList<>(this.limit);
		}
		
		public void add(T element) {
			data.add(element);
			if (data.size() > this.limit) {
				data.remove(0);
			}
		}
		
		public int size() {
			return data.size();
		}
		
		public T elementAt(int index) throws Exception {
			if (index >= data.size())
				throw new Exception("Index out of range: " + index);
			
			return data.get(index);
		}
		
	}
	
	public static boolean isValid(Buffer<Long> buffer, Long n) {
		try {
			for (int i = 0; i < buffer.size(); i++) {
				long a = buffer.elementAt(i);
				for (int j = i; j < buffer.size(); j++) {
					long b = buffer.elementAt(j);
					
					if (a == b)
						continue;
					
					if (a + b == n)
						return true;
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return false;
	}
	
	
	public static long checkStreak(List<Long> numbers, int start, long goalSum) {
		int index = start;
	
		long sum = 0;
		
		long smallest = Integer.MAX_VALUE;
		long largest = 0;
		
		while(sum < goalSum && index < numbers.size()) {
			long n = numbers.get(index);
			
			if (n < smallest)
				smallest = n;
			
			if (n > largest)
				largest = n;
			
			sum += n;
			if (sum == goalSum) {
				return smallest + largest;
			}
			index++;
		}
		
		return -1;
	}
	
	
	public static Stream<String> getInput() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("xmas_numbers.txt"));
			return br.lines();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} 
	}

	public static void main(String[] args) {
		
		int bufferSize = 25;
		
		Buffer<Long> buffer = new Buffer<Long>(bufferSize);
		
		getInput().mapToLong(Long::parseLong).limit(bufferSize).forEach(buffer::add);    //read preamble and store in buffer
		
		OptionalLong notValid = getInput().mapToLong(Long::parseLong)
				.skip(bufferSize)
		.filter(n -> {
			boolean valid = isValid(buffer, n);
			buffer.add(n);
			return !valid;
		}).findFirst();
		
		
		System.out.println("First not valid number: " + notValid.getAsLong());
		
		List<Long> allNumbers = getInput().map(s -> Long.parseLong(s)).collect(Collectors.toList());
		
		for (int i = 0; i < allNumbers.size(); i++) {
			
			long result = checkStreak(allNumbers, i, notValid.getAsLong());
			
			if (result != -1) {
				System.out.println("encryption weakness: " + result);
				break;
			}
			
		}

	}

}
