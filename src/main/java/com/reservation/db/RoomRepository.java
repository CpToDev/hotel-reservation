package com.reservation.db;

import com.reservation.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {

    private static final String DB_URL = "jdbc:sqlite:/Users/sauravagtl/Downloads/hotel-reservation/db/hotel.db";

    public static Room getRoomById(int roomId) {
        Room room = null;
        String query = "SELECT id, type, price, facility FROM rooms WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    room = new Room(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getDouble("price"),
                            rs.getString("facility"),
                            true // we assume available if fetching for booking
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return room;
    }

    public static List<Room> getAllAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT id, type, price, facility FROM rooms WHERE available = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("facility"),
                        true
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    public static void updateRoomAvailability(int roomId, boolean available) {
    String query = "UPDATE rooms SET available = ? WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, available ? 1 : 0);
        pstmt.setInt(2, roomId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}