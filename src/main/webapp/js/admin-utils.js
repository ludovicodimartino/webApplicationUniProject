// The Alert DOM element
let liveAlert;

// Function to perform the POST requests to the server
const performPOSTAjaxRequest = (url, body, contentType, afterResponseFunction) => {
    // The XMLHttpRequest object for making the AJAX call
    const xhr = new XMLHttpRequest();

    if(!xhr){
        const errMsg = "Unable to instantiate the XMLHttpRequest object.";
        console.error(errMsg);
        errorMessage(0, "", errMsg);
    }

    // set the handler of the results;
    xhr.onload = () => {
        const status = processResponse(xhr);
        afterResponseFunction(status);
    }

    // perform the AJAX request
    xhr.open("POST", url);

    // set the request header
    if(contentType !== null) xhr.setRequestHeader("Content-type", contentType);

    // set the request body
    xhr.send(body);
}

// Function to process the JSON message response from the server
const processResponse = (xhr) => {
    console.log("PROCESSING RESPONSE");
    if (xhr.status !== 200){
        errorMessage(xhr.status, "", xhr.responseText);
        return;
    }
    // parse the response to the corresponding JSON object
    const jsonData = JSON.parse(xhr.responseText);
    console.log(jsonData);

    // The message contains an error
    if(jsonData.message["error-code"]){
        errorMessage(jsonData.message["error-code"], jsonData.message["error-details"], jsonData.message["message"]);
        return -1;
    } else {
        successMessage(jsonData.message["message"]);
        return 0;
    }
}

const successMessage = (message) => {
    console.log("SUCCESS MESSAGE " + message);
    appendAlert(message,  'success');
}

// Function to display an error message
const errorMessage = (errorCode, errorDetails, errorMessage) => {
    console.log("ERROR MESSAGE " + errorMessage);
    const message = [
        '<h4>ERROR</h4>',
        '<ul>',
        `   <li>Code: ${errorCode}</li>`,
        `   <li>Message: ${errorMessage}</li>`,
        `   <li>Details: ${errorDetails}</li>`,
        '</ul>'
    ].join('')
    appendAlert(message,  'danger');
}

// Function to append the alert to the DOM
const appendAlert = (message, type) => {
    const wrapper = document.createElement('div');

    wrapper.innerHTML = [
        `<div class="alert alert-${type} alert-dismissible d-flex align-items-center" role="alert">`,
        `   <div>${message}</div>`,
        '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
        '</div>'
    ].join('')

    liveAlert.append(wrapper);
}