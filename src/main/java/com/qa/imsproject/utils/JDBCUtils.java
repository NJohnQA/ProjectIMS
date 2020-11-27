package com.qa.imsproject.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JDBCUtils {

	private static final Logger LOGGER = LogManager.getLogger();

	private final String DB_USER;

	private final String DB_PASS;

	private final String DB_URL = "jdbc:mysql://35.246.127.147/imssql?db_name&serverTimezone=UTC";

	private JDBCUtils(String username, String password) {
		this.DB_USER = username;
		this.DB_PASS = password;

		init();
	}

	public int init() {
		return this.init("src/main/database/sql-schema.sql", "src/main/database/imssql.sql");
	}

	public int init(String... paths) {
		int modified = 0;

		for (String path : paths) {
			modified += executeSQLFile(path);
		}

		return modified;
	}

	public int executeSQLFiles(String... paths) {
		int modified = 0;

		for (String path : paths) {
			modified += executeSQLFile(path);
		}

		return modified;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
	}

	public static JDBCUtils instance;

	public static JDBCUtils connect(String username, String password) {
		instance = new JDBCUtils(username, password);
		return instance;
	}

	public static JDBCUtils getInstance() {
		if (instance == null) {
			instance = new JDBCUtils("", "");
		}
		return instance;
	}

	public int executeSQLFile(String filePath) {
		int modified = 0;
		try (Connection connection = this.getConnection();
				BufferedReader br = new BufferedReader(new FileReader(filePath));) {
			String fileAsString = br.lines().reduce((acc, next) -> acc + next).orElse("");
			String[] queries = fileAsString.split(";");
			modified += Stream.of(queries).map(string -> {
				try (Statement statement = connection.createStatement();) {
					return statement.executeUpdate(string);
				} catch (Exception e) {
					LOGGER.debug(e);
					return 0;
				}
			}).reduce((acc, next) -> acc + next).orElse(0);
		} catch (Exception e) {
			LOGGER.debug(e);
		}
		return modified;
	}
}
