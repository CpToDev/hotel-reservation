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
                <th>Booking ID</th>
                <th>Room No</th>
                <th>Check-in Date</th>
                <th>Check-out Date</th>
                <th>Days</th>
                <th>Name</th>
                <th>Status</th>
                <th>Cancel Fee</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <% for (Booking b : bookings) { %>
                <tr>
                    <td><%= b.getBookingId() %></td>
                    <td><%= b.getRoomId() %></td>
                    <td><%= b.getCheckinDate() %></td>
                    <td><%= b.getCheckoutDate() %></td>
                    <td><%= b.getStayDays() %></td>
                    <td><%= b.getName() %></td>
                    <td><%= b.isCancelled() ? "Cancelled" : "Confirmed" %></td>
                    <td>₹ <%= b.getCancellationFee() %></td>
                    <td>
                        <% if (!b.isCancelled()) { %>
                            <button class="btn btn-danger btn-sm"
                                    data-bs-toggle="modal"
                                    data-bs-target="#cancelModal"
                                    data-booking-id="<%= b.getId() %>"
                                    data-checkin-date="<%= b.getCheckinDate() %>">
                                Cancel
                            </button>
                        <% } else { %>
                            <button class="btn btn-secondary btn-sm" disabled>Cancelled</button>
                        <% } %>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>


<!-- Cancel Modal -->
<div class="modal fade" id="cancelModal" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <form method="post" action="cancel-booking">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="cancelModalLabel">Cancel Booking?</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to cancel this booking?
                    <input type="hidden" name="booking_id" id="cancelBookingId">
                    <input type="hidden" name="checkin_date" id="cancelCheckinDate">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">No</button>
                    <button type="submit" class="btn btn-danger">Yes, Cancel</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const cancelModal = document.getElementById('cancelModal');
    cancelModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        const bookingId = button.getAttribute('data-booking-id');
        const checkinDate = button.getAttribute('data-checkin-date');
        document.getElementById('cancelBookingId').value = bookingId;
        document.getElementById('cancelCheckinDate').value = checkinDate;
    });
</script>
</body>
</html>