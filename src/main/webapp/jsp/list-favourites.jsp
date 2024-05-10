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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<%@ include file="toolbar.jsp" %>


<%-- Display any messages --%>
<c:if test="${not empty message}">
    <p><c:out value="${message.getMessage()}"/></p>
</c:if>

<div class="container">
    <h2>List of Favorites</h2>

<%-- Display the list of favorites --%>
<c:if test="${empty favouritesList}">
    <p>No favorites found.</p>
</c:if>
<c:if test="${not empty favouritesList}">
    <table class="table table-striped">
        <tbody>
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
                    <button class ="btn btn-primary btn-sm" onclick="confirmDelete('${favorite.circuit}', '${favorite.carBrand}', '${favorite.carModel}')">Delete</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

</div>

<%-- Hidden form for delete action --%>
    <form id="deleteForm" action="/wacar/user/deleteFavourite" method="post" style="display: none;">
    <input type="hidden" name="circuitName" id="circuitName" value="" />
    <input type="hidden" name="carBrand" id="carBrand" value="" />
    <input type="hidden" name="carModel" id="carModel" value="" />
</form>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>