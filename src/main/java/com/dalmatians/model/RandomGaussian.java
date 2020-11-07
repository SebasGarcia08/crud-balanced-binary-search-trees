package com.dalmatians.model;

import java.io.Serializable;
import java.util.Random;

public class RandomGaussian implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3974825372658953730L;
	private double stddev;
	private double mean;
	private Random random;
	
	public RandomGaussian(double mean, double stddev) {
		this.mean = mean;
		this.stddev = stddev;
		random = new Random();
	}
	
	public double generate() {
		return mean + random.nextGaussian() * stddev;
	}
	
}
