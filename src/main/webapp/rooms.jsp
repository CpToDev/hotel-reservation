<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.reservation.model.Room" %>
<%
    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Available Rooms</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <h2 class="text-center mb-4">Available Rooms</h2>
    <div class="row">
        <% for (Room room : rooms) { %>
        <div class="col-md-4">
            <div class="card mb-4 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Room No: <%= room.getId() %></h5>
                    <h6 class="card-subtitle mb-2 text-muted">Type: <%= room.getType() %></h6>
                    <p class="card-text">Price: ₹<%= room.getPrice() %> / night</p>
                    <p class="card-text">Facilities: <%= room.getFacility() %></p>
                    <% if (room.isAvailable()) { %>
                        <a href="book-room?id=<%= room.getId() %>" class="btn btn-success">Book Now</a>
                    <% } else { %>
                        <button class="btn btn-secondary" disabled>Not Available</button>
                    <% } %>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>

</body>
</html>
