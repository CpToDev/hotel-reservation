package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class BookingDetailsServletTest {

    private BookingDetailsServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setup() {
        servlet = new BookingDetailsServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void testRedirectToLoginIfUserNotLoggedIn() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    public void testBadRequestIfBookingIdMissing() throws Exception {
        when(session.getAttribute("user")).thenReturn(new User("user@gmail.com","12349rfkfsosklfkf"));
        when(request.getParameter("booking_id")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing booking ID");
    }

    @Test
    public void testBookingNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(new User("user@gmail.com","12349rfkfsosklfkf"));
        when(request.getParameter("booking_id")).thenReturn("INVALID_ID");

        // Stub static method
        mockStatic(BookingRepository.class).when(() ->
            BookingRepository.getBookingByBookingId("INVALID_ID")
        ).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
    }

}
