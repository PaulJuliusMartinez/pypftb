package com.abcanthur.website;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.abcanthur.website.health.TemplateHealthCheck;
import com.abcanthur.website.resources.AccountResource;
import com.abcanthur.website.resources.HelloWorldResource;
import com.bendb.dropwizard.jooq.JooqBundle;
import com.bendb.dropwizard.jooq.JooqFactory;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
                    final Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        final AccountResource acctResource = new AccountResource();
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(acctResource);
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
