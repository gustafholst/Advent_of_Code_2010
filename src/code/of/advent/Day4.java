package code.of.advent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day4 {
	
	
	private static class Passport {
		
		public String byr;				//		    byr (Birth Year)
		public String iyr;				//		    iyr (Issue Year)
		public String eyr;				//		    eyr (Expiration Year)
		public String hgt;				//		    hgt (Height)
		public String hcl;				//		    hcl (Hair Color)
		public String ecl;				//		    ecl (Eye Color)
		public String pid;				//		    pid (Passport ID)
		public String cid;				//		    cid (Country ID)
		
		public static Passport parseFields(List<String> tokens) {
			
			Passport p = new Passport();
		
			tokens.forEach(t -> {
				String[] keyval = t.split(":");
				switch (keyval[0]) {
				case "byr": p.byr = keyval[1]; break;
				case "iyr": p.iyr = keyval[1]; break;
				case "eyr": p.eyr = keyval[1]; break;
				case "hgt": p.hgt = keyval[1]; break;
				case "hcl": p.hcl = keyval[1]; break;
				case "ecl": p.ecl = keyval[1]; break;
				case "pid": p.pid = keyval[1]; break;
				case "cid": p.cid = keyval[1]; break;
				}
			});
			
			return p;
		}
		
		public boolean validHeight() {
			
			boolean cm = this.hgt.endsWith("cm");
			boolean in = this.hgt.endsWith("in");
			
			int val = 0;
			if (cm) {
				val = Integer.valueOf(this.hgt.substring(0, this.hgt.indexOf('c')));
				
				return val >= 150 && val <= 193;
			}
			else if (in) {
				val = Integer.valueOf(this.hgt.substring(0, this.hgt.indexOf('i')));
				
				return val >= 59 && val <= 76;
			}

			return false;	
		}
		
		public boolean validHairColor() {
			if (this.hcl.charAt(0) != '#')
				return false;
			
			final String validChars = "0123456789abcdef";
			
			return this.hcl.chars().skip(1).filter(c -> validChars.indexOf(c) > -1).count() == 6;
		}
		
		public boolean validEyeColor() {
			List<String> valid = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
			
			return valid.contains(this.ecl);
		}
		
		public boolean validPid() {
			return this.pid.chars().filter(c -> Character.isDigit(c)).count() == 9;
		}
		
		public boolean isValid() {

//		    byr (Birth Year) - four digits; at least 1920 and at most 2002.
//		    iyr (Issue Year) - four digits; at least 2010 and at most 2020.
//		    eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
//		    hgt (Height) - a number followed by either cm or in:
//		        If cm, the number must be at least 150 and at most 193.
//		        If in, the number must be at least 59 and at most 76.
//		    hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
//		    ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
//		    pid (Passport ID) - a nine-digit number, including leading zeroes.
//		    cid (Country ID) - ignored, missing or not.

			return byr != null && Integer.valueOf(byr) >= 1920 && Integer.valueOf(byr) <= 2002 &&
					iyr != null && Integer.valueOf(iyr) >= 2010 && Integer.valueOf(iyr) <= 2020 &&
					eyr != null && Integer.valueOf(eyr) >= 2020 && Integer.valueOf(eyr) <= 2030 &&
					hgt != null && validHeight() &&
					hcl != null && validHairColor() &&
					ecl != null && validEyeColor() &&
					pid != null && validPid();
		}
	}

	public static void main(String[] args) {
		
		List<Passport> passports = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("passports.txt"));
			
			List<String> fields = new ArrayList<>();
			
			String line = br.readLine();
			while (line != null) {
				
				if (line.equals("")) {
					Passport p = Passport.parseFields(fields);			
					passports.add(p);
					
					fields.clear();
				}
				
				for (String field : line.split(" ")) {
					fields.add(field);
				}
				
				
				line = br.readLine();
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		long validPassports = passports.stream().filter(p -> p.isValid()).count();

		System.out.println("Number of valid passports: " + validPassports);
	}

}
