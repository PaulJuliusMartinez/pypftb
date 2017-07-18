package com.abcanthur.website.views;

import io.dropwizard.views.View;

public class MethodsView extends View {
	private String testStr;
	public MethodsView(String s) {
		super("methods.mustache");
		this.testStr = s;
	}
	public String getStr() {
		return this.testStr;
	}
}
