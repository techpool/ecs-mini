package com.pratilipi.data.type.gae;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pratilipi.common.type.BatchProcessState;
import com.pratilipi.common.type.BatchProcessType;
import com.pratilipi.data.type.BatchProcess;

@Cache
@Entity( name = "BATCH_PROCESS" )
public class BatchProcessEntity implements BatchProcess {

	@Id
	private Long BATCH_PROCESS_ID;
	@Index
	private BatchProcessType TYPE;
	private String INIT_DOC;
	private String EXEC_DOC;
	private Date START_AT;

	@Index
	private BatchProcessState STATE_IN_PROGRESS;
	@Index
	private BatchProcessState STATE_COMPLETED;
	
	@Index
	private Date CREATION_DATE;
	@Index
	private Date LAST_UPDATED;


	public BatchProcessEntity() {}

	public BatchProcessEntity( Long id ) {
		this.BATCH_PROCESS_ID = id;
	}

	
	@Override
	public Long getId() {
		return BATCH_PROCESS_ID;
	}
	
	public void setId( Long id ) {
		this.BATCH_PROCESS_ID = id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Key<T> getKey() {
		return getId() == null ? null : (Key<T>) Key.create( getClass(), getId() );
	}
	
	@Override
	public <T> void setKey( Key<T> key ) {
		this.BATCH_PROCESS_ID = key.getId();
	}
	

	@Override
	public BatchProcessType getType() {
		return TYPE;
	}

	@Override
	public void setType( BatchProcessType type ) {
		this.TYPE = type;
	}

	@Override
	public String getInitDoc() {
		return INIT_DOC;
	}

	@Override
	public void setInitDoc( String intDoc ) {
		this.INIT_DOC = intDoc;
	}
	
	@Override
	public String getExecDoc() {
		return EXEC_DOC;
	}

	@Override
	public void setExecDoc( String execDoc ) {
		this.EXEC_DOC = execDoc;
	}
	
	@Override
	public Date getStartAt() {
		return START_AT == null ? CREATION_DATE : START_AT;
	}
	
	@Override
	public void setStartAt( Date startAt ) {
		this.START_AT = startAt;
	}

	
	@Override
	public BatchProcessState getStateInProgress() {
		return STATE_IN_PROGRESS;
	}

	@Override
	public void setStateInProgress( BatchProcessState state ) {
		this.STATE_IN_PROGRESS = state;
	}

	@Override
	public BatchProcessState getStateCompleted() {
		return STATE_COMPLETED;
	}

	@Override
	public void setStateCompleted( BatchProcessState state ) {
		this.STATE_COMPLETED = state;
	}


	@Override
	public Date getCreationDate() {
		return CREATION_DATE;
	}
	
	@Override
	public void setCreationDate( Date creationDate ) {
		this.CREATION_DATE = creationDate;
	}

	@Override
	public Date getLastUpdated() {
		return LAST_UPDATED == null ? CREATION_DATE : LAST_UPDATED;
	}

	@Override
	public void setLastUpdated( Date lastUpdated ) {
		this.LAST_UPDATED = lastUpdated;
	}

}
