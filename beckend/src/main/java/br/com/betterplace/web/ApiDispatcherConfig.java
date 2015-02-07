package br.com.betterplace.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

@Configuration
@EnableWebMvc
//@EnableWebSecurity
@ComponentScan(basePackages = {"br.com.betterplace"})
public class ApiDispatcherConfig extends WebMvcConfigurerAdapter {

    /**
     * We are adding a JSP View Resolver to process our JSP version of Swagger's
     * home page.
     */
    @Bean
    public ViewResolver jspResolver() {
        InternalResourceViewResolver jspResolver = new InternalResourceViewResolver();
        jspResolver.setPrefix("/swagger/");
        jspResolver.setSuffix(".jsp");
        return jspResolver;
    }

    /**
     * This is equivalent to mvc:default-servlet-handler. It's needed to support
     * static resources and other features.
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("dd/MM/yyyy"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    /**
     * These static resources enable Swagger's UI.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/docs/lib/**").addResourceLocations("/swagger/lib/");
        registry.addResourceHandler("/docs/css/**").addResourceLocations("/swagger/css/");
        registry.addResourceHandler("/docs/images/**").addResourceLocations("/swagger/images/");
        registry.addResourceHandler("/docs/*.js").addResourceLocations("/swagger/");
    }

    /**
     * This maps the / path to the 'index' view, which will act as welcome page
     * when somebody accesses the API URL without any specific path.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        ViewControllerRegistration addViewController = registry.addViewController("/docs/");
        addViewController.setViewName("index");
    }
}