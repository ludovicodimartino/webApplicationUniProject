$(document).ready(function() {
    const modalCircuitType = $("#newCircuitTypeModal");
    const typeNameInput = $("#circuitTypeName");
    const circuitTypeSelect = $("#circuitTypeSelect");
    const circuitTypeForm = $("#circuitTypeForm");

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

                // Set the newly created circuit type
                circuitTypeSelect.val(typeNameInput.val());

                // Update the circuitTypes data structure if not undefined
                if(typeof circuitTypes !== 'undefined'){
                    circuitTypes.push(typeNameInput.val())
                }

                // Trigger change event
                circuitTypeSelect.change();
            }

            //reset modal
            circuitTypeForm.removeClass('was-validated');
            typeNameInput.val("");
            modalCircuitType.modal('hide');

        }

        performPOSTAjaxRequest(url, requestBody, contentType, afterRequestFunction);
    });

});