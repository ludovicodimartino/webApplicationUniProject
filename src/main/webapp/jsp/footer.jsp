<!--
Author: Michele Scapinello (michele.scapinello@studenti.unipd.it)
Version: 1.0
Since: 1.0
-->
<html>
<head>
    <title>Header</title>
</head>
<body>

<c:set var="home" value="${pageContext.request.contextPath}/jsp/home.jsp" />

<footer class="bg-dark text-white text-center text-lg-start " id="footer">
    <!-- Grid container -->
    <div class="footerContainer">
        <div class="row">
            <div class="footerColumn">
                <h5 class="text-uppercase">WACAR</h5>
                <p> WaCar offers you a platform to book an experience with your favourite car, on your favourite track! What are you waiting for?!! </p>
            </div>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <div class="footerColumn">
                        <div class="footerNav">
                            <h5 class="text-uppercase">Our site</h5>
                            <ul class="list-unstyled">
                                <li>
                                    <button id="footer-logout" class="nav-link">Logout</button>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="footerColumn">
                        <div class="footerNav">
                            <h5 class="text-uppercase">Our site</h5>
                            <ul class="list-unstyled">
                                <li>
                                    <a class="nav-link" href="/wacar/signup/">Signup</a>
                                </li>
                                <li>
                                    <a class="nav-link" href="/wacar/login/">Login</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="footerColumn">
                <h5 class="text-uppercase mb-0">Contacts</h5>
                <address>
                    info.wacar@gmail.com
                </address>
            </div>
        </div>
    </div>

    <div class="text-center p-3" style="background-color: rgba(0, 0, 0, 0.2)">
        © 2024 Copyright: WaCar
    </div>
    <!-- Copyright -->
</footer>

<script type="text/javascript" src="<c:url value="/js/footer-logout.js"/>"></script>

</body>
</html>