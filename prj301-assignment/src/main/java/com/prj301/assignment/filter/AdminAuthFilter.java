/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.filter;

import com.prj301.assignment.utils.AppPaths;
import com.prj301.assignment.utils.Constants;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
public class AdminAuthFilter implements Filter {

	/**
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param chain The filter chain we are processing
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet error occurs
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
					FilterChain chain)
					throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession(false);
		boolean isAdmin = (session != null && session.getAttribute(Constants.SESSION_ADMIN) != null);

		String requestURI = httpRequest.getRequestURI();
		boolean isAdminDashboard = requestURI.endsWith(AppPaths.ADMIN) || requestURI.endsWith(AppPaths.ADMIN_DASHBOARD_JSP);
		boolean isAdminSupportTicket = requestURI.endsWith(AppPaths.ADMIN_SUPPORT_TICKET) || requestURI.endsWith(AppPaths.ADMIN_SUPPORT_TICKET_JSP);
		boolean isAdminUsers = requestURI.endsWith(AppPaths.ADMIN_USERS) || requestURI.endsWith(AppPaths.ADMIN_USERS_JSP);

		if ((isAdminDashboard || isAdminSupportTicket || isAdminUsers) && !isAdmin) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + AppPaths.NOT_FOUND_JSP);
			return;
		}

		chain.doFilter(request, response);
	}

	/**
	 * Destroy method for this filter
	 */
	@Override
	public void destroy() {
	}

	/**
	 * Init method for this filter
	 */
	@Override
	public void init(FilterConfig filterConfig) {
	}
}
