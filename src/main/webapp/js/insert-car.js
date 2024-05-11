$(document).ready(function(){
    const selectedImage = $("#selectedImage");
    const imageInput = $("#image");

    // Display the selected image
    imageInput.change( event => {
        const fileInput = event.target;

        if (fileInput.files && fileInput.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                selectedImage.attr('src', e.target.result);
            };
            reader.readAsDataURL(fileInput.files[0]);
        }
    });
});
