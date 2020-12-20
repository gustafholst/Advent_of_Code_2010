package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day17 {

	private static class Cube {
		public int x,y,z,w;
		public boolean active = false;
		
		public Cube(int x , int y, int z, int w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
			
		@Override
		public String toString() {
			return active ? "#" : ".";
		}

		public int countActiveNeighbours(Set<Cube> activeCells) {
			int[] dir = new int[] {-1,0,1};
			
			int count = 0;
			for (int x = 0; x < dir.length; x++) {
				for (int y = 0; y < dir.length; y++) {
					for (int z = 0; z < dir.length; z++) {
						for (int w = 0; w < dir.length; w++) {
							Cube c = new Cube(this.x + dir[x], this.y + dir[y], this.z + dir[z], this.w + dir[w]);					
							if (activeCells.contains(c) && !c.equals(this)) {
								count++;
							}	
						}				
					}
				}
			}
	
			return count;
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof Cube) {
				Cube o = (Cube)other;
				return o.x == this.x && o.y == this.y && o.z == this.z && o.w == this.w;
			}
			return false;
		}
				
		@Override
		public int hashCode() {
			return x & y & z & w;
		}		
	}
	
	
	public static void displayLayer(List<List<Cube>> cubes, int z) {
		
		Collections.sort(cubes, (a,b) -> Integer.compare(a.get(0).y, b.get(0).y));
		
		for (List<Cube> row : cubes) {
			Collections.sort(row, (a,b) -> Integer.compare(a.x, b.x));
			
			for (Cube c : row) {
				System.out.print(c);
			}
			
			System.out.println();
		}
		
	}
	
	public static void main(String[] args) {
			
		Set<Cube> activeCells = new HashSet<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("pocket_dimension.txt"));
			
			int col = 0;
			int row = 0;
			String line = br.readLine();
			while(line != null) {
				
				char[] chars = line.toCharArray();
				col = 0;
				for (char ch : chars) {
					col++;
					
					Cube c = new Cube(col, row, 0, 0);
					c.active = ch == '#' ? true : false;
					
					if (c.active) {	
						activeCells.add(c);
					}
				}
					
				row++;
			    line = br.readLine();
			}
			
			br.close();
			
			//System.out.println("Active cells: " + activeCells.size());
						
			for (int cycle = 0; cycle < 6; cycle++) {
				Set<Cube> nextGeneration = new HashSet<>();

//			   -  If a cube is active and exactly 2 or 3 of its neighbors are also active,
//				the cube remains active. Otherwise, the cube becomes inactive.
//			   -  If a cube is inactive but exactly 3 of its neighbors are active,
//				the cube becomes active. Otherwise, the cube remains inactive.
			
				/* slow */
				//int widthXY = 14;  // 8 (starting width XY) + 6 number of generations;
				//int widthZW = 7;    // 1 (starting width ZW) + number of generations
				
				/* check only section with active cells plus one "layer" outside to see if needs to activate */
				int startX = activeCells.stream().map(c -> c.x).min(Integer::compare).get() - 1;
				int endX = activeCells.stream().map(c -> c.x).max(Integer::compare).get() + 2;
				int startY = activeCells.stream().map(c -> c.y).min(Integer::compare).get() - 1;
				int endY = activeCells.stream().map(c -> c.y).max(Integer::compare).get() + 2;

				int startZ = activeCells.stream().map(c -> c.z).min(Integer::compare).get() - 1;
				int endZ = activeCells.stream().map(c -> c.z).max(Integer::compare).get() + 2;
				int startW = activeCells.stream().map(c -> c.w).min(Integer::compare).get() - 1;
				int endW = activeCells.stream().map(c -> c.w).max(Integer::compare).get() + 2;
								
				for (int x = startX; x < endX; x++) {
					for (int y = startY; y < endY; y++) {
						for (int z = startZ; z < endZ; z++) {
							for (int w = startW; w < endW; w++) {
								Cube c = new Cube(x, y, z, w);
								
								if (activeCells.contains(c)) {
									//check if 2 or 3 active neighbours otherwise delete (don't add)
									int activeNeighbours = c.countActiveNeighbours(activeCells);
									if (activeNeighbours == 2 || activeNeighbours == 3) {
										nextGeneration.add(c);
									}			
								}
								else {
									//check if 3 active neighbours, then add
									int activeNeighbours = c.countActiveNeighbours(activeCells);
									if (activeNeighbours == 3) {
										nextGeneration.add(c);
									}						
								}
							}							
						}
					}
				}
				
				activeCells = nextGeneration;
				System.out.println("Active cells: " + activeCells.size());
			}
					
			System.out.println("Active cells: " + activeCells.size());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
