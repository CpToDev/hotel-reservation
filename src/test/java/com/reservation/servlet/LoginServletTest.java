package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.model.User;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LoginServletTest {

    private LoginServlet loginServlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Test
    void testUserNotRegistered() throws Exception {
        when(request.getParameter("email")).thenReturn("unknown@example.com");
        when(request.getContextPath()).thenReturn("/app");

        try (MockedStatic<UserRepository> mockedRepo = mockStatic(UserRepository.class)) {
            mockedRepo.when(() -> UserRepository.getUserByEmail("unknown@example.com"))
                      .thenReturn(null);

            loginServlet.doPost(request, response);

            verify(response).sendRedirect("/app/login.html?error=not_registered");
        }
    }

    @Test
    void testInvalidCredentials() throws Exception {
        String email = "test@example.com";
        String wrongPassword = "wrongpass";

        User user = new User(email, PasswordUtil.hashPassword("correctpass"));

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(wrongPassword);
        when(request.getContextPath()).thenReturn("/app");

        try (MockedStatic<UserRepository> mockedRepo = mockStatic(UserRepository.class)) {
            mockedRepo.when(() -> UserRepository.getUserByEmail(email)).thenReturn(user);

            loginServlet.doPost(request, response);

            verify(response).sendRedirect("/app/login.html?error=invalid_credentials");
        }
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        String email = "test@example.com";
        String password = "password123";
        String hashed = PasswordUtil.hashPassword(password);

        User user = new User(email, hashed);

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getContextPath()).thenReturn("/app");
        when(request.getSession()).thenReturn(session);

        try (MockedStatic<UserRepository> mockedRepo = mockStatic(UserRepository.class)) {
            mockedRepo.when(() -> UserRepository.getUserByEmail(email)).thenReturn(user);

            loginServlet.doPost(request, response);

            verify(session).setAttribute("user", user);
            verify(response).sendRedirect("/app/dashboard.html");
        }
    }
}
