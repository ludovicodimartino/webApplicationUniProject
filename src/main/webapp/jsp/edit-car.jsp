<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="en">
<jsp:useBean id="car" scope="request" type="it.unipd.dei.webapp.wacar.resource.Car"/>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Car ${car.brand} ${car.model}</title>
</head>
<body>
<h1>Edit Car ${car.brand} ${car.model}</h1>
<form enctype="multipart/form-data" action="${pageContext.request.contextPath}/admin/editCar/" method="POST">
    <input type="hidden" id="brand" name="brand" value="${car.brand}" >
    <input type="hidden" id="model" name="model" value="${car.model}" hidden>
    <label for="description">Description:</label><br>
    <textarea id="description" name="description" required><c:out value="${car.description}"/></textarea><br><br>

    <label for="maxSpeed">Max Speed:</label><br>
    <input type="number" id="maxSpeed" name="maxSpeed" value="${car.maxSpeed}" required><br><br>

    <label for="horsepower">Horsepower:</label><br>
    <input type="number" id="horsepower" name="horsepower" value="${car.horsepower}" required><br><br>

    <label for="acceleration">0-100 (in seconds):</label><br>
    <input type="number" id="acceleration" name="acceleration" step=".01" value="${car.acceleration}" required><br><br>

    <label for="availability">Availability:</label><br>
    <select id="availability" name="availability" required>
        <c:choose>
            <c:when test="${car.available}">
                <option value="true" selected="selected">True</option>
                <option value="false">False</option>
            </c:when>
            <c:otherwise>
                <option value="true">True</option>
                <option value="false" selected="selected">False</option>
            </c:otherwise>
        </c:choose>
    </select><br><br>

    <label for="image">Choose the new car image (blank for not updating the image):</label><br>
    <input type="file" id="image" name="image" accept="image/png, image/jpeg"/><br><br>


    <label for="type">Type:</label><br>

    <%--    Display the car types--%>
    <jsp:useBean id="carTypeList" scope="request" type="java.util.List"/>
    <c:choose>
        <c:when test="${empty carTypeList}">
            <p>You don't have car types in the database. Please, create one <a
                    href="${pageContext.request.contextPath}/admin/insertCarType">here</a>.</p>
            <input type="submit" value="Submit" disabled>
        </c:when>
        <c:otherwise>
            <select id="type" name="type" required>
                <c:forEach items="${carTypeList}" var="carType" varStatus="loop">
                    <option value="${carType.name}"
                        <c:if test="${car.type == carType.name}">
                            selected="selected"
                        </c:if>
                        >
                    <c:out value="${carType.name}"/>
                    </option>
                </c:forEach>
            </select><br><br>
            <input type="submit" value="Submit">
        </c:otherwise>
    </c:choose>

</form>
</body>
</html>