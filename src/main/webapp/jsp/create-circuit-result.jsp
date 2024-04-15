<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create Circuit</title>
</head>

<body>
<h1>Create Circuit</h1>
<hr/>

<!-- display the message -->
<c:import url="/jsp/include/show-message.jsp"/>

<!-- display the just created circuit, if any and no errors -->
<jsp:useBean id="message" scope="request" type="it.unipd.dei.webapp.wacar.resource.Message"/>
<c:if test="${not empty circuit && !message.error}">
    <ul>
        <li>name: <c:out value="${circuit.name}"/></li>
        <li>address: <c:out value="${circuit.address}"/></li>
        <li>type: <c:out value="${circuit.type}"/></li>
        <li>description: <c:out value="${circuit.description}"/></li>
        <li>length: <c:out value="${circuit.length}"/></li>
        <li>corners number: <c:out value="${circuit.cornersNumber}"/></li>
        <li>lap price: <c:out value="${circuit.lapPrice}"/></li>
        <li>available: <c:out value="${circuit.available}"/></li>
        <li>image:
            <ul>
                <li>MIME media type: <c:out value="${circuit.imageMediaType}"/> </li>
                <li>image: <br/>
                    <img
                            src="<c:url value="/loadCircuitImage"><c:param name="name" value="${circuit.name}"/></c:url>" alt="circuit image"/>
                </li>
            </ul>
        </li>

    </ul>
</c:if>
</body>
</html>