package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;
import com.reservation.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class BookRoomServletTest {

    private BookRoomServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new BookRoomServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getSession()).thenReturn(session);
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testDoGet_userNotLoggedIn_redirectsToLogin() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    void testDoGet_validUserAndRoom_forwardsToBookingPage() throws Exception {
        User user = new User("test@example.com", "dkfkfkf");

        Room mockRoom = new Room();
        mockRoom.setId(1);
        mockRoom.setType("AC");
        mockRoom.setPrice(1200);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("id")).thenReturn("1");
        when(request.getParameter("checkin_date")).thenReturn("2025-08-01");
        when(request.getParameter("checkout_date")).thenReturn("2025-08-03");
        when(request.getRequestDispatcher("/book_room_form.jsp")).thenReturn(dispatcher);

        try (MockedStatic<RoomRepository> roomRepoMock = mockStatic(RoomRepository.class)) {
            roomRepoMock.when(() -> RoomRepository.getRoomById(1)).thenReturn(mockRoom);

            servlet.doGet(request, response);

            verify(request).setAttribute("room", mockRoom);
            verify(request).setAttribute("checkin_date", "2025-08-01");
            verify(request).setAttribute("checkout_date", "2025-08-03");
            verify(dispatcher).forward(request, response);
        }
}
}
