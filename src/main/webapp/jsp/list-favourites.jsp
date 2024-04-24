<%--
  Created by IntelliJ IDEA.
  User: aleleo
  Date: 23/04/24
  Time: 20:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>List of Favorites</title>
    <script>
        function confirmDelete(circuit, carBrand, carModel) {
            if (confirm("Are you sure you want to delete this favorite?")) {
                document.getElementById('circuitName').value = circuit;
                document.getElementById('carBrand').value = carBrand;
                document.getElementById('carModel').value = carModel;
                document.getElementById('deleteForm').submit();
            }
        }
    </script>
</head>
<body>
<h1>List of Favorites</h1>

<%-- Display any messages --%>
<c:if test="${not empty message}">
    <p><c:out value="${message.getMessage()}"/></p>
</c:if>

<%-- Display the list of favorites --%>
<p>
<c:if test="${empty favouritesList}">
    <p>No favorites found.</p>
</c:if>
<c:if test="${not empty favouritesList}">
    <table border="1">
        <tr>
            <th>Circuit</th>
            <th>Car Brand</th>
            <th>Car Model</th>
            <th>Account</th>
            <th>Created At</th>
            <th>Action</th>
        </tr>
        <c:forEach var="favorite" items="${favouritesList}">
            <tr>
                <td><c:out value="${favorite.circuit}"/></td>
                <td><c:out value="${favorite.carBrand}"/></td>
                <td><c:out value="${favorite.carModel}"/></td>
                <td><c:out value="${favorite.account}"/></td>
                <td><c:out value="${favorite.createdAt}"/></td>
                <td>
                    <button onclick="confirmDelete('${favorite.circuit}', '${favorite.carBrand}', '${favorite.carModel}')">Delete</button>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<%-- Hidden form for delete action --%>
    <form id="deleteForm" action="/wacar/user/deleteFavourite" method="post" style="display: none;">
    <input type="hidden" name="circuitName" id="circuitName" value="" />
    <input type="hidden" name="carBrand" id="carBrand" value="" />
    <input type="hidden" name="carModel" id="carModel" value="" />
</form>
</body>
</html>