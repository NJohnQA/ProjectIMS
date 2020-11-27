package com.qa.imsproject;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import com.qa.imsproject.controller.Action;
import com.qa.imsproject.controller.CrudController;
import com.qa.imsproject.controller.CustomerController;
import com.qa.imsproject.controller.ItemController;
import com.qa.imsproject.controller.OrderController;
import com.qa.imsproject.persistence.dao.CustomerDAO;
import com.qa.imsproject.persistence.dao.ItemDAO;
import com.qa.imsproject.persistence.dao.OrderDAO;
import com.qa.imsproject.persistence.domain.Domain;
import com.qa.imsproject.utils.JDBCUtils;
import com.qa.imsproject.utils.Utils;

public class IMSMain {

	public static final Logger LOGGER = LogManager.getLogger();

	private final CustomerController customers;
	private final Utils utils;
	private final ItemController items;
	private final OrderController orders;

	public IMSMain() {
		this.utils = new Utils();
		final CustomerDAO custDAO = new CustomerDAO();
		this.customers = new CustomerController(custDAO, utils);
		final ItemDAO itemDAO = new ItemDAO();
		final OrderDAO orderDAO = new OrderDAO();
		this.items = new ItemController(itemDAO, utils);
		this.orders = new OrderController(custDAO, itemDAO, orderDAO, utils);
		JDBCUtils.getInstance();
	}

	public void imsSystem() {
		LOGGER.info("Welcome to the Inventory Management System");
		LOGGER.info("Enter your username");
		String username = utils.getString();
		LOGGER.info("Enter your password");
		String password = utils.getString();

		JDBCUtils.connect(username, password);
		
		Domain domain = null;
		do {
			LOGGER.info("Which entity would you like to use?");
			Domain.printDomains();

			domain = Domain.getDomain(utils);

			domainAction(domain);

		} while (domain != Domain.STOP);
	}

	private void domainAction(Domain domain) {
		boolean changeDomain = false;
		do {

			CrudController<?> active = null;
			switch (domain) {
			case CUSTOMER:
				active = this.customers;
				break;
			case ITEM:
				active = this.items;
				break;
			case ORDER:
				active = this.orders;
				break;
			case STOP:
				return;
			default:
				break;
			}

			LOGGER.info("What would you like to do with " + domain.name().toLowerCase() + ":");

			Action.printActions();
			Action action = Action.getAction(utils);

			if (action == Action.RETURN) {
				changeDomain = true;
			} else {
				doAction(active, action);
			}
		} while (!changeDomain);
	}

	public void doAction(CrudController<?> crudController, Action action) {
		switch (action) {
		case CREATE:
			crudController.create();
			break;
		case READ:
			crudController.readAll();
			break;
		case UPDATE:
			crudController.update();
			break;
		case DELETE:
			crudController.delete();
			break;
		case RETURN:
			break;
		default:
			break;
		}
	}

}
