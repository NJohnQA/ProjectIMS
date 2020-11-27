package com.qa.imsproject.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qa.imsproject.utils.Utils;

public enum Action {
	CREATE("To save a new entity into the database"), READ("To read an entity from the database"),
	UPDATE("To change an entity already in the database"), DELETE("To remove an entity from the database"),
	RETURN("To return to domain selection");

	public static final Logger LOGGER = LogManager.getLogger();

	private String description;

	Action(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.name() + ": " + this.description;
	}

	public static void printActions() {
		for (Action action : Action.values()) {
			LOGGER.info(action.getDescription());
		}
	}


	public static Action getAction(Utils utils) {
		Action action = null;
		do {
			try {
				action = Action.valueOf(utils.getString().toUpperCase());
			} catch (IllegalArgumentException e) {
				LOGGER.error("Sorry please try again, invalid selection");
			}
		} while (action == null);
		return action;
	}

}
