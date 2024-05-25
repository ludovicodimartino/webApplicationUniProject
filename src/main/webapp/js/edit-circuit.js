$(document).ready(function () {
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");
    liveAlert = $("#liveAlertPlaceholder");
    const editCircuitForm = $("#editCircuitForm");

    // Placeholder image (the original image in the database)
    const placeholderImageURL = selectedImage.attr('src'); 

    // Display the selected image on file input change
    imageInput.change((e) => updateImage(e, placeholderImageURL, imageInput, selectedImage));

    // Edit circuit submit
    editCircuitForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = editCircuitForm[0].checkValidity();
        editCircuitForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        //set up post-request parameters.
        const url= "/wacar/admin/editCircuit/";

        // Let the browser to insert the content type and the boundary.
        const contentType = null;
        const body = new FormData(editCircuitForm[0]);
        const afterResponseFunction = (status) => {
            if(!status){ // Success -> redirect to circuit list
                window.location.href = "/wacar/circuit_list/"
            } else {
                console.error("Error in the editCircuit response");
            }
        }

        // Perform the Ajax Post request
        performPOSTAjaxRequest(url, body, contentType, afterResponseFunction);

    });
});