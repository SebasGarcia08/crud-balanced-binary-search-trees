package com.dalmatians.model;

import com.dalmatians.datastructures.AVLBSTree;
import com.dalmatians.datastructures.BalancedBSTree;

public class Database {
	
	public enum CRITERION {
		NAME,
		FULLNAME,
		ID,
		SURNAME
	}
	
	public enum EDITABLE_ATTRIBUTE {
		NAME,
		FULLNAME,
		SURNAME,
		SEX,
		BIRTHDATE,
		HEIGHT
	}
	
	private BalancedBSTree<String, Person> nameTree;
	private BalancedBSTree<String, Person> idTree;
	private BalancedBSTree<String, Person> fullnameTree;
	private BalancedBSTree<String, Person> surnameTree;
	
	public Database() {
		idTree = new AVLBSTree<>();
		nameTree = new AVLBSTree<>();
		fullnameTree = new AVLBSTree<>();
		surnameTree = new AVLBSTree<>();
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
