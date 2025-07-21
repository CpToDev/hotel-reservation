package com.reservation.db;

import com.reservation.model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/db/hotel.db";

    public static List<Room> getAvailableRooms(String startDate, String endDate) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms r " +
                     "WHERE r.id NOT IN (SELECT room_id FROM bookings " +
                     "WHERE cancelled = 0 AND (date(checkin_date) <= date(?) AND date(checkout_date) >= date(?)))";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, endDate);
            stmt.setString(2, startDate);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setId(rs.getInt("id"));
                    room.setType(rs.getString("type"));
                    room.setPrice(rs.getDouble("price"));
                    room.setFacility(rs.getString("facility"));
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static Room getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setId(rs.getInt("id"));
                    room.setType(rs.getString("type"));
                    room.setPrice(rs.getDouble("price"));
                    room.setFacility(rs.getString("facility"));
                    return room;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
