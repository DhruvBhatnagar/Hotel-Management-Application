<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="zc" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% 
session.setAttribute("CarryDTO", request.getAttribute("list"));
 %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Search Results</title>

	
</head>
<body>

<h1>List of Rooms Currently Available:</h1>
<form action="ControlServlet" method="post">

<table align="center" border="2">
	<tr>
		<th> Room Type </th>
		<th> No. of Rooms </th>
		<th> No. of Extra Beds </th>
		<th> Maximum Price Per Day </th>
		<th> Cost of Room for the Duration of Stay </th>
	</tr>
	
	<zc:forEach var="list" items="${list}">
	<tr>
	   	<td> 
    		${list.getType() }
    	</td>
    	<td> 
    		<select name="${list.getType() }" id="${list.getType() }">
    				<zc:forEach var="i" begin="0" end="${list.getMaximumRooms() }">
    					<option value="${i }"> ${i } </option>
    				</zc:forEach>
    		</select>  		
		</td>
		<td>
		   <zc:choose>
		      <zc:when test="${list.type eq 'single'}">
		          <select name="${list.type}_extra_beds" id="${list.type}_extra_beds" disabled>  
		      </zc:when>
		      <zc:otherwise>
			     <select name="${list.type}_extra_beds" id="${list.type}_extra_beds">		      
		      </zc:otherwise>
		   </zc:choose>
    				<zc:forEach var="i" begin="0" end="1">
    					<option value="${i }"> ${i } </option>
    				</zc:forEach>
    		</select>
    	</td>
    	<td>
    	<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${list.maxPrice}" />
    	</td>
    	<td>
    	<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${list.bookTotalPrice}" />
    	</td>
	</tr>
	</zc:forEach>
</table>
<br>
<div  style="color:red">
		${errors }
</div>
	<input type="submit" value="Proceed to checkout" style="height:20px;width:180px;font-size:85%"/>
	<input type="hidden" name="numRooms" value="${numRooms}"/>
	<input type="hidden" name="city" value="${city}"/>
	<input type="hidden" name="action" value="results"/>
</form>
<br>
<form action="ControlServlet" method="post">
    <input type="hidden" name="action" value="backToSearch"/>
    <input type="submit" value="Back To search" style="height:20px;width:180px;font-size:85%"/>    
</form>
</body>
</html>