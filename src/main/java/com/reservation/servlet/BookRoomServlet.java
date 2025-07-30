package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;

import com.reservation.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class BookRoomServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.html");
            return;
        }

        String roomIdStr = request.getParameter("id");
    String checkinDate = request.getParameter("checkin_date");
    String checkoutDate = request.getParameter("checkout_date");
        int roomId = Integer.parseInt(roomIdStr);
        Room room = RoomRepository.getRoomById(roomId);

        request.setAttribute("room", room);
        request.setAttribute("checkin_date", checkinDate);
        request.setAttribute("checkout_date", checkoutDate);

        request.getRequestDispatcher("/book_room_form.jsp").forward(request, response);
    }
}
