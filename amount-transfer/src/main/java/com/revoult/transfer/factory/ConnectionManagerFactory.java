/**
 * 
 */
package com.revoult.transfer.factory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.h2.tools.RunScript;

import com.revoult.transfer.common.AmountTransferUtil;

/**
 * @author Dheeraj Lalwani
 * This class configures the in-memory h2 database and loads the data in it.
 */
public class ConnectionManagerFactory implements AbstractFactory{
	
	public static final String driver = AmountTransferUtil.config.get("driver");
	public static final String connectionUrl = AmountTransferUtil.config.get("connectionUrl");
	public static final String user = AmountTransferUtil.config.get("user");
	public static final String password = AmountTransferUtil.config.get("password");
	
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void populateJpa() {
		try (Connection connection = DriverManager.getConnection(connectionUrl, user, password)){
			RunScript.execute(connection, new FileReader("src/main/resources/Script.sql"));
		} catch (SQLException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
