package br.com.betterplace.web.configuration;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.Log4jConfigurer;

import br.com.betterplace.web.swagger.SwaggerConfigurationHelper;

@Profile(ConfigurationProfiles.QA)
@PropertySource({ "classpath:ne-api-qa.properties", "classpath:mysql-db-qa.properties" })
@Configuration
public class QAProfilePropertiesConfiguration {

    /**
     * Swagger configuration here. Browse docs at <applicaton-context>/api-docs.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(SwaggerConfigurationHelper.getSwaggerProperties());
        configurer.setIgnoreUnresolvablePlaceholders(true);
        // Allows local override (through system properties, for example) of
        // properties found in files
        configurer.setLocalOverride(true);
        return configurer;
    }

    @PostConstruct
    public void initLog4j() throws FileNotFoundException {
        Log4jConfigurer.initLogging("classpath:log4j.xml");
    }

}
