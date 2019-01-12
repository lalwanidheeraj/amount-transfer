/**
 * 
 */
package com.revoult.transfer;

import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.factory.ConnectionManagerFactory;

/**
 * @author Dheeraj Lalwani
 * This is the starter class for the application.
 */
public class AmountTransferApp {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.setProperty("java.util.logging.config.file","logging.properties");
		System.setProperty("java.util.logging.manager","org.apache.juli.ClassLoaderLogManager");
		AmountTransferUtil.init("application.properties");
		ConnectionManagerFactory.populateJpa();
		MasterDataCache.cacheMasterData();
		TomcatServer tomcat = new TomcatServer();
		tomcat.start();
	}
}
