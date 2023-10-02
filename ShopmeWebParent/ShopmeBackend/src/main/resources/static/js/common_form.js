$(document).ready(function () {
  $("#cancelBtn").on("click", function () {
    // use global JS variable defined in user-form and account-form
    window.location = moduleURL;
  });

  $("#image-input").change(function () {
    fileSize = this.files[0].size;
    if (fileSize > 1000000) {
      this.setCustomValidity("File size must be smaller than 1MB");
      this.reportValidity();
    } else {
      this.setCustomValidity("");
      showImageThumbnail(this);
    }
  });
});

function showImageThumbnail(fileInput) {
  let file = fileInput.files[0];
  let reader = new FileReader();
  reader.onload = (e) => {
    $("#thumbnail").attr("src", e.target.result);
  };
  reader.readAsDataURL(file);
}
