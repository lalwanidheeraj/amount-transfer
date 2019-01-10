/**
 * 
 */
package com.revoult.transfer;

import org.apache.catalina.LifecycleException;

import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.factory.ConnectionManagerFactory;

/**
 * @author Dheeraj Lalwani
 * This is the starter class for the application.
 */
public class AmountTransferApp {

	/**
	 * @param args
	 * @throws LifecycleException 
	 */
	public static void main(String[] args) throws LifecycleException {
		AmountTransferUtil.init();
		ConnectionManagerFactory.populateJpa();
		TomcatServer tomcat = new TomcatServer();
		tomcat.start();
	}
}
