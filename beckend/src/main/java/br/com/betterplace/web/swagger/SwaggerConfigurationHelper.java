package br.com.betterplace.web.swagger;

import java.util.Properties;

public class SwaggerConfigurationHelper {

    public static Properties getSwaggerProperties() {
//        String basePath = System.getProperty("swagger.controller.path");
        Properties properties = new Properties();
        properties.setProperty("documentation.services.version", "1.0");
//        properties.setProperty("documentation.services.basePath", basePath);
//        properties.setProperty("documentation.services.basePath", "api-docs");
        return properties;
    }
}