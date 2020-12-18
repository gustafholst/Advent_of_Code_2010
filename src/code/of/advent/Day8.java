package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 {
	
	private static class Instruction {
		
		public String op;
		final public int value;
		
		public boolean executed = false;
		
		public Instruction(String op, int val) {
			this.op = op;
			this.value = val;
		}
		
		public void flipJMPNOP() {
			if (op.equals("jmp"))
				op = "nop";
			else if (op.equals("nop"))
				op = "jmp";
		}
		
		public static Instruction parseString (String str) {
			String op = str.substring(0, 3);
			int value = Integer.parseInt(str.substring(4));
			
			return new Instruction(op, value);
		}
	}
	
	
	private static class Computer {
		
		public static final int OK = 0;
		public static final int LOOP = -1;
		
		private int accumulator = 0;
		private int instructionPointer = 0;
		
		List<Instruction> instructions;
		
		private int returnCode = OK;
		
		public void reset() {
			accumulator = 0;
			instructionPointer = 0;
		}
		
		public int getAccumulatorValue() {
			return accumulator;
		}
		
		private void doInstruction(Instruction instruction) {		
				switch (instruction.op) {
				case "acc":
					accumulator += instruction.value;
					instructionPointer += 1;
					break;
				case "jmp":
					instructionPointer += instruction.value;
					break;
				case "nop":
					instructionPointer += 1;
					break;	
				}
						
				instruction.executed = true;
		}
		
		public int runCode(List<Instruction> code) {
			instructions = code;
			
			while (true) {			
				
				
				Instruction next = instructions.get(instructionPointer);
				
				if (next.executed) {
					return LOOP;
				}
			
				doInstruction(next);	
				
				if (instructionPointer >= instructions.size()) {
					return OK;		
				}
			}	
		}		
	}

	public static void main(String[] args) {
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("code_day8.txt"));
			
			List<Instruction> code = br.lines().map(Instruction::parseString).collect(Collectors.toList());
			
			Computer c = new Computer();
			
			for (Instruction instruction : code) {
				if (instruction.op.equals("nop") || instruction.op.equals("jmp")) {
					instruction.flipJMPNOP();
					
					int returnCode = c.runCode(code);
					if (returnCode == Computer.OK) {
						System.out.println("return code: " + returnCode);
						System.out.println("Accumulator before first repeated instruction: " + c.getAccumulatorValue());
						break;
					}
					
					instruction.flipJMPNOP();
					c.reset();
					code.forEach(i -> i.executed = false);
				}
			}	
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
