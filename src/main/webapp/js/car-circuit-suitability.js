// gloabal variable (array) of circuit types
let circuitTypes;

$(document).ready(function(){
    const mappingForm = $("#cCSuitForm");
    const carTypeSelect = $("#carTypeSelect");
    const circuitTypeSelect = $("#circuitTypeSelect");
    const circuitTypeBtn = $("#circuitTypeBtn");
    const deleteButtons = $(".deleteBtn");
    liveAlert = $("#liveAlertPlaceholder");

    // Data structure of the associations
    const associations = {};

    // Arry of the circuit types
    circuitTypes = $('#circuitTypeSelect option')
    .filter(function() {
        return $(this).val() !== "";
    })
    .map(function() {
        return $(this).val();
    })
    .get();

    console.log("CIRCUIT TYPES:", circuitTypes);

    let lastRowSpan = 1;
    let carType;

    // Load the content of the table in the associations object
    $('#associationBody tr').each(function(){
        let isNewCarType = false;
        if(lastRowSpan-- <= 1){
            carType = $(this).find('td:first').text().trim();
            const currentRowSpan = $(this).find('td:first').prop('rowspan');
            lastRowSpan = currentRowSpan === undefined ? 0 : currentRowSpan;
            isNewCarType = true;
        }

        // Get the circuit list
        $(this).find('td').each(function(index) {
            // Don't include in the association object the delete button column and the car column
            if(!$(this).hasClass('deleteBtnCol') && (index !== 0 || !isNewCarType)) {
                let circuitType = $(this).text().trim();
                if (!associations[carType]) {
                    associations[carType] = [];
                }
                associations[carType].push(circuitType);
            }
        });
    });

    console.log("ASSOCIATIONS OBJECT: ", associations);

    // Submit the form to create a new mapping
    mappingForm.submit((e) => {
        // Prevents the form from the default submission
        e.preventDefault();
        e.stopPropagation();

        // Checks whether the input is valid
        const isValid = mappingForm[0].checkValidity();
        mappingForm.addClass('was-validated');

        // return if the form is not valid
        if(!isValid) return;

        // set up post-request parameters.
        const url= "/wacar/admin/insertMapping/";

        // Let the browser insert the content type and the boundary.
        const contentType = null;
        const body = new FormData(mappingForm[0]);
        const afterResponseFunction = (status) => {
            if (!status) { // Success

                addRowTable(carTypeSelect.val(), circuitTypeSelect.val());

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

    // When a change is detected in the dropdowns activate the next dropdown
    carTypeSelect.change(() => {
       if(carTypeSelect.val() === ""){ // Invalid car type -> disable circuit type
           circuitTypeSelect.prop("disabled", true);
           circuitTypeBtn.prop("disabled", true);
       } else {
           // update options in circuitTypeSelect removing the associations already present in the table
           let options = '<option value="">Select circuit type</option>';
        
           for(circuit of circuitTypes){
               if(associations[carTypeSelect.val()] && associations[carTypeSelect.val()].includes(circuit)) continue;
               options += `<option value="${circuit}">${circuit}</option>`;
            }
            circuitTypeSelect.empty().append(options);

            // enable select circuit types and add new circuit type button
            circuitTypeSelect.prop("disabled", false);
            circuitTypeBtn.prop("disabled", false);
        }
    });

    // Perform delete request
    const deleteRequest = (carType, circuitType) => {
        // set up post-request parameters.
        const url= "/wacar/admin/deleteMapping/";
        const contentType = "application/x-www-form-urlencoded";
        const body = "carType=" + carType + "&circuitType=" + circuitType;
        const afterResponseFunction = (status) => {
            console.log("DELETE REQUEST PERFORMED");
            if(!status){ // success
                deleteRowTable(carType, circuitType);
            }
        }
        performPOSTAjaxRequest(url, body, contentType, afterResponseFunction);
    }

    // Add event listener to each delete button
    deleteButtons.click(function() {deleteRequest($(this).attr('cartype'), $(this).attr('circuittype'))});


    // Update the table and the association data structure with the newly inserted association
    const addRowTable = (carType, circuitType) => {
        console.log("UPDATE TABLE");
        const newButton = [
            `<td class="deleteBtnCol"><button carType="${carType}" circuitType="${circuitType}" class="deleteBtn btn btn-danger">`,
            ' <span class="material-symbols-outlined">delete</span>',
            '</button></td>'
        ].join('');
        const newTableRow = [
            '<tr><td>',
            circuitType,
            '</td>',
            newButton,
            '</tr>'
        ].join('');
        if (associations[carType]) {
            // update the associations object
            associations[carType].push(circuitType);

            let rowspanCell = $('#associationBody td:contains(' + carType + ')').first();
            let currentRowspan = parseInt(rowspanCell.attr('rowspan')) || 1;
            rowspanCell.attr('rowspan', currentRowspan + 1);

            let lastRow = rowspanCell.closest('tr').first();
            $(newTableRow).insertAfter(lastRow);
        } else {
            associations[carType] = [circuitType];
            $('#associationBody').append([
                '<tr>',
                    '<td rowspan="1">',
                        carType,
                    '</td>',
                    '<td>',
                        circuitType,
                    '</td>',
                    newButton,
                '</tr>'
            ].join(''));
        }

        // Add listeners to the new delete buttons
        $(`.deleteBtn[cartype='${carType}'][circuittype='${circuitType}']`).click(function() {deleteRequest($(this).attr('cartype'), $(this).attr('circuittype'))});

    }

    // Delete a row from the HTML table and the corresponding association from the associations object
    const deleteRowTable = (carType, circuitType) => {
        if (associations[carType]) {
            const index = associations[carType].indexOf(circuitType);
            if(index === -1){
                console.error(`Circuit type ${circuitType} not associated with car ${carType}`);
                return;
            } 

            // Delete the circuitType from the associations object
            associations[carType].splice(index, 1);

            // If no other circuit types are associated with carType, delete the carType
            if (associations[carType].length === 0) {
                delete associations[carType];
            }

            // Update HTML table
            const rows = $('#associationBody tr');
            rows.each(function() {
                const row = $(this);
                const carTypeCell = row.find('td:first');
                if (carTypeCell.text().trim() === carType) {
                    let rowSpanCount = carTypeCell.attr('rowspan');
                    if (rowSpanCount > 1) {
                        carTypeCell.attr('rowspan', rowSpanCount - 1);
                        let toRemove = row.find(`td:contains(${circuitType})`);
                        if(toRemove.length > 0){ // First element
                            toRemove.remove();
                            row.find('td.deleteBtnCol').remove();
                            row.append(row.next().find('td'));
                        } else {
                            row.nextAll().find(`td:contains(${circuitType})`).parent().remove();
                        }
                    } else {
                        row.remove();
                    }
                    return false;
                }
            });
        }
    }

});