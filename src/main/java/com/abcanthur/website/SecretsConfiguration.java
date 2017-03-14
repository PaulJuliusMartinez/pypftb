package com.abcanthur.website;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class SecretsConfiguration extends Configuration {
    @NotEmpty private String databaseUrl;
    @JsonProperty public String getDatabaseUrl() { return databaseUrl; }
    @JsonProperty public void setDatabaseUrl(String databaseUrl) { this.databaseUrl = databaseUrl; }

    @NotEmpty private String databasePassword;
    @JsonProperty public String getDatabasePassword() { return databasePassword; }
    @JsonProperty public void setDatabasePassword(String password) { this.databasePassword = password; }

    @NotEmpty private String databaseUser;
    @JsonProperty public String getDatabaseUser() { return databaseUser; }
    @JsonProperty public void setDatabaseUser(String user) { this.databaseUser = user; }
}
