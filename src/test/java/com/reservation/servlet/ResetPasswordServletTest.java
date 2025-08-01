package com.reservation.servlet;

import com.reservation.db.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class ResetPasswordServletTest {

    private ResetPasswordServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        servlet = new ResetPasswordServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
    }

    @Test
    void testUserNotRegistered() throws IOException {
        try (MockedStatic<UserRepository> mockedRepo = Mockito.mockStatic(UserRepository.class)) {
            when(request.getParameter("email")).thenReturn("unknown@example.com");
            when(request.getContextPath()).thenReturn("/app");
            mockedRepo.when(() -> UserRepository.userExists("unknown@example.com")).thenReturn(false);

            servlet.doPost(request, response);

            verify(response).sendRedirect("/app/reset_password.html?error=not_registered");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testUserExists() throws IOException {
        try (MockedStatic<UserRepository> mockedRepo = Mockito.mockStatic(UserRepository.class)) {
            when(request.getParameter("email")).thenReturn("test@example.com");
            when(request.getContextPath()).thenReturn("/app");
            when(request.getSession()).thenReturn(session);
            mockedRepo.when(() -> UserRepository.userExists("test@example.com")).thenReturn(true);

            servlet.doPost(request, response);

            verify(session).setAttribute("reset_email", "test@example.com");
            verify(response).sendRedirect("/app/set_new_password.html");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
