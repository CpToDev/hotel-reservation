package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

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

        Map<String, User> users = UserRepository.loadUsers();
        if (users.containsKey(email)) {
            response.sendRedirect(request.getContextPath() + "/register.html?error=already_registered");
            return;
        }

        String hashed = PasswordUtil.hashPassword(password);
        users.put(email, new User(email, hashed));
        UserRepository.saveUsers(users);

        response.sendRedirect(request.getContextPath() + "/login.html?success=registered");
    }
}
