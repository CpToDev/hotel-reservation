<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.reservation.model.Room" %>
<%
    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
%>
<%
    String success = request.getParameter("success");
%>

<% if ("booked".equals(success)) { %>
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        Room booked successfully!
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<% } %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Available Rooms</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <h2 class="text-center mb-4">Search Available Rooms</h2>

    <form action="search-available-rooms" method="get" class="row g-3 mb-4">
        <div class="col-md-4">
            <label class="form-label">Check-in Date</label>
            <input type="text" id="start_date" name="start_date" class="form-control" required>
            <div class="form-text">Must be at least 14 days from today.</div>
        </div>

        <div class="col-md-4">
            <label class="form-label">Check-out Date</label>
            <input type="text" id="end_date" name="end_date" class="form-control" required>
            <div class="form-text">Must be after Check-in date.</div>
        </div>

        <div class="col-md-4 d-flex align-items-end">
            <button type="submit" class="btn btn-primary w-100">Search Available Rooms</button>
        </div>
    </form>

    <h3 class="text-center mb-4">Available Rooms</h3>
    <div class="row">
        <% if (rooms != null) {
            for (Room room : rooms) { %>
            <div class="col-md-4">
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <h5 class="card-title">Room No: <%= room.getId() %></h5>
                        <h6 class="card-subtitle mb-2 text-muted">Type: <%= room.getType() %></h6>
                        <p class="card-text">Price: ₹<%= room.getPrice() %> / night</p>
                        <p class="card-text">Facilities: <%= room.getFacility() %></p>
                        <a href="book-room?id=<%= room.getId() %>&checkin_date=<%= request.getParameter("start_date") %>&checkout_date=<%= request.getParameter("end_date") %>" class="btn btn-success">Book Now</a>
                    </div>
                </div>
            </div>
        <% }
        } %>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

<script>
    const checkInCalendar = flatpickr("#start_date", {
        minDate: new Date().fp_incr(14),
        dateFormat: "Y-m-d",
        onChange: function(selectedDates, dateStr, instance) {
            checkOutCalendar.set("minDate", dateStr);
        }
    });

    const checkOutCalendar = flatpickr("#end_date", {
        minDate: new Date().fp_incr(14),
        dateFormat: "Y-m-d",
    });
</script>

</body>
</html>
