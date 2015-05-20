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
    var numberOfChannels = 1;
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
    // function setBorder() {
    //     var form = document.getElementById('region');
    //     var str = '<form role="form" method="post" id="coor-form" action="">';
    //     str += '<div class="col-md-12"><button type="submit" class="btn btn-primary" onclick="setRecBounds(this.form); return false;">Set boundary</button></div>';
    //     str += '<div class="col-md-12"><br></div>'
    //     str += '<div class="col-md-6"><div class="form-group"><label>Upper left latitude:</label><input type="number" class="form-control" name="ulla" value="38"></div></div>';
    //     str += '<div class="col-md-6"><div class="form-group"><label>Upper left longitude:</label><input type="number" class="form-control" name="ullg" value="-82"></div></div>';
    //     str += '<div class="col-md-6"><div class="form-group"><label>Lower right latitude:</label><input type="number" class="form-control" name="lrla" value="36"></div></div>';
    //     str += '<div class="col-md-6"><div class="form-group"><label>Lower right longitude:</label><input type="number" class="form-control" name="lrlg" value="-79"></div></div>';
    //     str += '</form>';
    //     form.innerHTML = str;
    // }
</script>

<script type="text/javascript">
function getContent() {
    // setBorder();
    var e = document.getElementById("selc");
    numberOfChannels = parseInt(e.options[e.selectedIndex].value);
    console.log(numberOfChannels);
    switch (numberOfChannels) {
    case 1:
        var str = "<button type='button' class='btn btn-warning' onclick='deleteMarkers();'>Reset</button>";
        str += "<br><br><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 2:
        var str = "<button type='button' class='btn btn-info' onclick='select1();'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='select2();'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-warning' onclick='deleteMarkers();'>Reset</button>";
        str += "<br><br><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 3:
        var str = "<button type='button' class='btn btn-info' onclick='select1();'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='select2();'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-info' onclick='select3();'>Select location of PU(s) for channel 2</button>";
        str += " <button type='button' class='btn btn-warning' onclick='deleteMarkers();'>Reset</button>";
        str += "<br><br><div id='googleMap' style='width:100%; height:380px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    default:
        document.getElementById("mapArea").innerHTML = "<p>Unknown choice!</p>";
        break;
    }
}
</script>

<?php
    /* add external helper php script */
    // require 'script-dev.php';
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
        $str = "<script>window.location = 'result-dev.php';</script>";
        // header('Location:result-dev.php');
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
                [2014 IEEE International Symposium on Dynamic Spectrum Access Networks (DYSPAN), p 236-47, 2014]
            </cite>
        </p>
    </div>

    <!-- dropdown -->
    <h3>Specify number of channels</h3>
    <div class="row">
        <form role="form">
            <div class="form-group">
                <div class="col-md-3">
                <select class="form-control" id="selc" onchange="getContent();">
                    <option>1</option>
                    <option>2</option>
                    <option>3</option>
                </select>
                </div>
            </div>
        </form>
    </div>

    <!-- anaysis region -->
    <h3>Specify analysis region</h3>
    <div id="region">
        <form role="form" method="post" id="coor-form" action="">
        <div class="col-md-12"><button type="submit" class="btn btn-primary" onclick="setRecBounds(this.form); return false;">Set boundary</button></div>
        <div class="col-md-12"><br></div>
        <div class="col-md-6"><div class="form-group"><label>Upper left latitude:</label><input type="number" class="form-control" name="ulla" value="38"></div></div>
        <div class="col-md-6"><div class="form-group"><label>Upper left longitude:</label><input type="number" class="form-control" name="ullg" value="-82"></div></div>
        <div class="col-md-6"><div class="form-group"><label>Lower right latitude:</label><input type="number" class="form-control" name="lrla" value="36"></div></div>
        <div class="col-md-6"><div class="form-group"><label>Lower right longitude:</label><input type="number" class="form-control" name="lrlg" value="-79"></div></div>
    </div>
    <!-- google map -->
    <h3>Specify location of Primary users</h3> 
    <div id="mapArea">
        <button type='button' class='btn btn-warning' onclick='deleteMarkers();'>Reset</button>
        <br><br>
        <div id='googleMap' style='width:100%; height:380px;'></div>
    </div>

    <h3>Specify number of queries</h3>
    <!-- <form role="form" method="post" action="<?php //echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> -->
    <form role="form" method="post" action="">
        <div class="row">
            <div class="form-group">
                <div class="col-md-4">
                <input type="number" class="form-control" name="queries" id="queries" placeholder="Enter number of queries">
                </div>
                <p class="error">
                    <?php 
                        if ($queryErr != "") 
                            $str = "<div class='alert alert-danger'>" . $queryErr . "</div>";
                        echo $str; 
                    ?>
                </p>
            </div>
        </div>
        <br>
        <button type="submit" class="btn btn-success" data-toggle="modal" data-target="#myModal" onclick="SendParams(); return false;">Confirm parameters</button>
    </form>

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

    <br><br>
    <!-- result of passed params -->
    <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
        <button type="submit" class="btn btn-primary">Start demo</button>
    </form>
</div>
</body>
</html>