package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Map<String, User> users = UserRepository.loadUsers();
        if (!users.containsKey(email)) {
            response.sendRedirect(request.getContextPath() + "/login.html?error=not_registered");
            return;
        }

        User user = users.get(email);
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
