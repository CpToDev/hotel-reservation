package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

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

        String hashedPassword = PasswordUtil.hashPassword(newPassword);
        UserRepository.updatePassword(email, hashedPassword);

        session.removeAttribute("reset_email");
        response.sendRedirect(request.getContextPath() + "/login.html?success=password_reset");
    }
}
