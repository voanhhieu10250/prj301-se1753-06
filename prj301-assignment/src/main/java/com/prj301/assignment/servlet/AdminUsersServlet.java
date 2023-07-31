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
public class AdminUsersServlet extends HttpServlet {

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
		String id = request.getParameter("id");
		String deleteAccount = request.getParameter("delete");
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.ADMIN_USERS_JSP);
		int userId = 0;
		UserDAO userDAO = new UserDAO();
		HttpSession session = request.getSession(false);
		boolean deleteUser = false;

		if (session == null || session.getAttribute(Constants.SESSION_ADMIN) == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		try {
			userId = Integer.parseInt(id == null || id.trim().isEmpty() ? "0" : id);
		} catch (NumberFormatException e) {
		}

		if (deleteAccount != null && deleteAccount.trim().equalsIgnoreCase("true")) {
			deleteUser = userDAO.deleteUser(userId);

			if (deleteUser == true) {
				Notification n = new Notification("Success", "User deleted successfully!", "success");
				request.setAttribute("notifications", n);
				List<User> listUsers = userDAO.getUserList();
				request.setAttribute("userList", listUsers);
				rd = request.getRequestDispatcher(AppPaths.ADMIN_USERS_JSP);
				rd.forward(request, response);
				return;
			} else {
				Notification e = new Notification("Error", "Failed to delete user!", "error");
				request.setAttribute("notifications", e);
			}
		}

		List<User> listUsers = userDAO.getUserList();
		request.setAttribute("userList", listUsers);

		if (userId > 0) {
			User userInfo = userDAO.getUserInfo(userId);

			if (userInfo != null) {
				request.setAttribute("userInfo", userInfo);
			}
		}

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
		String id = request.getParameter("id");
		String newPassword = request.getParameter("newPassword");
		String reEnterPassword = request.getParameter("retypePassword");
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.ADMIN_USERS_JSP);
		int userId = 0;
		boolean changePassword = false;
		UserDAO userDAO = new UserDAO();
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute(Constants.SESSION_ADMIN) == null) {
			rd = request.getRequestDispatcher(AppPaths.NOT_FOUND_JSP);
			rd.forward(request, response);
			return;
		}

		try {
			userId = Integer.parseInt(id == null || id.trim().isEmpty() ? "0" : id);
		} catch (NumberFormatException e) {
		}

		if (!newPassword.isEmpty()) {
			if (reEnterPassword.equals(newPassword)) {
				changePassword = userDAO.changePassword(userId, newPassword);
				if (changePassword == true) {
					Notification n = new Notification("Success", "Change password successfully!", "success");
					request.setAttribute("notifications", n);
				} else {
					Notification e = new Notification("Error", "Change password failed!", "error");
					request.setAttribute("notifications", e);
				}
			} else {
				Notification e = new Notification("Error", "Re-enter password must be match!", "error");
				request.setAttribute("notifications", e);
			}
		} else {
			Notification e = new Notification("Error", "New password cannot be left blank", "error");
			request.setAttribute("notifications", e);
		}

		List<User> listUsers = userDAO.getUserList();
		request.setAttribute("userList", listUsers);

		User userInfo = userDAO.getUserInfo(userId);
		request.setAttribute("userInfo", userInfo);

		rd.forward(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Admin Users";
	}// </editor-fold>

}
