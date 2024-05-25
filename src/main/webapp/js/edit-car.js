$(document).ready(function () {
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");
    liveAlert = $("#liveAlertPlaceholder");
    const editCarForm = $("#editCarForm");

    // Placeholder image (the original image in the database)
    const placeholderImageURL = selectedImage.attr('src'); 

    // Display the selected image on file input change
    imageInput.change((e) => updateImage(e, placeholderImageURL, imageInput, selectedImage));

    // Edit circuit submit
    editCarForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = editCarForm[0].checkValidity();
        editCarForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        //set up post-request parameters.
        const url= "/wacar/admin/editCar/";

        // Let the browser to insert the content type and the boundary.
        const contentType = null;
        const body = new FormData(editCarForm[0]);
        const afterResponseFunction = (status) => {
            if(!status){ // Success -> redirect to car list
                window.location.href = "/wacar/car_list/"
            } else {
                console.error("Error in the editCar response");
            }
        }

        // Perform the Ajax Post request
        performPOSTAjaxRequest(url, body, contentType, afterResponseFunction);

    });
});