package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;

import com.reservation.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

public class AvailableRoomsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String startDate = request.getParameter("start_date");
        String endDate = request.getParameter("end_date");

        List<Room> rooms = null;
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            rooms = RoomRepository.getAvailableRooms(startDate, endDate);
        }

        request.setAttribute("rooms", rooms);
        request.getRequestDispatcher("/available-rooms.jsp").forward(request, response);
    }
}
