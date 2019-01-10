/**
 * 
 */
package com.revoult.transfer.factory;

/**
 * @author Dheeraj Lalwani
 * This is the factory class, which returns instance of Service or Dao factory.
 */
public class AppFactoryProvider {

	/**
	 * This method returns the instance of service or dao factory.
	 * @param factoryType
	 * @return
	 */
	public static AbstractFactory create(String factoryType) {
		switch(factoryType) {
			case "Service" :
				return new ServiceFactory();
			case "Dao" :
				return new DaoFactory();
			default :
				return new ServiceFactory();
		}
	}
}
