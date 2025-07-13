<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.reservation.model.Room" %>
<%
    Room room = (Room) request.getAttribute("room");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book Room</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2 class="mb-4 text-center">Book Room: Room No <%= room.getId() %> - <%= room.getType() %></h2>

    <% if (error != null) { %>
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <%= error.equals("invalid_date") ? "Check-in date must be at least 15 days from today." :
                 error.equals("room_not_available") ? "Selected room is not available." :
                 "Something went wrong. Please try again." %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <form action="confirm-booking" method="post">
        <input type="hidden" name="room_id" value="<%= room.getId() %>">

        <div class="mb-3">
            <label for="checkin_date" class="form-label">Check-in Date</label>
            <input type="date" class="form-control" name="checkin_date" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Name</label>
            <input type="text" class="form-control" name="name" required>
        </div>
        <div class="mb-3">
            <label class="form-label">DOB</label>
            <input type="date" class="form-control" name="dob" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Aadhar</label>
            <input type="text" class="form-control" name="aadhar" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Address</label>
            <textarea class="form-control" name="address" required></textarea>
        </div>
        <div class="mb-3">
            <label class="form-label">Mobile</label>
            <input type="text" class="form-control" name="mobile" required>
        </div>

        <button type="submit" class="btn btn-primary">Confirm Booking</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
