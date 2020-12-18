package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {
	
	private static char[][] grid = null;

	public static void main(String[] args) {
		
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("forest.txt"));
			
			List<String> lines = br.lines().collect(Collectors.toList());
			grid = new char[lines.size()][lines.get(0).length()];
			
			
			for (int y = 0; y < grid.length; y++) {
				for (int x = 0; x < grid[y].length; x++) {
					grid[y][x] = lines.get(y).charAt(x);
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//displayGrid(grid);

		int numTrees = countTrees(grid, 3, 1);
		
		System.out.println("Number of trees encountered: " + numTrees);
		
		//displayGrid(grid);
		
		
		//part 2
		
		Position[] slopes = new Position[] {
				new Position(1,1),
				new Position(3,1),
				new Position(5,1),
				new Position(7,1),
				new Position(1,2),
		};
		
		long product = Arrays.stream(slopes).map(s -> (long)countTrees(grid, s.x, s.y)).reduce(1L, (acc, next) -> acc * next);
		
		System.out.println("Product: " + product);
	}
	
	private static class Position {
		public int x;
		public int y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	
	public static int countTrees(char[][] grid, int deltaX, int deltaY) {
		int count = 0;
		
		Position pos = new Position(0,0);
		
		while (pos.y < grid.length) {
			if (grid[pos.y][pos.x % grid[0].length] == '#') {
				//grid[pos.y][pos.x % grid[0].length] = 'O';
				count++;
			}
			
			pos = new Position(pos.x + deltaX, pos.y + deltaY);
		}
		
		return count;
	}
	
	public static void displayGrid(char[][] grid) {
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[y].length; x++) {
				System.out.print(grid[y][x]);
			}
			System.out.println();
		}
	}

}
