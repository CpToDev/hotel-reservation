package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
        String checkinDateStr = request.getParameter("checkin_date");

        LocalDate checkinDate = LocalDate.parse(checkinDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate today = LocalDate.now();

        long hoursDiff = ChronoUnit.HOURS.between(today.atStartOfDay(), checkinDate.atStartOfDay());

        double fee = 0;
        if (hoursDiff < 48) {
            fee = (48 - hoursDiff) * 10;  // ₹10 per hour for late cancellation
        }

        BookingRepository.cancelBooking(bookingId, fee);

        List<Booking> bookings = BookingRepository.getBookingsByUser(user.getEmail());
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/bookings.jsp").forward(request, response);
    }
}
