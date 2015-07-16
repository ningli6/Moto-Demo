/*
 * This script is used to select query methods
 */

// file name of user uploaded text file
var file_name;  

// html code for queryingForm with randomly generated queries
var randQueries = '';
randQueries += '<form class="form-inline" role="form" method="post" action="">';
randQueries += '<div class="form-group">';
randQueries += '<label>Specify number of queries: </label>';
randQueries += '<input type="number" class="form-control" name="queries" id="queries" placeholder="100" min="1" step="100">';
randQueries += '</div><br></form>';

// html code for queryingForm with uploaded queries
var uploadQueries = '';
uploadQueries += '<form class="form-inline" role="form" method="post" action="upload.php">';
uploadQueries += '<div class="form-group">';
uploadQueries += '<label>Browse files...</label>';
uploadQueries += '<input type="file" class="form-control" id="file-select" name="uploadthisfile">';
uploadQueries += '<button type="submit" class="btn btn-default" id="upload-button" onclick="uploadfile(); return false;">Upload</button>';
uploadQueries += '</div></form>';

function randLoc () {
    document.getElementById("queryingForm").innerHTML = randQueries;
}
function upldLoc () {
    document.getElementById("queryingForm").innerHTML = uploadQueries;
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