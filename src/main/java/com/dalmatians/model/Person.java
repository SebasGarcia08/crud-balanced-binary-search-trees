package com.dalmatians.model;

import java.time.LocalDate;

public class Person {

	public enum SEX {
		M, F
	}

	private int id;
	private String name;
	private String lastName;
	private SEX sex;
	private double height;
	private LocalDate birthdate;
	private String nationality;
	
	public Person(int id, String name, String lastName, SEX sex, double height, LocalDate birthdate,
			String nationality) {
		super();
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.sex = sex;
		this.height = height;
		this.birthdate = birthdate;
		this.nationality = nationality;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the lastName
	 */
	public String getSurname() {
		return lastName;
	}

	/**
	 * @return the sex
	 */
	public SEX getSex() {
		return sex;
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @return the birthdate
	 */
	public LocalDate getBirthdate() {
		return birthdate;
	}

	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setSurname(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(SEX sex) {
		this.sex = sex;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", lastName=" + lastName + ", sex=" + sex + ", height=" + height
				+ ", birthdate=" + birthdate + ", nationality=" + nationality + "]";
	}
	
}