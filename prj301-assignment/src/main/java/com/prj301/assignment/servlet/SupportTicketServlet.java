/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.SupportTicketDAO;
import com.prj301.assignment.javabean.Notification;
import com.prj301.assignment.model.SupportTicket;
import com.prj301.assignment.utils.AppPaths;
import com.prj301.assignment.utils.Constants;
import com.prj301.assignment.utils.TicketStatus;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author khoad
 */
public class SupportTicketServlet extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.ADMIN_SUPPORT_TICKET_JSP);
		SupportTicketDAO stDAO = new SupportTicketDAO();
		HttpSession session = request.getSession(false);
		String tId = request.getParameter("ticket_id");
		String status = request.getParameter("status");

		if (session == null || session.getAttribute(Constants.SESSION_ADMIN) == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		int ticketId = 0;

		try {
			ticketId = Integer.parseInt(tId == null || tId.trim().isEmpty() ? "0" : tId);
		} catch (NumberFormatException e) {
		}

		SupportTicket ticket = null;
		if (ticketId > 0) {
			ticket = stDAO.getTicketInfo(ticketId);

			boolean updated = false;
			if (status != null && (status.equals("PENDING") || status.equals("CLOSED") || status.equals("RESOLVED"))) {
				updated = stDAO.updateTicketStatus(ticketId, status);
				if (updated) {
					Notification n = new Notification("Success", "Ticket updated successfully!", "success");
					request.setAttribute("notifications", n);
					ticket.setStatus(TicketStatus.valueOf(status));
				} else {
					Notification e = new Notification("Error", "Failed to update ticket!", "error");
					request.setAttribute("notifications", e);
				}
			}

			request.setAttribute("ticketDetails", ticket);
		}
		request.setAttribute("ticketDetails", ticket);

		List<SupportTicket> supTickets = stDAO.getSupportTicketsForAdmin();
		request.setAttribute("supportTickets", supTickets);
		rd.forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.ADMIN_SUPPORT_TICKET_JSP);
		SupportTicketDAO stDAO = new SupportTicketDAO();
		HttpSession session = request.getSession(false);
		String tId = request.getParameter("ticket_id");
		String deleteParam = request.getParameter("delete_ticket");

		if (session == null || session.getAttribute(Constants.SESSION_ADMIN) == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		int ticketId = 0;

		try {
			ticketId = Integer.parseInt(tId == null || tId.trim().isEmpty() ? "0" : tId);
		} catch (NumberFormatException e) {
		}

		if (ticketId > 0) {
			SupportTicket ticket = stDAO.getTicketInfo(ticketId);
			request.setAttribute("ticketDetails", ticket);

		}

		if (deleteParam != null && deleteParam.trim().equalsIgnoreCase("true")) {
			boolean deleted = stDAO.deleteTicket(ticketId);
			if (deleted) {
				request.setAttribute("ticketDetails", null);
				Notification n = new Notification("Success", "Ticket deleted successfully!", "success");
				request.setAttribute("notifications", n);
			} else {
				Notification e = new Notification("Error", "Failed to delete ticket!", "error");
				request.setAttribute("notifications", e);
			}
		}
		List<SupportTicket> supTickets = stDAO.getSupportTicketsForAdmin();
		request.setAttribute("supportTickets", supTickets);
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
