package com.dalmatians.model;

import java.util.List;

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
	
	private BalancedBSTree<String, List<Person>> nameTree;
	private BalancedBSTree<String, List<Person>> idTree;
	private BalancedBSTree<String, List<Person>> fullnameTree;
	private BalancedBSTree<String, List<Person>> surnameTree;
	private RandomGaussian femaleHeightsGenerator;
	private RandomGaussian maleHeightsGenerator;
	
	public Database() {
		idTree = new AVLBSTree<>();
		nameTree = new AVLBSTree<>();
		fullnameTree = new AVLBSTree<>();
		surnameTree = new AVLBSTree<>();
		femaleHeightsGenerator = new RandomGaussian(1.65, 0.05);
		maleHeightsGenerator = new RandomGaussian(1.70, 0.05);
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
}
