package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;
import com.reservation.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class SearchAvailableRoomsServletTest {

    @Test
    public void testDoGet_withLoggedInUserAndDates() throws Exception {
        // Arrange
        SearchAvailableRoomsServlet servlet = new SearchAvailableRoomsServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        User user = new User("test@example.com", "hashedpass");

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("start_date")).thenReturn("2025-08-01");
        when(request.getParameter("end_date")).thenReturn("2025-08-05");
        when(request.getRequestDispatcher("/available-rooms.jsp")).thenReturn(dispatcher);

        List<Room> mockRooms = Arrays.asList(new Room(1, "Deluxe", 100.0,"f1"), new Room(2, "Suite", 150.0,"f2"));

        try (MockedStatic<RoomRepository> roomRepoMock = mockStatic(RoomRepository.class)) {
            roomRepoMock.when(() -> RoomRepository.getAvailableRooms("2025-08-01", "2025-08-05"))
                        .thenReturn(mockRooms);

            // Act
            servlet.doGet(request, response);

            // Assert
            verify(request).setAttribute("rooms", mockRooms);
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    public void testDoGet_userNotLoggedIn_redirectsToLogin() throws IOException, ServletException, ServletException {
        SearchAvailableRoomsServlet servlet = new SearchAvailableRoomsServlet();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }
}
