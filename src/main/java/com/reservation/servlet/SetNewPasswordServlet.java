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

public class SetNewPasswordServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("reset_email");
        String newPassword = request.getParameter("new_password");

        if (email == null) {
            response.sendRedirect(request.getContextPath() + "/reset_password.html");
            return;
        }

        Map<String, User> users = UserRepository.loadUsers();
        User user = users.get(email);
        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        UserRepository.saveUsers(users);

        session.removeAttribute("reset_email");
        response.sendRedirect(request.getContextPath() + "/login.html?success=password_reset");
    }
}
