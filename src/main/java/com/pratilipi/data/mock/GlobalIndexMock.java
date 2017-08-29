package com.pratilipi.data.mock;

import static com.pratilipi.data.mock.AuthorMock.guAuthor_1;
import static com.pratilipi.data.mock.AuthorMock.hiAuthor_1;
import static com.pratilipi.data.mock.AuthorMock.taAuthor_1;
import static com.pratilipi.data.mock.PratilipiMock.guPratilipi_1;
import static com.pratilipi.data.mock.PratilipiMock.guPratilipi_2;
import static com.pratilipi.data.mock.PratilipiMock.guPratilipi_3;
import static com.pratilipi.data.mock.PratilipiMock.hiPratilipi_1;
import static com.pratilipi.data.mock.PratilipiMock.hiPratilipi_2;
import static com.pratilipi.data.mock.PratilipiMock.hiPratilipi_3;
import static com.pratilipi.data.mock.PratilipiMock.taPratilipi_1;
import static com.pratilipi.data.mock.PratilipiMock.taPratilipi_2;
import static com.pratilipi.data.mock.PratilipiMock.taPratilipi_3;

import java.util.LinkedList;
import java.util.List;

import com.pratilipi.common.exception.UnexpectedServerException;
import com.pratilipi.data.util.PratilipiDataUtil;

public class GlobalIndexMock {

	public static final List<Object> GLOBAL_INDEX = new LinkedList<>();
	
	
	static {
		try {
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( hiPratilipi_1, hiAuthor_1 ) );
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( hiPratilipi_2, hiAuthor_1 ) );
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( hiPratilipi_3, hiAuthor_1 ) );
			
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( guPratilipi_1, guAuthor_1 ) );
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( guPratilipi_2, guAuthor_1 ) );
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( guPratilipi_3, guAuthor_1 ) );
			
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( taPratilipi_1, taAuthor_1 ) );
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( taPratilipi_2, taAuthor_1 ) );
			GLOBAL_INDEX.add( PratilipiDataUtil.createPratilipiData( taPratilipi_3, taAuthor_1 ) );
		} catch (UnexpectedServerException e) {
			e.printStackTrace();
		}
	}
	
}