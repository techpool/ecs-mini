package com.pratilipi.data;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class DataListCursorTuple<T> implements Serializable {

	private List<T> dataList;
	private String cursor;
	private Long numberFound;
	
	
	public DataListCursorTuple( List<T> dataList, String cursor ) {
		this.dataList = dataList;
		this.cursor = cursor;
	}

	public DataListCursorTuple( List<T> dataList, String cursor, Long numberFound ) {
		this.dataList = dataList;
		this.cursor = cursor;
		this.numberFound = numberFound;
	}

	
	public List<T> getDataList() {
		return dataList;
	}
	
	public String getCursor() {
		return cursor;
	}
	
	public Long getNumberFound() {
		return numberFound;
	}
	
}
