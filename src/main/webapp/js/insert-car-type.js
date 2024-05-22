$(document).ready(function() {
    const modalCarType = $("#newCarTypeModal");
    const typeNameInput = $("#carTypeName");
    const carTypeSelect = $("#carTypeSelect");
    const carTypeForm = $("#carTypeForm");

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


        // Perform the AJAX request
        const url = "/wacar/admin/insertCarType/";
        const requestBody = "name=" + typeNameInput.val();
        const contentType = "application/x-www-form-urlencoded";
        const afterRequestFunction = (status) => {
            if(!status) { // Success
                // If the type was correctly inserted in the db
                carTypeSelect.append($('<option>', {
                    value: typeNameInput.val(),
                    text: typeNameInput.val()
                }));

                // Set the newly created car type
                carTypeSelect.val(typeNameInput.val());

                // Trigger change event
                carTypeSelect.change();
            }

            //reset modal
            carTypeForm.removeClass('was-validated');
            typeNameInput.val("");
            modalCarType.modal('hide');

        }

        performPOSTAjaxRequest(url, requestBody, contentType, afterRequestFunction);
    });
});