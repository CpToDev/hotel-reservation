package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.db.RoomRepository;
import com.reservation.model.Booking;
import com.reservation.model.Room;
import com.reservation.model.User;

import jakarta.servlet.http.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

class ConfirmBookingServletTest {

    @Test
    void testDoPost_successfulBooking() throws Exception {
        // Setup servlet and mocks
        ConfirmBookingServlet servlet = new ConfirmBookingServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        // Prepare dates
        LocalDate checkinDate = LocalDate.now().plusDays(15);  // valid (15 days from today)
        LocalDate checkoutDate = checkinDate.plusDays(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Prepare room
        Room mockRoom = new Room();
        mockRoom.setId(101);
        mockRoom.setType("AC");
        mockRoom.setPrice(1000);

        // Prepare user
        User mockUser = new User("test@example.com", "testpassword");

        // Mock session and request parameters
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getParameter("room_id")).thenReturn("101");
        when(request.getParameter("checkin_date")).thenReturn(checkinDate.format(formatter));
        when(request.getParameter("checkout_date")).thenReturn(checkoutDate.format(formatter));
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("dob")).thenReturn("1990-01-01");
        when(request.getParameter("aadhar")).thenReturn("123412341234");
        when(request.getParameter("address")).thenReturn("123 Street, City");
        when(request.getParameter("mobile")).thenReturn("9876543210");

        try (
            MockedStatic<RoomRepository> roomRepoMock = mockStatic(RoomRepository.class);
            MockedStatic<BookingRepository> bookingRepoMock = mockStatic(BookingRepository.class)
        ) {
            // Static method mocking
            roomRepoMock.when(() -> RoomRepository.getRoomById(101)).thenReturn(mockRoom);

            // no-op for saving
            bookingRepoMock.when(() -> BookingRepository.saveBooking(any(Booking.class))).thenAnswer(invocation -> null);

            // Execute
            servlet.doPost(request, response);

            // Verify redirection
            verify(response).sendRedirect("rooms?success=booked");
        }
    }
}
