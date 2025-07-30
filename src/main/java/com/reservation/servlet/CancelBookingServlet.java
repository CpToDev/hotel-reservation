package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CancelBookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        int bookingId = Integer.parseInt(request.getParameter("booking_id"));
        String checkinDateStr = request.getParameter("checkin_date"); // e.g., 2024-07-30

        // Parse check-in date, assume time is 12:00 AM
        LocalDate checkinDate = LocalDate.parse(checkinDateStr);
        LocalDateTime checkinDateTime = checkinDate.atStartOfDay(); // 00:00

        // Capture cancellation time
        LocalDateTime cancellationTime = LocalDateTime.now();

        // Calculate difference in hours
        long hoursLeft = Duration.between(cancellationTime, checkinDateTime).toHours();

        // Calculate cancellation fee
        double fee = 0;
        if (hoursLeft < 48) {
            fee = (48 - hoursLeft) * 10; // ₹10 per missing hour
        }

        // Call repository method
        BookingRepository.cancelBooking(bookingId, Timestamp.valueOf(cancellationTime), fee);

        // Load updated bookings
        List<Booking> bookings = BookingRepository.getBookingsByUser(user.getEmail());
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/bookings.jsp").forward(request, response);
    }
}
