/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.UserDAO;
import com.prj301.assignment.javabean.Notification;
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
 * @author Admin
 */
public class DeleteProfileServlet extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		HttpSession session = request.getSession(false); // pass false to prevent creation of new session       
		UserDAO dao = new UserDAO();

		if (session != null && session.getAttribute(Constants.SESSION_USER) != null) {
			boolean success = dao.deleteUser(((User) session.getAttribute(Constants.SESSION_USER)).getUserId());
			if (success) {
				session.invalidate();
				response.sendRedirect(request.getContextPath() + AppPaths.LOGIN + "?success=Delete account successfully");
			} else {
				Notification n = new Notification("Error", "Something went wrong! Please try again later", "error");
				request.setAttribute("notifications", n);
				RequestDispatcher rd = request.getRequestDispatcher(AppPaths.DELETE_USER_JSP);
				rd.forward(request, response);
			}
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
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.DELETE_USER_JSP);
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
		return "Delete profile";
	}// </editor-fold>

}
