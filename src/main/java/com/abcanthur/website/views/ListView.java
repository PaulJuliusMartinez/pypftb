package com.abcanthur.website.views;

import io.dropwizard.views.View;

public class ListView extends View {
	private String testStr;
	public ListView(String s) {
		super("list.mustache");
		this.testStr = s;
	}
	public String getStr() {
		return this.testStr;
	}
}
