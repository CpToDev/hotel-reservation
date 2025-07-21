package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class SearchAvailableRoomsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");

        List<Room> availableRooms = RoomRepository.getAvailableRooms(startDate, endDate);

        request.setAttribute("rooms", availableRooms);
        request.getRequestDispatcher("/available-rooms.jsp").forward(request, response);
    }
}
