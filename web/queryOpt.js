/*
 * This script is used to select query methods
 */

var numberOfQueries;       // number of queries
var smartNumOfQueries;     // smart number of queries
var file_name;             // file name of user uploaded text file

$(document).ready(function(){
    $('[data-toggle="qtip"]').tooltip();   
});

/**
 * Enable/disable input box for number of queries
 */
function queryInput (args) {
    if (args == "random") {
        document.getElementById("randomQueryInput").disabled = !document.getElementById("randomQueryInput").disabled;
    } else if (args == "smart") {
        document.getElementById("smartQueryInput").disabled = !document.getElementById("smartQueryInput").disabled;
    }
}

/**
 * Adjust value of query numbers so that it is multiple of 10
 * Min: 10, Max: 500
 */
function adjustValue(args) {
    var id = 'id';
    var aux = 'aux_id';
    if (args == 'random') {
        id = 'randomQueryInput';
        aux = 'smartQueryInput';
    } else {
        id = 'smartQueryInput';
        aux = 'randomQueryInput';
    }
    if (!isNumeric(document.getElementById(id).value)) {
        document.getElementById(id).value = 100;
        document.getElementById(aux).value = 100;
        return;
    }
    var val = document.getElementById(id).value;
    var newVal = Math.round(val / 10) * 10;
    if (newVal < 10) {
        newVal = 10;
    }
    if (newVal > 500) {
        newVal = 500;
    }
    document.getElementById(id).value = newVal;
    document.getElementById(aux).value = newVal;
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