package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (!password.equals(confirmPassword)) {
            response.sendRedirect("register.html?error=password_mismatch");
            return;
        }

        if (UserRepository.userExists(email)) {
            response.sendRedirect("register.html?error=already_registered");
            return;
        }

        String hashed = PasswordUtil.hashPassword(password);
        UserRepository.saveUser(new User(email, hashed));

        response.sendRedirect("login.html?success=registered");
    }
}