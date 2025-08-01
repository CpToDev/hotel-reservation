package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;
import com.reservation.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class RoomListServletTest {

    private RoomListServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new RoomListServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    void testRedirectsToLoginWhenUserNotLoggedIn() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    void testDisplaysAvailableRoomsWithDateRange() throws Exception {
        User user = new User("test@example.com", "hashedPassword");
        List<Room> mockRooms = Arrays.asList(new Room(1, "101",10.0,"f1"), new Room(2, "102",10.0,"f2"));

        try (MockedStatic<RoomRepository> roomRepoMock = mockStatic(RoomRepository.class)) {
            when(request.getSession()).thenReturn(session);
            when(session.getAttribute("user")).thenReturn(user);

            when(request.getParameter("start_date")).thenReturn("2025-01-01");
            when(request.getParameter("end_date")).thenReturn("2025-01-03");

            roomRepoMock.when(() -> RoomRepository.getAvailableRooms("2025-01-01", "2025-01-03"))
                    .thenReturn(mockRooms);

            when(request.getRequestDispatcher("/available-rooms.jsp")).thenReturn(dispatcher);

            servlet.doGet(request, response);

            verify(request).setAttribute("rooms", mockRooms);
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void testDisplaysAvailableRoomsWhenDateNotProvided() throws Exception {
        User user = new User("test@example.com", "hashedPassword");
         List<Room> mockRooms = List.of(new Room(1, "101", 10.0, "f1"));

        try (MockedStatic<RoomRepository> roomRepoMock = mockStatic(RoomRepository.class)) {
            when(request.getSession()).thenReturn(session);
            when(session.getAttribute("user")).thenReturn(user);

            when(request.getParameter("start_date")).thenReturn(null);
            when(request.getParameter("end_date")).thenReturn(null);

            roomRepoMock.when(() -> RoomRepository.getAvailableRooms("1900-01-01", "2999-12-31"))
                    .thenReturn(mockRooms);

            when(request.getRequestDispatcher("/available-rooms.jsp")).thenReturn(dispatcher);

            servlet.doGet(request, response);

            verify(request).setAttribute("rooms", mockRooms);
            verify(dispatcher).forward(request, response);
        }
    }
}
