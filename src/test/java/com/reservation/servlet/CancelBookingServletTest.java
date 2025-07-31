package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;

class CancelBookingServletTest {

    private CancelBookingServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setup() {
        servlet = new CancelBookingServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testDoPost_WhenUserIsLoggedIn_AndValidBooking() throws Exception {
        // Mock user session
        User user = new User("john@example.com", "password");
        when(session.getAttribute("user")).thenReturn(user);

        // Mock request parameters
        when(request.getParameter("booking_id")).thenReturn("101");
        when(request.getParameter("checkin_date")).thenReturn(LocalDate.now().plusDays(1).toString());

        when(request.getRequestDispatcher("/bookings.jsp")).thenReturn(dispatcher);

        // Mock BookingRepository static calls
        try (MockedStatic<BookingRepository> bookingRepo = mockStatic(BookingRepository.class)) {
            bookingRepo.when(() -> BookingRepository.cancelBooking(anyInt(), any(), anyDouble()))
                    .then(invocation -> null);

            bookingRepo.when(() -> BookingRepository.getBookingsByUser("john@example.com"))
                    .thenReturn(Collections.emptyList());

            servlet.doPost(request, response);

            // Verify the static method was called
            bookingRepo.verify(() -> BookingRepository.cancelBooking(eq(101), any(), anyDouble()));
            bookingRepo.verify(() -> BookingRepository.getBookingsByUser("john@example.com"));

            // Ensure forward to bookings.jsp
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void testDoPost_WhenUserNotLoggedIn() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect(contains("login.html"));
    }
}
