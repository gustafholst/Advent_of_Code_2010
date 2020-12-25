package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Stack;

public class Day18 {
	
	private static class Calculator {
		
		public final char OPEN_PAREN = '(';
		public final char CLOSE_PAREN = ')';
		public final char PLUS = '+';
		public final char MULT = '*';
		
		
		public long calculate(String input) {
			String postFix = parseInfixString(input);
			return calculatePostFix(postFix);
		}
		
		static int precedence(char ch) 
	    { 
	        switch (ch) 
	        { 
	        case '+': 
	            return 2;        
	        case '*': 
	            return 1; 
	        } 
	        return -1; 
	    } 

		public String parseInfixString(String infix) {
			Stack<Character> stack = new Stack<>();
			StringBuilder postFix = new StringBuilder();

			for (int i = 0; i < infix.length(); ++i) { 
				char c = infix.charAt(i); 
				
				if (c == ' ')
					continue;

				if (Character.isLetterOrDigit(c)) {
					postFix.append(c);
				} else if (c == OPEN_PAREN) {
					stack.push(c); 
				} else if (c == CLOSE_PAREN) { 
					while (!stack.isEmpty() && stack.peek() != OPEN_PAREN) {
						postFix.append(stack.pop()); 
					}
						
					stack.pop(); 
				} else { 
					while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) { 	                    
						postFix.append(stack.pop()); 
					} 
					stack.push(c); 
				}     	
			}

			// pop all the operators from the stack 
			while (!stack.isEmpty()){ 
				if(stack.peek() == '(') 
					return "Invalid Expression"; 
				postFix.append(stack.pop()); 
			} 
			
			return postFix.toString(); 
		}
		
		public long calculatePostFix(String postFix) {
			Stack<Long> stack = new Stack<>();

			for (int i = 0; i < postFix.length(); ++i) { 
				char c = postFix.charAt(i); 

				if (Character.isDigit(c)) {
					stack.push((long) (c - '0'));
				}
				else {
					long op1 = stack.pop();
					long op2 = stack.pop();

					switch (c) {
					case PLUS:
						stack.push(op1 + op2); break;
					case MULT:
						stack.push(op1 * op2); break;	
					}
				}

			}
			return stack.pop();
		}		
	}

	public static void main(String[] args) {
		
		Calculator calculator = new Calculator();
		
		//System.out.println(calculator.calculate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"));
		
		//System.out.println(calculator.parseInfixString("1 + (2 * 3) + (4 * (5 + 6))"));
		

		try {
			BufferedReader br = new BufferedReader(new FileReader("homework.txt"));
						
			long sum = br.lines().map(l -> calculator.calculate(l)).mapToLong(Long::valueOf).sum();
			
			System.out.println("Sum: " + sum);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
