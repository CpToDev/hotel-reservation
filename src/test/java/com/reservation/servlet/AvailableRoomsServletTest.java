package com.reservation.servlet;

import com.reservation.db.RoomRepository;
import com.reservation.model.Room;
import com.reservation.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class AvailableRoomsServletTest {

    @InjectMocks
    private AvailableRoomsServlet servlet;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRedirectsToLoginIfUserNull() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("login.html");
    }

    @Test
    void testDoesNotFetchRoomsIfDatesMissing() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User("user@gmail.com","skffororrorof"));
        when(request.getParameter("start_date")).thenReturn(null);
        when(request.getParameter("end_date")).thenReturn(null);
        when(request.getRequestDispatcher("/available-rooms.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("rooms", null);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testFetchesRoomsIfDatesPresent() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new User("user@gmail.com","skffororrorof"));
        when(request.getParameter("start_date")).thenReturn("2024-01-01");
        when(request.getParameter("end_date")).thenReturn("2024-01-03");

        mockStatic(RoomRepository.class);
        when(RoomRepository.getAvailableRooms("2024-01-01", "2024-01-03")).thenReturn(Collections.emptyList());
        when(request.getRequestDispatcher("/available-rooms.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("rooms", Collections.emptyList());
        verify(dispatcher).forward(request, response);
    }
}
