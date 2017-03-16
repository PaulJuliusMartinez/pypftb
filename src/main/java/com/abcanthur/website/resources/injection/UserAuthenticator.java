package com.abcanthur.website.resources.injection;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

import org.glassfish.hk2.api.Factory;

import com.abcanthur.website.codegen.tables.records.UsersRecord;

public class UserAuthenticator implements Factory<String> {

	private final ContainerRequestContext context;

	@Inject
	public UserAuthenticator(ContainerRequestContext context) {
		this.context = context;
	}

	@Override
	public String provide() {
		return "lollipop";
	}

	@Override
	public void dispose(String ur) {
		/* Empty apparently */
	}

}
