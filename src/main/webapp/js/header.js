document.addEventListener("DOMContentLoaded", function() {
    var currentPage = window.location.pathname;

    if (currentPage.includes("/home")) {
        document.getElementById("nav-home").classList.add("hidden");
    }
    if (currentPage.includes("/circuit_list/")) {
        document.getElementById("nav-circuit-list").classList.add("hidden");
    }
    if (currentPage.includes("/car_list/")) {
        document.getElementById("nav-car-list").classList.add("hidden");
    }
    if (currentPage.includes("/user/listOrdersByAccount")) {
        document.getElementById("nav-orders").classList.add("hidden");
    }
    if (currentPage.includes("/user/list-favourite")) {
        document.getElementById("nav-favourites").classList.add("hidden");
    }
    if (currentPage.includes("/user/create-order/cars")) {
        document.getElementById("nav-new-order").classList.add("hidden");
    }
    if (currentPage.includes("/user/user-info")) {
        document.getElementById("nav-account-user").classList.add("hidden");
    }
    if (currentPage.includes("/admin/insertCar")) {
        document.getElementById("nav-insert-car").classList.add("hidden");
    }
    if (currentPage.includes("/admin/insertCircuit")) {
        document.getElementById("nav-insert-circuit").classList.add("hidden");
    }
    if (currentPage.includes("/admin/insertMapping")) {
        document.getElementById("nav-insert-mapping").classList.add("hidden");
    }
    if (currentPage.includes("/admin/admin-info")) {
        document.getElementById("nav-account-admin").classList.add("hidden");
    }
});