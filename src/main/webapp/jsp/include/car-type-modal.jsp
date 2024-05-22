<!--
Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
Version: 1.00
Since: 1.00
-->

<!-- Car type Modal -->
<div class="modal fade" id="newCarTypeModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">New car type</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="carTypeForm" method="post" class="needs-validation" novalidate>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" id="carTypeName" name="name" required>
                        <label for="carTypeName">Car type name</label>
                        <div class="invalid-feedback">
                            Please provide a valid car type name.
                        </div>
                    </div>
                    <input type="submit" class="btn btn-primary" id="addTypeBtn" value="Add">
                </form>
            </div>
        </div>
    </div>
</div>