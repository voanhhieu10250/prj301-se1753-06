/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.DeckDAO;
import com.prj301.assignment.javabean.Notification;
import com.prj301.assignment.model.Deck;
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
public class CreateDeckServlet extends HttpServlet {

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
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.CREATE_DECK_JSP);
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
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String visibility = request.getParameter("visibility");
		int privateDeck = 0;

		if (visibility.equals("private")) {
			privateDeck = 1;
		} else {
			privateDeck = 0;
		}

		HttpSession session = request.getSession(false);
		User user = null;

		if (session != null && session.getAttribute(Constants.SESSION_USER) != null) {
			user = (User) session.getAttribute(Constants.SESSION_USER);
		}

		DeckDAO decksDAO = new DeckDAO();
		Deck newDecks = null;
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.CREATE_DECK_JSP);

		if (name.isEmpty()) {
			Notification e = new Notification("Error", "Name can't be left blank!", "error");
			request.setAttribute("notifications", e);
			rd.forward(request, response);
			return;
		}

		if (name.length() > 40) {
			Notification e = new Notification("Error", "Name can only contain up to 40 characters", "error");
			request.setAttribute("notifications", e);
			rd.forward(request, response);
			return;
		}

		if (description.length() > 200) {
			Notification e = new Notification("Error", "Description can only contain up to 200 characters", "error");
			request.setAttribute("notifications", e);
			rd.forward(request, response);
			return;
		}

		newDecks = decksDAO.createDeck(name, description, privateDeck, user);

		if (newDecks != null) {
			response.sendRedirect(request.getContextPath() + AppPaths.HOME + "?success=Create deck successfully");
		} else {
			Notification e = new Notification("Error", "Create deck error!", "error");
			request.setAttribute("notifications", e);
			rd.forward(request, response);
		}
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
