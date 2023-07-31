/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.SupportTicketDAO;
import com.prj301.assignment.javabean.Notification;
import com.prj301.assignment.model.SupportTicket;
import com.prj301.assignment.model.User;
import com.prj301.assignment.utils.AppPaths;
import com.prj301.assignment.utils.Constants;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hieunghia
 */
public class ContactServlet extends HttpServlet {

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

	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
//        processRequest(request, response);
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.CONTACT_JSP);
		rd.forward(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String message = request.getParameter("message");
		HttpSession session = request.getSession(false);
		User user = null;

		if (session != null) {
			user = (User) session.getAttribute(Constants.SESSION_USER);
		}

		SupportTicketDAO supTicketDao = new SupportTicketDAO();
		SupportTicket newTicket = null;
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.CONTACT_JSP);

		if (email.isEmpty() || name.isEmpty() || message.isEmpty()) {
			Notification e = new Notification("Error", "Please fill in all the fields!", "error");
			request.setAttribute("notifications", e);
			rd.forward(request, response);
			return;
		}
		newTicket = supTicketDao.addTicket(email, name, message, user);

		if (newTicket != null) {
			Notification n = new Notification("Success", "Send message successfully!", "success");
			request.setAttribute("notifications", n);
		} else {
			Notification e = new Notification("Error", "Send message error!", "error");
			request.setAttribute("notifications", e);
		}
		rd.forward(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Contact";
	}// </editor-fold>

}
