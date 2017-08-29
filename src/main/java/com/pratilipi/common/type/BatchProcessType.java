package com.pratilipi.common.type;

public enum BatchProcessType {

	NOTIFACTION_BY_AUTHOR_FILTER(
			BatchProcessState.INIT,
			BatchProcessState.GET_USER_IDS_BY_AUTHOR_FILTER,
			BatchProcessState.CREATE_NOTIFICATIONS_FOR_USER_IDS,
			BatchProcessState.VALIDATE_NOTIFICATION_COUNT,
			BatchProcessState.COMPLETED ),
	;
	
	
	public BatchProcessState[] states;
	
	private BatchProcessType( BatchProcessState ... states ) {
		this.states = states;
	}
	
	
	public BatchProcessState getNextState( BatchProcessState state ) {

		if( state == null )
			return states.length == 0 ? null : states[ 0 ];
		
		for( int i = 0; i < states.length - 1; i++ )
			if( state == states[ i ] )
				return states[ i + 1 ];
		
		return null;
		
	}
	
}