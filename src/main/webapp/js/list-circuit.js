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
	card.addEventListener("click", handleCircuitClick);
})

/**
 * Searches for circuits that are suitable for the selected car type.
 *
 * @returns {boolean} true if the HTTP request was successful; false otherwise.
 */
function handleCircuitClick() {
    // Fill modal image
    const modalImg = document.getElementById("modal-img");
    modalImg.src = "/wacar/loadCircuitImage?circuitName=" + this.id;

    // Fill admin edit link
    if (sessionStorage.getItem("Authorization") && sessionStorage.getItem("accountType") === "ADMIN") {
        const adminEditCircuit = document.getElementById("admin-edit-circuit");
        adminEditCircuit.href = "/wacar/admin/editCircuit/?name=" + this.id;
    }
    
    const circuitUrl = "/wacar/rest/circuit/" + this.id; // TODO
    const xhrBody = new XMLHttpRequest();

	if (!xhrBody) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}
    // set up the call back for handling the request
	xhrBody.onreadystatechange = function () {
		processCircuitBody(this);
	};
	xhrBody.open('GET', circuitUrl, true);
	xhrBody.send();
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */
function processCircuitBody(xhr) {
    // not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
			xhr.readyState);
		return;
	}

    const circuit = JSON.parse(xhr.responseText).circuit;
    console.log("circuit", circuit);

    document.getElementById("circuitModalTitle").innerHTML = circuit.name;
    document.getElementById("type").innerHTML = circuit.type;
    document.getElementById("length").innerHTML = circuit.length;
    document.getElementById("cornersNumber").innerHTML = circuit.cornersNumber;
    document.getElementById("address").innerHTML = circuit.address;
    document.getElementById("lapPrice").innerHTML = circuit.lapPrice;
    let isAvailable = document.getElementById("isAvailable");
    if (circuit.available) {
        isAvailable.classList.add("text-success");
        isAvailable.classList.remove("text-danger");
    } else {
        isAvailable.classList.add("text-danger");
        isAvailable.classList.remove("text-success");
    }
    document.getElementById("available").innerHTML = circuit.available
    document.getElementById("description").innerHTML = circuit.description;}
