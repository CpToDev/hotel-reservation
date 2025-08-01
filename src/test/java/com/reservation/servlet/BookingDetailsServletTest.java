package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingDetailsServletTest {

    @InjectMocks
    private BookingDetailsServlet servlet;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;

    private static MockedStatic<BookingRepository> bookingMock;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingMock = mockStatic(BookingRepository.class);
    }

    @AfterEach
    void tearDown() {
        bookingMock.close();
    }

    @Test
    void testRedirectsToLoginIfUserNotInSession() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    void testReturnsErrorIfBookingIdMissing() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User("user@gmail.com","skffororrorof"));
        when(request.getParameter("booking_id")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing booking ID");
    }

    @Test
    void testForwardsToJspOnValidBooking() throws Exception {
        Booking mockBooking = new Booking();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User("user@gmail.com","skffororrorof"));
        when(request.getParameter("booking_id")).thenReturn("BK123");
        when(BookingRepository.getBookingByBookingId("BK123")).thenReturn(mockBooking);
        when(request.getRequestDispatcher("/bookingDetails.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("booking", mockBooking);
        verify(dispatcher).forward(request, response);
    }
}
