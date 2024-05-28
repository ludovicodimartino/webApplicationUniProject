<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>List of Favorites</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        .modal-dialog {
            max-width: 600px;
        }
    </style>
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
</head>
<body>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<%@ include file="header.jsp" %>
<%@ include file="toolbar.jsp" %>


<%-- Display any messages --%>
<div class="container">
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger">
                ${sessionScope.errorMessage.message}
        </div>
        <c:remove var="sessionScope.errorMessage" />
    </c:if>

    <h2>List of Favorites</h2>

<%-- Display the list of favorites --%>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>Circuit</th>
            <th>Car Brand</th>
            <th>Car Model</th>
            <th>Account</th>
            <th>Created At</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${not empty favouritesList}">
        <c:forEach var="favorite" items="${favouritesList}">
            <tr>
                <td><c:out value="${favorite.circuit}"/></td>
                <td><c:out value="${favorite.carBrand}"/></td>
                <td><c:out value="${favorite.carModel}"/></td>
                <td><c:out value="${favorite.account}"/></td>
                <td><c:out value="${favorite.createdAt}"/></td>
                <td>
                    <button class="btn btn-outline-danger btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModal"
                            data-circuit="${favorite.circuit}" data-carbrand="${favorite.carBrand}" data-carmodel="${favorite.carModel}">Delete</button>
                </td>
            </tr>
        </c:forEach>
        </c:if>
        </tbody>
        </thead>
    </table>
</div>
<!-- Modal for DELETE -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">Deleting selected Favourite</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="deleteForm" action="/wacar/user/deleteFavourite" method="post">
                    <input type="hidden" name="circuitName" id="circuitName" value="" />
                    <input type="hidden" name="carBrand" id="carBrand" value="" />
                    <input type="hidden" name="carModel" id="carModel" value="" />
                    <p>Are you sure you want to delete this favorite?</p>
                    <p><strong>Circuit:</strong> <span id="modal-circuit"></span></p>
                    <p><strong>Car Brand:</strong> <span id="modal-carBrand"></span></p>
                    <p><strong>Car Model:</strong> <span id="modal-carModel"></span></p>
                    <button type="submit" class="btn btn-danger">Confirm</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script>
    var deleteModal = document.getElementById('deleteModal');
    deleteModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget; // Button that triggered the modal
        var circuit = button.getAttribute('data-circuit');
        var carBrand = button.getAttribute('data-carbrand');
        var carModel = button.getAttribute('data-carmodel');

        var modalTitle = deleteModal.querySelector('.modal-title');
        var modalCircuit = deleteModal.querySelector('#modal-circuit');
        var modalCarBrand = deleteModal.querySelector('#modal-carBrand');
        var modalCarModel = deleteModal.querySelector('#modal-carModel');

        modalTitle.textContent = 'Deleting Favourite';
        modalCircuit.textContent = circuit;
        modalCarBrand.textContent = carBrand;
        modalCarModel.textContent = carModel;

        var circuitNameInput = deleteModal.querySelector('#circuitName');
        var carBrandInput = deleteModal.querySelector('#carBrand');
        var carModelInput = deleteModal.querySelector('#carModel');

        circuitNameInput.value = circuit;
        carBrandInput.value = carBrand;
        carModelInput.value = carModel;
    });
</script>

</body>
</html>