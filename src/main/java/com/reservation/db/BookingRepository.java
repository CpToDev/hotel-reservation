package com.reservation.db;

import com.reservation.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingRepository {
    private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/hotel.db";

    // Save new booking
    public static void saveBooking(Booking booking) {
        String generatedBookingId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        booking.setBookingId(generatedBookingId);

        String insert = "INSERT INTO bookings (booking_id, user_email, room_id, checkin_date, checkout_date, stay_days, name, dob, aadhar, address, mobile) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insert)) {

            pstmt.setString(1, generatedBookingId);
            pstmt.setString(2, booking.getUserEmail());
            pstmt.setInt(3, booking.getRoomId());
            pstmt.setString(4, booking.getCheckinDate());
            pstmt.setString(5, booking.getCheckoutDate());
            pstmt.setInt(6, booking.getStayDays());
            pstmt.setString(7, booking.getName());
            pstmt.setString(8, booking.getDob());
            pstmt.setString(9, booking.getAadhar());
            pstmt.setString(10, booking.getAddress());
            pstmt.setString(11, booking.getMobile());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Cancel existing booking and apply cancellation fee
    public static void cancelBooking(int bookingId, double fee) {
        String update = "UPDATE bookings SET cancelled = 1, cancellation_fee = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(update)) {

            pstmt.setDouble(1, fee);
            pstmt.setInt(2, bookingId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Booking> getBookingsByUser(String email) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setBookingId(rs.getString("booking_id"));
                booking.setUserEmail(rs.getString("user_email"));
                booking.setRoomId(rs.getInt("room_id"));
                booking.setCheckinDate(rs.getString("checkin_date"));
                booking.setCheckoutDate(rs.getString("checkout_date"));
                booking.setStayDays(rs.getInt("stay_days"));
                booking.setName(rs.getString("name"));
                booking.setDob(rs.getString("dob"));
                booking.setAadhar(rs.getString("aadhar"));
                booking.setAddress(rs.getString("address"));
                booking.setMobile(rs.getString("mobile"));

                booking.setCancelled(rs.getInt("cancelled") == 1);
                booking.setCancellationFee(rs.getDouble("cancellation_fee"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

}
