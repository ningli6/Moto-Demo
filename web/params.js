/**
 * This script collect parameters from web interface and pass it to php script
 */

var numberOfChannels = 1;  // number of channels [1, 3]
var location_PU = [];      // pu's location
var countermeasure = [];   // countermeasures
var cmVal = []             // cm parameters
var tradeOffAD = false;    // countermeasures that need to plot trade-off curve
var tradeOffTF = false; 
var inputParams = false;   // whether to include input parameters in the email
var plotGoogleMap = false; // whether to include google map in the email
var numberOfQueries;       // number of queries
var queryFile;             // name of querying file
var args;                  // formatted params as a argument for java program
var email;                 // send result to this email

/**
 * Check parameters first, then consturct argument string for backend program,
 * build modal at last
 * @return {void}
 */
function getParams () {
    // check region boundaries
    if (rect == undefined || recRegion == undefined) {
        alert("Please draw anaysis area. Make sure it covers all primary users.");
        return;
    }
    // check number of channels
    if (numberOfChannels < 1 || numberOfChannels > 3) {
        alert("Number of channels is not valid!");
        return;
    }
    // make sure primary user exists on each channel
    if (numberOfChannels == 1 && markers_one.length == 0) {
        alert("Please define working channel and location of primary users!");
        return;
    }

    if (numberOfChannels == 2 && (markers_two_channel0.length == 0 || markers_two_channel1.length == 0)) {
        alert("Please define primary users on both channels!");
        return;
    }

    if (numberOfChannels == 3 && (markers_three_channel0.length == 0 || markers_three_channel1.length == 0 || markers_three_channel2.length == 0)) {
        alert("Please define primary users on all working channels!");
        return;
    }

    // check ic vs q & trade off curve
    if (!document.getElementById("cmval1").checked && document.getElementById("tradeOff1").checked) {
        alert("Trade off box can't be checked unless its countermeasure is selected!");
        return;
    }

    if (!document.getElementById("cmval2").checked && document.getElementById("tradeOff2").checked) {
        alert("Trade off box can't be checked unless its countermeasure is selected!");
        return;
    }

    // get pus' location
    location_PU = [];
    if (numberOfChannels == 1) location_PU.push(markers_one);
    if (numberOfChannels == 2) {
        location_PU.push(markers_two_channel0);
        location_PU.push(markers_two_channel1);
    }
    if (numberOfChannels == 3) {
        location_PU.push(markers_three_channel0);
        location_PU.push(markers_three_channel1);
        location_PU.push(markers_three_channel2);
    }

    countermeasure = [];
    cmVal = [];
    // get countermeasure and cm value
    if (document.getElementById('cmopt0').checked) {
        countermeasure.push("NO_COUNTERMEASURE");
        cmVal.push(null);
    }
    if (document.getElementById('cmopt1').checked) {
        if (!isNumeric(document.getElementById('cmval1').value)) {
            alert("Noise level is not a valid number");
            return;
        }
        var val = document.getElementById('cmval1').value;
        if (val > 1 || val < 0) {
            alert("Noise level should be in the range from 0 to 1 inclusively");
            return;
        }
        countermeasure.push("ADDITIVE_NOISE");
        cmVal.push(val);
        if (document.getElementById('tradeOff1').checked) {
            tradeOffAD = true;
        }
    }
    if (document.getElementById('cmopt2').checked) {
        if (!isNumeric(document.getElementById('cmval2').value)) {
            alert("Number of sides is not a valid number");
            return;
        }
        var val = document.getElementById('cmval2').value;
        if (val < 3 || !(val % 1 === 0)) {
            alert("Sides for polygon should be an integer greater than 2");
            return;
        }
        countermeasure.push("TRANSFIGURATION");
        cmVal.push(val);
        if (document.getElementById('tradeOff2').checked) {
            tradeOffTF = true;
        }
    }
    if (document.getElementById('cmopt3').checked) {
        if (!isNumeric(document.getElementById('cmval3').value)) {
            alert("K for anonymity is not a valid number");
            return;
        }
        var val = document.getElementById('cmval3').value;
        if (val < 0 || !(val % 1 === 0)) {
            alert("K for k anonymity should be an positive integer");
            return;
        }
        countermeasure.push("K_ANONYMITY");
        cmVal.push(val);
    }
    if (document.getElementById('cmopt4').checked) {
        if (!isNumeric(document.getElementById('cmval4').value)) {
            alert("K for clustering is not a valid number");
            return;
        }
        var val = document.getElementById('cmval4').value;
        if (val < 0 || !(val % 1 === 0)) {
            alert("K for k clustering should be an positive integer");
            return;
        }
        countermeasure.push("K_CLUSTERING");
        cmVal.push(val);
    }
    if (countermeasure.length == 0) {
        alert("No countermeasure is selected!");
        return;
    }

    // get query method
    if (document.getElementById("input_query0").checked) {
        if (!isNumeric(document.getElementById('randomQuery').value)) {
            alert("Number of query is not a valid number!");
            return;
        }
        numberOfQueries = document.getElementById("randomQuery").value;
        if (numberOfQueries == undefined) {
            alert("Please specify number of queries!");
            return;
        }
        if (numberOfQueries <= 0 || numberOfQueries % 1 != 0) {
            alert("Number of queries should be a positive integer");
            return;
        }
    }
    if (document.getElementById("input_query1").checked) {
        queryFile = file_name;
        if (queryFile == undefined) {
            alert("Please upload a text file containing location information");
            return;
        }
    }
    if (numberOfQueries == undefined && queryFile == undefined) {
        alert("Please specify querying method");
        return;
    }

    // get email
    email = document.getElementById("email").value;
    if (email == "" || email == null || email == undefined) {
        alert("Please provide your email address! Simulation result will be sent to this address.");
        return;
    }
    if (!validateEmail(email)) {
        alert("Email address is not valid!");
        return;
    }

    // get email option
    if (document.getElementById("inputParams").checked) {
        inputParams = true;
    }
    if (document.getElementById("plotGoogleMap").checked) {
        plotGoogleMap = true;
    }

    // formatting params
    // NE lat, SW lng, SW lat, NE lng
    args = "-a " + recRegion.getNorthEast().lat() + " " + recRegion.getSouthWest().lng() + " " + recRegion.getSouthWest().lat() + " " + recRegion.getNorthEast().lng() + " ";
    args += "-c " + numberOfChannels + " ";
    for (var i = 0; i < location_PU.length; i++) {
        args += "-C ";
        for (var j = 0; j < location_PU[i].length; j++) {
            args += location_PU[i][j].position.lat() + " " + location_PU[i][j].position.lng() + " ";
        }
    }

    switch(countermeasure) {
        case "ADDITIVE_NOISE":
            args += "-an " + cmVal + " ";
            break;
        case "TRANSFIGURATION":
            args += "-tf " + cmVal + " ";
            break;
        case "K_ANONYMITY":
            args += "-ka " + cmVal + " ";
            break;
        case "K_CLUSTERING":
            args += "-kc " + cmVal + " ";
            break;
    }

    if (numberOfQueries != null) {
        args += "-q " + numberOfQueries;
    }
    else {
        args += "-f " + queryFile;
    }

    args += " -e " + email;

    // output formatted args to console
    console.log("args=" + args);

    /* fill in wells */
    // number of channels
    document.getElementById("wellchannel").innerHTML = numberOfChannels;
    // anaysis area
    var regionstr = "";
    regionstr += "North latitude: " + recRegion.getNorthEast().lat() + "<br>";
    regionstr += "South latitude: " + recRegion.getSouthWest().lat() + "<br>";
    regionstr += "West longitude: " + recRegion.getSouthWest().lng() + "<br>";
    regionstr += "East longitude: " + recRegion.getNorthEast().lng() + "<br>";
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
            cmstr += "Additive noise<br>Noise level: " + cmVal;
            break;
        case "TRANSFIGURATION":
            cmstr += "Transfiguration<br>Number of sides: " + cmVal;
            break;
        case "K_ANONYMITY":
            cmstr += "K anonymity<br>K: " + cmVal;
            break;
        case "K_CLUSTERING":
            cmstr += "K Clustering<br>K: " + cmVal;
            break;
        default:
            cmstr += "No countermeasure";
            break;
    }
    document.getElementById("wellcm").innerHTML = cmstr;
    // query
    var querystr = "";
    if (numberOfQueries != null) {
        querystr = "Randomly generated location<br>Number of queries: " + numberOfQueries;
    }
    else {
        querystr = "Use location from " + queryFile;
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
                alert("Demo has been started successfully!\nThe simulation usually takes about 30 seconds. Please check your email for result.\nThanks for using!");
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

/**
 * Validating email address
 * @param  {String} email user email
 * @return {boolean}      true if email is valid
 */
function validateEmail(email) {
    var re = new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
    return re.test(email);
}