$(document).ready(function(){
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");
    liveAlert = $("#liveAlertPlaceholder");
    const insertCarForm = $("#insertCarForm");

    // Set the image as invalid
    imageInput[0].setCustomValidity("Invalid image.");

    // Placeholder image
    const placeholderImageURL = "/wacar/images/carImagePlaceholder.png"

    // Display the selected image on file input change
    imageInput.change((e) => updateImage(e, placeholderImageURL, imageInput, selectedImage));

    // Insert car
    insertCarForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = insertCarForm[0].checkValidity();
        insertCarForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        //set up post-request parameters.
        const url= "/wacar/admin/insertCar/";

        //Let the browser to insert the content type and the boundary.
        const contentType = null;
        const body = new FormData(insertCarForm[0]);
        const afterResponseFunction = (status) => {
            if(!status){ // Success

                // clear form
                $(':input','#insertCarForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .prop('checked', false)
                    .prop('selected', false);

                // remove validations form
                insertCarForm.removeClass('was-validated');

                // remove image
                selectedImage.attr('src', placeholderImageURL);
                imageInput[0].setCustomValidity("Invalid image.");
            }

        }

        // Perform the Ajax Post request
        performPOSTAjaxRequest(url, body, contentType, afterResponseFunction);

    });
});
