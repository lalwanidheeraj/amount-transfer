/**
 * 
 */
package com.revoult.transfer.factory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.h2.engine.Database;
import org.h2.tools.RunScript;

import com.revoult.transfer.common.AmountTransferUtil;

/**
 * @author Dheeraj Lalwani This class configures the in-memory h2 database and
 *         loads the data in it.
 */
public class ConnectionManagerFactory implements AbstractFactory {

	private static Logger _logger = Logger.getLogger(ConnectionManagerFactory.class.getName());

	public static final String driver = AmountTransferUtil.config.get("driver");
	public static final String connectionUrl = AmountTransferUtil.config.get("connectionUrl");
	public static final String user = AmountTransferUtil.config.get("user");
	public static final String password = AmountTransferUtil.config.get("password");

	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			_logger.severe("An error occured while loading the H2 database driver. " + e);
		}
	}

	public static void populateJpa() throws IOException {
		try (Connection connection = DriverManager.getConnection(connectionUrl, user, password);
				Statement statement = connection.createStatement()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try (InputStream inputStream = loader.getResourceAsStream("Script.sql");
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
				String line;
				StringBuffer sb = new StringBuffer();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				String[] sql = sb.toString().split(";");
				for (int i = 0; i < sql.length; i++) {
					if (!sql[i].trim().equals("")) {
						statement.executeUpdate(sql[i]);
					}
				}
			}
		} catch (SQLException e) {
			_logger.severe("An error occured while inserting data in database. " + e);
		} catch (FileNotFoundException e) {
			_logger.severe("An error occured while loading the Script.sql file. " + e);
		}
	}
}
