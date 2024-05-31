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

//Enable tooltips
const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))

let order = {
	id: -1,
	date: null,
	account: null,
	carBrand: null,
	carModel: null,
	circuit: null,
	createdAt: null,
	nLaps: -1,
	price: -1,
}

let lapPrice = -1

let today = new Date();
let day = String(today.getDate()).padStart(2, '0');
let month = String(today.getMonth() + 1).padStart(2, '0'); // I mesi in JavaScript vanno da 0 a 11, quindi aggiungiamo 1
let year = today.getFullYear();
let todayString = `${year}-${month}-${day}`;
document.getElementById("date").setAttribute("min", todayString);
today.setHours(0, 0, 0, 0);

// select all the buttons with the class name "btn"
const buttons = document.querySelectorAll(".carBtn");

// Add an event listener to the button,
// to invoke the function making the AJAX call
buttons.forEach(function(button) {
	button.addEventListener("click", handleSelectCarClick);
})

const btnOrder = document.getElementById("createOrder");
btnOrder.addEventListener("click", handleCreateOrderClick);

document.getElementById("nLaps").addEventListener("change", handleUpdateTotalPrice);
document.getElementById("proceedOrder").addEventListener("click", populateOrderRecap);
document.getElementById("addFavBtn").addEventListener("click", handleAddFavouriteClick);
document.getElementById("delFavBtn").addEventListener("click", handleDeleteFavouriteClick);


// document.getElementById("getCircuitByCarTypeButton").addEventListener("click", getCircuitByCarType);
console.log("Event listener added to getCircuitByCarTypeButton.")

/**
 * Searches for circuits that are suitable for the selected car type.
 *
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function handleSelectCarClick() {
	// Remove green backgroung on previous selected car card
	let alerts = document.querySelectorAll(".carBtn");
	alerts.forEach((alert) => {
		alert.classList.remove("card-car-selected");
	})
	// Show green background on selected car card
	this.classList.add("card-car-selected");

	// get the value of the salary from the form field
	// const carTypeObject = document.getElementById("carType");
	const carType = this.getAttribute("carType");

	console.log("car type", carType);
	order.carBrand = this.getAttribute("carBrand");
	order.carModel = this.getAttribute("carModel");
	order.circuit = "";

	const url = "/wacar/rest/user/order/create/" + carType;
	console.log("Request URL: %s.", url)
	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();
	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processCircuitsByCarType(this);
	};

	// perform the request
	console.log("Performing the HTTP GET request.");

	xhr.open('GET', url, true);
	const auth = sessionStorage.getItem("Authorization");
	xhr.setRequestHeader('Authorization', sessionStorage.getItem("Authorization"));
	xhr.withCredentials = true;
	console.log();
	xhr.send();

	console.log("HTTP GET request sent. ", xhr);
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */
function processCircuitsByCarType(xhr) {
	// not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
			xhr.readyState);
		return;
	}

	const divOrder = document.getElementById("completeOrder");
	divOrder.classList.remove("show");
	divOrder.classList.add("hidden");

	// remove all the children of the circuits div, appended by a previous call, if any
	const divCircuits = document.getElementById("circuits");
	divCircuits.replaceChildren();

	if (xhr.status !== 200) {
		console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
		console.log(xhr.response);

		divCircuits.appendChild(document.createTextNode("Unable to perform the AJAX request."));

		return;
	}

	const title = document.createElement("h2");
	title.textContent = "Select a circuit";
	divCircuits.appendChild(title);

	const circuitSection = document.createElement("div");
	circuitSection.classList.add("row", "row-cols-3");
	divCircuits.appendChild(circuitSection);

	// parse the response as JSON and extract the resource-list array
	const resourceList = JSON.parse(xhr.responseText)["resource-list"];

	if (resourceList.length > 0) {
		circuitSection.classList.remove("noItems-alert");

		for (let i = 0; i < resourceList.length; i++) { // Loop inside circuits
			let circuit = resourceList[i].circuit;
	
			console.log(circuit)
	
			let card = document.createElement("div");
			card.classList.add("circuitBtn", "card");
			card.type = "submit";
			card.setAttribute("circuitName", circuit.name);
			card.setAttribute("lapPrice", circuit["lapPrice"]);
			card.addEventListener("click", handleSelectCircuitClick);
			circuitSection.appendChild(card);
	
			let image = document.createElement("img");
			image.className = "card-img-top";
			image.src = "/wacar/loadCircuitImage?circuitName=" + circuit.name;
			card.appendChild(image);
	
			let cardBody = document.createElement("div");
			cardBody.className = "card-body";
			card.appendChild(cardBody);
	
			let circuitName = document.createElement("p");
			circuitName.className = "h5";
			circuitName.textContent = circuit.name;
			cardBody.appendChild(circuitName);
	
			let circuitType = document.createElement("p");
			circuitType.className = "h6";
			circuitType.textContent = circuit.type;
			cardBody.appendChild(circuitType);
		}
	} else {
		let div = document.createElement("div");
		div.classList.add("alert", "alert-info");
		div.role = "alert";
		div.innerHTML = "There are not any available circuits for the selected car. Sorry for the inconvenience.";
		circuitSection.classList.add("noItems-alert");
		circuitSection.appendChild(div);
	}

	// Initialize number of laps
	document.getElementById("nLaps").value = 1;
	// Show container with circuits
	divCircuits.classList.remove("hidden");
	divCircuits.classList.add("show");

	console.log("HTTP GET request successfully performed and processed.");
}

function handleSelectCircuitClick() {
	// Hide div for completing the order
	let div = document.getElementById("completeOrder");
	div.classList.add("hidden");
	div.classList.remove("show");
	div.scrollIntoView();

	// Remove previous selected circuit
	let alerts = document.querySelectorAll(".circuitBtn");
	alerts.forEach((alert) => {
		alert.classList.remove("card-circuit-selected");
	})

	this.classList.add("card-circuit-selected");

	// Check if favourite already exists
	const params = "?circuitName=" + this.getAttribute("circuitName") + "&carBrand=" + order.carBrand + "&carModel=" + order.carModel
	const url = "/wacar/rest/user/favourite/" + params;
	console.log("Request URL: %s.", url)
	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processExistsFavourite(this);
	};
	// perform the request
	console.log("Performing the HTTP GET request.");

	xhr.open('POST', url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	const auth = sessionStorage.getItem("Authorization");
	xhr.setRequestHeader('Authorization', sessionStorage.getItem("Authorization"));
	xhr.withCredentials = true;
	xhr.send();

	console.log("HTTP POST request sent. ", xhr);

	// Show div for completing the order
	div.classList.remove("hidden");
	div.classList.add("show");
	div.scrollIntoView();

	order.circuit = this.getAttribute("circuitName");
	lapPrice = parseInt(this.getAttribute("lapPrice"));
	order.price = lapPrice

	let label = document.getElementById("totalPrice");
	label.textContent = "Total price: €" + lapPrice;

	console.log(order);
}

function processExistsFavourite(xhr) {
	// not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]", xhr.readyState);
		return;
	}

	// Show add/delete favourite based on the result of the request
	if (xhr.status !== 302) {
		if (xhr.status === 404) {
			console.log("i'm here, favourite not found");
			document.getElementById("addFavBtn").classList.remove('d-none');
			document.getElementById("delFavBtn").classList.add('d-none');
		}
	} else {
		console.log("i'm here, favourite found");
		document.getElementById("delFavBtn").classList.remove('d-none');
		document.getElementById("addFavBtn").classList.add('d-none');
	}
}

function handleUpdateTotalPrice() {
	let label = document.getElementById("totalPrice");
	if (lapPrice != null && this.value != null) {
		label.textContent = "Total price: €" + lapPrice*this.value;
	}
}

function handleCreateOrderClick() {
	let date = document.getElementById("date");
	order.date = date.value;
	let nLaps = document.getElementById("nLaps");
	order.nLaps = parseInt(nLaps.value);
	order.price = parseInt(order.nLaps * lapPrice);


	const url = "/wacar/rest/user/order/create/complete";

	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();
	xhr.open("POST", url);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.setRequestHeader("Authorization", sessionStorage.getItem("Authorization"));

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processCreateOrder(this);
	};

	// perform the request
	console.log("Performing the HTTP GET request.");

	const timeNow = new Date();
	order.createdAt = timeNow.toISOString();
	console.log(order.createdAt);

	order.account = sessionStorage.getItem("email");

	var orderString = JSON.stringify({ "order": order });
	console.log(orderString)
	xhr.send(orderString);

	console.log("HTTP GET request sent. ", xhr);
}

function processCreateOrder(xhr) {
	// not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]", xhr.readyState);
		return;
	}

	// Show alert based on the success (or failure) of the request
	if (xhr.status !== 201) {
		console.log("i'm here, error");
		const msg = JSON.parse(xhr.responseText);
		document.getElementById("errorAlert").classList.remove("d-none");
		document.getElementById("orderErrorMessage").innerHTML = msg.message.message;
		document.getElementById("successAlert").classList.add("d-none");
	} else {
		console.log("i'm here, success");
		const msg = JSON.parse(xhr.responseText);
		document.getElementById("successAlert").classList.remove('d-none');
		document.getElementById("orderSuccessMessage").innerHTML = msg.message.message;
		document.getElementById("errorAlert").classList.add('d-none');
	}
	
	document.getElementById("returnHome").addEventListener("click", returnHome);
}

function returnHome() {
	window.location.replace("/wacar/");
}

function handleAddFavouriteClick() {
	const favourite = {
		circuit: order.circuit,
		carBrand: order.carBrand,
		carModel: order.carModel,
		account: sessionStorage.getItem("email"),
		createdAt: -1,
	}

	const url = "/wacar/rest/user/favourite/add";
	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.setRequestHeader("Authorization", sessionStorage.getItem("Authorization"));

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processFavouriteOperation(this, "add");
	};

	// perform the request
	console.log("Performing the HTTP GET request.");

	const timeNow = new Date();
	favourite.createdAt = timeNow.toISOString();
	var favouriteString = JSON.stringify({ "favourite": favourite });

	xhr.withCredentials = true;
	xhr.send(favouriteString);

	console.log("HTTP GET request sent. ", xhr);
}

function handleDeleteFavouriteClick() {
	const favourite = {
		circuit: order.circuit,
		carBrand: order.carBrand,
		carModel: order.carModel,
		account: sessionStorage.getItem("email"),
	}
	
	const url = "/wacar/rest/user/favourite/delete";
	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();
	xhr.open("DELETE", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.setRequestHeader("Authorization", sessionStorage.getItem("Authorization"));

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processFavouriteOperation(this, "delete");
	};

	// perform the request
	console.log("Performing the HTTP GET request.");

	var favouriteString = JSON.stringify({ "favourite": favourite });

	xhr.withCredentials = true;
	xhr.send(favouriteString);

	console.log("HTTP GET request sent. ", xhr);
}

function populateOrderRecap(e){
	console.log("Order recap population");
	var recapOrderModal = new bootstrap.Modal(document.getElementById('orderModal'));

	// Check for date validity
	const inputDate = document.getElementById('date');
	if (inputDate.value === "") {
		inputDate.setCustomValidity("Please select a valid date.");
		inputDate.reportValidity();
		return;
	} else {
		const orderDate = new Date(inputDate.value).setHours(0, 0, 0, 0);	
		console.log(today);
		console.log(orderDate);
		if (today >= orderDate) {
			inputDate.setCustomValidity("The order date cannot be earlier than today's date.");
			inputDate.reportValidity();
			return;
		}
	}

	// Show recap order modal
	recapOrderModal.show();

	// ADD THE CAR IMAGE
	const carImageDiv = document.getElementById("recapOrderCarImage");
	console.log("Order: ", order);
	carImageDiv.innerHTML = '';
	const carImg = document.createElement('img');
	carImg.src = `/wacar/loadCarImage?brand=${order.carBrand}&model=${order.carModel}`;
	carImg.alt = `Car image of ${order.carBrand} ${order.carModel}`;
	carImg.classList.add('img-fluid', 'rounded');
	carImageDiv.appendChild(carImg);
	const carLabel = document.getElementById("carLabelRecapOrder");
	carLabel.innerHTML = `${order.carBrand} ${order.carModel}`;

	// ADD THE CIRCUIT IMAGE
	const circuitImageDiv = document.getElementById("recapOrderCircuitImage");
	circuitImageDiv.innerHTML = '';
	const circuitImg = document.createElement('img');
	circuitImg.src = `/wacar/loadCircuitImage?circuitName=${order.circuit}`;
	circuitImg.alt = `Circuit image of ${order.circuit}`;
	circuitImg.classList.add('img-fluid', 'rounded');
	circuitImageDiv.appendChild(circuitImg);
	const circuitLabel = document.getElementById("circuitLabelRecapOrder");
	circuitLabel.innerHTML = `${order.circuit}`;

	//Add DATE
	const dateDiv = document.getElementById("orderRecapDate");
	dateDiv.innerHTML = '';
	let textElement = document.createElement('p');
	textElement.textContent = inputDate.value;
	dateDiv.appendChild(textElement);

	//Add laps
	const laps = document.getElementById("nLaps");
	const lapNoDiv = document.getElementById("orderRecapLapNo");
	lapNoDiv.innerHTML = '';
	textElement = document.createElement('p');
	textElement.textContent = laps.value;
	lapNoDiv.appendChild(textElement);

	//Add PRICE
	const priceDiv = document.getElementById("orderRecapPrice");
	priceDiv.innerHTML = document.getElementById("totalPrice").innerHTML.substring(13);
}

function processFavouriteOperation(xhr, op) {
	// not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]", xhr.readyState);
		return;
	}

	console.log(xhr.status);

	if (op == "add") {
		if (xhr.status !== 201) {
			console.log("i'm here, error");
			const msg = JSON.parse(xhr.responseText);
			document.getElementById("addFavErrorMessage").innerHTML = msg.message.message;


			document.getElementById("errorFavAlert").classList.remove('d-none');
			document.getElementById("successFavAlert").classList.add('d-none');
		} else {
			console.log("i'm here, success");
			const msg = JSON.parse(xhr.responseText);

			document.getElementById("addFavSuccesMessage").innerHTML = msg.message.message;
			document.getElementById("successFavAlert").classList.remove('d-none');
			document.getElementById("errorFavAlert").classList.add('d-none');
	
			document.getElementById("delFavBtn").classList.remove('d-none');
			document.getElementById("addFavBtn").classList.add('d-none');

		}
	} else if (op == "delete") {
		if (xhr.status !== 200) {
			const msg = JSON.parse(xhr.responseText);
			console.log("i'm here, error");
			document.getElementById("addFavErrorMessage").innerHTML = msg.message.message;

			document.getElementById("errorFavAlert").classList.remove('d-none');
			document.getElementById("successFavAlert").classList.add('d-none');
		} else {
			console.log("i'm here, success");
			const msg = JSON.parse(xhr.responseText);
			document.getElementById("addFavSuccesMessage").innerHTML = msg.message.message;
			document.getElementById("successFavAlert").classList.remove('d-none');
			document.getElementById("errorFavAlert").classList.add('d-none');
	
			document.getElementById("addFavBtn").classList.remove('d-none');
			document.getElementById("delFavBtn").classList.add('d-none');
		}
	}


	console.log("Add favourite completed");
}