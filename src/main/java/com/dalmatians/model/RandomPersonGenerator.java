package com.dalmatians.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

import com.dalmatians.model.Person.SEX;

public class RandomPersonGenerator {

	/**
	 * This matrix represents the distribution of ages in United States 
	 * and will be used in order to generate random ages that 
	 * follow this distribution.
	 */
	private final double[][] AGES_DISTRIBUTIONS = { 
			{ 0.1862, 0, 14 }, 
			{ 0.1312, 15, 24 }, 
			{ 0.3929, 25, 54 },
			{ 0.1294, 55, 64 }, 
			{ 0.1603, 65, 100 } 
	};

	private RandomGaussian femaleHeightsGenerator;

	private RandomGaussian maleHeightsGenerator;

	private Random uniformGenerator;

	private String[] surnames;

	private String[] nationalities;

	private double[] nationalitiesProportions;

	private double[][] cummulativeAgesDist;

	private HashMap<String, String[]> gender2Name;

	private int ithPersonGenerated;

	private int start;
	
	private int end;
	
	public RandomPersonGenerator(String[] surnames, String[] natioanlities, double[] natilalitiesProportions,
			HashMap<String, String[]> gender2Name) {
		this.surnames = surnames;
		this.nationalities = natioanlities;
		this.nationalitiesProportions = natilalitiesProportions;
		this.gender2Name = gender2Name;

		femaleHeightsGenerator = new RandomGaussian(1.65, 0.05);
		maleHeightsGenerator = new RandomGaussian(1.72, 0.05);
		uniformGenerator = new Random();

		// Calculate the cumulative sum of percentages over the proportions of
		// population with ages
		cummulativeAgesDist = new double[AGES_DISTRIBUTIONS.length][AGES_DISTRIBUTIONS[0].length];
		cummulativeAgesDist[0] = AGES_DISTRIBUTIONS[0];
		for (int i = 1; i < AGES_DISTRIBUTIONS.length; i++) {
			cummulativeAgesDist[i] = AGES_DISTRIBUTIONS[i];
			cummulativeAgesDist[i][0] = cummulativeAgesDist[i - 1][0] + cummulativeAgesDist[i][0];
		}
	}

	public void reset(int start, int end) {
		this.start = start;
		this.end = end;
		this.ithPersonGenerated = start;
	}
	
	public Person getNextPerson() {
		if(ithPersonGenerated < end) {
			Person p = createRandomPerson(ithPersonGenerated);
			ithPersonGenerated++;
			return p;
		}
		else return null;
	}

	private Person createRandomPerson(int id) {
		double pGender = Math.random();
		SEX sex = (pGender >= 0.5) ? SEX.M : SEX.F;
		LocalDate birthdate = generateBirthDate();
		double height = (pGender >= 0.5) ? maleHeightsGenerator.generate() : femaleHeightsGenerator.generate();
		String nationality = generateNationality();
		String name = getRandomName(sex);
		String lastName = getRandomLastName();
		return new Person(id, name, lastName, sex, height, birthdate, nationality);
	}

	private String generateNationality() {
		double randomVal = Math.random();
		String nationality = "";

		if (randomVal <= cummulativeAgesDist[0][0]) {
			nationality = nationalities[0];
		} else {
			boolean done = false;
			for (int i = 0; i < nationalities.length - 1 && !done; i++) {
				if (randomVal > nationalitiesProportions[i] && randomVal <= nationalitiesProportions[i + 1]) {
					nationality = nationalities[i + 1];
					done = true;
				}
			}
		}
		return nationality;
	}

	private String getRandomName(SEX sex) {
		int idx = (sex == SEX.M) ? generateRandomIntInRange(0, gender2Name.get("boy").length - 1)
				: generateRandomIntInRange(0, gender2Name.get("girl").length - 1);

		return ((sex == SEX.M) ? gender2Name.get("boy")[idx] : gender2Name.get("girl")[idx]).toUpperCase();
	}

	private String getRandomLastName() {
		return surnames[generateRandomIntInRange(0, surnames.length - 1)] + " "
				+ surnames[generateRandomIntInRange(0, surnames.length - 1)];
	}

	private LocalDate generateBirthDate() {
		return generateBirthdate(generateNextAge());
	}

	private int generateNextAge() {
		double randomNum2DetermineAgeRange = uniformGenerator.nextDouble();
		int age;
		double max = 0;
		double min = 0;

		if (randomNum2DetermineAgeRange <= cummulativeAgesDist[0][0]) {
			min = cummulativeAgesDist[0][1];
			max = cummulativeAgesDist[0][2];
		} else {
			boolean minMaxFixed = false;
			for (int i = 0; i < cummulativeAgesDist.length - 1 && !minMaxFixed; i++) {
				if (randomNum2DetermineAgeRange > cummulativeAgesDist[i][0]
						&& randomNum2DetermineAgeRange <= cummulativeAgesDist[i + 1][0]) {
					min = cummulativeAgesDist[i + 1][1];
					max = cummulativeAgesDist[i + 1][2];
					minMaxFixed = true;
				}
			}
		}

		age = generateRandomIntInRange(min, max);
		return age;
	}

	private LocalDate generateBirthdate(int age) {
		return LocalDate.now().minusYears(age).minusDays(generateRandomIntInRange(0.0, 360.0)); // Adds noise to the
																								// birthdate
	}

	private static int generateRandomIntInRange(double min, double max) {
		return (int) ((Math.random() * (max - min)) + min);
	}

}
