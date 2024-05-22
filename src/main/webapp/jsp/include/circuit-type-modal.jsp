<!--
Author: Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
Version: 1.00
Since: 1.00
-->

<!-- New circuit type Modal -->
<div class="modal fade" id="newCircuitTypeModal" tabindex="-1" aria-labelledby="exampleModalLabel"
     aria-hidden="true">

    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="exampleModalLabel">New circuit type</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="circuitTypeForm" method="post" class="needs-validation" novalidate>
                    <div class="form-floating mb-3">
                        <input type="text" class="form-control" id="circuitTypeName" name="name" required>
                        <label for="circuitTypeName">Circuit type name</label>
                        <div class="invalid-feedback">
                            Please provide a valid circuit type name.
                        </div>
                    </div>
                    <input type="submit" class="btn btn-primary" id="addTypeBtn" value="Add">
                </form>
            </div>
        </div>
    </div>
</div>