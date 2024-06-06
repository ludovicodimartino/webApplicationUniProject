document.addEventListener('DOMContentLoaded', (event) => {
    var deleteModal = document.getElementById('deleteModal');
    deleteModal.addEventListener('show.bs.modal', function (event) {
        var button = event.relatedTarget; // Button that triggered the modal
        var circuit = button.getAttribute('data-circuit');
        var carBrand = button.getAttribute('data-carbrand');
        var carModel = button.getAttribute('data-carmodel');

        var modalTitle = deleteModal.querySelector('.modal-title');
        var modalCircuit = deleteModal.querySelector('#modal-circuit');
        var modalCarBrand = deleteModal.querySelector('#modal-carBrand');
        var modalCarModel = deleteModal.querySelector('#modal-carModel');

        modalTitle.textContent = 'Deleting Favourite';
        modalCircuit.textContent = circuit;
        modalCarBrand.textContent = carBrand;
        modalCarModel.textContent = carModel;

        var circuitNameInput = deleteModal.querySelector('#circuitName');
        var carBrandInput = deleteModal.querySelector('#carBrand');
        var carModelInput = deleteModal.querySelector('#carModel');

        circuitNameInput.value = circuit;
        carBrandInput.value = carBrand;
        carModelInput.value = carModel;
    });
});
