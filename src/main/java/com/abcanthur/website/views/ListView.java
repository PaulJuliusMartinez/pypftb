package com.abcanthur.website.views;

import java.util.List;

import com.abcanthur.website.codegen.tables.pojos.PledgePojo;

import io.dropwizard.views.View;

public class ListView extends View {
	
	private List<PledgePojo> pledges;
	private int count;
	private int offset;
	private int total;
	
	public ListView(List<PledgePojo> pledges, int count, int offset, int total) {
		super("list.mustache");
		this.pledges = pledges;
		this.count = count;
		this.offset = offset;
		this.total = total;
	}
	
	public List<PledgePojo> getPledges() {
		return this.pledges;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public String getLinkToEarlierPledges() {
		if (offset == 0) return null;
		
		int newOffset = offset - count;
		if (newOffset < 0) newOffset = 0;
		
		return "/list?count=" + count + "&offset=" + newOffset; 
	}
	
	public String getLinkToLaterPledges() {
		if (offset + count >= total) return null;
		return "/list?count=" + count + "&offset=" + (offset + count);		
	}
	
}
