<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="comp9321.dao.*, java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style.css" media="screen" />
<title>Add Room To Booking</title>
</head>
<body>
<jsp:include page="/logout.jsp"/>
	<c:choose>
		<c:when test="${selectedbooking eq null}">
			Error: The booking no longer exists.
		</c:when>
		<c:otherwise>

			<div id=bookingdetails>
				<table>
					<tr>
						<td>Select the rooms available for the booking: <br>
							Booking Reference: ${selectedbooking.id} <br> Name:
							${selectedbooking.fname} ${selectedbooking.lname} <br> Date
							${selectedbooking.checkin} to ${selectedbooking.checkout} <br>
							Branch: ${selectedbooking.city}
						</td>
					</tr>
				</table>
			</div>
			
			Assigned Rooms:
			<table>
				<tr>
					<th>Room Id</th>
					<th>Room Type</th>
					<th>Hotel Branch</th>
					<th></th>

					<c:forEach items="${RoomsAssigned}" var="b">
						<tr>
							<td>${b.id}</td>
							<td>${b.roomTypeName}</td>
							<td>${b.hotelBranch}</td>
							<td>
								<form action=staff method="GET">
									<input type=hidden name='action' value="checkoutroom">
									<input type=hidden name='selectedbookingid'
										value="${selectedbooking.id}"> <input type=hidden
										name='checkoutroomid' value="${b.id}"> <input
										type="submit" class="editbutton" value="Check out">
								</form>
							</td>

						</tr>
					</c:forEach>
				</tr>
			</table>
			<br>

			<c:choose>
				<c:when test="${allroomsassigned eq true}">
					<form action=staff method="GET">
						<input type='hidden' name='action' value="backtobookings">
						All rooms have been assigned <input type="submit"
							class="editbutton" value="Back to Booking">
					</form>
				</c:when>
				<c:when test="${noroomsavailable eq true}">
				No rooms available<br>

				</c:when>
				<c:otherwise>
			Add Rooms: 
			<table>
						<tr>
							<th>Room Id</th>
							<th>Room Type</th>
							<th>Hotel Branch</th>
							<th></th>
						</tr>

						<c:forEach items="${SelectedAvailableRooms}" var="b">
							<tr>
								<td>${b.id}</td>
								<td>${b.roomTypeName}</td>
								<td>${b.hotelBranch}</td>
								<td>
									<form action=staff method="GET">
										<input type=hidden name='action' value="selectroom"> <input
											type=hidden name='selectedbookingid'
											value="${selectedbooking.id}"> <input type=hidden
											name='selectedroom' value="${b.id}"> <input
											type="submit" class="editbutton" value="Select Room">
									</form>
								</td>

							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>

		</c:otherwise>
	</c:choose>

	

</body>
</html>