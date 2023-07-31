/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.google.gson.Gson;
import com.prj301.assignment.DAO.CardDAO;
import com.prj301.assignment.DAO.DeckDAO;
import com.prj301.assignment.javabean.DeckMeta;
import com.prj301.assignment.model.Card;
import com.prj301.assignment.model.User;
import com.prj301.assignment.utils.AppPaths;
import com.prj301.assignment.utils.Constants;
import java.io.IOException;
import java.io.PrintWriter;
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
public class AddCardServlet extends HttpServlet {

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
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute(Constants.SESSION_USER) == null) {
			// Set HTTP status code to indicate a bad request
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			// Include an error message in the response body
			out.println("Invalid request");
			out.close();
			return;
		}

		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String front = request.getParameter("card-front");
		String back = request.getParameter("card-back");
		String tags = request.getParameter("tags");
		String dId = request.getParameter("deckId");

		if (!isNotNull(front) || !isNotNull(back) || !isNotNull(dId)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("Please fill in all the fields");
			out.close();
			return;
		}

		tags = isNotNull(tags) ? tags : "";
		int deckId = Integer.parseInt(dId);
		CardDAO cardDao = new CardDAO();
		Gson gson = new Gson();
		Card addedCard = cardDao.addCard(front, back, tags, deckId, user.getUserId());

		if (addedCard == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("Deck not found or you are not the owner of the deck");
			out.close();
			return;
		}

		response.setStatus(HttpServletResponse.SC_OK);
		String json = gson.toJson(addedCard);
		out.println(json);
		// close the PrintWriter object to flush and close the response
		out.close();
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
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute(Constants.SESSION_USER) != null) {
			User user = (User) session.getAttribute(Constants.SESSION_USER);
			DeckDAO deckDao = new DeckDAO();
			List<DeckMeta> decksMeta = deckDao.getDeckMetaByOwnerId(user.getUserId());

			request.setAttribute("decksInfo", decksMeta);
		}
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.ADD_CARDS_JSP);
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

	public boolean isNotNull(String param) {
		return !(param == null || param.trim().isEmpty());
	}
}
