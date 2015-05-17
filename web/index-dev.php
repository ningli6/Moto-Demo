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
    <script src="googlemap.js"></script>
</head>

<script type="text/javascript">
function SelectPU(objLanguage) {
    var objMedia = document.getElementById("pus");
    objMedia.options.length = 0;
        objMedia.disabled = false;
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
        var str = "<button type='button' class='btn btn-success' onclick='deleteMarkers();'>Clear Markers</button><div id='googleMap' style='width:100%; height:380px;'></div>";
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
        if (isset($_POST["channels"]) || isset($_POST["queries"])) {
            $output = startDemo($number_of_channels, $number_of_queries, $channelErr, $queryErr);
        }
    }
    if ($output == "Program is unable to start!") {
        $alert = "<script type='text/javascript'>window.alert('Fail to start demo')</script>";
        echo $alert;
    }
    else if ($output != "") {
        /* use session to store message to keep values between pages */
        $_SESSION['NUMBER_OF_CHANNELS'] = $number_of_channels;
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

    <form role="form" method="post" id="coor-form">
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
        <button type="submit" class="btn btn-default" onclick="setRecBounds(this.form);">Set boundary</button>
    </form>

    <br>
    <form role="form" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>">
        <div class="form-group">
            <label>Number of channels:</label>
            <input type="number" class="form-control" name="channels" placeholder="Enter number of channels">
            <p class="error">
                <?php 
                    if ($channelErr != "")
                        $str = "<div class='alert alert-danger'>" . $channelErr . "</div>";
                    echo $str; 
                ?>
            </p>
        </div>
        <div class="form-group">
            <label>Number of queries:</label>        
            <input type="number" class="form-control" name="queries" placeholder="Enter number of queries">
            <p class="error">
                <?php 
                    if ($queryErr != "") 
                        $str = "<div class='alert alert-danger'>" . $queryErr . "</div>";
                    echo $str; 
                ?>
            </p>
        </div>
        <button type="submit" class="btn btn-default">Start demo</button>
    </form>
</div>
</body>
</html>