package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class BookingListServletTest {

    private BookingListServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setup() {
        servlet = new BookingListServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void testDoGet_userNotLoggedIn_redirectToLogin() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    void testDoGet_userLoggedIn_forwardsToBookingsPage() throws Exception {
        User mockUser = new User("test@example.com", "test12jdkfkdkskskdkfk");
        List<Booking> mockBookings = List.of(new Booking());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getRequestDispatcher("/bookings.jsp")).thenReturn(dispatcher);

        try (MockedStatic<BookingRepository> mockedRepo = mockStatic(BookingRepository.class)) {
            mockedRepo.when(() -> BookingRepository.getBookingsByUser("test@example.com"))
                      .thenReturn(mockBookings);

            servlet.doGet(request, response);

            verify(request).setAttribute("bookings", mockBookings);
            verify(dispatcher).forward(request, response);
        }
    }
}
