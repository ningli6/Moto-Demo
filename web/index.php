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
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false&libraries=drawing"></script>
    <!-- Custom styles for this template -->
    <link href="sticky-footer.css" rel="stylesheet">
</head>

<script type="text/javascript">
var map;
var drawingManager; var lastShape; var bounds;
var myCenter = new google.maps.LatLng(37.227799, -80.422054);
var numberOfChannels = 1;; var chanls = 1;
var markers_one = [];
var markers_two_channel0 = [];
var markers_two_channel1 = [];
var markers_three_channel0 = [];
var markers_three_channel1 = [];
var markers_three_channel2 = [];
var ulla; var ullg; var lrla; var lrlg;

function initialize() {
    try {
        var mapProp = {
            zoom: 5,
            center: myCenter,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            disableDefaultUI: true,
            zoomControl: true
        };

        var shapeOptions = {
            strokeWeight: 1,
            strokeOpacity: 1,
            fillOpacity: 0.2,
            editable: true,
            clickable: false,
            strokeColor: '#3399FF',
            fillColor: '#3399FF'
        };

        map = new google.maps.Map(document.getElementById("googleMap"), mapProp);

        // create a drawing manager attached to the map to allow the user to draw markers, lines, and shapes.
        drawingManager = new google.maps.drawing.DrawingManager({
            drawingMode: null,
            drawingControlOptions: {drawingModes: [google.maps.drawing.OverlayType.RECTANGLE]},
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
                bounds = lastShape.getBounds();
                console.log(bounds.toString());
            }
            map.setOptions({draggable: true});
                        drawingManager.setDrawingMode(null);
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

function placeMarker(location) {
    console.log(numberOfChannels);
    console.log(chanls);

    if (lastShape == null || bounds == null || !bounds.contains(location)) {
        alert("Must select location with in analysis area");
        return;
    }
    if (chanls == -1) {
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

function showMarkers() {
    if (markers.length == 0) {
        console.log("No PU");
        document.getElementById("markers").innerHTML = "<p>No Primary user on the map</p>";
        return;
    }
    var html = "<p>";
    for (var index = 0; index < markers.length; index++) {
        html += markers[index].position.lat() + ', ' + markers[index].position.lng() + '<br>';
    }
    html += "</p>";
    document.getElementById("markers").innerHTML = html;
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

function resetAllMarkers() {
    if (lastShape != null) lastShape.setMap(null);
    bounds = null;
    chanls = -1;
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

function countDot(str) {
    var count = 0;
    for (var i = 0; i < str.length; i++) {
        if (str.charAt(i) == '.') count++;
    }
    return count;
}

function markersOnChannel(markers) {
    hideAllMarkers();
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(map);
    }
}

function SendParams()
{
    var xmlhttp;
    if (window.XMLHttpRequest)
        {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
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

<script type="text/javascript">
function getChannel() {
    resetAllMarkers();
    var e = document.getElementById("selc");
    numberOfChannels = parseInt(e.options[e.selectedIndex].value);
    console.log(numberOfChannels);
    switch (numberOfChannels) {
    case 1:
        console.log("Channel: " + numberOfChannels);
        var str = "<button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += "<br><br><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 2:
        console.log("Channel: " + numberOfChannels);
        var str = "<button type='button' class='btn btn-info' onclick='selectChannel(0);'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='selectChannel(1);'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += "<br><br><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 3:
        console.log("Channel: " + numberOfChannels);
        var str = "<button type='button' class='btn btn-info' onclick='selectChannel(0);'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='selectChannel(1);'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-info' onclick='selectChannel(2);'>Select location of PU(s) for channel 2</button>";
        str += " <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += "<br><br><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    default:
        document.getElementById("mapArea").innerHTML = "<p>Unknown choice!</p>";
        break;
    }
}
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
function clearCounterMeasure (argument) {
    document.getElementById("countermeasure").innerHTML = "";
}

function counterFunc1() {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="noise">Noise level:</label>';
    str += '<input type="number" class="form-control" id="noise" min="0.0" max="1.0" step="0.1" placeholder="0.5">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}
function counterFunc2 () {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="polygon">Sides for convex polygon:</label>';
    str += '<input type="number" class="form-control" id="sides" min="3" placeholder="3">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}

function counterFunc3 () {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="ka">K for K-Anonymity:</label>';
    str += '<input type="number" class="form-control" id="kanonymity" min="1" placeholder="2">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}

function counterFunc4 () {
    var str = "";
    str += '<form role="form">';
    str += '<div class="form-group">';
    str += '<label for="kc">K for K-Clustring:</label>';
    str += '<input type="number" class="form-control" id="kclustering" min="1" placeholder="2">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}
</script>

<script type="text/javascript">
function randLoc () {
    document.getElementById("formGoesHere").innerHTML = document.getElementById('form_numberOfQueries').innerHTML;
}
function upldLoc () {
    document.getElementById("formGoesHere").innerHTML = document.getElementById('form_uploadQueries').innerHTML;
}
</script>

<script id="form_numberOfQueries" language="text">
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
        <button type="submit" class="btn btn-success" data-toggle="modal" data-target="#myModal" onclick="SendParams(); return false;">Confirm parameters</button>
    </form>
</script>

<script id="form_uploadQueries" language="text">
    <form class="form-inline" role="form" method="post" action="">
        <div class="form-group">
            <label>Browse files...</label>
            <input type="file" class="form-control" name="queries" id="queries">
        </div>
        <br><br>
        <button type="submit" class="btn btn-success" data-toggle="modal" data-target="#myModal" onclick="SendParams(); return false;">Confirm parameters</button>
    </form>
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
        // header('Location:result.php');
        echo $str;
    }
?>

<body>
<div class="container">
    <div class="jumbotron">
        <h2>Moto demo</h2>      
        <p>
            This is a demo for several techniques for protecting the Primary Users’ operational privacy in spectrum sharing 
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
                <select class="form-control" id="selc" onchange="getChannel();">
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
       <span class="help-block">
            Click rectangle icon to draw analysis area
        </span>
        <div id='googleMap' style='width:100%; height:420px;'></div>
    </div>

    <!-- choose countermeasure -->
    <form role="form">
        <h3>Choose countermeasure</h2>
        <div class="radio">
          <label><input type="radio" name="cmopt" value="0" onchange="clearCounterMeasure();">No countermeasure</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="cmopt" value="1" onchange="counterFunc1();">Additive Noise</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="cmopt" value="2" onchange="counterFunc2();">Transfiguration</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="cmopt" value="3" onchange="counterFunc3();">K-Anonymity</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="cmopt" value="4" onchange="counterFunc4();">K-Clustering</label>
        </div>
    </form>

    <div class="row">
        <div class="col-md-4" id="countermeasure"></div>
    </div>

    <form role="form">
        <h3>Specify queries</h3>
        <div class="radio">
          <label><input type="radio" name="rand_upload" value="0" onchange="randLoc();">Generate query locations randomly</label>
        </div>
        <div class="radio">
          <label><input type="radio" name="rand_upload" value="1" onchange="upldLoc();">Upload text files specifing query locations</label>
        </div>
    </form>

    <div id="formGoesHere"></div>

    <!-- Modal -->
    <div class="modal fade" id="myModal" role="dialog">
        <div class="modal-dialog">
          <!-- Modal content-->
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal">&times;</button>
              <h4 class="modal-title">Parameters</h4>
            </div>
            <div class="modal-body">
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
        <button type="submit" class="btn btn-primary">Start demo <span class="glyphicon glyphicon-play"></span></button>
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