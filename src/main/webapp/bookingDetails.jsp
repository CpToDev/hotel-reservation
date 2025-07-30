<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Booking Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 20px;
        }
        .booking-container {
            border: 1px solid #ccc;
            border-radius: 10px;
            padding: 15px;
            width: 500px;
        }
        .booking-container h2 {
            margin-top: 0;
        }
        .booking-container p {
            margin: 6px 0;
        }
        .cancelled {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="booking-container">
    <h2>Booking Details</h2>

    <p><strong>Booking ID:</strong> ${booking.bookingId}</p>
    <p><strong>User Email:</strong> ${booking.userEmail}</p>
    <p><strong>Room ID:</strong> ${booking.roomId}</p>
    <p><strong>Check-in:</strong> ${booking.checkinDate}</p>
    <p><strong>Check-out:</strong> ${booking.checkoutDate}</p>

    <c:choose>
        <c:when test="${booking.cancelled}">
            <p class="cancelled">Cancelled</p>
            <p><strong>Cancellation Time:</strong> ${booking.cancellationTime}</p>
            <p><strong>Cancellation Fee:</strong> ₹${booking.cancellationFee}</p>
        </c:when>
        <c:otherwise>
            <p><strong>Status:</strong> Active</p>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
