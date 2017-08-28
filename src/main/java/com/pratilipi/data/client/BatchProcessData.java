package com.pratilipi.data.client;

import java.util.Date;

import com.pratilipi.common.type.BatchProcessState;
import com.pratilipi.common.type.BatchProcessType;

public class BatchProcessData {
	
	private Long batchProcessId;
	
	private BatchProcessType type;
	
	private String initDoc;
	
	private String execDoc;

	private BatchProcessState stateInProgress;
	
	private BatchProcessState stateCompleted;

	private Date creationDate;
	
	private Date lastUpdated;
	
	
	public Long getId() {
		return batchProcessId;
	}

	public void setId( Long id ) {
		this.batchProcessId = id;
	}

	public BatchProcessType getType() {
		return type;
	}

	public void setType( BatchProcessType type ) {
		this.type = type;
	}
	
	public String getInitDoc() {
		return initDoc;
	}

	public void setInitDoc( String initDoc ) {
		this.initDoc = initDoc;
	}
	
	public String getExecDoc() {
		return execDoc;
	}
	
	public void setExecDoc( String execDoc ) {
		this.execDoc = execDoc;
	}

	public BatchProcessState getStateInProgress() {
		return stateInProgress;
	}

	public void setStateInProgress( BatchProcessState state ) {
		this.stateInProgress = state;
	}

	public BatchProcessState getStateCompleted() {
		return stateCompleted;
	}

	public void setStateCompleted( BatchProcessState state ) {
		this.stateCompleted = state;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate( Date creationDate ) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated( Date lastUpdated ) {
		this.lastUpdated = lastUpdated;
	}

}
