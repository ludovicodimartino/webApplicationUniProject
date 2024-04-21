<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Car Type</title>
</head>
<body>
<h1>Car Types</h1>
<hr/>
<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>
<h2>Add new car type</h2>
<form action="${pageContext.request.contextPath}/admin/insertCarType/" method="POST">
    <label for="name">Name:</label><br>
    <input type="text" id="name" name="name" required><br><br>
    <input type="submit" value="Add">
</form>
<br><br>

<%--Show tables of car types --%>
<h2>Car types</h2>

<jsp:useBean id="carTypesList" scope="request" type="java.util.List"/>
<c:choose>
    <c:when test="${empty carTypesList}">
        <p>You don't have car types at the moment.</p>
    </c:when>
    <c:otherwise>
        <table id="carTypesTable">
            <thead>
            <tr>
                <th>Car Type</th>
            </tr>
            </thead>
            <tbody id="carTypesTableBody">
            <c:forEach items="${carTypesList}" var="carType" varStatus="loop">
                <tr>
                    <td><c:out value="${carType.name}"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>