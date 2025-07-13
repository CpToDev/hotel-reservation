package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.model.Booking;
import com.reservation.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

public class BookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        int roomId = Integer.parseInt(request.getParameter("room_id"));
        String checkinDateStr = request.getParameter("checkin_date");
        String name = request.getParameter("name");
        String dob = request.getParameter("dob");
        String aadhar = request.getParameter("aadhar");
        String address = request.getParameter("address");
        String mobile = request.getParameter("mobile");

        LocalDate checkinDate = LocalDate.parse(checkinDateStr);
        LocalDate today = LocalDate.now();
        if (checkinDate.isBefore(today) || checkinDate.isAfter(today.plusDays(14))) {
            response.sendRedirect("rooms?error=invalid_date");
            return;
        }

        Booking booking = new Booking();
        booking.setUserEmail(user.getEmail());
        booking.setRoomId(roomId);
        booking.setCheckinDate(checkinDateStr);
        booking.setName(name);
        booking.setDob(dob);
        booking.setAadhar(aadhar);
        booking.setAddress(address);
        booking.setMobile(mobile);

        BookingRepository.saveBooking(booking);

        response.sendRedirect("rooms?success=booked");
    }
}
