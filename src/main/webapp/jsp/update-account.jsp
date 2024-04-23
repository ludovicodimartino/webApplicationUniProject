<%--
  Created by IntelliJ IDEA.
  User: aleleo
  Date: 23/04/24
  Time: 15:30
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update your Account</title>
</head>
    <body>
        <h2>Update Your Account</h2>

        <form action="UpdateAccountServlet" method="post">

            <label for="password">Password:</label><br>
            <input type="password" id="password" name="password" value="${user.password}"><br><br>

            <label for="address">Address:</label><br>
            <input type="text" id="address" name="address" value="${user.address}"><br><br>

            <input type="submit" value="Update Account">
        </form>
    </body>
</html>