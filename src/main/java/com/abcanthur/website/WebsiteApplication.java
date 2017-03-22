package com.abcanthur.website;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;

import com.abcanthur.website.codegen.tables.records.UserRecord;
import com.abcanthur.website.resources.AccountResource;
import com.abcanthur.website.resources.TodoResource;
import com.abcanthur.website.resources.injection.UserAuthenticator;
import com.bendb.dropwizard.jooq.JooqBundle;
import com.bendb.dropwizard.jooq.JooqFactory;

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
                    final Environment environment) throws ClassNotFoundException {
        final AccountResource acctResource = new AccountResource();
        final TodoResource todoResource = new TodoResource();
        environment.jersey().register(acctResource);
        environment.jersey().register(todoResource);

        // Hacky way to get DB access in the UserAuthenticator
        UserAuthenticator.jooqConfig = configuration
			.getJooqFactory()
			.build(environment, configuration.getDataSourceFactory(), "auth");
        environment.jersey().getResourceConfig().register(new AbstractBinder() {
        	@Override
        	protected void configure() {
        		bindFactory(UserAuthenticator.class)
						.to(UserRecord.class)
						.in(RequestScoped.class);
        	}
        });

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
