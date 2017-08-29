package com.pratilipi.data.type.doc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pratilipi.data.type.PratilipiMetaDoc;

public class PratilipiMetaDocImpl implements PratilipiMetaDoc {
	
	private Map<String, Integer> wordCounts = new HashMap<>();
	@SuppressWarnings("unused")
	private Date lastUpdated;

	
	@Override
	public Map<String, Integer> getWordCounts() {
		return wordCounts == null ? new HashMap<String, Integer>() : wordCounts;
	}
	
	@Override
	public void setWordCounts( Map<String, Integer> wordCounts ) {
		this.wordCounts = wordCounts == null || wordCounts.size() == 0 ? null : wordCounts;
		lastUpdated = new Date();
	}	
	
}
