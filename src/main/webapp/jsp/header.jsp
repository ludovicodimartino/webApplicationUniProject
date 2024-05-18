<html>
<head>
    <title>Header</title>
</head>
<body>

<c:set var="home" value="${pageContext.request.contextPath}/jsp/home.jsp" />


<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <c:choose>
            <c:when test="${pageContext.request.requestURI eq home}">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/home">Welcome to
                    <img src="${pageContext.request.contextPath}/images/logo_light.png" class="m-2" alt="Wacar logo" width="100">
                </a>
            </c:when>
            <c:otherwise>
                <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                    <img src="${pageContext.request.contextPath}/images/logo_light.png" class="m-2" alt="Wacar logo" width="100">
                </a>
            </c:otherwise>
        </c:choose>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item">
                            <button id="logout" class="nav-link">Logout</button>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="/wacar/signup/">Signup</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/wacar/login/">Login</a>
                        </li>
                        <i class="fa-solid fa-user"></i>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>


<script type="text/javascript" src="<c:url value="/js/logout.js"/>"></script>

</body>
</html>
