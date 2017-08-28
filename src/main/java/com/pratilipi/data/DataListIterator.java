package com.pratilipi.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;


public class DataListIterator<T> implements Iterator<T> {

	private QueryResultIterator<? extends T> iterator;

	
	public DataListIterator( QueryResultIterator<? extends T> iterator ) {
		this.iterator = iterator;
	}

	
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		iterator.remove();
	}


	public List<T> list() {
		LinkedList<T> notifList = new LinkedList<>();
		while( iterator.hasNext() )
			notifList.add( iterator.next() );
		return notifList;
	}

	public String getCursor() {
		Cursor cursor = iterator.getCursor();
		return cursor == null ? null : cursor.toWebSafeString();
	}
	
}