<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.reservation.model.Room" %>
<%
    List<Room> rooms = (List<Room>) request.getAttribute("rooms");
    String success = request.getParameter("success");
    String startDateParam = request.getParameter("start_date");
    String endDateParam = request.getParameter("end_date");
%>

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

    <% if ("booked".equals(success)) { %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Room booked successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <h2 class="text-center mb-4">Search Available Rooms</h2>

    <!-- Alert container -->
    <div id="dateAlert" class="alert alert-danger d-none" role="alert">
        Checkout date must be at least 1 day after check-in date.
    </div>

    <!-- Search Form -->
    <form id="searchForm" action="search-available-rooms" method="get" class="row g-3 mb-4">
        <div class="col-md-4">
            <label class="form-label">Check-in Date</label>
            <input type="text" id="start_date" name="start_date" value="<%= startDateParam != null ? startDateParam : "" %>" class="form-control" required>
            <div class="form-text">Must be at least 14 days from today.</div>
        </div>

        <div class="col-md-4">
            <label class="form-label">Check-out Date</label>
            <input type="text" id="end_date" name="end_date" value="<%= endDateParam != null ? endDateParam : "" %>" class="form-control" required>
            <div class="form-text">Must be after Check-in date.</div>
        </div>

        <div class="col-md-4 d-flex align-items-end">
            <button type="submit" class="btn btn-primary w-100">Search Available Rooms</button>
        </div>
    </form>

    <h3 class="text-center mb-4">Available Rooms</h3>
    <div class="row" id="roomContainer">
        <% if ((startDateParam == null || endDateParam == null) || (startDateParam.isEmpty() || endDateParam.isEmpty())) { %>
            <div class="col-12 text-center">
                <div id="emptyDateAlert" class="alert alert-warning d-none">Please select both Check-in and Check-out dates to view available rooms.</div>
            </div>
        <% } else if (rooms != null && !rooms.isEmpty()) {
            for (Room room : rooms) { %>
                <div class="col-md-4 room-card">
                    <div class="card mb-4 shadow-sm">
                        <div class="card-body">
                            <h5 class="card-title">Room No: <%= room.getId() %></h5>
                            <h6 class="card-subtitle mb-2 text-muted">Type: <%= room.getType() %></h6>
                            <p class="card-text">Price: ₹<%= room.getPrice() %> / night</p>
                            <p class="card-text">Facilities: <%= room.getFacility() %></p>
                            <a href="book-room?id=<%= room.getId() %>&checkin_date=<%= startDateParam %>&checkout_date=<%= endDateParam %>"
                               class="btn btn-success book-btn">Book Now</a>
                        </div>
                    </div>
                </div>
        <%  }
        } else if (startDateParam != null && endDateParam != null) { %>
            <div class="col-12 text-center">
                <div class="alert alert-warning">No rooms available for selected dates.</div>
            </div>
        <% } %>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

<script>
    const startDateInput = document.getElementById("start_date");
    const endDateInput = document.getElementById("end_date");
    const alertBox = document.getElementById("dateAlert");

    const checkInCalendar = flatpickr(startDateInput, {
        minDate: new Date().fp_incr(14),
        dateFormat: "Y-m-d",
        onChange: function (selectedDates) {
            if (selectedDates.length > 0) {
                checkOutCalendar.set("minDate", new Date(selectedDates[0].getTime() + 86400000));
            }
        }
    });

    const checkOutCalendar = flatpickr(endDateInput, {
        minDate: new Date().fp_incr(15),
        dateFormat: "Y-m-d",
    });

    function showAlert(message) {
        alertBox.textContent = message;
        alertBox.classList.remove("d-none");
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    startDateInput.addEventListener('input', () => alertBox.classList.add("d-none"));
    endDateInput.addEventListener('input', () => alertBox.classList.add("d-none"));

    // Booking validation
    document.querySelectorAll(".book-btn").forEach(btn => {
        btn.addEventListener("click", function (e) {
            const checkin = new Date(startDateInput.value);
            const checkout = new Date(endDateInput.value);

            if (!startDateInput.value || !endDateInput.value || checkout <= checkin) {
                e.preventDefault();
                showAlert("Checkout date must be at least 1 day after check-in date.");
            }
        });
    });
    startDateInput.addEventListener('input', () => {
    document.getElementById("emptyDateAlert").classList.add("d-none");
    });

    endDateInput.addEventListener('input', () => {
        document.getElementById("emptyDateAlert").classList.add("d-none");
    });
    document.getElementById("searchForm").addEventListener("submit", function (e) {
    const checkin = startDateInput.value.trim();
    const checkout = endDateInput.value.trim();
    const checkinDate = new Date(checkin);
    const checkoutDate = new Date(checkout);

    const emptyDateAlert = document.getElementById("emptyDateAlert");

    // If either date is missing
    if (!checkin || !checkout) {
        e.preventDefault();
        emptyDateAlert.classList.remove("d-none");
        window.scrollTo({ top: 0, behavior: "smooth" });
        return;
    }

    // If checkout is not after checkin
    if (checkoutDate <= checkinDate) {
        e.preventDefault();
        showAlert("Checkout date must be at least 1 day after check-in date.");
        return;
    }

    // Dates are valid, hide the alert if shown
    emptyDateAlert.classList.add("d-none");
});




    // Show available rooms manually if dates are missing
    document.addEventListener("DOMContentLoaded", () => {
        const showBtn = document.getElementById("showRoomsBtn");
        const roomCards = document.querySelectorAll(".room-card");

        if (showBtn) {
            roomCards.forEach(card => card.classList.add("d-none"));

            showBtn.addEventListener("click", () => {
                roomCards.forEach(card => card.classList.remove("d-none"));
                showBtn.style.display = "none";
            });
        }
    });
</script>

</body>
</html>
