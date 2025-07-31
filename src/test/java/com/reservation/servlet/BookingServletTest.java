package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.User;
import com.reservation.model.Booking;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class BookingServletTest {

    @InjectMocks
    private BookingServlet servlet;

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;

    private MockedStatic<BookingRepository> bookingRepoMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        // Create a static mock of BookingRepository
        bookingRepoMock = mockStatic(BookingRepository.class);
    }

    @AfterEach
    void tearDown() {
        // Close the static mock after each test
        bookingRepoMock.close();
    }

    @Test
    void testRedirectToLoginWhenUserNotLoggedIn() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    void testInvalidDateRedirects() throws Exception {
        User user = new User("test@example.com", "token");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("room_id")).thenReturn("101");
        when(request.getParameter("checkin_date")).thenReturn(LocalDate.now().minusDays(1).toString());

        servlet.doPost(request, response);

        verify(response).sendRedirect("rooms?error=invalid_date");
    }

    @Test
    void testValidBookingRedirectsSuccess() throws Exception {
        User user = new User("test@example.com", "token");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("room_id")).thenReturn("101");
        when(request.getParameter("checkin_date")).thenReturn(LocalDate.now().plusDays(1).toString());
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("dob")).thenReturn("1990-01-01");
        when(request.getParameter("aadhar")).thenReturn("123456789012");
        when(request.getParameter("address")).thenReturn("Delhi");
        when(request.getParameter("mobile")).thenReturn("9999999999");

        // Mock static saveBooking
        bookingRepoMock.when(() -> BookingRepository.saveBooking(any(Booking.class))).thenAnswer(invocation -> null);

        servlet.doPost(request, response);

        // Verify redirect after booking
        verify(response).sendRedirect("rooms?success=booked");
        // Verify saveBooking was called once
        bookingRepoMock.verify(() -> BookingRepository.saveBooking(any(Booking.class)), times(1));
    }
}
