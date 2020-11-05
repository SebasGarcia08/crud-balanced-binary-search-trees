package com.dalmatians.model;

import java.util.Random;

public class RandomGaussian {
	
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
