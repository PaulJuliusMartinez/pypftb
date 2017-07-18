package com.abcanthur.website.views;

import io.dropwizard.views.View;

public class HomeView extends View {
	private String testStr;
	public HomeView(String s) {
		super("home.mustache");
		this.testStr = s;
	}
	public String getStr() {
		return this.testStr;
	}
}
