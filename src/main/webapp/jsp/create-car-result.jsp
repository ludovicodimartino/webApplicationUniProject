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
<jsp:useBean id="car" scope="request" type="it.unipd.dei.webapp.wacar.resource.Car"/>
<c:if test="${not empty car && !message.error}">
    <ul>
        <li>brand: <c:out value="${car.brand}"/></li>
        <li>model: <c:out value="${car.model}"/></li>
        <li>description: <c:out value="${car.description}"/></li>


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