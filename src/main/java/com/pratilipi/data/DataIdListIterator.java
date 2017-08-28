package com.pratilipi.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;


public class DataIdListIterator<T> implements Iterator<Long> {

	private QueryResultIterator<? extends Key<? extends T>> iterator;

	
	public DataIdListIterator( QueryResultIterator<? extends Key<? extends T>> iterator ) {
		this.iterator = iterator;
	}

	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Long next() {
		return iterator.next().getId();
	}

	@Override
	public void remove() {
		iterator.remove();
	}


	public List<Long> list() {
		LinkedList<Long> idList = new LinkedList<>();
		while( iterator.hasNext() )
			idList.add( iterator.next().getId() );
		return idList;
	}

	public String getCursor() {
		Cursor cursor = iterator.getCursor();
		return cursor == null ? null : cursor.toWebSafeString();
	}
	
}