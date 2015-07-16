/**
 * This script collect parameters from web interface and pass it to php script
 */

var channel_number;        // number of channels
var analysis_region = [];  // region
var location_PU = [];      // pu's location
var countermeasure;        // countermeasure
var queries_number;        // number of queries
var queries_file;          // name of querying file
var args;                  // formatted params as a argument for java program
var email;                 // send result to this email

function getParams () {
    // get number of channels
    channel_number = numberOfChannels;
    // get region boundaries
    if (rect == undefined || recRegion == undefined) {
        alert("Please draw anaysis area. Make sure it covers all primary users.");
        return;
    }
    analysis_region = [];
    // upper lat
    analysis_region.push(recRegion.getNorthEast().lat());
    // right lng
    analysis_region.push(recRegion.getNorthEast().lng());
    // lower lat
    analysis_region.push(recRegion.getSouthWest().lat());
    // left lng
    analysis_region.push(recRegion.getSouthWest().lng());

    if (channel_number == 1 && markers_one.length == 0) {
        alert("Please define working channel and location for primary user!");
        return;
    } 
    if (channel_number == 2 && markers_two_channel0.length == 0 && markers_two_channel1.length == 0) {
        alert("Please define working channel and location for primary user!");
        return;
    } 
    if (channel_number == 3 && markers_three_channel0.length == 0 && markers_three_channel1.length == 0 && markers_three_channel2.length == 0) {
        alert("Please define working channel and location for primary user!");
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

    if (countermeasure == "ADDITIVE_NOISE" && (document.getElementById('cmval').value > 1 || document.getElementById('cmval').value < 0)) {
        alert("Noise level should be in the range from 0 to 1 inclusively");
        return;
    }

    if (countermeasure == "TRANSFIGURATION" && document.getElementById('cmval').value < 3) {
        alert("Sides for polygon should be greater than 2");
        return;
    }

    if (countermeasure == "TRANSFIGURATION" && !(document.getElementById('cmval').value % 1 === 0)) {
        alert("Sides for polygon must be an integer");
        return;
    }

    if (countermeasure == undefined) {
        alert("Undefined countermeasure!");
        return;
    }

    // get query method
    if (document.getElementById("input_query0").checked) {
        queries_number = document.getElementById("queries").value;
        if (queries_number == "") {
            alert("Please specify number of queries!");
            return;
        }
    }
    if (document.getElementById("input_query1").checked) {
        queries_file = file_name;
        if (queries_file == null) {
            alert("Please upload a text file containing location information");
            return;
        }
    }
    if (queries_number == null && queries_file == null) {
        alert("Please specify querying method");
        return;
    }

    // get email
    email = document.getElementById("email").value;
    if (email == "" || email == null || email == undefined) {
        alert("Please provide your email address! Demo result will be sent to this address.");
        return;
    }
    if (!validateEmail(email)) {
        alert("Email address is not valid!");
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

    args += " -e " + email;

    // output formatted args to console
    console.log("args=" + args);

    /* fill in wells */
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

    // email
    document.getElementById("wellemail").innerHTML = email;

    /* if everything works well, start modal */
    $('#myModal').modal({
        backdrop: true,
        keyboard: true,
        show: true
    })
}

function sendParams()
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
            var res = xmlhttp.responseText;
            if (res == "OK") {
                alert("Demo has been started successfully! The simulation will usually take about 30 seconds. Please check your email for result.\nThanks for using!");
            }
            else {
                alert("Demo failed. " + res);
            }
            // reload page
            window.location = 'index-dev.html';
        }
        if (xmlhttp.readyState == 4 && xmlhttp.status != 200)
        {
            // document.getElementById("params").innerHTML=xmlhttp.responseText;
            alert("Sorry. Failed to start demo. " + xmlhttp.status);
            // reload page
            window.location = 'index-dev.html';
        }
    }
    xmlhttp.open("POST","result-dev.php", true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("args=" + args);
}

function validateEmail(email) {
    var re = new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
    return re.test(email);
}