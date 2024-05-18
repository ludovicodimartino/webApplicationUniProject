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

const button = document.getElementById("signup");
button.addEventListener("click", handleSignup);

function handleSignup() {
  const email = document.getElementById("email").value;
  const name = document.getElementById("name").value;
  const surname = document.getElementById("surname").value;
  const address = document.getElementById("address").value;
  const password = document.getElementById("password").value;

  const params = "?email=" + email + "&name=" + name + "&surname=" + surname + "&address=" + address + "&password=" + password;
  const url = "http://localhost:8081/wacar/signup/" + params;

  	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processSignup(this);
	};

  xhr.open('POST', url, true);
	xhr.send();

  console.log("HTTP POST request sent. ", xhr);
}

/**
 * Processes the HTTP response and writes the results back to the HTML page.
 *
 * @param xhr the XMLHttpRequest object performing the request.
 */
function processSignup(xhr) {
  // not finished yet
  if (xhr.readyState !== XMLHttpRequest.DONE) {
    console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]", xhr.readyState);
    
    if (xhr.readyState == XMLHttpRequest.HEADERS_RECEIVED) {
      const headers = xhr.getAllResponseHeaders();
      console.log("headers: ", headers);
  
      const array = headers.trim().split(/[\r\n]+/);
      console.log("array: ", array);
  
      array.forEach((line) => {
        const parts = line.split(": ");
        const header = parts.shift();
        if (header === "authorization") {
          const token = parts.join(": ");
          console.log("token: ", token);
  
          sessionStorage.setItem("Authorization", token);
          sessionStorage.setItem("email", email.value);
        }
      });

      // window.location.replace("http://localhost:8081/wacar/");
    }
    return;
  }
}