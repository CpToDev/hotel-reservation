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
    <link href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2 class="mb-4 text-center">Book Room: Room No <%= room.getId() %> - <%= room.getType() %></h2>

    <% if (error != null) { %>
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <%= error.equals("invalid_date") ? "Check-in date must be at least 14 days from today." :
                 error.equals("room_not_available") ? "Selected room is not available." :
                 "Something went wrong. Please try again." %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <form action="confirm-booking" method="post">
        <input type="hidden" name="room_id" value="<%= room.getId() %>">

        <div class="mb-3">
            <label class="form-label">Check-in Date</label>
            <input type="text" id="checkin_date" class="form-control" name="checkin_date"
       value="<%= request.getAttribute("checkin_date") != null ? request.getAttribute("checkin_date") : "" %>" required>

        </div>

        <div class="mb-3">
            <label class="form-label">Check-out Date</label>
            <input type="text" id="checkout_date" class="form-control" name="checkout_date"
       value="<%= request.getAttribute("checkout_date") != null ? request.getAttribute("checkout_date") : "" %>" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Name</label>
            <input type="text" class="form-control" name="name" required>
        </div>

        <div class="mb-3">
            <label class="form-label">DOB</label>
            <input type="date" class="form-control" name="dob" max="<%= java.time.LocalDate.now().minusYears(18) %>" required>
            <div class="form-text">DOB must show you are at least 18 years old.</div>
        </div>

        <div class="mb-3">
            <label class="form-label">Aadhar</label>
            <input type="text" class="form-control" name="aadhar" pattern="[0-9]{12}" title="Aadhar must be 12 digits." required>
            <div class="form-text">Must be a valid 12-digit Aadhar number.</div>
        </div>

        <div class="mb-3">
            <label class="form-label">Address</label>
            <textarea class="form-control" name="address" required></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Mobile</label>
            <input type="text" class="form-control" name="mobile" pattern="[0-9]{10}" title="Mobile must be 10 digits." required>
            <div class="form-text">Must be a valid 10-digit phone number.</div>
        </div>

        <button type="submit" class="btn btn-primary">Confirm Booking</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

<script>
    const minCheckin = new Date();
    minCheckin.setDate(minCheckin.getDate() + 13);

    const checkinCalendar = flatpickr("#checkin_date", {
        minDate: minCheckin,
        dateFormat: "Y-m-d",
        onChange: function (selectedDates, dateStr, instance) {
            checkoutCalendar.set("minDate", dateStr);
        }
    });

    const checkoutCalendar = flatpickr("#checkout_date", {
        minDate: minCheckin,
        dateFormat: "Y-m-d",
    });
</script>
</body>
</html>
