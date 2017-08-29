package com.pratilipi.data.type.gae;

import java.util.LinkedList;
import java.util.List;

import com.pratilipi.data.type.Navigation;

@SuppressWarnings("serial")
public class NavigationEntity implements Navigation {

	private String title;
	
	private List<Link> linkList;
	
	
	public NavigationEntity( String title ) {
		this.title = title;
		this.linkList = new LinkedList<Link>();
	}

	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle( String title ) {
		this.title = title;
	}
	
	@Override
	public List<Link> getLinkList() {
		return linkList;
	}
	
	@Override
	public void addLink( Link link ) {
		this.linkList.add( link );
	}

}
