package com.revoult.transfer.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * @author Dheeraj Lalwani
 * This is the utility class which loads the datasource properties from the property file.
 */
public class AmountTransferUtil {
	
	private static Logger _logger = Logger.getLogger(AmountTransferUtil.class.getName());
	
	public static Map<String,String> config = new HashMap<String, String>();
	
	public static void init(String propFile) {
		try {
			Properties properties = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties.load(loader.getResourceAsStream(propFile));
			for (String key : properties.stringPropertyNames()) {
			    String value = properties.getProperty(key);
			    config.put(key, value);
			}
		} catch (IOException e) {
			_logger.severe("Could not load the datasource config from property file " + e);
		}
	}
}
