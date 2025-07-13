package com.reservation.servlet;

import com.reservation.db.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (!UserRepository.userExists(email)) {
            response.sendRedirect(request.getContextPath() + "/reset_password.html?error=not_registered");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("reset_email", email);
            response.sendRedirect(request.getContextPath() + "/set_new_password.html");
        }
    }
}
