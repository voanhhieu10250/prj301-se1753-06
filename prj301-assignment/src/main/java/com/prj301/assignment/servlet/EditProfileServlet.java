/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.UserDAO;
import com.prj301.assignment.javabean.EditUserErrors;
import com.prj301.assignment.javabean.Notification;
import com.prj301.assignment.model.User;
import com.prj301.assignment.utils.AppPaths;
import com.prj301.assignment.utils.Constants;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@MultipartConfig
public class EditProfileServlet extends HttpServlet {

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
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.EDIT_USER_JSP);
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// retrieve updated user information from request parameters
		String name = request.getParameter("name");
		String password = request.getParameter("newPassword");
		String re_password = request.getParameter("retypePassword");

		// retrieve user object from session
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(Constants.SESSION_USER);
		EditUserErrors errObj = new EditUserErrors();
		RequestDispatcher rd = null;
		Notification n = null;

		// Validate name
		if (name == null || name.trim().isEmpty()) {
			errObj.setNameErr("Name is required");
		}
		errObj.setName(name);

		if (password != null && !password.trim().isEmpty()) {
			if (password.length() < 6) {
				errObj.setPasswordErr("Password must be at least 6 characters long");
			}

			// Validate retype password
			if (re_password == null || re_password.trim().isEmpty()) {
				errObj.setRetypePasswordErr("Retype password is required");
			} else if (!re_password.equals(password)) {
				errObj.setRetypePasswordErr("Passwords do not match");
			}
		}

		// If there is an error, forward error back to the page
		if (errObj.getNameErr() != null
						|| errObj.getPasswordErr() != null
						|| errObj.getRetypePasswordErr() != null) {
			request.setAttribute("ERRORS", errObj);
			rd = request.getRequestDispatcher(AppPaths.EDIT_USER_JSP);
			rd.forward(request, response);
			return;
		}

		if (user != null) {
			// update user in database
			UserDAO userDAO = new UserDAO();
			boolean success = userDAO.updateUser(
							name,
							password != null && !password.trim().isEmpty() ? password : user.getPassword(),
							user
			);

			if (success) {
				// update succeed

				// update session data
				String pw = password != null && !password.trim().isEmpty() ? password : user.getPassword();
				user.setName(name);
				user.setPassword(pw);

				n = new Notification("Success", "Update successfully", "success");
				request.setAttribute("notifications", n);
				rd = request.getRequestDispatcher(AppPaths.EDIT_USER_JSP);
				rd.forward(request, response);
			} else {
				// update failed
				n = new Notification("Error", "Something went wrong. Please try again", "error");
				request.setAttribute("notifications", n);
				rd = request.getRequestDispatcher(AppPaths.EDIT_USER_JSP);
				rd.forward(request, response);
			}
		} else {
			// handle user not found in session
			n = new Notification("Error", "User is not authorized", "error");
			request.setAttribute("notifications", n);
			rd = request.getRequestDispatcher(AppPaths.EDIT_USER_JSP);
			rd.forward(request, response);
		}
	}

	@Override
	public String getServletInfo() {
		return "Edit profile";
	}// </editor-fold>

}
