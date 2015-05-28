<!-- This is mainly users' input page -->

<!DOCTYPE HTML> 
<html lang="en-US">
<head>
    <title>Moto Demo</title>
    <meta charset="UTF-8">
    <meta name="author" content="Ning Li">
    <meta name="description" 
    content="Demo of countermeasures for protecting Primary User privacy in spectrum sharing">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Latest compiled JavaScript -->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
   <!--  // <script src="http://maps.googleapis.com/maps/api/js"></script> -->
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?v=3.exp&libraries=drawing"></script>
    <!-- Custom styles for this template -->
    <link href="sticky-footer.css" rel="stylesheet">
</head>

<script type="text/javascript">
var map;                         // google map instance
var drawingManager;              // helper object for drawing shapes on google map
var lastShape; var recRegion;       // rectangle object and its boundary
var myCenter = new google.maps.LatLng(37.227799, -80.422054); // center for google map region
var numberOfChannels = 1;; var chanls; // number of channels & user selected channel
var markers_one = [];            // channel for 1 channel
var markers_two_channel0 = [];   // channel 0 for 2 channels
var markers_two_channel1 = [];   // channel 1 for 2 channels
var markers_three_channel0 = []; // channel 0 for 3 channels
var markers_three_channel1 = []; // channel 1 for 3 channels
var markers_three_channel2 = []; // channel 2 for 3 channels
/*
 * Initialize google map
 */
function initialize() {
    try {
        // create a google map
        var mapProp = {
            zoom: 5,
            center: myCenter,
            // mapTypeId: google.maps.MapTypeId.ROADMAP,
            // disableDefaultUI: false,
            // zoomControl: true
        };
        map = new google.maps.Map(document.getElementById("map-canvas"), mapProp);

        // create a drawing manager attached to the map to allow the user to draw markers, lines, and shapes.
        var shapeOptions = {
            strokeWeight: 1,
            strokeOpacity: 1,
            fillOpacity: 0.2,
            editable: true,
            clickable: false,
            strokeColor: '#3399FF',
            fillColor: '#3399FF'
        };
        drawingManager = new google.maps.drawing.DrawingManager({
            drawingMode: null,
            drawingControl: true,
            drawingControlOptions: {
                position: google.maps.ControlPosition.TOP_CENTER,
                drawingModes: [google.maps.drawing.OverlayType.RECTANGLE]
            },
            rectangleOptions: shapeOptions,
            map: map
        });

        google.maps.event.addListener(drawingManager, 'overlaycomplete', function(e) {
            if (lastShape != undefined) {
                lastShape.setMap(null);
            }

            lastShape = e.overlay;
            lastShape.type = e.type;

            if (lastShape.type == google.maps.drawing.OverlayType.RECTANGLE) {
                lastShape.setMap(map);
                recRegion = lastShape.getBounds();
                updatebounds(recRegion);
                console.log(recRegion.toString());
            }
            drawingManager.setDrawingMode(null);
            // Add an event listener on the rectangle.
            google.maps.event.addListener(e.overlay, 'bounds_changed', function() {
                lastShape = e.overlay;
                lastShape.type = e.type;
                recRegion = lastShape.getBounds();
                updatebounds(recRegion);
                console.log("Bounds changed!");
                console.log(recRegion.toString());
            });
        });

        google.maps.event.addListener(map, 'click', function(event) {
            placeMarker(event.latLng);
        });
    }
    catch (err) {
        console.log("Can't deploy google map on this page");
    }
}

google.maps.event.addDomListener(window, 'load', initialize);

/*
 * Place markers on google map
 */
function placeMarker(location) {
    if (lastShape == null || recRegion == null || !recRegion.contains(location)) {
        alert("Must select location with in analysis area");
        return;
    }
    if (chanls == undefined && numberOfChannels != 1) {
        alert("Must select channels first");
        return;
    }
    var marker = new google.maps.Marker({
        position: location,
        map: map,
    });
    var infowindow = new google.maps.InfoWindow({
        content: 'Latitude: ' + location.lat() + '<br>Longitude: ' + location.lng()
    });
    infowindow.open(map,marker);
    if (numberOfChannels == 1) markers_one.push(marker);
    if (numberOfChannels == 2) {
        if (chanls == 0) markers_two_channel0.push(marker);
        if (chanls == 1) markers_two_channel1.push(marker);
    }
    if (numberOfChannels == 3) {
        if (chanls == 0) markers_three_channel0.push(marker);
        if (chanls == 1) markers_three_channel1.push(marker);
        if (chanls == 2) markers_three_channel2.push(marker);
    }
}

// Deletes all markers in the array by removing references to them.
function hideAllMarkers() {
    for (var i = 0; i < markers_one.length; i++) {
        markers_one[i].setMap(null);
    }
    for (var i = 0; i < markers_two_channel0.length; i++) {
        markers_two_channel0[i].setMap(null);
    }
    for (var i = 0; i < markers_two_channel1.length; i++) {
        markers_two_channel1[i].setMap(null);
    }
    for (var i = 0; i < markers_three_channel0.length; i++) {
        markers_three_channel0[i].setMap(null);
    }
    for (var i = 0; i < markers_three_channel1.length; i++) {
        markers_three_channel1[i].setMap(null);
    }
    for (var i = 0; i < markers_three_channel2.length; i++) {
        markers_three_channel2[i].setMap(null);
    }
}
/*
 * Delete all markers on the map
 * Delete rectangle on the map
 */
function resetAllMarkers() {
    if (lastShape != null) lastShape.setMap(null);
    recRegion = null;
    chanls = undefined;
    for (var i = 0; i < markers_one.length; i++) {
        markers_one[i].setMap(null);
    }
    markers_one = [];
    for (var i = 0; i < markers_two_channel0.length; i++) {
        markers_two_channel0[i].setMap(null);
    }
    markers_two_channel0 = [];
    for (var i = 0; i < markers_two_channel1.length; i++) {
        markers_two_channel1[i].setMap(null);
    }
    markers_two_channel1 = [];
    for (var i = 0; i < markers_three_channel0.length; i++) {
        markers_three_channel0[i].setMap(null);
    }
    markers_three_channel0 = [];
    for (var i = 0; i < markers_three_channel1.length; i++) {
        markers_three_channel1[i].setMap(null);
    }
    markers_three_channel1 = [];
    for (var i = 0; i < markers_three_channel2.length; i++) {
        markers_three_channel2[i].setMap(null);
    }
    markers_three_channel2 = [];
}

/*
 * Display markers in @markers, hinding all other markers on map
 */
function markersOnChannel(markers) {
    hideAllMarkers();
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

function updatebounds (recRegion) {
    var boundstr = "<h4>Coordinates</h4>";
    boundstr += "<div class='well'>"
    boundstr += "North latitude: " + recRegion.getNorthEast().lat() + "<br>";
    boundstr += "South latitude: " + recRegion.getSouthWest().lat() + "<br>";
    boundstr += "West longitude: " + recRegion.getSouthWest().lng() + "<br>";
    boundstr += "East longitude: " + recRegion.getNorthEast().lng() + "<br>";
    boundstr += "</div>"
    document.getElementById("showBounds").innerHTML = boundstr;
}
</script>

<script type="text/javascript">
/* 
 * Dispalying google map area in accordance with user specified number of channels
 */
function setChannels() {
    resetAllMarkers();
    var e = document.getElementById("selc");
    numberOfChannels = parseInt(e.options[e.selectedIndex].value);
    switch (numberOfChannels) {
    case 1:
        var str = "<button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += '<span class="help-block">Click rectangle icon to draw analysis area</span>'
        str += "<div id='map-canvas' style='width:100%; height:420px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 2:
        var str = "<button type='button' class='btn btn-info' onclick='selectChannel(0);'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='selectChannel(1);'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += '<span class="help-block">Click rectangle icon to draw analysis area</span>'
        str += "<div id='map-canvas' style='width:100%; height:420px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 3:
        var str = "<button type='button' class='btn btn-info' onclick='selectChannel(0);'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='selectChannel(1);'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-info' onclick='selectChannel(2);'>Select location of PU(s) for channel 2</button>";
        str += " <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += '<span class="help-block">Click rectangle icon to draw analysis area</span>'
        str += "<div id='map-canvas' style='width:100%; height:420px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    default:
        document.getElementById("mapArea").innerHTML = "<p>Unknown choice!</p>";
        break;
    }
}
/*
 * When user select a channel, display only markers on that channel while hiding all others
 */
function selectChannel (arg) {
    chanls = arg;
    if (numberOfChannels == 1) markersOnChannel(markers_one);
    if (numberOfChannels == 2 && chanls == 0) markersOnChannel(markers_two_channel0);
    if (numberOfChannels == 2 && chanls == 1) markersOnChannel(markers_two_channel1);
    if (numberOfChannels == 3 && chanls == 0) markersOnChannel(markers_three_channel0);
    if (numberOfChannels == 3 && chanls == 1) markersOnChannel(markers_three_channel1);
    if (numberOfChannels == 3 && chanls == 2) markersOnChannel(markers_three_channel2);
}
</script>

<script type="text/javascript">
/* 
 * This script is used for displaying "countermeasure" div 
 * according to user selected countermeasure
 */
function clearCounterMeasure (argument) {
    document.getElementById("countermeasure").innerHTML = "";
}

function counterFunc1() {
    var str = "";
    str += '<form clas="form-inline" role="form">';
    str += '<div class="form-group">';
    str += '<label for="noise">Noise level:</label>';
    str += '<input type="number" class="form-control" id="cmval" min="0.0" max="1.0" step="0.1" placeholder="0.5">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}
function counterFunc2 () {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="polygon">Sides for convex polygon:</label>';
    str += '<input type="number" class="form-control" id="cmval" min="3" placeholder="3">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}

function counterFunc3 () {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="ka">K for K-Anonymity:</label>';
    str += '<input type="number" class="form-control" id="cmval" min="1" placeholder="2">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}

function counterFunc4 () {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="kc">K for K-Clustering:</label>';
    str += '<input type="number" class="form-control" id="cmval" min="1" placeholder="2">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}
</script>

<script type="text/javascript">
/*
 * This script is used to display "queryingForm" div according to user's choice of query
 */
function randLoc () {
    document.getElementById("queryingForm").innerHTML = document.getElementById('randomQueries').innerHTML;
}
function upldLoc () {
    document.getElementById("queryingForm").innerHTML = document.getElementById('uploadQueries').innerHTML;
}
</script>

<script id="randomQueries" language="text">
<!-- HTML wraped as script -->
    <form class="form-inline" role="form" method="post" action="">
        <div class="form-group">
            <label>Specify number of queries: </label>
            <input type="number" class="form-control" name="queries" id="queries" placeholder="100" min="1" step="100">
            <p class="error">
                <?php 
                    if ($queryErr != "") 
                        $str = "<div class='alert alert-danger'>" . $queryErr . "</div>";
                    echo $str; 
                ?>
            </p>
        </div>
        <br>
        <!-- <button type="submit" class="btn btn-success" data-toggle="modal" data-target="#myModal" onclick="SendParams(); return false;">Confirm parameters</button> -->
    </form>
</script>

<script id="uploadQueries" language="text">
<!-- HTML wraped as script -->
    <form class="form-inline" role="form" method="post" action='upload.php'>
        <div class="form-group">
            <label>Browse files...</label>
            <input type="file" class="form-control" id="file-select" name="uploadthisfile">
        </div>
        <br><br>
        <button type="submit" class="btn btn-success" id="upload-button" onclick="uploadfile(); return false;">Upload</button>
    </form>
</script>

<script type="text/javascript">
var file_name;  // file name of user uploaded text file
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
</script>

<script type="text/javascript">
var channel_number;        // number of channels
var analysis_region = [];  // region
var location_PU = [];      // pu's location
var countermeasure;        // countermeasure
var queries_number;        // number of queries
var queries_file;          // name of querying file
var args;                  // formatted params as a argument for java program

function getParams () {
    // get number of channels
    channel_number = numberOfChannels;
    if (channel_number <= 0 || channel_number > 3) {
        alert("Channel number invalid! Please check your channel selection");
        return;
    }
    console.log("Channel number: " + channel_number);
    // get region boundaries
    analysis_region = [];
    if (recRegion != null) {
        // upper lat
        analysis_region.push(recRegion.getNorthEast().lat());
        // right lng
        analysis_region.push(recRegion.getNorthEast().lng());
        // lower lat
        analysis_region.push(recRegion.getSouthWest().lat());
        // left lng
        analysis_region.push(recRegion.getSouthWest().lng());
    }
    if (channel_number == 1 && markers_one.length == 0) {
        alert("No primary user on channel 1!");
        return;
    } 
    if (channel_number == 2 && markers_two_channel0.length == 0 && markers_two_channel1.length == 0) {
        alert("No primary user on channel 1 or channel 2!");
        return;
    } 
    if (channel_number == 3 && markers_three_channel0.length == 0 && markers_three_channel1.length == 0 && markers_three_channel2.length == 0) {
        alert("No primary user on channel 1, channel 2 or channel 3!");
        return;
    }
    // get pus' location
    location_PU = [];
    if (channel_number == 1) location_PU.push(markers_one);
    if (channel_number == 2) {
        location_PU.push(markers_two_channel0);
        location_PU.push(markers_two_channel1);
    }
    if (channel_number == 3) {
        location_PU.push(markers_three_channel0);
        location_PU.push(markers_three_channel1);
        location_PU.push(markers_three_channel2);
    }

    // get countermeasure
    if (document.getElementById('cmopt0').checked) {
        countermeasure = "NO_COUNTERMEASURE";
    }
    if (document.getElementById('cmopt1').checked) {
        countermeasure = "ADDITIVE_NOISE";
    }
    if (document.getElementById('cmopt2').checked) {
        countermeasure = "TRANSFIGURATION";
    }
    if (document.getElementById('cmopt3').checked) {
        countermeasure = "K_ANONYMITY";
    }
    if (document.getElementById('cmopt4').checked) {
        countermeasure = "K_CLUSTERING";
    }

    if (countermeasure == undefined) {
        alert("Undefined countermeasure!");
        return;
    }
    console.log("Countermeasure: " + countermeasure);

    // get query method
    queries_file = null; queries_number = null;
    if (document.getElementById("input_query0").checked) {
        queries_number = document.getElementById("queries").value;
        console.log("Query number: " + queries_number);
        if (queries_number < 0 || queries_number == null) {
            alert("Queries must be nonnegative!");
            return;
        }
    }
    if (document.getElementById("input_query1").checked) {
        queries_file = file_name;
        console.log("Query file: " + queries_file);
        if (queries_file == null) {
            alert("File name empty! Please check upload process");
            return;
        }
    }
    if (queries_number == null && queries_file == null) {
        alert("Please specify querying method");
        return;
    }

    // formatting params
    args = "-a " + analysis_region[0] + " " + analysis_region[3] + " " + analysis_region[2] + " " + analysis_region[1] + " ";
    args += "-c " + channel_number + " ";
    for (var i = 0; i < location_PU.length; i++) {
        args += "-C ";
        for (var j = 0; j < location_PU[i].length; j++) {
            args += location_PU[i][j].position.lat() + " " + location_PU[i][j].position.lng() + " ";
        }
    }

    switch(countermeasure) {
        case "ADDITIVE_NOISE":
            args += "-an " + document.getElementById("cmval").value + " ";
            break;
        case "TRANSFIGURATION":
            args += "-tf " + document.getElementById("cmval").value + " ";
            break;
        case "K_ANONYMITY":
            args += "-ka " + document.getElementById("cmval").value + " ";
            break;
        case "K_CLUSTERING":
            args += "-kc " + document.getElementById("cmval").value + " ";
            break;
    }

    if (queries_number != null) {
        args += "-q " + queries_number;
    }
    else {
        args += "-f " + queries_file;
    }
    // output formatted args to console
    console.log("args=" + args);
    // xmlhttp.send("args=" + args);

    // number of channels
    document.getElementById("wellchannel").innerHTML = channel_number;
    // anaysis area
    var regionstr = "";
    regionstr += "North latitude: " + analysis_region[0] + "<br>";
    regionstr += "South latitude: " + analysis_region[2] + "<br>";
    regionstr += "West longitude: " + analysis_region[3] + "<br>";
    regionstr += "East longitude: " + analysis_region[1] + "<br>";
    document.getElementById("wellregion").innerHTML = regionstr;
    // location of pu
    var pustr = "";
    for (var i = 0; i < location_PU.length; i++) {
        pustr += "Primary user on channel " + i + "<br>";
        for (var j = 0; j < location_PU[i].length; j++) {
            pustr += "( " + location_PU[i][j].position.lat() + ", " + location_PU[i][j].position.lng() + " ) <br>";
        }
    }
    document.getElementById("wellpu").innerHTML = pustr;
    // countermeasure
    var cmstr = "";
    switch(countermeasure) {
        case "ADDITIVE_NOISE":
            cmstr += "Additive noise<br>Noise level: " + document.getElementById("cmval").value;
            break;
        case "TRANSFIGURATION":
            cmstr += "Transfiguration<br>Number of sides: " + document.getElementById("cmval").value;
            break;
        case "K_ANONYMITY":
            cmstr += "K anonymity<br>K: " + document.getElementById("cmval").value;
            break;
        case "K_CLUSTERING":
            cmstr += "K Clustering<br>K: " + document.getElementById("cmval").value;
            break;
        default:
            cmstr += "No countermeasure";
            break;
    }
    document.getElementById("wellcm").innerHTML = cmstr;
    // query
    var querystr = "";
    if (queries_number != null) {
        querystr = "Randomly generated location<br>Number of queries: " + queries_number;
    }
    else {
        querystr = "Use location from " + queries_file;
    }
    document.getElementById("wellquery").innerHTML = querystr;
}

function SendParams()
{
    var xmlhttp;
    if (window.XMLHttpRequest)
        {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        }
    else
    {// code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200)
        {
            // document.getElementById("params").innerHTML=xmlhttp.responseText;
        }
    }
    xmlhttp.open("POST","ajax.php", true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    var args = "-a " + ulla + " " + ullg + " " + lrla + " " + lrlg + " ";
    args += "-c " + numberOfChannels + " -C ";
    for (var i = 0; i < markers.length; i++) {
        args += markers[i].position.lat() + " " + markers[i].position.lng() + " ";
    }
    var nq = document.getElementById("queries").value;
    args += "-q " + nq;
    console.log("args=" + args);
    xmlhttp.send("args=" + args);
    var fstr = "args=" + args;
    document.getElementById("confirmParam").innerHTML = fstr;
    // window.alert("args: " + args);
}
</script>

<?php
    /* add external helper php script */
    session_start();
    /* nuber of channels */
    $number_of_channels;
    /* nuber of queries */
    $number_of_queries;
    /* error message */
    $channelErr = $queryErr = "";
    /* handle form submit */
    if ($_SERVER["REQUEST_METHOD"] == "POST") {
        // $args = $_REQUEST['args'];
        $args = $_SESSION["args"];
        // if (empty($args)) {
        //     echo "Args is empty";
        // } else {
        //     echo $args;
        // }
        // $log = "<script type='text/javascript'>console.log('Fail to start demo')</script>";
        // echo $args;
        if (!empty($args)) {
            $output = "";
            // $output = startDemo($args);
            $command = "java -cp Project tests/Main " . $args;
            // echo $command;
            exec($command, $output);
            if (empty($output)) 
                $output = "Program is unable to start!";
            // echo $output;
        }
        // if (isset($_POST["queries"])) {
        //     // $output = startDemo($number_of_channels, $number_of_queries, $channelErr, $queryErr);
        // }
    }
    if ($output == "Program is unable to start!") {
        $alert = "<script type='text/javascript'>window.alert('Fail to start demo')</script>";
        echo $alert;
    }
    else if ($output != "") {
        /* use session to store message to keep values between pages */
        // $_SESSION['NUMBER_OF_CHANNELS'] = $number_of_channels;
        // $_SESSION['NUMBER_OF_QUERIES'] = $number_of_queries;
        $_SESSION['OUTPUT'] = $output;
        // echo "Jump";
        /* jump to result page */
        $str = "<script>window.location = 'result.php';</script>";
        echo $str;
    }
?>

<body>
<div class="container">
    <div class="jumbotron">
        <h2>Moto demo</h2>      
        <p>
            This is a demo of techniques for protecting the Primary Users’ operational privacy in spectrum sharing 
            proposed in the paper: 
        </p>
        <p>
            <cite>
                Protecting the Primary Users’ Operational Privacy in Spectrum Sharing. 
                <small>[2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]</small>
            </cite>
        </p>
    </div>

    <!-- dropdown -->
    <h3>Specify number of channels</h3>
    <div class="row">
        <form role="form">
            <div class="form-group">
                <div class="col-md-3">
                <select class="form-control" id="selc" onchange="setChannels();">
                    <option class="optclass" selected="selected">1</option>
                    <option class="optclass">2</option>
                    <option class="optclass">3</option>
                </select>
                </div>
            </div>
        </form>
    </div>

    <!-- google map -->
    <h3>Specify analysis area and location of primary users</h3> 
    <div id="mapArea">
        <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>
        <span class="help-block">Click rectangle icon to draw analysis area</span>
        <div id='map-canvas' style='width:100%; height:420px;'></div>
    </div>
    <div id="showBounds"></div>

    <!-- choose countermeasure -->
    <form role="form">
        <h3>Choose countermeasure</h2>
        <div class="radio">
          <label><input type="radio" id="cmopt0" name="cmopt" value=0 checked="checked" onchange="clearCounterMeasure();">No countermeasure</label>
        </div>
        <div class="radio">
          <label><input type="radio" id="cmopt1" name="cmopt" value=1 onchange="counterFunc1();">Additive Noise</label>
        </div>
        <div class="radio">
          <label><input type="radio" id="cmopt2" name="cmopt" value=2 onchange="counterFunc2();">Transfiguration</label>
        </div>
        <div class="radio">
          <label><input type="radio" id="cmopt3" name="cmopt" value=3 onchange="counterFunc3();">K-Anonymity</label>
        </div>
        <div class="radio">
          <label><input type="radio" id="cmopt4" name="cmopt" value=4 onchange="counterFunc4();">K-Clustering</label>
        </div>
    </form>

    <div class="row">
        <div class="col-md-4" id="countermeasure"></div>
    </div>

    <form role="form">
        <h3>Specify queries</h3>
        <div class="radio">
          <label><input type="radio" id="input_query0" name="input_query" value="0" onchange="randLoc();">Generate query locations randomly</label>
        </div>
        <div class="radio">
          <label><input type="radio" id="input_query1" name="input_query" value="1" onchange="upldLoc();">Upload text files specifing query locations</label>
        </div>
    </form>

    <div id="queryingForm"></div>

    <!-- Modal -->
    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog">
          <!-- Modal content-->
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">Confirm parameters</h4>
            </div>
            <div class="modal-body">
                <p>Number of channels</p>
                <div class="well" id="wellchannel"></div>
                <p>Analysis region</p>
                <div class="well" id="wellregion"></div>
                <p>Location of Primary user</p>
                <div class="well" id="wellpu"></div>
                <p>Countermeasure</p>
                <div class="well" id="wellcm"></div>
                <p>Queries</p>
                <div class="well" id="wellquery"></div>
                <p id="confirmParam"></p>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
          </div>   
        </div>
    </div>
    <br>
    <!-- result of passed params -->
    <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
        <button type="submit" class="btn btn-primary" data-toggle="modal" data-target="#myModal" onclick="getParams(); return false;">Start demo <span class="glyphicon glyphicon-play"></span></button>
    </form>
    <br><br>
</div>

<footer class="footer">
    <div class="container" id="fter" style="margin-right: 55px">
        <p class="text-muted" style="text-align: right"><a href="#">Contact us</a></p>
    </div>
</footer>

</body>
</html>