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
      @keyframes show {
        100% {
          opacity: 1;
          transform: none;
        }
      }

      .container {
        background-color: red;
        border-radius: 1rem;
        box-shadow: 0px 0px 10px darkred;
        margin-top: 2rem;
        padding: 1rem;

        opacity: 0;
        transform: rotateX(-90deg);
        transform-origin: top center;

        animation: show 600ms 100ms cubic-bezier(0.38, 0.97, 0.56, 0.76) forwards;
      }

      .container h2 {
        margin-left: 2rem;
      }

      .row {
        padding-left: 0rem;
        padding-right: 0rem;

        opacity: 0;
        transform: rotateX(-90deg);
        transform-origin: top center;

        animation: show 600ms 100ms cubic-bezier(0.38, 0.97, 0.56, 0.76) forwards;
      }

      .row .card {
        border-radius: 10px;
        height: 21rem;
        width: 22rem;
        background-color: lightgrey;
        display: flex;
        flex-direction: column;
        margin: auto;
        padding-right: 0%;
        padding-left: 0%;
      }

      .row .card .card-img-top {
        height: 60%;
        width: 100%;
        object-position: center;
        border-radius: 10px;
        position: relative;
      }

      .row .card .card-body {
        height: 40%;
        padding-top: 1rem;
        padding-bottom: 1rem;
      }

      .row .card .card-body .card-text {
        margin-top: 0;
        margin-bottom: 0.5rem;
      }

      .formComplete {
        margin-left: 2rem;
      }
		</style>

      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
	</head>

    <body>
        <h1>Create a new order</h1>
        <hr>
        <c:if test="${not empty carsList}">
          <div class="container">         
            <h2>Select a car</h2>
            <div class="row row-cols-3">
              <c:forEach var="car" items="${carsList}">
                <div class="card">
                    <img class="card-img-top" src="<c:url value="/loadCarImage"><c:param name="brand" value="${car.brand}"/><c:param name="model" value="${car.model}"/></c:url>"/>
                    <div class="card-body">
                      <h5 class="card-title"><c:out value="${car.brand} ${car.model}"/></h5>
                      <p class="card-text"><c:out value="${car.type}"/></p>
                      <button class="carBtn btn btn-primary" type="submit" id="getCircuitByCarTypeButton" carBrand="${car.brand}" carModel="${car.model}" carType="${car.type}">Select</a>
                    </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </c:if>

        <div class="container" id="circuits">
          <h2>Select a circuit</h2>
        </div>
        <!-- <div id="addFavourite"></div> -->
        <div class="container" id="completeOrder"></div>

        <script type="text/javascript" src="<c:url value="/js/ajax_create_order.js"/>"></script>
    </body>
</html>