package com.dalmatians.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.dalmatians.datastructures.AVLBSTree;
import com.dalmatians.datastructures.BalancedBSTree;
import com.dalmatians.model.Person.SEX;

/**
 * number of possible combinations of 2 surnames: factorial(1000)/(factorial(998) * factorial(2)) =  499500
 * number of names = 6781
 * number of possible names + 2 surnames = 6781 * 499500 = 3 387 109 500.0
 * @author sebastian
 *
 */
public class Database implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8335733353300452339L;

	public enum CRITERION {
		NAME, FULLNAME, ID, SURNAME
	}

	public enum EDITABLE_ATTRIBUTE {
		NAME, FULLNAME, SURNAME, SEX, BIRTHDATE, HEIGHT
	}
	
	public static final String SERIALIZATION_PATH = "src/main/resources/data/database.dat";
		
	private Person[] people;
	
	private BalancedBSTree<Integer, Person> idTree;

	private BalancedBSTree<String, Person> surnameTree;
	
	private BalancedBSTree<String, Person> fullnameTree;
		
	private HashMap<String, String[]> gender2Name;
	
	private String[] surnames;
	
	private String[] nationalities;
	
	private double[] nationalitiesProportions;
	
	public Database() {
		idTree = new AVLBSTree<>();
		fullnameTree = new AVLBSTree<>();
		surnameTree = new AVLBSTree<>();

		gender2Name = new HashMap<>();
		gender2Name.put("boy", new String[3437]); // 3437 is the number of boy names		
		gender2Name.put("girl", new String[3345]); // 3437 is the number of girl names
		surnames = new String[1000];
		nationalitiesProportions = new double[33]; // 33 countries
		nationalities = new String[33];
		people = new Person[0];
		try {
			readNames();
			readSurnames();
			readNationalities();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		Database db = new Database();
		System.out.println(db.fullnameTree);
	}
	
	public void preapteForGenerate(int n) throws IOException, URISyntaxException {
		if(!areNamesLoaded()) readNames();
		if(!areSurnamesLoaded()) readSurnames();
		if(!areNationalitiesLoaded()) readNationalities();
		
		this.people = new Person[n];
	}
	
	public void addIthPersonToTrees(int i) {
		Person person = people[i];
		idTree.add(person.getId(), person);
		surnameTree.add(person.getSurname(), person);
		fullnameTree.add(person.getFullName(), person);		
	}
	
	public void addPeopleToTrees() {
		
		for(Person person:people) {
			idTree.add(person.getId(), person);
			surnameTree.add(person.getSurname(), person);
			fullnameTree.add(person.getFullName(), person);
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
	
	public void readNationalities() throws IOException, URISyntaxException {
//		Create the CSVFormat object
		CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(',');
//		String filePath = "src/main/resources/data/surnames.csv";
		String filePath  = getClass().getResource("/data/nationalities.csv").toURI().getPath();
		CSVParser parser = new CSVParser(new FileReader(filePath), format);
		int i = 0;
		for(CSVRecord record : parser) {
			nationalities[i] = record.get("country");
			nationalitiesProportions[i] = Double.parseDouble(record.get("cumulative_proportion"));
			i++;
		}
	}
	
	public boolean areNamesLoaded() {
		return gender2Name.get("boy")[0] != null;
	}

	public boolean areSurnamesLoaded() {
		return surnames[0] != null;
	}
	
	public boolean areNationalitiesLoaded() {
		return nationalities[0] != null;
	}
	
	public static Database load() throws IOException, ClassNotFoundException {
		File file = new File(SERIALIZATION_PATH);
		if (file.exists()) {
			FileInputStream fileInput = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileInput);
			Database db = (Database) in.readObject();
			return db;
		}
		return null;
	}

	public void save() throws IOException {
		FileOutputStream file = new FileOutputStream(SERIALIZATION_PATH);
		ObjectOutputStream out = new ObjectOutputStream(file);
		out.writeObject(this);
		out.close();
	}
	
	public static Database getDatabase() throws ClassNotFoundException, IOException {
		Database db = load();
		if (db != null) {
			return db;
		}
		return new Database();
	}
	
	public Person search(CRITERION criteria, String value) {
		return null;
	}

	public void delete(Person p) {
		idTree.delete(p.getId());
		fullnameTree.delete(p.getName() + " " + p.getSurname());
		surnameTree.delete(p.getSurname());
	}


	
	public <T> void update(Person p, EDITABLE_ATTRIBUTE attr, T newValue) {
		switch (attr) {
		case NAME:
			p.setName((String) newValue );
			break;
		case FULLNAME:
			String[] fullName = ((String) newValue).split(" ");
			p.setName(fullName[0]);
			p.setSurname(fullName[1] + " " + fullName[2]);
			break;
		case SURNAME:
			p.setSurname((String) newValue);
			break;
		case SEX:
			p.setSex((SEX) newValue);
			break;
		case BIRTHDATE:
			p.setBirthdate((LocalDate) newValue);
			break;
		case HEIGHT:
			p.setHeight((Double) newValue);
			break;
		default:
			break;
		}
	}

	/**
	 * @return the idTree
	 */
	public BalancedBSTree<Integer, Person> getIdTree() {
		return idTree;
	}

	/**
	 * @return the people
	 */
	public Person[] getPeople() {
		return people;
	}

	/**
	 * @return the fullnameTree
	 */
	public BalancedBSTree<String, Person> getFullnameTree() {
		return fullnameTree;
	}

	/**
	 * @param people the people to set
	 */
	public void clear() {
		this.people = new Person[0];
	}

	/**
	 * @return the gender2Name
	 */
	public HashMap<String, String[]> getGender2Name() {
		return gender2Name;
	}

	/**
	 * @return the surnames
	 */
	public String[] getSurnames() {
		return surnames;
	}

	/**
	 * @return the nationalities
	 */
	public String[] getNationalities() {
		return nationalities;
	}

	/**
	 * @return the nationalitiesProportions
	 */
	public double[] getNationalitiesProportions() {
		return nationalitiesProportions;
	}

	/**
	 * @return the surnameTree
	 */
	public BalancedBSTree<String, Person> getSurnameTree() {
		return surnameTree;
	}	
}
