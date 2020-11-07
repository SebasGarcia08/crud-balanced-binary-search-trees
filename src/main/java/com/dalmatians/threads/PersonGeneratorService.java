package com.dalmatians.threads;

import com.dalmatians.model.Database;
import com.dalmatians.model.Person;
import com.dalmatians.model.RandomPersonGenerator;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PersonGeneratorService extends Service<Void> {

	private Database db;
	private int n;
	private int start;
	private int end;
	private int id;
	private RandomPersonGenerator randomPersonGen;
	
	public PersonGeneratorService(Database db) {
		randomPersonGen = new RandomPersonGenerator(start, end, db.getSurnames(), db.getNationalities(),
				db.getNationalitiesProportions(), db.getGender2Name());
	}

	public void generate(int start, int end) {
		this.n = end - start;
		this.id = start;
		reset();
		start();
	}
	
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() {
				this.updateProgress(0, n);
				if (!isCancelled()) {
					updateMessage("Running...");
					int workDone = 0;
					Person p;
					while ((p = randomPersonGen.getNextPerson()) != null) {
						db.getPeople()[id++] = p;
						updateProgress(workDone++, n);
					}
				}
				return null;
			}

		};
	}
}