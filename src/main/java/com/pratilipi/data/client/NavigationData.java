package com.pratilipi.data.client;

import java.util.List;

import com.pratilipi.data.type.Navigation.Link;

public class NavigationData {

	private String title;
	
	private List<Link> linkList;
	
	
	public NavigationData( String title, List<Link> linkList ) {
		this.title = title;
		this.linkList = linkList;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}
	
	public List<Link> getLinkList() {
		return linkList;
	}
	
	public void addLink( Link link ) {
		this.linkList.add( link );
	}

}