package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Day12 {
	
	
	private static class Entity {
		
		public final static String LEFT = "L";
		public final static String RIGHT = "R";
		public final static String NORTH = "N";
		public final static String EAST = "E";
		public final static String WEST = "W";
		public final static String SOUTH = "S";
		
		final static String FORWARD = "F";
		
		public String direction;
		public int longitude = 0;
		public int latitude = 0;
		
		public Entity(String initialDir) {
			direction = initialDir;
		}
		
		public Entity(int lon, int lat) {
			this.longitude = lon;
			this.latitude = lat;
		}

		protected void displace(String dir, int value) {
			switch (dir) {
			case EAST:
				longitude += value; break;
			case WEST:
				longitude -= value; break;
			case NORTH:
				latitude -= value; break;
			case SOUTH:
				latitude += value; break;
			}
		}
		
		
	}
	
	private static class WayPoint extends Entity {

		public WayPoint(int lon, int lat) {
			super(lon, lat);
		}	
		
		protected void turnRight(int value) {
			int numTurns = value / 90;
			
			while (numTurns > 0) {
				turnRightOneStep();
				numTurns--;
			}		
		}
		
		private void turnLeftOneStep() {
			final int initialLon = longitude;
			final int initialLat = latitude;
			
			longitude = initialLat;
			latitude = initialLon * -1;
		}
		
		private void turnRightOneStep() {
			final int initialLon = longitude;
			final int initialLat = latitude;
			
			latitude = initialLon;
			longitude = initialLat * -1;
		}

		protected void turnLeft(int value) {
			int numTurns = value / 90;
	
			while (numTurns > 0) {
				turnLeftOneStep();
				numTurns--;
			}
		}
	}
	
	private static class Ferry extends Entity {
		
		private WayPoint wayPoint;
		
		
		public Ferry(String initialDir) {
			super(initialDir);
			wayPoint = new WayPoint(10, -1);
		}
		
		public int getManhattanDistanceFromOrigin() {
			return Math.abs(this.longitude) + Math.abs(this.latitude);
		}
		
		protected void moveForward(int value) {
			longitude += value * wayPoint.longitude; 
			latitude += value * wayPoint.latitude;
		}
		
		protected void displace(String dir, int value) {
			wayPoint.displace(dir, value);
		}

		/* Part 1 
	    F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.
	    N3 would move the ship 3 units north to east 10, north 3.
	    F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.
	    R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.
	    F11 would move the ship 11 units south to east 17, south 8.
		 */
		public void doInstruction(String instruction) {
			 String op = instruction.substring(0,1);
			 int value = Integer.parseInt(instruction.substring(1));
			 		
			 switch (op) {
			 case FORWARD:
				 moveForward(value);
				 break;
			 case NORTH:
			 case EAST:
			 case WEST:
			 case SOUTH:
				 displace(op, value);
				 break;
			 case RIGHT:
				 wayPoint.turnRight(value);
				 break;
			 case LEFT:
				 wayPoint.turnLeft(value);
				 break;
			 }	 
		}
		
		protected void turnRight(int value) {
			int numTurns = value / 90;
	
			String[] turns = new String[]{EAST, SOUTH, WEST, NORTH};
			int index = List.of(turns).indexOf(this.direction);
			
			index = (index + numTurns) % turns.length;
			
			this.direction = turns[index];
		}

		protected void turnLeft(int value) {
			int numTurns = value / 90;
	
			String[] turns = new String[]{EAST, NORTH, WEST, SOUTH};
			int index = List.of(turns).indexOf(this.direction);
			
			index = (index + numTurns) % turns.length;
			
			this.direction = turns[index];
		}
		

		public void display() {
			String eastWest = longitude >= 0 ? EAST : WEST;
			String northSouth = latitude >= 0 ? SOUTH : NORTH;
			
			System.out.println(eastWest + ": " 
			+ Math.abs(longitude) + "\t" 
					+ northSouth + ": " 
			+ Math.abs(latitude) + "\tdirection: " + direction
			+ "\tWaypoint -> lat: " + wayPoint.latitude + "\tlon: " + wayPoint.longitude);
		}
	}

	public static void main(String[] args) {

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("navigation.txt"));
				
			List<String> instructions = br.lines().collect(Collectors.toList());
			
			Ferry ferry = new Ferry(Ferry.EAST);
			
			instructions.forEach(s -> {
				ferry.doInstruction(s);
				ferry.display();
			});
			
			System.out.println("Distance from origin: " + ferry.getManhattanDistanceFromOrigin());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		

	}

}
