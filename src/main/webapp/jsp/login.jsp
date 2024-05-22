<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/form.css">


    <style>

    </style>
</head>
<body>
<div class="container">
    <div class="form-container">
        <h1>LOGIN</h1>
        <div id="errorAlert" class="alert alert-danger d-none" role="alert">
            <span id="errorAlertText"></span>
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text"><i class="fa-solid fa-envelope"></i></span>
            <input name="email" id="email" type="email" class="form-control" required placeholder="Enter your email">
        </div>
        <div class="input-group mb-3">
            <span class="input-group-text"><i class="fa-solid fa-lock"></i></span>
            <input name="password" id="password" type="password" class="form-control" oninput="this.setCustomValidity('')"
                    aria-describedby="pswhelp"
                    pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                    oninvalid="this.setCustomValidity('Must contain at least one number and one uppercase ' +
                    'and lowercase letter, and at least 8 or more characters')" required
                    placeholder="Enter your password">
        </div>
        <button id="login" type="submit" class="btn btn-success">Submit</button>
        <button id="reset" type="reset" class="btn btn-secondary">Reset</button>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value="/js/login.js"/>"></script>
</body>
</html>

