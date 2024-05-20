const footerbtn = document.getElementById("footer-logout");
footerbtn.addEventListener("click", handleLogout);

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

  // Remove all saved items
  sessionStorage.clear();

  window.location.replace("http://localhost:8081/wacar/");
}
