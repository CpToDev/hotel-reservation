package com.reservation.servlet;

import com.reservation.db.UserRepository;
import com.reservation.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.mockito.Mockito.*;

class SetNewPasswordServletTest {

    private SetNewPasswordServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        servlet = new SetNewPasswordServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testSetNewPasswordSuccessfully() throws IOException {
        String email = "test@example.com";
        String rawPassword = "newPassword";
        String hashedPassword = "hashedPassword123";

        when(session.getAttribute("reset_email")).thenReturn(email);
        when(request.getParameter("new_password")).thenReturn(rawPassword);

        // Mock PasswordUtil.hashPassword
        try (
            MockedStatic<PasswordUtil> mockedPasswordUtil = mockStatic(PasswordUtil.class);
            MockedStatic<UserRepository> mockedUserRepo = mockStatic(UserRepository.class)
        ) {
            mockedPasswordUtil.when(() -> PasswordUtil.hashPassword(rawPassword)).thenReturn(hashedPassword);

            servlet.doPost(request, response);

            mockedUserRepo.verify(() -> UserRepository.updatePassword(email, hashedPassword));
            verify(session).removeAttribute("reset_email");
            verify(response).sendRedirect(contains("/login.html?success=password_reset"));
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSetNewPasswordWithoutEmailInSession() throws IOException, ServletException {
        when(session.getAttribute("reset_email")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("/reset_password.html"));
        verify(session, never()).removeAttribute(any());
    }
}
