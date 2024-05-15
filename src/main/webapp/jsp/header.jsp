<html>
<head>
    <title>Header</title>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Welcome to WaCar!</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item">
                            <form method="POST" action="/wacar/logout/">
                                <button class="nav-link">Logout</button>
                            </form>
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

</body>
</html>
