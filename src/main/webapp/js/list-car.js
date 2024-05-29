/*
    Copyright 2018-2023 University of Padua, Italy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

    Author: Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
    Version: 1.0
    Since: 1.0
*/

const cards = document.querySelectorAll(".card");
// Add an event listener to the button,
// to invoke the function making the AJAX call
cards.forEach(function(card) {
	card.addEventListener("click", handleCarClick);
})

if (sessionStorage.getItem("accountType") !== "ADMIN") {
    document.getElementById("admin-edit").classList.add("d-none");
}

/**
 * Searches for cars that are suitable for the selected car type.
 *
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function handleCarClick() {
    const brand = this.id.split(",")[0];
    const model = this.id.split(",")[1];

    // Fill modal image
    const params = "?brand=" + brand + "&model=" + model;
    const modalImg = document.getElementById("modal-img");
    modalImg.src = "/wacar/loadCarImage" + params;

    console.log("auth ", sessionStorage.getItem("Authorization"));
    // Fill admin edit link
    if (sessionStorage.getItem("Authorization") && sessionStorage.getItem("accountType") === "ADMIN") {
        const adminEditCar = document.getElementById("admin-edit-car");
        adminEditCar.href = "/wacar/admin/editCar/" + params;
    }
    
    const carUrl = "/wacar/rest/car/" + brand + "/" + model;
    const xhrBody = new XMLHttpRequest();

	if (!xhrBody) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}
    // set up the call back for handling the request
	xhrBody.onreadystatechange = function () {
		processCarBody(this);
	};
	xhrBody.open('GET', carUrl, true);
	xhrBody.send();
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */
function processCarBody(xhr) {
    // not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
			xhr.readyState);
		return;
	}

    const car = JSON.parse(xhr.responseText).car;
    console.log("car", car);

    document.getElementById("carModalTitle").innerHTML = car.brand + " " + car.model;
    document.getElementById("type").innerHTML = car.type;
    document.getElementById("maxSpeed").innerHTML = car.maxSpeed;
    document.getElementById("horsepower").innerHTML = car.horsepower;
    document.getElementById("acceleration").innerHTML = car.acceleration;
    let isAvailable = document.getElementById("isAvailable");
    if (car.available) {
        isAvailable.classList.add("text-success");
        isAvailable.classList.remove("text-danger");
    } else {
        isAvailable.classList.add("text-danger");
        isAvailable.classList.remove("text-success");
    }
    document.getElementById("available").innerHTML = car.available
    document.getElementById("description").innerHTML = car.description;
}
