package com.abcanthur.website.resources;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.mindrot.jbcrypt.BCrypt;
import org.postgresql.util.Base64;

import com.abcanthur.website.codegen.tables.records.SessionsRecord;
import com.abcanthur.website.codegen.tables.records.UsersRecord;

import static com.abcanthur.website.codegen.Tables.*;

import java.net.HttpCookie;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Path("/accounts")
public class AccountResource {
	
	static String COOKIE_NAME = "session-token";
	
	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(
			@FormParam("email") Optional<String> email,
			@FormParam("password") Optional<String> password,
			@Context DSLContext database,
			@Context HttpServletResponse response
	) {
		if (!password.isPresent() || !email.isPresent()) {
			throw new WebApplicationException("Login Failed", 401); 
		}
		UsersRecord user = database.selectFrom(USERS)
			.where(USERS.EMAIL.equal(email.get()))
			.fetchOne();
		if (user == null) {
			throw new WebApplicationException("Login Failed", 401);
		}
		if (!BCrypt.checkpw(password.get(), user.getPassword())) {
			throw new WebApplicationException("Login Failed", 401); 
		}
		SessionsRecord session = database.selectFrom(SESSIONS)
			.where(SESSIONS.USER_ID.equal(user.getId()))
			.fetchOne();

		String token;
		if (session == null) {
			token = generateSessionToken();
			database
				.insertInto(SESSIONS, SESSIONS.USER_ID, SESSIONS.TOKEN)
				.values(user.getId(), token)
				.execute();
		} else {
			long thirtyDays = 30L * 24L * 60L * 60L * 1000L;
			Timestamp newTS = new Timestamp(System.currentTimeMillis() + thirtyDays);
			session.setExpiresAt(newTS);
			session.update();
			token = session.getToken();
		}
		Cookie cookie = new Cookie(AccountResource.COOKIE_NAME, token);
		cookie.setMaxAge(30 * 24 * 60 * 60);
		response.addCookie(cookie);
		return AccountResource.COOKIE_NAME + "=" + token;
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.TEXT_PLAIN)
	public String create(
			@FormParam("email") Optional<String> email,
			@FormParam("password") Optional<String> password,
			@Context DSLContext database
	) {
		if (!email.isPresent()) throw new WebApplicationException("Please provide an email address", 400);
		if (!password.isPresent()) throw new WebApplicationException("Please provide a password", 400);
		if (password.get().length() < 8) throw new WebApplicationException("Password must be at least 8 characters long", 400);
		if (!isEmailValid(email.get())) throw new WebApplicationException("Please provide a valid email address", 400);
		
		try {
			database.insertInto(USERS, USERS.EMAIL, USERS.PASSWORD)
				.values(email.get(), BCrypt.hashpw(password.get(), BCrypt.gensalt()))
				.execute();
		} catch (DataAccessException e) {
			throw new WebApplicationException("email already in use by another account", 400);
		}
		String createAcctLog = "account created: " + email.get();
		return createAcctLog;
	}
	
	public boolean isEmailValid(String email) {
		if (!email.contains("@")) return false;
		String[] emailNameDom = email.split("@");
		if (emailNameDom.length > 2) return false;
		if (emailNameDom[0].length() < 1) return false;
		if (emailNameDom[1].length() < 1) return false;
		if (!emailNameDom[1].contains(".")) return false;
		String[] emailDom = emailNameDom[1].split("\\.");
		if (emailDom[0].length() < 1) return false;
		if (emailDom[1].length() < 1) return false;
		return true;
	}
	
	public String generateSessionToken() {
		Random ran = new SecureRandom();
		byte [] tokenBytes = new byte[32];
		ran.nextBytes(tokenBytes);
		String token = Base64.encodeBytes(tokenBytes);
		return token;
	}
	
	@GET
	@Path("/whoami")
	@Produces(MediaType.TEXT_PLAIN)
	public String whoami(
		@HeaderParam("Cookie") String cookie,
		@Context DSLContext database,
		@Context String injected
	) {
		System.out.println(injected);
		String token = "No Session Token found";
		List<HttpCookie> cookies = HttpCookie.parse(cookie);
		for(int x = 0; x < cookies.size(); x++) {
			if(cookies.get(x).getName().equals(AccountResource.COOKIE_NAME)) {
				token = cookies.get(x).getValue();
				break;
			}
		}
		
		SessionsRecord session = database.selectFrom(SESSIONS)
				.where(SESSIONS.TOKEN.equal(token))
				.fetchOne();
		
		UsersRecord user = database.selectFrom(USERS)
				.where(USERS.ID.equal(session.getUserId()))
				.fetchOne();
		
		System.out.println("Welcome to Mission Control " + user.getEmail());
		
		return cookie;
	}
	
}
