<%@ page import="it.unipd.dei.webapp.wacar.resource.User" %>
<%@ page import="java.util.Objects" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome!</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f2f2f2;
        }

        .container {
            max-width: 800px;
            margin: 50px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .card {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding: 20px;
            background-color: #f0f0f0;
            border-radius: 8px;
            box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        }

        .card a {
            text-decoration: none;
            color: #333;
            font-weight: bold;
        }

        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Welcome!</h1>

    <!-- Circuit list -->
    <div class="card">
        <a href="/wacar/html/circuit_list.html">Circuit list</a>
        <a href="/wacar/html/circuit_list.html"><img src="circuit_icon.png" alt="circuit_icon" width="50"></a>
    </div>

    <!-- Car list -->
    <div class="card">
        <a href="/wacar/html/car_list.html">Car list</a>
        <a href="/wacar/html/car_list.html"><img src="car_icon.png" alt="car_icon" width="50"></a>
    </div>

    <% User user = (User) session.getAttribute("account"); %>
    <% if (user != null) { %>
    <% if (Objects.equals(user.getType(), "USER")) { %>
    <!-- Reservations -->
    <div class="card">
        <a href="/wacar/user/reservations/">Reservations</a>
        <a href="/wacar/user/reservations/"><img src="icon.png" alt="res_icon" width="50"></a>
    </div>
    <div class="card">
        <a href="/wacar/user/">User page</a>
        <a href="/wacar/user/"><img src="icon.png" alt="res_icon" width="50"></a>
    </div>
    <% } %>
    <% if (Objects.equals(user.getType(), "ADMIN")) { %>
    <div class="card">
        <a href="/wacar/admin/insertCar/">Insert new Car</a>
    </div>
    <div class="card">
        <a href="/wacar/admin/insertCircuit/">Insert new Circuit</a>
    </div>
    <div class="card">
        <a href="/wacar/admin/insertMapping/">Map a car to a Circuit</a>
    </div>
    <div class="card">
        <a href="/wacar/admin/">Admin page</a>
        <a href="/wacar/user/"><img src="icon.png" alt="res_icon" width="50"></a>
    </div>
    <% } %>
    <!-- Logout -->
    <form method="GET" action="/wacar/user/logout/">
        <button type="submit">Logout</button>
    </form>
    <% } else { %>
    <!-- Signup -->
    <form method="GET" action="/wacar/user/signup/">
        <button type="submit">Signup</button>
    </form>
    <!-- Login -->
    <form method="GET" action="/wacar/user/login/">
        <button type="submit">Login</button>
    </form>
    <% } %>

</div>

</body>
</html>

