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
      <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create-order.css">
      <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
      <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
      <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
      <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"  integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	</head>

    <body>
      <%@ include file="header.jsp" %>
      <%@ include file="toolbar.jsp" %>

        <h1>Create a new order</h1>
        <hr>
        <c:if test="${not empty carsList}">
          <div class="container">
            <h2>Select a car</h2>
            <div class="row row-cols-3">
              <c:forEach var="car" items="${carsList}">
                <!-- <div class="col"> -->
                  <div class="cardBtn card" id="getCircuitByCarTypeButton" type="submit" carBrand="${car.brand}" carModel="${car.model}" carType="${car.type}">
                      <img class="card-img-top" src="<c:url value="/loadCarImage"><c:param name="brand" value="${car.brand}"/><c:param name="model" value="${car.model}"/></c:url>"/>
                      <div class="card-body">
                        <p class="h5"><c:out value="${car.brand} ${car.model}"/></p>
                        <p class="h6"><c:out value="${car.type}"/></p>
                      </div>
                  </div>
                <!-- </div> -->
              </c:forEach>
            </div>
          </div>
        </c:if>

        <div class="container hidden" id="circuits"></div>
        <div class="container hidden" id="completeOrder"></div>

        <!-- Create Order Modal -->
        <div class="modal fade" id="orderModal" tabindex="-1" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">Create the order</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <div class="row">
                  <div class="col md-6" id="recapOrderCarImage">
                  </div>
                  <div class="col md-6" id="recapOrderCircuitImage">
                  </div>
                </div>
                <div class="row mb-4">
                  <div class="col md-6" id="carLabelRecapOrder">
                  </div>
                  <div class="col md-6" id="circuitLabelRecapOrder">
                  </div>
                </div>
                <div class="row">
                  <div class="col-1">
                    <span data-bs-toggle="tooltip" data-bs-title="Date" class="material-symbols-outlined">event</span>
                  </div>
                  <div class="col md-9" id="orderRecapDate">
                  </div>
                </div>
                <div class="row">
                  <div class="col-1">
                    <span data-bs-toggle="tooltip" data-bs-title="Laps number" class="material-symbols-outlined">laps</span>
                  </div>
                  <div class="col" id="orderRecapLapNo">
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <div class="flex-grow-1 lead">
                    TOTAL <b id="orderRecapPrice"></b><b>€</b>
                </div>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" id="createOrder" class="btn btn-primary" data-bs-target="#completeModal" data-bs-toggle="modal">Create order</button>
              </div>
            </div>
          </div>
        </div>
        <!-- Order Complete or refuse -->
        <div class="modal fade" id="completeModal" tabindex="-1" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
              <div class="modal-body">
                <div>
                  <div id="successAlert" class="alert alert-success d-flex align-items-center" role="alert">
                    <span class="material-symbols-outlined bi flex-shrink-0 me-2">check_circle</span>
                    <div>
                      An example success alert with an icon
                    </div>
                  </div>
                  <div id="errorAlert" class="alert alert-danger d-flex align-items-center" role="alert">
                    <span class="material-symbols-outlined bi flex-shrink-0 me-2">error</span>
                    <div>
                      An example danger alert with an icon
                    </div>
                  </div>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" id="returnHome" class="btn btn-primary">Return to home</button>
              </div>
            </div>
          </div>
        </div>



        <script type="text/javascript" src="<c:url value="/js/create_order.js"/>"></script>

    </body>
<c:import url="footer.jsp"/>
</html>