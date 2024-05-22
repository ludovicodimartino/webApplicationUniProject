$(document).ready(function(){
    const mappingForm = $("#cCSuitForm");
    const carTypeSelect = $("#carTypeSelect");
    const circuitTypeSelect = $("#circuitTypeSelect");
    const circuitTypeBtn = $("#circuitTypeBtn");
    liveAlert = $("#liveAlertPlaceholder");

    mappingForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = mappingForm[0].checkValidity();
        mappingForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        //set up post-request parameters.
        const url= "/wacar/admin/insertMapping/";

        //Let the browser to insert the content type and the boundary.
        const contentType = null;
        const body = new FormData(mappingForm[0]);
        const afterResponseFunction = (status) => {
            if (!status) { // Success

                updateTable();

                // clear form
                $(':input', '#cCSuitForm')
                    .not(':button, :submit, :reset, :hidden')
                    .val('')
                    .prop('checked', false)
                    .prop('selected', false);

                // Trigger select change
                carTypeSelect.change();

                // remove validations form
                mappingForm.removeClass('was-validated');
            }
        };

        // Perform the Ajax Post request
        performPOSTAjaxRequest(url, body, contentType, afterResponseFunction);
    });

    carTypeSelect.change((e) => {
       if(carTypeSelect.val() === ""){ // Invalid car type -> disable circuit type
           circuitTypeSelect.prop("disabled", true);
           circuitTypeBtn.prop("disabled", true);
       } else {
           circuitTypeSelect.prop("disabled", false);
           circuitTypeBtn.prop("disabled", false);

           //TO-DO: update options in circuitTypeSelect removing the associations already present in the table
       }
    });

    const updateTable = () => {
        console.log("UPDATE TABLE");
    }
});