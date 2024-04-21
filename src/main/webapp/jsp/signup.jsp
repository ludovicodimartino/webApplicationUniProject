<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Registration</title>
</head>
<body>
<h1>REGISTRATION</h1>
<hr/>
<c:choose>
    <c:when test="${not empty message.error}">
        <div class="msgmargin">
            <div class="alert alert-danger" role="alert">
                <span>${message.message}</span>
            </div>
        </div>
    </c:when>
    <c:otherwise></c:otherwise>
</c:choose>
<form method="POST" action="/wacar/signup/">
    <label for="email">Enter your email:</label>
    <input name="email" id="email" type="email" required placeholder="Insert here your email"/><br/><br/><br/>
    <label for="name">Enter your name:</label>
    <input name="name" id="name" type="text" required placeholder="Insert here your name"/><br/><br/><br/>
    <label for="surname">Enter your surname:</label>
    <input name="surname" id="surname" type="text" required placeholder="Insert here your surname"/><br/><br/><br/>
    <label for="address">Enter your address:</label>
    <input name="address" id="address" type="text" required placeholder="Insert here your address"/><br/><br/><br/>

    <label for="password">Enter your password:</label>
    <input name="password" id="password" type="password" oninput="this.setCustomValidity('')"
           aria-describedby="pswhelp"
           pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
           oninvalid="this.setCustomValidity('Must contain at least one number and one uppercase ' +
            'and lowercase letter, and at least 8 or more characters')" required
           placeholder="Insert here your password"/><br/><br/><br/>

    <button type="submit">Submit</button><br/><br/><br/>
    <button type="reset">Reset</button>
</form>
</body>
</html>
