package com.dalmatians.model;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.dalmatians.datastructures.AVLBSTree;
import com.dalmatians.datastructures.BalancedBSTree;

/**
 * number of possible combinations of 2 surnames: factorial(1000)/(factorial(998) * factorial(2)) =  499500
 * number of names = 6781
 * number of possible names + 2 surnames = 6781 * 499500 = 3387109500.0
 * @author sebastian
 *
 */
public class Database {

	public enum CRITERION {
		NAME, FULLNAME, ID, SURNAME
	}

	public enum EDITABLE_ATTRIBUTE {
		NAME, FULLNAME, SURNAME, SEX, BIRTHDATE, HEIGHT
	}

	/**
	 * This matrix represents the distribution of ages in United States 
	 * and will be used in order to generate random ages that 
	 * follow this distribution.
	 */
	public final double[][] AGES_DISTRIBUTIONS = { 
			{ 0.1862, 0, 14 }, 
			{ 0.1312, 15, 24 }, 
			{ 0.3929, 25, 54 }, 
			{ 0.1294, 55, 64 },
			{ 0.1603, 65, 100 } 
	};
	
	public double[][] cummulativeAgesDist;

	private BalancedBSTree<String, Person> nameTree;
	private BalancedBSTree<String, Person> idTree;
	private BalancedBSTree<String, Person> fullnameTree;
	private BalancedBSTree<String, Person> surnameTree;
	private RandomGaussian femaleHeightsGenerator;
	private RandomGaussian maleHeightsGenerator;
	private Random uniformGenerator;
	private HashMap<String, String[]> gender2Name;
	private String[] surnames;
	private List<Person> database;
	
	public Database() {
		idTree = new AVLBSTree<>();
		nameTree = new AVLBSTree<>();
		fullnameTree = new AVLBSTree<>();
		surnameTree = new AVLBSTree<>();
		femaleHeightsGenerator = new RandomGaussian(1.65, 0.05);
		maleHeightsGenerator = new RandomGaussian(1.70, 0.05);
		uniformGenerator = new Random(); 
		
		// Calculate the cumulative sum of percentages over the proportions of population with ages
		cummulativeAgesDist = new double[AGES_DISTRIBUTIONS.length][AGES_DISTRIBUTIONS[0].length];
		cummulativeAgesDist[0] = AGES_DISTRIBUTIONS[0];
		for(int i = 1; i < AGES_DISTRIBUTIONS.length; i++) {
			cummulativeAgesDist[i] = AGES_DISTRIBUTIONS[i];
			cummulativeAgesDist[i][0] = cummulativeAgesDist[i-1][0] + cummulativeAgesDist[i][0]; 
		}
		gender2Name = new HashMap<>();
		gender2Name.put("boy", new String[3437]); // 3437 is the number of boy names		
		gender2Name.put("girl", new String[3345]); // 3437 is the number of girl names
		surnames = new String[1000];
		try {
			readNames();
			readSurnames();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Database db = new Database();
	}
	
	public void generate(int n) throws IOException, URISyntaxException {
		if(!areNamesLoaded()) readNames();
		if(!areSurnamesLoaded()) readSurnames();
		database = new ArrayList<>(n);
		for(int i = 0; i<n ; i++) {
			new Person(id, name, lastName, sex, height, birthdate, nationality)
		}
	}
	
	public void readNames() throws IOException, URISyntaxException {
//		Create the CSVFormat object
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
//		String filePath = "src/main/resources/data/names.csv";
		String filePath  = getClass().getResource("/data/names.csv").toURI().getPath();
		CSVParser parser = new CSVParser(new FileReader(filePath), format);
		int boysi = 0;
		int girlsi = 0;
		for(CSVRecord record : parser) {
			if(record.get("gender").equals("boy")) 
				gender2Name.get("boy")[boysi++] = record.get("name"); 
			else 
				gender2Name.get("girl")[girlsi++] = record.get("name"); 				
		}
	}

	public void readSurnames() throws IOException, URISyntaxException {
//		Create the CSVFormat object
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
//		String filePath = "src/main/resources/data/surnames.csv";
		String filePath  = getClass().getResource("/data/surnames.csv").toURI().getPath();
		CSVParser parser = new CSVParser(new FileReader(filePath), format);
		int i = 0;
		for(CSVRecord record : parser) {
			surnames[i++] = record.get("surname");
		}
	}
	
	public boolean areNamesLoaded() {
		return gender2Name.get("boy")[0] != null;
	}

	public boolean areSurnamesLoaded() {
		return surnames[0] != null;
	}
	
	public static void load(String path) {

	}

	public void save(String path) {

	}

	public Person createRandomPerson() {
		return null;
	}

	public Person search(CRITERION criteria, String value) {
		return null;
	}

	public void delete(Person p) {

	}

	public <T> void update(EDITABLE_ATTRIBUTE attr, T newValue) {

	}
	
	public LocalDate generateBirthDate() {
		return generateBirthdate(generateNextAge());
	}
	
	private int generateNextAge() {
		double randomNum2DetermineAgeRange = uniformGenerator.nextDouble();
		int age;
		double max=0;
		double min=0;
		
		if(randomNum2DetermineAgeRange <= cummulativeAgesDist[0][0]) {
			min = cummulativeAgesDist[0][1];
			max = cummulativeAgesDist[0][2];
		} else {
			boolean minMaxFixed = false;
			for(int i = 0; i < cummulativeAgesDist.length - 1 && !minMaxFixed; i++) {
				if(randomNum2DetermineAgeRange > cummulativeAgesDist[i][0] && randomNum2DetermineAgeRange <= cummulativeAgesDist[i+1][0]) {
					min = cummulativeAgesDist[i+1][1];
					max = cummulativeAgesDist[i+1][2];
					minMaxFixed=true;
				}
			}
		}
		
		age = generateRandomIntInRange(min, max);
		return age;
	}
	
	private LocalDate generateBirthdate(int age) {
		return LocalDate.now()
				.minusYears(age)
				.minusDays(generateRandomIntInRange(0.0, 360.0)); // Adds noise to the birthdate
	}
	
	public static int generateRandomIntInRange(double min, double max) {
		return (int) ((Math.random()  * (max - min)) + min);
	}
}
