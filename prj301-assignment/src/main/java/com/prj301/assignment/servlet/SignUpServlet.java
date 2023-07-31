package com.prj301.assignment.servlet;

import com.prj301.assignment.DAO.UserDAO;
import com.prj301.assignment.javabean.Notification;
import com.prj301.assignment.javabean.SignUpErrors;
import com.prj301.assignment.utils.AppPaths;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author khoad
 */
public class SignUpServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.SIGNUP_JSP);
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String re_password = request.getParameter("re-enterpassword");

		SignUpErrors errObj = new SignUpErrors();

		// Validate email
		if (email == null || email.trim().isEmpty()) {
			errObj.setEmailErr("Email is required");
		} else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
			errObj.setEmailErr("Invalid email format");
		}
		errObj.setEmail(email); // save email for next try (user no need to retype it)

		// Validate name
		if (name == null || name.trim().isEmpty()) {
			errObj.setNameErr("Name is required");
		}
		errObj.setName(name);

		// Validate password
		if (password == null || password.trim().isEmpty()) {
			errObj.setPasswordErr("Password is required");
		} else if (password.length() < 6) {
			errObj.setPasswordErr("Password must be at least 6 characters long");
		}

		// Validate retype password
		if (re_password == null || re_password.trim().isEmpty()) {
			errObj.setRetypePasswordErr("Retype password is required");
		} else if (!re_password.equals(password)) {
			errObj.setRetypePasswordErr("Passwords do not match");
		}

		// If there is an error, forward error back to the page
		if (errObj.getEmailErr() != null
						|| errObj.getNameErr() != null
						|| errObj.getPasswordErr() != null
						|| errObj.getRetypePasswordErr() != null) {
			request.setAttribute("ERRORS", errObj);
			rd.forward(request, response);
			return;
		}

		UserDAO userdao = new UserDAO();

		if (!userdao.isUserExist(email)) {
			// User not exist -> allow signup
			boolean success = userdao.addUser(email, password, name);
			Notification n;

			if (success) {
				response.sendRedirect(request.getContextPath() + AppPaths.LOGIN + "?success=Sign up successfully");
				return;
			} else {
				n = new Notification("Error", "Something went wrong. Please try again", "error");
				request.setAttribute("notifications", n);
			}
		} else {
			// User exist -> denide signup request
			errObj.setEmailErr("User's email already exist !");
			request.setAttribute("ERRORS", errObj);
		}
		rd.forward(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(AppPaths.SIGNUP_JSP);
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Sign up";
	}// </editor-fold>

}
