<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form.css">
</head>
<body>
<div class="container">
    <div class="form-container">
        <h1>REGISTRATION</h1>
        <c:choose>
            <c:when test="${not empty message.error}">
                <div class="alert alert-danger" role="alert">
                    <span>${message.message}</span>
                </div>
            </c:when>
        </c:choose>
        <!-- <form method="POST" action="/wacar/signup/"> -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
                <input name="email" id="email" type="email" class="form-control rounded-input" required placeholder="Enter your email">
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                <input name="name" id="name" type="text" class="form-control rounded-input" required placeholder="Enter your name">
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa-solid fa-user"></i></span>
                <input name="surname" id="surname" type="text" class="form-control rounded-input" required placeholder="Enter your surname">
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa-solid fa-house-chimney"></i></span>
                <input name="address" id="address" type="text" class="form-control rounded-input" required placeholder="Enter your address">
            </div>
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
                <input name="password" id="password" type="password" class="form-control rounded-input" oninput="this.setCustomValidity('')"
                       aria-describedby="pswhelp"
                       pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                       oninvalid="this.setCustomValidity('Must contain at least one number and one uppercase ' +
                        'and lowercase letter, and at least 8 or more characters')" required
                       placeholder="Enter your password">
            </div>
            <button id="signup" type="submit" class="btn btn-success">Submit</button>
            <button id="reset" type="reset" class="btn btn-secondary">Reset</button>
        <!-- </form> -->
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/js/signup.js"/>"></script>
</body>
</html>
