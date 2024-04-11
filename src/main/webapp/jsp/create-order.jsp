<!--
    Author: Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
    Version: 1.00
    Since: 1.00
-->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>

<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Create a new order</title>
		<style>
            .card{
              border-radius: 10px;
              height: 300px;
              width: 400px;
              background-color: lightgrey;
              padding: 15px;
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
            }

            .card img {
              height: 70%;
              width: 100%;
              object-fit: cover;
              object-position: center;
              border-radius: 10px;
              position: relative;
            }

            .card h1 {
              font-size: 22px;
              margin-top: 8px;
            }

            .card h5 {
              font-size: 18px;
              color: grey;
              margin-top: 10px;
              margin-bottom: 15px;
            }

            .card .btn {
                width: 100%;
                height: 10%;
                object-position: center;
                border-radius: 10px;
                background-color: white;
                text-decoration: none;
                display: flex;
                align-items: center;
                justify-content: center;
            }
		</style>
	</head>

    <body>
       <!-- display the message -->
        <div>
            <c:if test="${message.error}">
                <c:import url="/jsp/show-message.jsp"/>
            </c:if>
        </div>
        <div class="row">
            <c:if test="${not empty carsList}">
                <h1>Select a car</h1>
                <c:forEach var="car" items="${carsList}">
                    <div>
                        <div>
                            <div class="card">
                                <h1><c:out value="${car.brand} ${car.model}"/></h1>
                                <h5><c:out value="${car.type}"/></h5>
                                <a href="circuits/?carBrand=${car.brand}&carModel=${car.model}&carType=${car.type}" class="btn link" type="button">
                                    Select
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>
        <div class="row">
            <c:if test="${not empty circuitsList}">
                <h1>Select a circuit</h1>
                <c:forEach var="circuit" items="${circuitsList}">
                    <div>
                        <div>
                            <div class="card">
                                <h1><c:out value="${circuit.name}"/></h1>
                                <h5><c:out value="${circuit.type}"/></h5>
                                <a href="../complete-order/?carBrand=${carBrand}&carModel=${carModel}&circuitName=${circuit.name}&circuitLapPrice=${circuit.lapPrice}" class="btn link" type="button">
                                    Select
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </body>
</html>