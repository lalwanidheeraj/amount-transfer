package com.revoult.transfer;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.config.SwaggerConfig;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Info;
import io.swagger.models.Swagger;

/**
 * @author Dheeraj Lalwani This is the swagger configuration class for the
 *         rest-api documentation.
 */
public class SwaggerConfiguration {

	private static final String REST_PACKAGE = "com.revoult.transfer.rest";

	public static void registerSwaggerJsonResource(ResourceConfig rc) {
		new SwaggerContextService().withSwaggerConfig(new SwaggerConfig() {
			public Swagger configure(Swagger swagger) {
				Info info = new Info();
				info.setTitle("Revoult App Swagger Config");
				info.setVersion("1.0");
				swagger.setInfo(info);
				swagger.setBasePath("/");
				return swagger;
			}

			public String getFilterClass() {
				return null;
			}
		}).withSwaggerConfig(getBeanConfig()).initConfig();
		rc.packages(io.swagger.jaxrs.listing.ApiListingResource.class.getPackage().getName());
	}

	private static SwaggerConfig getBeanConfig() {
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setResourcePackage(REST_PACKAGE);
		beanConfig.setPrettyPrint(true);
		beanConfig.setDescription("Money Transfer App Swagger Config");
		beanConfig.setTitle("Money Transfer App Swagger Config");
		beanConfig.setScan(true);
		return beanConfig;
	}
}
