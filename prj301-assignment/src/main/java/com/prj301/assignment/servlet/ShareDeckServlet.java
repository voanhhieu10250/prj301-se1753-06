/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.DeckDAO;
import com.prj301.assignment.DAO.UserDAO;
import com.prj301.assignment.javabean.DeckDetailsBean;
import com.prj301.assignment.javabean.Notification;
import com.prj301.assignment.model.ShareDeck;
import com.prj301.assignment.model.User;
import com.prj301.assignment.utils.AppPaths;
import com.prj301.assignment.utils.Constants;
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
 * @author hieunghia
 */
public class ShareDeckServlet extends HttpServlet {

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
		String searchName = request.getParameter("search");
		String dId = request.getParameter("id");
		UserDAO userDAO = new UserDAO();
		DeckDAO deckDAO = new DeckDAO();
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.SHARE_DECK_JSP);
		HttpSession session = request.getSession(false);
		String s = searchName == null || searchName.trim().isEmpty() ? "" : searchName;

		int deckId = 0;
		int userId = 0;

		try {
			deckId = Integer.parseInt(dId == null || dId.trim().isEmpty() ? "0" : dId);
		} catch (NumberFormatException e) {
		}

		if (deckId < 1) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		if (session == null || session.getAttribute(Constants.SESSION_USER) == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		User user = (User) session.getAttribute(Constants.SESSION_USER);
		userId = user.getUserId();

		DeckDetailsBean deck = deckDAO.getDeckInfoByUserId(deckId, userId);

		if (deck == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}
		List<User> sharedUsers = deckDAO.getSharedUsers(deckId);
		List<User> listUser = userDAO.searchUserByName(s, deckId, userId);

		request.setAttribute("userList", listUser);
		request.setAttribute("sharedUsersList", sharedUsers);
		request.setAttribute("deckInfo", deck);
		request.setAttribute("search", s);
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
		String[] sUsers = request.getParameterValues("selectedUsers");
		String searchName = request.getParameter("search");
		String dId = request.getParameter("deckId");
		String s = searchName == null || searchName.trim().isEmpty() ? "" : searchName;
		int deckId = 0;
		int userId = 0;
		DeckDAO deckDAO = new DeckDAO();
		UserDAO userDAO = new UserDAO();
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.SHARE_DECK_JSP);
		HttpSession session = request.getSession(false);

		try {
			deckId = Integer.parseInt(dId == null || dId.trim().isEmpty() ? "0" : dId);
		} catch (NumberFormatException e) {
		}

		if (deckId < 1) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		if (session == null || session.getAttribute(Constants.SESSION_USER) == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		User user = (User) session.getAttribute(Constants.SESSION_USER);
		userId = user.getUserId();

		DeckDetailsBean deckInfo = deckDAO.getDeckInfoByUserId(deckId, userId);

		if (deckInfo == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		if (sUsers == null || sUsers.length == 0) {
			List<User> listUser = userDAO.searchUserByName(s, deckId, userId);
			request.setAttribute("userList", listUser);

			response.sendRedirect(request.getContextPath() + AppPaths.SHARE_DECK + "?id=" + deckId + "&error=Please select user you want to share");
			return;
		}

		int[] selectedUsers = new int[sUsers.length];
		for (int i = 0; i < sUsers.length; i++) {
			selectedUsers[i] = Integer.parseInt(sUsers[i]);
		}

		ShareDeck shareDeck = deckDAO.shareDeckToUser(deckId, selectedUsers);

		List<User> listUser = userDAO.searchUserByName(s, deckId, userId);
		request.setAttribute("userList", listUser);

		if (shareDeck != null) {
			Notification n = new Notification("Success", "Share deck successfully!", "success");
			request.setAttribute("notifications", n);
			List<User> sharedUsers = deckDAO.getSharedUsers(deckId);
			request.setAttribute("sharedUsersList", sharedUsers);
		} else {
			Notification e = new Notification("Error", "Failed to share deck!", "error");
			request.setAttribute("notifications", e);
		}

		request.setAttribute("deckInfo", deckInfo);
		rd.forward(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Share deck";
	}// </editor-fold>

}
