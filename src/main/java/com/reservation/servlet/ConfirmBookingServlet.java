package com.reservation.servlet;

import com.reservation.db.BookingRepository;
import com.reservation.db.RoomRepository;
import com.reservation.model.Booking;
import com.reservation.model.Room;
import com.reservation.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ConfirmBookingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        try {
            int roomId = Integer.parseInt(request.getParameter("room_id"));
            String checkinDateStr = request.getParameter("checkin_date");
            String checkoutDateStr = request.getParameter("checkout_date");

            String name = request.getParameter("name");
            String dob = request.getParameter("dob");
            String aadhar = request.getParameter("aadhar");
            String address = request.getParameter("address");
            String mobile = request.getParameter("mobile");

            Room room = RoomRepository.getRoomById(roomId);
            if (room == null) {
                response.sendRedirect("book-room?id=" + roomId + "&error=room_not_available");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkinDate = LocalDate.parse(checkinDateStr, formatter);
            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, formatter);
            LocalDate today = LocalDate.now();

            // Validate check-in date must be at least 15 days from today
            if (!checkinDate.isAfter(today.plusDays(14))) {
                response.sendRedirect("book-room?id=" + roomId + "&error=invalid_date");
                return;
            }

            // Validate stay days
            long stayDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
            if (stayDays <= 0) {
                response.sendRedirect("book-room?id=" + roomId + "&error=invalid_date");
                return;
            }

            // Create booking object and save
            Booking booking = new Booking();
            booking.setUserEmail(user.getEmail());
            booking.setRoomId(roomId);
            booking.setRoomType(room.getType());
            booking.setCheckinDate(checkinDateStr);
            booking.setCheckoutDate(checkoutDateStr);
            booking.setStayDays((int) stayDays);
            booking.setName(name);
            booking.setDob(dob);
            booking.setAadhar(aadhar);
            booking.setAddress(address);
            booking.setMobile(mobile);

            BookingRepository.saveBooking(booking);


            response.sendRedirect("rooms?success=booked");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("rooms?error=unknown");
        }
    }
}
