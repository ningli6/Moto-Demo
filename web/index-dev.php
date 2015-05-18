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
    <script src="http://maps.googleapis.com/maps/api/js"></script>
</head>

<script type="text/javascript">
var myCenter=new google.maps.LatLng(37.227799, -80.422054);
var map;
var markers = [];
var ulla; var ullg; var lrla; var lrlg;

function initialize() {
    var mapProp = {
        center:myCenter,
        zoom:5,
        mapTypeId:google.maps.MapTypeId.ROADMAP
    };
    try {
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
    if (ulla == null) {
        ulla = 38;
    }
    if (ullg == null) {
        ullg = -82;
    }
    if (lrla == null) {
        lrla = 36;
    }
    if (lrlg == null) {
        lrlg = -79;
    }
    var ul = new google.maps.LatLng(ulla, ullg);
    var ur = new google.maps.LatLng(ulla, lrlg);
    var ll = new google.maps.LatLng(lrla, ullg);
    var lr = new google.maps.LatLng(lrla, lrlg);
    var bounds=[ul,ur,lr,ll,ul];
    var path=new google.maps.Polyline({
      path:bounds,
      strokeColor:"#0000FF",
      strokeOpacity:0.8,
      strokeWeight:2
      });
    path.setMap(map);
    google.maps.event.addListener(map, 'click', function(event) {
        placeMarker(event.latLng);
    });
    }
    catch (err) {
        console.log("Can't deploy google map on this page");
    }
}
google.maps.event.addDomListener(window, 'load', initialize);

function setRecBounds (form) {
    ulla = form.ulla.value;
    ullg = form.ullg.value;
    lrla = form.lrla.value;
    lrlg = form.lrlg.value;
    var n1 = ulla.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    var n2 = ullg.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    var n3 = lrla.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    var n4 = lrlg.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    if (n1 != 0 || n2 != 0 || n3 != 0 || n4 != 0 || 
        (countDot(ulla) > 1) ||
        (countDot(ullg) > 1) ||
        (countDot(lrla) > 1) ||
        (countDot(lrlg) > 1)) {
        alert("Invalid coordinates!");
        return;
    }
    initialize();
}

function placeMarker(location) {
  var marker = new google.maps.Marker({
    position: location,
    map: map,
  });
  var infowindow = new google.maps.InfoWindow({
    content: 'Latitude: ' + location.lat() + '<br>Longitude: ' + location.lng()
  });
  infowindow.open(map,marker);
  markers.push(marker);
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
function hideMarkers() {
  for (var i = 0; i < markers.length; i++) {
    markers[i].setMap(null);
  }
}

function deleteMarkers() {
    hideMarkers();
    markers = [];
    showMarkers();
}

function countDot(str) {
    var count = 0;
    for (var i = 0; i < str.length; i++) {
        if (str.charAt(i) == '.') count++;
    }
    return count;
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
            document.getElementById("params").innerHTML=xmlhttp.responseText;
        }
    }
    xmlhttp.open("POST","ajax.php", true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    var args = "-a " + ulla + " " + ullg + " " + lrla + " " + lrlg + " ";
    args += "-c " + selectVal + " -C ";
    for (var i = 0; i < markers.length; i++) {
        args += markers[i].position.lat() + " " + markers[i].position.lng() + " ";
    }
    var nq = document.getElementById("queries").value;
    args += "-p " + nq;
    console.log("args=" + args);
    xmlhttp.send("args=" + args);
    // window.alert("args: " + args);
}
</script>

<script type="text/javascript">
var selectVal;
function SelectPU(objLanguage) {
    var objMedia = document.getElementById("pus");
    objMedia.options.length = 0;
        objMedia.disabled = false;
    selectVal = objLanguage.value;
    switch (objLanguage.value) {
    case "1":
        objMedia.options.add(new Option("Specify location of PUs"));
        objMedia.options.add(new Option("Specify location of PUs on this channel"));
        break;
    case "2":
        objMedia.options.add(new Option("Specify location of PUs"));
        objMedia.options.add(new Option("Specify location of PUs on channel 0"));
        objMedia.options.add(new Option("Specify location of PUs on channel 1"));
        break;
    case "3":
        objMedia.options.add(new Option("Specify location of PUs"));
        objMedia.options.add(new Option("Specify location of PUs on channel 0"));
        objMedia.options.add(new Option("Specify location of PUs on channel 1"));
        objMedia.options.add(new Option("Specify location of PUs on channel 2"));
        break;
    default:
        objMedia.options.add(new Option("Specify location of PUs"));
        objMedia.disabled = true;
        break;
    }
}
</script>

<script type="text/javascript">
function getContent() {
    var e = document.getElementById("pus");
    var strPU = e.options[e.selectedIndex].value;
    console.log(strPU);
    switch (strPU) {
    case "Specify location of PUs on this channel":
        var str = "<button type='button' class='btn btn-danger' onclick='deleteMarkers();'>Clear Markers</button>";
        str += "<button type='button' class='btn btn-warning' onclick='hideMarkers();'>Hide Markers</button>";
        str += "<button type='button' class='btn btn-success' onclick='showMarkers();'>Show Markers</button><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case "Specify location of PUs on channel 0":
        document.getElementById("mapArea").innerHTML = "<p>Good choice!</p>";
        break;
    case "Specify location of PUs on channel 1":
        document.getElementById("mapArea").innerHTML = "<p>Good choice!</p>";
        break;
    case "Specify location of PUs on channel 2":
        document.getElementById("mapArea").innerHTML = "<p>Good choice!</p>";
        break;  
    default:
        document.getElementById("mapArea").innerHTML = "<p>Default choice!</p>";
        break;
    }
}
</script>

<?php
    /* add external helper php script */
    require 'script.php';
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
        if (!empty($args)) 
            $output = startDemo($args);
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
        $_SESSION['NUMBER_OF_QUERIES'] = $number_of_queries;
        $_SESSION['OUTPUT'] = $output;
        /* jump to result page */
        header('Location: result.php');
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
                [2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]
            </cite>
        </p>
    </div>

    <!-- dropdown list -->
    <select name="number_of_channels" id="channels" onchange="SelectPU(this)">
        <option>Number of channels</option>
        <option>1</option>
        <option>2</option>
        <option>3</option>
    </select>
    <select name="PU_on_each_channel" id="pus" disabled="disabled" onchange="if (this.selectedIndex) getContent();">
        <option>Specify location of PUs</option>
    </select>

    <!-- google map -->
    <div id="mapArea"></div>

    <form role="form" method="post" id="coor-form" action="">
        <div class="form-group">
            <label>Upper left latitude:</label>
            <input type="number" class="form-control" name="ulla" value="38">
        </div>
        <div class="form-group">
            <label>Upper left longitude:</label>
            <input type="number" class="form-control" name="ullg" value="-82">
        </div>
        <div class="form-group">
            <label>Lower right latitude:</label>
            <input type="number" class="form-control" name="lrla" value="36">
        </div>
        <div class="form-group">
            <label>Lower right longitude:</label>
            <input type="number" class="form-control" name="lrlg" value="-79">
        </div>
        <button type="submit" class="btn btn-default" onclick="setRecBounds(this.form); return false;">Set boundary</button>
    </form>

    <!-- show location of PU -->
    <div id="markers"></div>

    <!-- <form role="form" method="post" action="<?php //echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> -->
    <form role="form" method="post" action="">
        <div class="form-group">
            <label>Number of queries:</label>        
            <input type="number" class="form-control" name="queries" id="queries" placeholder="Enter number of queries">
            <p class="error">
                <?php 
                    if ($queryErr != "") 
                        $str = "<div class='alert alert-danger'>" . $queryErr . "</div>";
                    echo $str; 
                ?>
            </p>
        </div>
        <button type="submit" class="btn btn-default" onclick="SendParams(); return false;">Show params</button>
    </form>

    <!-- result of passed params -->
    <div><p id="params"></p></div>
    <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
        <button type="submit" class="btn btn-default">Start demo</button>
    </form>
</div>
</body>
</html>