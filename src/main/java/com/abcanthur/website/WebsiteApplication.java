package com.abcanthur.website;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.abcanthur.website.health.TemplateHealthCheck;
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
                    final Environment environment) throws Exception {
        final SecretsConfiguration secrets = loadSecrets(configuration);
        final HelloWorldResource resource = new HelloWorldResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }

    private SecretsConfiguration loadSecrets(WebsiteConfiguration conf) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        System.out.println(conf.getSecretsConfigPath());
        InputStream in = new FileInputStream(conf.getSecretsConfigPath());
        return yaml.loadAs(in, SecretsConfiguration.class);
    }

}
