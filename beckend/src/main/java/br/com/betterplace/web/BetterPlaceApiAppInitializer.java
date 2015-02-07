package br.com.betterplace.web;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import br.com.betterplace.web.configuration.ConfigurationProfiles;

import com.google.common.base.Strings;

public class BetterPlaceApiAppInitializer implements WebApplicationInitializer {

    private final ServletContainerSettingsEnsurance containerSettingsInsurance = new ServletContainerSettingsEnsurance();

    private static final Logger LOG = Logger.getLogger(BetterPlaceApiAppInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.containerSettingsInsurance.safellyVerifyServlet();
        
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        if (!this.isProfileSet()) {
            LOG.info("Using development profile.");
            rootContext.getEnvironment().setActiveProfiles(ConfigurationProfiles.DEVELOPMENT);
        } else {
            LOG.info(String.format("Using %s profile.", StringUtils.join(rootContext.getEnvironment().getActiveProfiles(), ",")));
        }
        
        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));
        
        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(ApiDispatcherConfig.class);
        
        // Register and map the dispatcher servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
        // Create charset encoding filter
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        Dynamic filter = servletContext.addFilter("charset-encoding-filter", characterEncodingFilter);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }

    private boolean isProfileSet() {
        String profile = System.getProperty("spring.profiles.active");
        return !Strings.isNullOrEmpty(profile);
    }
}