package com.qa.imsproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Runner {

	public static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		IMSMain ims = new IMSMain();
		ims.imsSystem();
		LOGGER.info("Thank you for taking your time.");
	}

}
