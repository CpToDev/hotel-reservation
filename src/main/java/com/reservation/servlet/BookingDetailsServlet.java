package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class BookingDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String bookingId = request.getParameter("booking_id");

        if (bookingId == null || bookingId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing booking ID");
            return;
        }

        Booking booking = BookingRepository.getBookingByBookingId(bookingId);

        if (booking == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
            return;
        }

        request.setAttribute("booking", booking);
        request.getRequestDispatcher("/bookingDetails.jsp").forward(request, response);
    }
}
