package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = UserRepository.getUserByEmail(email);

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.html?error=not_registered");
            return;
        }

        String hashed = PasswordUtil.hashPassword(password);
        if (!user.getPasswordHash().equals(hashed)) {
            response.sendRedirect(request.getContextPath() + "/login.html?error=invalid_credentials");
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/dashboard.html");
    }
}
