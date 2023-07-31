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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
public class EditAvatarServlet extends HttpServlet {

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
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.EDIT_AVATAR_JSP);
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

		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.EDIT_AVATAR_JSP);
		String avatar = request.getParameter("avatar");
		UserDAO userDao = new UserDAO();
		User user = (User) request.getSession(false).getAttribute(Constants.SESSION_USER);

		if (avatar == null || avatar.trim().isEmpty()) {
			String defaultAvatar = "https://api.dicebear.com/5.x/adventurer/svg?seed=" + user.getName();
			userDao.updateUserAvatar(user.getUserId(), defaultAvatar);
			user.setAvatar(defaultAvatar); // update the current user session with updated avatar

			request.setAttribute("notifications", new Notification("Success", "Using default avatar.", "success"));
			rd.forward(request, response);
			return;
		}

		String regex = "^(http|https)://[a-zA-Z0-9\\-\\.]+(\\.[a-zA-Z]{2,})(:[0-9]+)?(/[\\w\\-./?%&=]*)?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(avatar);

		if (matcher.matches()) {
			// The avatar is a valid URL
			boolean success = userDao.updateUserAvatar(user.getUserId(), avatar);
			if (success) {
				user.setAvatar(avatar); // update the current user session with updated avatar
				request.setAttribute("notifications", new Notification("Success", "Update avatar successfully.", "success"));
			} else {
				request.setAttribute("notifications", new Notification("Error", "Something went wrong. Please try again", "error"));
			}
		} else {
			// The avatar is not a valid URL
			EditUserErrors errObj = new EditUserErrors();
			errObj.setAvatarErr("Invalid url pattern! Please follow the instruction.");
			errObj.setAvatar(avatar);
			request.setAttribute("ERRORS", errObj);
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
		return "Short description";
	}// </editor-fold>

}
