<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    <title>Toolbar</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty accountType}">
        <c:choose>
            <c:when test="${accountType eq 'USER'}">
                <div class="navbar">
                    <a href="/wacar/">Home</a>
                    <a href="/wacar/circuit_list/">Circuit List</a>
                    <a href="/wacar/car_list/">Car List</a>
                    <a href="/wacar/user/listOrdersByAccount">Orders</a>
                    <a href="/wacar/user/user-info">Account</a>
                </div>
            </c:when>
            <c:when test="${accountType eq 'ADMIN'}">
                <div class="navbar">
                    <a href="/wacar/">Home</a>
                    <a href="/wacar/circuit_list/">Circuit List</a>
                    <a href="/wacar/admin/insertCar/">Insert new Car</a>
                    <a href="/wacar/admin/insertCircuit/">Insert new Circuit</a>
                    <a href="/wacar/admin/insertMapping/">Insert new Mapping</a>
                    <a href="/wacar/admin/admin-info/">Account</a>
                </div>
            </c:when>
        </c:choose>
    </c:when>
    <c:when test="${empty accountType}">
        <div class="navbar">
            <a href="/wacar/">Home</a>
        </div>
    </c:when>
</c:choose>
</body>
</html>
