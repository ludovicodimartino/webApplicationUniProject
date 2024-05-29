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

const button = document.getElementById("logout");
if (button != null)  {
  button.addEventListener("click", handleLogout);
}

function handleLogout() {
  const url = "http://localhost:8081/wacar/logout/";

  	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processLogout(this);
	};

  xhr.open('POST', url, true);
	xhr.send();
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */
function processLogout(xhr) {
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]", xhr.readyState);
	}
	else if (xhr.status == 200) {
		console.log("Logout successful");
		sessionStorage.clear();
		window.location.replace("http://localhost:8081/wacar/");
	}
 	else if (xhr.status == 401) {
		console.error("User not logged in");
		alert("User not logged in");
		window.location.replace("http://localhost:8081/wacar/login/");}
	else {
		console.error("Logout failed. Status: ", xhr.status);
		alert("Logout failed");
	}
}
