package com.abcanthur.website;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.bendb.dropwizard.jooq.JooqFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.*;

public class WebsiteConfiguration extends Configuration {
    private SecretsConfiguration secrets;

    @NotEmpty private String secretsConfigPath;
    @JsonProperty public String getSecretsConfigPath() { return this.secretsConfigPath; }
    @JsonProperty public void setSecretsConfigPath(String path) { this.secretsConfigPath = path; }

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
    public DataSourceFactory getDataSourceFactory() {
        SecretsConfiguration secrets = this.getSecretsConfiguration();
        this.database.setPassword(secrets.getDatabasePassword());
        this.database.setUser(secrets.getDatabaseUser());
        this.database.setUrl(secrets.getDatabaseUrl());
        return this.database;
    }
    public void setDataSourceFactory(DataSourceFactory factory) { this.database = factory; }

    @Valid
    @NotNull
    @JsonProperty
    private JooqFactory jooq = new JooqFactory();
    public JooqFactory getJooqFactory() { return this.jooq; }
    public void setJooqFactory(JooqFactory factory) { this.jooq = factory; }

    private SecretsConfiguration getSecretsConfiguration() {
        if (this.secrets != null) return this.secrets;
        Yaml yaml = new Yaml();
        InputStream in;
        try {
            in = new FileInputStream(this.getSecretsConfigPath());
            this.secrets = yaml.loadAs(in, SecretsConfiguration.class);
            in.close();
        } catch (Exception e) {
            System.err.println("Could not load secrets file.");
            System.exit(1);
        }
        return this.secrets;
    }
}
