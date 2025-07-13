package com.reservation.db;

import com.reservation.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/hotel.db";

    public static void saveBooking(Booking booking) {
        String insert = "INSERT INTO bookings (user_email, room_id, checkin_date, name, dob, aadhar, address, mobile) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insert)) {

            pstmt.setString(1, booking.getUserEmail());
            pstmt.setInt(2, booking.getRoomId());
            pstmt.setString(3, booking.getCheckinDate());
            pstmt.setString(4, booking.getName());
            pstmt.setString(5, booking.getDob());
            pstmt.setString(6, booking.getAadhar());
            pstmt.setString(7, booking.getAddress());
            pstmt.setString(8, booking.getMobile());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Booking> getBookingsByUser(String userEmail) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.id, b.room_id, r.type AS room_type, b.checkin_date, " +
                     "b.name, b.dob, b.aadhar, b.address, b.mobile " +
                     "FROM bookings b " +
                     "INNER JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.user_email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new Booking(
                        rs.getInt("id"),
                        userEmail,
                        rs.getInt("room_id"),
                        rs.getString("room_type"),
                        rs.getString("checkin_date"),
                        rs.getString("name"),
                        rs.getString("dob"),
                        rs.getString("aadhar"),
                        rs.getString("address"),
                        rs.getString("mobile") ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookings;
    }
}
