<html>
<head>
    <title>Header</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
</head>
<body>

<c:set var="home" value="${pageContext.request.contextPath}/jsp/home.jsp"/>


<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                    <img src="${pageContext.request.contextPath}/images/logo_light.png" class="m-2" alt="Wacar logo"
                         width="100">
                </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item" id="nav-home">
                            <a class="nav-link" href="/wacar/">Home</a>
                        </li>
                        <li class="nav-item" id="nav-circuit-list">
                            <a class="nav-link" href="/wacar/circuit_list/">Circuit List</a>
                        </li>
                        <li class="nav-item" id="nav-car-list">
                            <a class="nav-link" href="/wacar/car_list/">Car List</a>
                        </li>
                        <c:choose>
                            <c:when test="${sessionScope.account.type eq 'USER'}">
                                <li class="nav-item" id="nav-orders">
                                    <a class="nav-link" href="/wacar/user/listOrdersByAccount">Orders</a>
                                </li>
                                <li class="nav-item" id="nav-favourites">
                                    <a class="nav-link" href="/wacar/user/list-favourite">Favourites</a>
                                </li>
                                <li class="nav-item" id="nav-new-order">
                                    <a class="nav-link" href="/wacar/user/create-order/cars">New Order</a>
                                </li>
                                <li class="nav-item" id="nav-account-user">
                                    <a class="nav-link" href="/wacar/user/user-info">Account</a>
                                </li>
                            </c:when>
                            <c:when test="${sessionScope.account.type eq 'ADMIN'}">
                                <li class="nav-item" id="nav-insert-car">
                                    <a class="nav-link" href="/wacar/admin/insertCar/">Insert Car</a>
                                </li>
                                <li class="nav-item" id="nav-insert-circuit">
                                    <a class="nav-link" href="/wacar/admin/insertCircuit/">Insert Circuit</a>
                                </li>
                                <li class="nav-item" id="nav-insert-mapping">
                                    <a class="nav-link" href="/wacar/admin/insertMapping/">Insert Mapping</a>
                                </li>
                                <li class="nav-item" id="nav-account-admin">
                                    <a class="nav-link" href="/wacar/admin/admin-info/">Account</a>
                                </li>
                            </c:when>
                        </c:choose>
                        <li class="nav-item">
                            <button id="logout" class="nav-link btn btn-link">Logout</button>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item" id="nav-home">
                            <a class="nav-link" href="/wacar/">Home</a>
                        </li>
                        <li class="nav-item" id="nav-circuit-list">
                            <a class="nav-link" href="/wacar/circuit_list/">Circuit List</a>
                        </li>
                        <li class="nav-item" id="nav-car-list">
                            <a class="nav-link" href="/wacar/car_list/">Car List</a>
                        </li>
                        <li class="nav-item" id="nav-signup">
                            <a class="nav-link" href="/wacar/signup/">Signup</a>
                        </li>
                        <li class="nav-item" id="nav-login">
                            <a class="nav-link" href="/wacar/login/">Login</a>
                        </li>
                        <li class="nav-item">
                            <i class="fa-solid fa-user"></i>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>


<script type="text/javascript" src="<c:url value="/js/logout.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/header.js"/>"></script>

</body>
</html>
