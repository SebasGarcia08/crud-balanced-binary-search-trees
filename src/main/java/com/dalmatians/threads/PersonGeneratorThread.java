package com.dalmatians.threads;

import com.dalmatians.controller.UserManagement;
import com.dalmatians.model.Database;
import com.dalmatians.model.Person;
import com.dalmatians.model.RandomPersonGenerator;

import javafx.application.Platform;

public class PersonGeneratorThread extends Thread {

	private Database db;
	private int n;
	private int start;
	private int end;
	private int id;
	private RandomPersonGenerator randomPersonGen;
	private UserManagement controller;

	public PersonGeneratorThread(UserManagement controller, Database db, int start, int end) {
		this.controller = controller;
		this.randomPersonGen = new RandomPersonGenerator(db);
		n = end - start;
		id = start;
	}

	@Override
	public void run() {
		randomPersonGen.reset(start, end);
		Person p;
		int workDone = 0;
		while ((p = randomPersonGen.getNextPerson()) != null) {
			db.getPeople()[id++] = p;
			final int progress = workDone++;

			Platform.runLater(() -> {
				controller.updateProgress(progress, n);
			});

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
