/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.CardDAO;
import com.prj301.assignment.javabean.BrowseBean;
import com.prj301.assignment.javabean.CardMeta;
import com.prj301.assignment.javabean.Notification;
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
 * @author Admin
 */
public class BrowseServlet extends HttpServlet {

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
		String searchStr = request.getParameter("search");
		String searchBy = request.getParameter("searchBy");
		String currentPage = request.getParameter("page");
		String pPage = request.getParameter("perPage");

		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.BROWSE_JSP);
		int curPage, perPage;
		HttpSession session = request.getSession(false);

		try {
			curPage = Integer.parseInt(currentPage == null || currentPage.trim().isEmpty() ? "1" : currentPage);
			perPage = Integer.parseInt(pPage == null || pPage.trim().isEmpty() ? "10" : pPage);
		} catch (NumberFormatException e) {
			curPage = 1;
			perPage = 10;
		}
		CardDAO cardDao = new CardDAO();

		String s = searchStr == null || searchStr.trim().isEmpty() ? "" : searchStr;
		String sBy = searchBy == null || searchBy.trim().isEmpty() ? "cardname" : searchBy;

		if (session != null && session.getAttribute(Constants.SESSION_USER) != null) {
			// if user logged in then return user's decks and shared decks. And recent decks
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			List<CardMeta> cards = null;
			BrowseBean browse = new BrowseBean();
			int totalCards = 0;

			if (sBy.equalsIgnoreCase("cardname")) {
				cards = cardDao.getCardsSearchByCardName(user.getUserId(), s, curPage, perPage);
				totalCards = cardDao.getTotalCardsSearchByCardName(user.getUserId(), s);
			} else if (sBy.equalsIgnoreCase("tagname")) {
				cards = cardDao.getCardsSearchByTagName(user.getUserId(), s, curPage, perPage);
				totalCards = cardDao.getTotalCardsSearchByTagName(user.getUserId(), s);
			} else if (sBy.equalsIgnoreCase("deckname")) {
				cards = cardDao.getCardsSearchByDeckName(user.getUserId(), s, curPage, perPage);
				totalCards = cardDao.getTotalCardsSearchByDeckName(user.getUserId(), s);
			}
			browse.setCards(cards);
			browse.setTotalCards(totalCards);
			request.setAttribute("browse", browse);
			rd.forward(request, response);
		} else {
			// if user not logged in then return 404 page
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			request.setAttribute("notifications", new Notification("Error", "Invalid request", "error"));
			rd.forward(request, response);
		}
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
		processRequest(request, response);
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
		processRequest(request, response);
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
