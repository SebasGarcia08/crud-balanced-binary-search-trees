package com.dalmatians.model;

public class Database {
	
	public enum CRITERION {
		NAME,
		FULLNAME,
		ID,
		SURNAME
	}
	
	enum EDITABLE_ATTRIBUTE {
		NAME,
		FULLNAME,
		SURNAME,
		SEX,
		BIRTHDATE,
		HEIGHT
	}
	
	public void generate(int n) {
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
	
	public double getRandomHeight(double mean, double stdv) {
		return 0.0;
	}
	
}
