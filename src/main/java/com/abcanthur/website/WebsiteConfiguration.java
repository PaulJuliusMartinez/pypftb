package com.abcanthur.website;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.bendb.dropwizard.jooq.JooqFactory;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.*;

public class WebsiteConfiguration extends Configuration {
	@NotEmpty
	private String template;
	@JsonProperty
	public String getTemplate() { return template; }
	@JsonProperty
	public void setTemplate(String template) { this.template = template; }

	@NotEmpty
	private String defaultName = "Stranger";
	@JsonProperty
	public String getDefaultName() { return defaultName; }
	@JsonProperty
	public void setDefaultName(String name) { this.defaultName = name; }

	@NotEmpty
	private String secretsConfigPath;
	@JsonProperty
	public String getSecretsConfigPath() { return this.secretsConfigPath; }
	@JsonProperty
	public void setSecretsConfigPath(String path) { this.secretsConfigPath = path; }

	@Valid
	@NotNull
	@JsonProperty
	private DataSourceFactory database = new DataSourceFactory();
	public DataSourceFactory getDataSourceFactory() { return this.database; }
	public void setDataSourceFactory(DataSourceFactory factory) { this.database = factory; }

	@Valid
	@NotNull
	@JsonProperty
	private JooqFactory jooq = new JooqFactory();
	public JooqFactory getJooqFactory() { return this.jooq; }
	public void setJooqFactory(JooqFactory factory) { this.jooq = factory; }
}
