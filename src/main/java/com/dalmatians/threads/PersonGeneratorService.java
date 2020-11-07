package com.dalmatians.threads;

import java.util.Arrays;

import com.dalmatians.controller.UserManagement;
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
	private UserManagement controller;

	public PersonGeneratorService(UserManagement controller, Database db) {
		randomPersonGen = new RandomPersonGenerator(db);
		this.controller = controller;
		setOnSucceeded((s) -> {
			System.out.println("Finished!!!");
			System.out.println(Arrays.toString(db.getPeople()));
		});

		setOnRunning((s) -> {
			System.out.println("Running");
		});
	}

	public void generate(int start, int end) {
		if (!isRunning()) {
			n = end - start;
			id = start;
			randomPersonGen.reset(start, end);
			this.reset();
			this.start();
		}
	}

	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {

			@Override
			protected Void call() {
				updateProgress(0, n);
				if (!isCancelled()) {
					updateMessage("Running...");
					Person p;
					int workDone = 0;
					while ((p = randomPersonGen.getNextPerson()) != null) {
						if (isCancelled()) {
							System.out.println("Canceled");
							updateProgress(0, n);
						}
						System.out.println(p);
						db.getPeople()[id++] = p;
						updateProgress(workDone++, n);
					}
				}
				return null;
			}

		};
	}
}