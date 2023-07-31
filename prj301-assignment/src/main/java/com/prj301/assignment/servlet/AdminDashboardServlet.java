/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.DeckDAO;
import com.prj301.assignment.DAO.SupportTicketDAO;
import com.prj301.assignment.DAO.UserDAO;
import com.prj301.assignment.model.SupportTicket;
import com.prj301.assignment.utils.AppPaths;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
public class AdminDashboardServlet extends HttpServlet {

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
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.ADMIN_DASHBOARD_JSP);
		// get new users doday
		// get new decks today
		// get decks study today
		// get top 5 support tickets order by status
		UserDAO userDao = new UserDAO();
		DeckDAO deckDao = new DeckDAO();
		SupportTicketDAO supDao = new SupportTicketDAO();
		
		int numOfNewUsers = userDao.getNumOfNewUsersToday();
		int numOfNewDecks = deckDao.getNumOfNewDecksToday();
		int numOfDecksStudies = deckDao.getNumOfDecksStudiesToday();
		List<SupportTicket> supList = supDao.getSupportTicketsForDashboard();


		request.setAttribute("newUsersNum", numOfNewUsers);
		request.setAttribute("newDecksNum", numOfNewDecks);
		request.setAttribute("decksStudiesNum", numOfDecksStudies);
		request.setAttribute("supList", supList);

		rd.forward(request, response);
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
