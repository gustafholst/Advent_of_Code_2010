package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {
	
	private static class Seat {
		
		public final int x, y;
		
		public char state = '.';
		
		public Seat(int x, int y, char initialState) {
			this.x = x;
			this.y = y;
			this.state = initialState;
		}
		
		@Override
		public String toString() {
			return String.valueOf(state);
		}
		
	}
	
	private static class SeatingSystem {
		
		private Seat[][] current;
		private Seat[][] next;
		
		private boolean changed = false;
		
		public SeatingSystem(List<String> lines) {
			
			current = new Seat[lines.size()][lines.get(0).length()];
			next = new Seat[lines.size()][lines.get(0).length()];
			
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					current[i][j] = new Seat(i, j, lines.get(i).charAt(j));
					next[i][j] = new Seat(i, j, lines.get(i).charAt(j));;
				}
			}
		}
		
		public boolean wasChanged() {
			return changed;
		}
		
		public int countOccupied() {
			int count = 0;
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					if (current[i][j].state == '#') {
						count++;
					}				
				}
			}
			
			return count;
		}
		

		public void morphe() {
			changed = false;
			
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					if (current[i][j].state != '.') {
						if (next[i][j].state != current[i][j].state) {
							changed = true;
						}
						
						current[i][j].state = next[i][j].state;
					}				
				}
			}		
		}
		

		public void calculateNextState() {
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					if (current[i][j].state != '.') {
						next[i][j].state = getNextState(current[i][j]);	
					}				
				}
			}
		}
		
		
		public char getNextState(Seat s) {
//		    If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
//		    If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
//		    Otherwise, the seat's state does not change.	
			
			/* part 1 */
//			if (s.state == 'L') {
//				if (countAdjacent(s.x, s.y, '#') == 0) {
//					return '#';
//				}
//			}
//			else if(s.state == '#') {
//				if (countAdjacent(s.x, s.y, '#') >= 4) {
//					return 'L';
//				}
//			}
			
			/* part 2 */
			
			if (s.state == 'L') {
				if (countVisibleOccupied(s.x, s.y) == 0) {
					return '#';
				}
			}
			else if(s.state == '#') {
				if (countVisibleOccupied(s.x, s.y) >= 5) {
					return 'L';
				}
			}
			
			return s.state; //error
		}
		
		public int countVisibleOccupied(int y, int x) {
			int count = 0;
			
			if (x == current[0].length-1 && y == current.length-1) {
				System.out.println();
			}
			
			if (y < current.length - 1) {
				//down
				for (int i = y + 1; i < current.length; i++) {
					if (current[i][x].state == 'L') {
						break;
					}
					if (current[i][x].state == '#') {
						count++;
						break;
					}
					
				}
			}
			
			if (y > 0) {
				//up
				for (int i = y - 1; i >= 0; i--) {
					if (current[i][x].state == 'L') {
						break;
					}
					if (current[i][x].state == '#') {
						count++;
						break;
					}
					
				}
			}
			
			if (x > 0) {
				//left
				for (int i = x - 1; i >= 0; i--) {
					if (current[y][i].state == 'L') {
						break;
					}
					if (current[y][i].state == '#') {
						count++;
						break;
					}
					
				}
			}
			
			if (x < current[0].length - 1) {
				//right
				for (int i = x + 1; i < current[0].length; i++) {
					if (current[y][i].state == 'L') {
						break;
					}
					if (current[y][i].state == '#') {
						count++;
						break;
					}
					
				}
			}
			
			if (y > 0 && x < current[0].length) {
				//up right
				int i = y - 1;
				int j = x + 1;

				while (i >= 0 && j < current[i].length)	{
					if (current[i][j].state == 'L') {
						break;
					}
					if (current[i][j].state == '#') {
						count++;
						break;
					}
					i--;
					j++;
				}
			}
			
			if (y > 0 && x > 0) {
				//up left
				int i = y - 1;
				int j = x - 1;
				
				while (i >= 0 && j >= 0) {
					if (current[i][j].state == 'L') {
						break;
					}
					if (current[i][j].state == '#') {
						count++;
						break;
					}
					i--;
					j--;
				}
			}
			
			
			if (y <= current.length && x <= current[0].length) {
				//down right
				int i = y + 1;
				int j = x + 1;
				
				while(i < current.length && j < current[i].length) {
					if (current[i][j].state == 'L') {
						break;
					}
					if (current[i][j].state == '#') {
						count++;
						break;
					}
					
					i++;
					j++;
				}
			}
			
			if (y <= current.length && x > 0) {
				//down left
				int i = y + 1;
				int j = x - 1;
				while(i < current.length && j >= 0 ) {
					if (current[i][j].state == 'L') {
						break;
					}
					if (current[i][j].state == '#') {
						count++;
						break;
					}
	
					i++;
					j--;
				}
				
			}
				
			
			return count;
		}
		
		public int countAdjacent(int x, int y, char state) {
			int count = 0;
			
			//above
			if (y > 0 ) {
				if (x > 0) {
					if (current[x-1][y-1].state == state) count++;
				}	
				if (current[x][y-1].state == state) count++;
				if (x < current.length -1) {
					if (current[x+1][y-1].state == state) count++;
				}		
			}
			
			//below
			if (y < current[0].length - 1) {
				if (x > 0) {
					if (current[x-1][y+1].state == state) count++;
				}
				if (current[x][y+1].state == state) count++;
				if (x < current.length -1) {
					if (current[x+1][y+1].state == state) count++;
				}
			}
			
			
			//left
			if (x > 0) {
				if (current[x-1][y].state == state) count++;
			}
			
			//right
			if (x < current.length -1) {
				if (current[x+1][y].state == state) count++;
			}
			
					
//			int i = y - 1;
//			int j = x - 1;
//			
//			int y_off = 1;
//			int x_off = 1;
//			
//			if (y == 0) i++;
//			if (y == current.length - 1) y_off = 0;
//			
//			if (x == 0) j++;
//			if (x == current[0].length - 1) x_off = 0;
//						
//			for (; i < y + y_off; i++) {	
//				for (; j < x + x_off; j++) {					
//					if (i == y && j == x)  // do not count center
//						continue;
//							
//					if (current[i][j].state == state) {
//						count++;
//					}				
//				}
//			}
			return count;
		}
			
		public void display() {
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					System.out.print(current[i][j]);
				}
				System.out.println();
			}
			System.out.println();
		}
		 
	}

	public static void main(String[] args) {


		try {
			
			BufferedReader br = new BufferedReader(new FileReader("seats.txt"));
			
			List<String> lines = br.lines().collect(Collectors.toList());
			
			SeatingSystem ss = new SeatingSystem(lines);
			
			boolean changed = true;
			while (changed) {
				ss.display();
				
				ss.calculateNextState();
				
				ss.morphe();
				changed = ss.wasChanged();
			}
			
			System.out.println("Occupied seats: " + ss.countOccupied());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
