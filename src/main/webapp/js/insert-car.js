$(document).ready(function(){
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");
    const modalCarType = $("#newCarTypeModal");
    const typeNameInput = $("#typeName");
    const carTypeSelect = $("#type");
    const liveAlert = $("#liveAlertPlaceholder");
    const carTypeForm = $("#carTypeForm");
    const insertCarForm = $("#insertCarForm");

    // Display the selected image
    imageInput.change( event => {
        const fileInput = event.target;

        if (fileInput.files && fileInput.files[0]) {
            const reader = new FileReader();
            reader.onload = e =>  selectedImage.attr('src', e.target.result);
            reader.readAsDataURL(fileInput.files[0]);
        }
    });

    // Focus on the input as soon as the modal is opened
    modalCarType.on('shown.bs.modal', () => {
       typeNameInput.focus();
    });

    // Add car type
    carTypeForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = typeNameInput[0].checkValidity();
        carTypeForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        // The XMLHttpRequest object for making the AJAX call
        const xhr = new XMLHttpRequest();
        const url = "/wacar/admin/insertCarType/";
        const requestBody = "name=" + typeNameInput.val();

        if(!xhr){
            console.error("Unable to instantiate the XMLHttpRequest object.");
        }

        // set the handler of the results;
        xhr.onload = () => processResponse(xhr);

        // perform the AJAX request
        xhr.open("POST", url);

        // set the request header
        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        // set the request body
        xhr.send(requestBody);

        console.log("AJAX request performed.");
    });

    // Insert car
    insertCarForm.submit((e) => {

        // Checks whether the input is valid
        const isValid = insertCarForm[0].checkValidity();
        insertCarForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid){
            // Prevents the form from the default submission
            e.preventDefault();
            e.stopPropagation();
        }

    });

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
        } else {
            successMessage(jsonData.message["message"]);
            // If the type was correctly inserted in the db
            carTypeSelect.append($('<option>', {
                value: typeNameInput.val(),
                text: typeNameInput.val()
            }));

            // Set the newly created car type
            carTypeSelect.val(typeNameInput.val());
        }

        //reset modal
        carTypeForm.removeClass('was-validated');
        typeNameInput.val("");
        modalCarType.modal('hide');
    }

    const successMessage = (message) => {
        console.log("SUCCESS MESSAGE " + message);
        appendAlert(message,  'success');
    }

    const errorMessage = (errorCode, errorDetails, errorMessage) => {
        console.log("ERROR MESSAGE " + errorMessage);
        appendAlert("ERROR NO. " + errorCode + ": " + errorMessage + ". " + errorDetails,  'danger');
    }

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
});



