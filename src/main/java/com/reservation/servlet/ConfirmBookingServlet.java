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
            String name = request.getParameter("name");
            String dob = request.getParameter("dob");
            String aadhar = request.getParameter("aadhar");
            String address = request.getParameter("address");
            String mobile = request.getParameter("mobile");

            Room room = RoomRepository.getRoomById(roomId);
            if (room == null || !room.isAvailable()) {
                response.sendRedirect("book-room?id=" + roomId + "&error=room_not_available");
                return;
            }

            LocalDate checkinDate = LocalDate.parse(checkinDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate today = LocalDate.now();
            if (!checkinDate.isAfter(today.plusDays(14))) {
                response.sendRedirect("book-room?id=" + roomId + "&error=invalid_date");
                return;
            }

            Booking booking = new Booking();
            booking.setUserEmail(user.getEmail());
            booking.setRoomId(roomId);
            booking.setRoomType(room.getType());
            booking.setCheckinDate(checkinDateStr);
            booking.setName(name);
            booking.setDob(dob);
            booking.setAadhar(aadhar);
            booking.setAddress(address);
            booking.setMobile(mobile);

            BookingRepository.saveBooking(booking);
            RoomRepository.updateRoomAvailability(roomId, false); // mark as unavailable

            response.sendRedirect("rooms?success=booked");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("rooms?error=unknown");
        }
    }
}
