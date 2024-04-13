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
		    .row{
		        display: flex;
		    }

            .card{
              border-radius: 10px;
              height: 300px;
              width: 400px;
              background-color: lightgrey;
              padding: 15px;
              display: inline-block;
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
        <c:if test="${not empty carsList}">
            <h1>Select a car</h1>
            <div class="row">
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
            </div>
        </c:if>
        <c:if test="${not empty circuitsList}">
            <h1>Select a circuit</h1>
            <div class="row">
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
            </div>
        </c:if>

        <div>
            <c:if test="${not empty carBrand && not empty carModel && not empty circuitName}">
                <h1>Save as favourite</h1>
                <form method="GET" action="/wacar/user/complete-order/">
                    <input type="hidden" name="carBrand" value="${carBrand}">
                    <input type="hidden" name="carModel" value="${carModel}">
                    <input type="hidden" name="circuitName" value="${circuitName}">
                    <label>You do not know when you can race? save the car and the circuit!</label>
                    <button type="submit">Save as favourite</button><br/>
                </form>
                <h1>Complete your order</h1>
                <form method="GET" action="/wacar/user/create-order/recap">
                    <label for="date">Select a date:</label>
                    <input type="date" name="date" min="2018-01-01" /></br>
                    <label for="nLaps">Select the number of laps:</label>
                    <input type="number" name="nLaps" min="1"></br>
                    <button type="submit">Create order</button><br/>
                    <input type="hidden" name="carBrand" value="${carBrand}">
                    <input type="hidden" name="carModel" value="${carModel}">
                    <input type="hidden" name="circuitName" value="${circuitName}">
                    <input type="hidden" name="lapPrice" value="${lapPrice}">
                </form>
            </c:if>
        </div>
    </body>
</html>