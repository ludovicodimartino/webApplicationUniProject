<!--
    Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
    Version: 1.00
    Since: 1.00
-->

<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create Car</title>
</head>

<body>
<h1>Create Car</h1>
<hr/>

<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<!-- display the just created car, if any and no errors -->
<jsp:useBean id="message" scope="request" type="it.unipd.dei.webapp.wacar.resource.Message"/>
<c:if test="${not empty car && !message.error}">
    <ul>
        <li>brand: <c:out value="${car.brand}"/></li>
        <li>model: <c:out value="${car.model}"/></li>
        <li>description: <c:out value="${car.description}"/></li>
        <li>type: <c:out value="${car.type}"/></li>
        <li>available: <c:out value="${car.available}"/></li>
        <li>horsepower: <c:out value="${car.horsepower}"/> cv</li>
        <li>maxSpeed: <c:out value="${car.maxSpeed}"/> km/h</li>
        <li>0-100: <c:out value="${car.acceleration}"/> s</li>

                <li>image:
                    <ul>
                        <li>MIME media type: <c:out value="${car.imageMediaType}"/> </li>
                        <li>image: <br/>
                            <img
                                    src="<c:url value="/loadCarImage"><c:param name="model" value="${car.model}"/><c:param name="brand" value="${car.brand}"/></c:url>" alt="car image"/>
                        </li>
                    </ul>
                </li>

    </ul>
</c:if>
</body>
</html>