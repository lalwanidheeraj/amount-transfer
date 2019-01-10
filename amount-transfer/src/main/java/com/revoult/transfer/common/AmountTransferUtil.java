package com.revoult.transfer.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 */

/**
 * @author Dheeraj Lalwani
 * This is the utility class which loads the datasource properties from the property file.
 */
public class AmountTransferUtil {
	
	public static Map<String,String> config = new HashMap<String, String>();
	
	public static void init() {
		try {
			Properties properties = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties.load(loader.getResourceAsStream("application.properties"));
			for (String key : properties.stringPropertyNames()) {
			    String value = properties.getProperty(key);
			    config.put(key, value);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
