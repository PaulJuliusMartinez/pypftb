package com.abcanthur.website.resources.injection;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

import org.glassfish.hk2.api.Factory;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static com.abcanthur.website.codegen.Tables.*;

import com.abcanthur.website.codegen.tables.records.UsersRecord;

public class UserAuthenticator implements Factory<String> {

	public static Configuration jooqConfig;
	private final ContainerRequestContext context;

	@Inject
	public UserAuthenticator(ContainerRequestContext context) {
		this.context = context;
	}

	@Override
	public String provide() {
		String cookie = this.context.getHeaderString("Cookie");
		DSLContext database = DSL.using(UserAuthenticator.jooqConfig);
		return "lollipop";
	}

	@Override
	public void dispose(String ur) {
		/* Empty apparently */
	}

}
