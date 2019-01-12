package com.revoult.transfer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import com.revoult.transfer.rest.CountryBranchController;
import com.revoult.transfer.rest.TransactionController;
import com.revoult.transfer.rest.UserAccountController;

/**
 * @author Dheeraj Lalwani
 * This class configures and starts the tomcat instance.
 */
public class TomcatServer {

	/**
	 * This is the method, which configures and start tomcat instance.
	 * @throws Exception 
	 */
	public void start() throws Exception {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		Context context = tomcat.addContext("", new File("./").getAbsolutePath());
		
		Tomcat.addServlet(context, "jersey-container-servlet", config());
		context.addServletMapping("/*", "jersey-container-servlet");
		tomcat.start();
		tomcat.getServer().await();
	}

	/**
	 * This is the method, which configures and returns the servlet container.
	 * @return
	 */
	private ServletContainer config() {
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		properties.put(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
		ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.register(UserAccountController.class).register(TransactionController.class)
			.register(CountryBranchController.class);
		SwaggerConfiguration.registerSwaggerJsonResource(resourceConfig);
		return new ServletContainer(resourceConfig.addProperties(properties));
	}
}
