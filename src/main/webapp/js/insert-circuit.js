
// The Alert DOM element
let liveAlert;
$(document).ready(function(){
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");
    const modalCircuitType = $("#newCircuitTypeModal");
    const typeNameInput = $("#typeName");
    const circuitTypeSelect = $("#type");
    liveAlert = $("#liveAlertPlaceholder");
    const circuitTypeForm = $("#circuitTypeForm");
    const insertCircuitForm = $("#insertCircuitForm");

    // Set the image as invalid
    imageInput[0].setCustomValidity("Invalid image.");

    // Placeholder image
    const placeholderImageURL = "/wacar/images/circuitImagePlaceholder.png"

    // Display the selected image
    imageInput.change( event => {
        const fileInput = event.target;
        const validImageTypes = ['image/jpeg', 'image/png'];
        if (fileInput.files && fileInput.files[0] && validImageTypes.includes(fileInput.files[0]['type'])) {
            const reader = new FileReader();
            reader.onload = e =>  selectedImage.attr('src', e.target.result);
            reader.readAsDataURL(fileInput.files[0]);
            imageInput[0].setCustomValidity("");
        }else{ //invalid image
            selectedImage.attr('src', placeholderImageURL);
            imageInput[0].setCustomValidity("Invalid image.");
        }
    });

    // Focus on the input as soon as the modal is opened
    modalCircuitType.on('shown.bs.modal', () => {
        typeNameInput.focus();
    });

    // Add circuit type
    circuitTypeForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = typeNameInput[0].checkValidity();
        circuitTypeForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;


        // Perform the AJAX request
        const url = "/wacar/admin/insertCircuitType/";
        const requestBody = "name=" + typeNameInput.val();
        const contentType = "application/x-www-form-urlencoded";
        const afterRequestFunction = (status) => {
            if(!status) { // Success
                // If the type was correctly inserted in the db
                circuitTypeSelect.append($('<option>', {
                    value: typeNameInput.val(),
                    text: typeNameInput.val()
                }));

                // Set the newly created car type
                circuitTypeSelect.val(typeNameInput.val());
            }

            //reset modal
            circuitTypeForm.removeClass('was-validated');
            typeNameInput.val("");
            modalCircuitType.modal('hide');

        }

        performPOSTAjaxRequest(url, requestBody, contentType, afterRequestFunction);
    });

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
