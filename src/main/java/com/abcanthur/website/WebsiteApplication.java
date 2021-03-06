package com.abcanthur.website;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.abcanthur.website.resources.APIResource;
import com.abcanthur.website.resources.DocumentResource;
import com.bendb.dropwizard.jooq.JooqBundle;
import com.bendb.dropwizard.jooq.JooqFactory;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class WebsiteApplication extends Application<WebsiteConfiguration> {

    public static void main(final String[] args) throws Exception {
        new WebsiteApplication().run(args);
    }

    @Override
    public String getName() {
        return "Website";
    }

    @Override
    public void initialize(final Bootstrap<WebsiteConfiguration> bootstrap) {
    	bootstrap.addBundle(new AssetsBundle());
    	bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new JooqBundle<WebsiteConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(WebsiteConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }

            @Override
            public JooqFactory getJooqFactory(WebsiteConfiguration configuration) {
                return configuration.getJooqFactory();
            }
        });
    }

    @Override
    public void run(final WebsiteConfiguration configuration,
                    final Environment environment) throws ClassNotFoundException {
        final DocumentResource documentResource = new DocumentResource();
        final APIResource apiResource = new APIResource();
        
        environment.jersey().register(documentResource);
        environment.jersey().register(apiResource);

        addCors(environment);
    }
    
    public void addCors(Environment environment) {
    	final FilterRegistration.Dynamic cors =
            environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

}
