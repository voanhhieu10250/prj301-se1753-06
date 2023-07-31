/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.google.gson.Gson;
import com.prj301.assignment.DAO.CardDAO;
import com.prj301.assignment.model.User;
import com.prj301.assignment.utils.Constants;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
public class DeleteCardServlet extends HttpServlet {

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
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			out.println("Invalid request");
			out.close();
			return;
		}

		User user = (User) session.getAttribute(Constants.SESSION_USER);

		String cId = request.getParameter("id");
		if (!isNotNull(cId)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("Invalid card ID");
			out.close();
			return;
		}

		int cardId = Integer.parseInt(cId);
		CardDAO cardDao = new CardDAO();
		boolean success = cardDao.deleteCard(cardId, user.getUserId());
		if (!success) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println("Card not found or you are not the owner of the card");
			out.close();
			return;
		}

		Gson gson = new Gson();
		response.setStatus(HttpServletResponse.SC_OK);
		out.println(gson.toJson("success"));
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

	public boolean isNotNull(String param) {
		return !(param == null || param.trim().isEmpty());
	}
}
