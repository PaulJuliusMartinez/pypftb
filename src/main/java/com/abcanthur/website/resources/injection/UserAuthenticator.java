package com.abcanthur.website.resources.injection;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

import org.glassfish.hk2.api.Factory;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static com.abcanthur.website.codegen.Tables.*;

import java.net.HttpCookie;
import java.util.List;

import com.abcanthur.website.codegen.tables.records.SessionRecord;
import com.abcanthur.website.codegen.tables.records.UserRecord;
import com.abcanthur.website.resources.AccountResource;

public class UserAuthenticator implements Factory<UserRecord> {

	public static Configuration jooqConfig;
	private final ContainerRequestContext context;

	@Inject
	public UserAuthenticator(ContainerRequestContext context) {
		this.context = context;
	}

	@Override
	public UserRecord provide() {
		String cookie = this.context.getHeaderString("Cookie");
		DSLContext database = DSL.using(UserAuthenticator.jooqConfig);
		
		if (cookie == null) return null;
		
		String token = null;
		List<HttpCookie> cookies = HttpCookie.parse(cookie);
		for (int x = 0; x < cookies.size(); x++) {
			if (cookies.get(x).getName().equals(AccountResource.COOKIE_NAME)) {
				token = cookies.get(x).getValue();
				break;
			}
		}
		
		if (token == null) return null;
		
		SessionRecord session = database.selectFrom(SESSIONS)
				.where(SESSIONS.TOKEN.equal(token))
				.fetchOne();
		
		if (session == null) return null;
		
		return database.selectFrom(USERS)
				.where(USERS.ID.equal(session.getUserId()))
				.fetchOne();
	}

	@Override
	public void dispose(UserRecord ur) {
		/* Empty apparently */
	}

}
