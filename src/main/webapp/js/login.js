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

const loginBtn = document.getElementById("login");
loginBtn.addEventListener("click", handleLogin);


const resetBtn = document.getElementById("reset");
resetBtn.addEventListener("click", function() {
  document.getElementById("password").value = "";
  document.getElementById("email").value = "";
});

function handleLogin() {
  const alert = document.getElementById("errorAlert");
  if (!alert.classList.contains("d-none")) {
    alert.classList.add("d-none");
  }

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const url = "/wacar/login/" + "?email=" + email + "&password=" + password;
  const regexEmail = /^[a-z0-9+_.-]+@[a-z0-9.-]+\.[a-z]{2,}$/;
  const regexPsw = /^(?=.*[A-Z])(?=.*[0-9]).{8,}$/;

    if (!email) {
        showError("Please insert your email.");
        return;
    }
    if (!regexEmail.test(email)) {
        showError("Please enter a valid email.");
        return;
    }

    if (!password) {
        showError("Please insert your password.");
        return;
    }
    if (!regexPsw.test(password)) {
        showError("Password must contain at least one number, one uppercase letter, and be at least 8 characters long.");
        return;
    }

	// the XMLHttpRequest object
	const xhr = new XMLHttpRequest();

	if (!xhr) {
		console.log("Cannot create an XMLHttpRequest instance.")

		alert("Giving up :( Cannot create an XMLHttpRequest instance");
		return false;
	}

	// set up the call back for handling the request
	xhr.onreadystatechange = function () {
		processLogin(this);
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
function processLogin(xhr) {
  // not finished yet
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		console.log("Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]", xhr.readyState);
    return;
  }
    
  if (xhr.status == 401) {
    console.log("error message ", xhr.responseText);
    var message = JSON.parse(xhr.responseText);
    console.log("error message ", message.message.message);
    showError(message.message.message)
    return;
  } else if (xhr.status == 200) {
    const headers = xhr.getAllResponseHeaders();

    const array = headers.trim().split(/[\r\n]+/);

    array.forEach((line) => {
      const parts = line.split(": ");
      const header = parts.shift();
      if (header === "authorization") {
        const token = parts.join(": ");

        sessionStorage.setItem("Authorization", token);
        sessionStorage.setItem("email", email.value);
      }
    });
    // Save userType
    const user = JSON.parse(xhr.responseText).user;
    sessionStorage.setItem("accountType", user.accountType);

    window.location.replace("/wacar/");
  }
  return;
}

function showError(message) {
    const alert = document.getElementById("errorAlert");
    const alertText = document.getElementById("errorAlertText");
    alertText.textContent = message;
    alert.classList.remove("d-none");
}
