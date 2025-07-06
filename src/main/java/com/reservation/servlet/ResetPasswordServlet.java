package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

public class ResetPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        Map<String, User> users = UserRepository.loadUsers();

        if (!users.containsKey(email)) {
            response.sendRedirect(request.getContextPath() + "/reset_password.html?error=not_registered");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("reset_email", email);
            response.sendRedirect(request.getContextPath() + "/set_new_password.html");
        }
    }
}
