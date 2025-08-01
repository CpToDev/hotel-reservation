package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;
import com.reservation.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class RegisterServletTest {

    @Test
    void testPasswordMismatch() throws IOException, ServletException {
        RegisterServlet servlet = new RegisterServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("pass1");
        when(request.getParameter("confirm_password")).thenReturn("pass2");

        servlet.doPost(request, response);

        verify(response).sendRedirect("register.html?error=password_mismatch");
    }

    @Test
    void testUserAlreadyRegistered() throws IOException {
        RegisterServlet servlet = new RegisterServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("email")).thenReturn("existing@example.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirm_password")).thenReturn("password123");

        try (MockedStatic<UserRepository> mockedRepo = mockStatic(UserRepository.class)) {
            mockedRepo.when(() -> UserRepository.userExists("existing@example.com")).thenReturn(true);

            servlet.doPost(request, response);

            verify(response).sendRedirect("register.html?error=already_registered");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
