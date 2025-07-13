<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.reservation.model.Booking" %>
<%
    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Your Bookings</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2 class="text-center mb-4">Your Bookings</h2>
    <div class="table-responsive">
        <table class="table table-bordered shadow">
            <thead class="table-primary">
            <tr>
                <th>Room No</th>
                <th>Room Type</th>
                <th>Check-in Date</th>
                <th>Name</th>
                <th>Aadhar</th>
                <th>Address</th>
                <th>Mobile</th>
            </tr>
            </thead>
            <tbody>
            <% for (Booking b : bookings) { %>
                <tr>
                    <td><%= b.getRoomId() %></td>
                    <td><%= b.getRoomType() %></td>
                    <td><%= b.getCheckinDate() %></td>
                    <td><%= b.getName() %></td>
                    <td><%= b.getAadhar() %></td>
                    <td><%= b.getAddress() %></td>
                    <td><%= b.getMobile() %></td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
