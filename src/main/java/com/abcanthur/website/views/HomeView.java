package com.abcanthur.website.views;

import io.dropwizard.views.View;

public class HomeView extends View {
	private int pledgeCount;
	
	public HomeView(int pledgeCount) {
		super("home.mustache");
		this.pledgeCount = pledgeCount;
	}
	
	public int getPledgeCount() {
		return this.pledgeCount;
	}
}
