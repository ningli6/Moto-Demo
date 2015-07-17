/*
 * This script is used to select query methods
 */

// file name of user uploaded text file
var file_name;  

function disableRandomQueries () {
    document.getElementById("randomQuery").disabled = true;
}

function enableRandomQueries () {
    document.getElementById("randomQuery").disabled = false;
}

function uploadfile () {
    var form = document.getElementById('file-form');
    var fileSelect = document.getElementById('file-select');
    var uploadButton = document.getElementById('upload-button');
    // Update button text.
    uploadButton.innerHTML = 'Uploading...';
    // Get the selected files from the input.
    var files = fileSelect.files;
    // Create a new FormData object.
    var formData = new FormData();
    // Get the file name.
    var file = files[0];
    file_name = file.name;
    // Attach file in FormData
    formData.append('uploadthisfile', file, file.name);
    // Set up the request.
    var xhr = new XMLHttpRequest();
    // Open the connection.
    xhr.open('POST', 'upload.php', true);

    // Set up a handler for when the request finishes.
    xhr.onload = function () {
        if (xhr.status === 200) {
            alert(xhr.responseText);
            // File(s) uploaded.
            uploadButton.innerHTML = 'Upload';
        } else {
            alert('An error occurred!');
        }
    };
    // Send the Data.
    xhr.send(formData);
}