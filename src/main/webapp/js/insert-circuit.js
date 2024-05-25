$(document).ready(function(){
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");
    liveAlert = $("#liveAlertPlaceholder");
    const insertCircuitForm = $("#insertCircuitForm");

    // Set the image as invalid
    imageInput[0].setCustomValidity("Invalid image.");

    // Placeholder image
    const placeholderImageURL = "/wacar/images/circuitImagePlaceholder.png"

    // Display the selected image on file input change
    imageInput.change((e) => updateImage(e, placeholderImageURL, imageInput, selectedImage));

    // Insert circuit
    insertCircuitForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = insertCircuitForm[0].checkValidity();
        insertCircuitForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        //set up post-request parameters.
        const url= "/wacar/admin/insertCircuit/";

        //Let the browser to insert the content type and the boundary.
        const contentType = null;
        const body = new FormData(insertCircuitForm[0]);
        const afterResponseFunction = (status) => {
            if(!status){ // Success

                // clear form
                $(':input','#insertCircuitForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .prop('checked', false)
                    .prop('selected', false);

                // remove validations form
                insertCircuitForm.removeClass('was-validated');

                // remove image
                selectedImage.attr('src', placeholderImageURL);
                imageInput[0].setCustomValidity("Invalid image.");
            }

        }

        // Perform the Ajax Post request
        performPOSTAjaxRequest(url, body, contentType, afterResponseFunction);

    });

});
