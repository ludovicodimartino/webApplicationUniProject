<!--
Author: Michele Scapinello (michele.scapinello@studenti.unipd.it)
Version: 1.0
Since: 1.0
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="bg-dark text-white text-center text-lg-start " id="footer">
    <!-- Grid container -->
    <div class="footerContainer">
        <div class="row">
            <div class="footerColumn">
                <h5 class="text-uppercase">WACAR</h5>
                <p> WaCar offers you a platform to book an experience with your favourite car, on your favourite track! What are you waiting for?!! </p>
            </div>

            <div class="footerColumn">
                <div class="footerNav">
                    <h5 class="text-uppercase">Our site</h5>
                    <ul class="list-unstyled">
                        <li>
                            <a href="<c:url value="/"/>" class="text-white">Login</a>
                        </li>
                        <li>
                            <a href="<c:url value="/"/>" class="text-white">Sign Up</a>
                        </li>
                    </ul>
                </div>
            </div>

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
