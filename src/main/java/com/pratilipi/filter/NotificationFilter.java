package com.pratilipi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.pratilipi.common.type.NotificationState;
import com.pratilipi.common.type.RequestParameter;
import com.pratilipi.data.DataAccessor;
import com.pratilipi.data.DataAccessorFactory;
import com.pratilipi.data.type.Notification;

public class NotificationFilter implements Filter {

	@Override
	public void init( FilterConfig filterConfig ) throws ServletException {	}

	@Override
	public void destroy() {	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		String notificationIdStr = ( ( HttpServletRequest ) req ).getParameter( RequestParameter.NOTIFICATION_ID.getName() );
		if( notificationIdStr != null && ! notificationIdStr.trim().isEmpty() ) {
			DataAccessor dataAccessor = DataAccessorFactory.getDataAccessor();
			Notification notification = dataAccessor.getNotification( Long.parseLong( notificationIdStr.trim() ) );
			if( notification != null
					&& notification.getState() == NotificationState.UNREAD
					&& AccessTokenFilter.getAccessToken().getUserId().equals( notification.getUserId() ) ) {
				notification.setState( NotificationState.READ );
				notification = dataAccessor.createOrUpdateNotification( notification );
			}
		}

		chain.doFilter( req, resp );

	}

}
