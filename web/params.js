/**
 * This script collect parameters from web interface and pass it to php script
 */

var location_PU;      // pu's location
var email;            // send result to this email
var inputParams;      // whether to include input parameters in the email
var args;             // formatted params as a argument for java program

/**
 * Check parameters first, then consturct argument string for backend program,
 * build modal at last
 */
function getParams () {
    /**
     * Reset parameters when page is refreshed
     */
    /* save location of PUs */
    location_PU = [];    // list of list of markers
    /* Countermeasures and their value */
    countermeasure = []; // list of string
    cmVal = [];          // list of number
    /* Plot options */
    gmapNO = false;
    tradeOffAD = false;
    gmapAD = false;
    tradeOffTF = false;
    gmapTF = false;
    tradeOffKA = false;
    gmapKA = false;
    tradeOffKC = false;
    gmapKC = false;
    /* Whether to include input parameters */
    inputParams = false;
    /* Number of queries */
    numberOfQueries = -1;
    /* Email address */
    email = "";
    /* Passing arguments */
    args = "";

    /**
     * Check necessary parameters and verify their value
     */
    // check cell size, if cell size not equal to 0.005, 0.01 or 0.05, assign it to 0.005
    if (cellSize != 0.005 && cellSize != 0.01 && cellSize != 0.05) {
        cellSize = 0.005;
    }
    // check parameters and grab values from forms
    if (!(rect && recRegion)) {
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

    // check ic vs q
    if (!document.getElementById("cmopt0").checked && document.getElementById("gmno").checked) {
        alert("No countermeasure must be selected!");
        return;
    }

    if (!document.getElementById("cmopt1").checked && (document.getElementById("tradeOff1").checked || document.getElementById("gmad").checked)) {
        alert("Countermeasure additive noise must be selected!");
        return;
    }

    if (!document.getElementById("cmopt2").checked && (document.getElementById("tradeOff2").checked || document.getElementById("gmtf").checked)) {
        alert("Countermeasure transfiguration must be selected!");
        return;
    }

    if (!document.getElementById("cmopt3").checked && (document.getElementById("gmka").checked || document.getElementById("tradeOff3").checked)) {
        alert("Countermeasure k anonymity must be selected!");
        return;
    }

    if (!document.getElementById("cmopt4").checked && (document.getElementById("gmkc").checked || document.getElementById("tradeOff4").checked)) {
        alert("Countermeasure k clustering must be selected!");
        return;
    }

    // make sure number of markers are more than 1 when checking trade off bar of KA and KC
    if (numberOfMarkers < 2 && (document.getElementById("tradeOff3").checked || document.getElementById("tradeOff4").checked)) {
        alert("Trade off bar of K-Anoymity and K-Clustering should be selected only when number of primary users is more than 1");
        return;
    }

    // User must select at least one option
    if (!document.getElementById("cmopt0").checked
        && !document.getElementById("cmopt1").checked
        && !document.getElementById("cmopt2").checked
        && !document.getElementById("cmopt3").checked
        && !document.getElementById("cmopt4").checked) {
        alert("Please select at least one option under Inaccuracy vs Queries!");
        return;
    }

    // at leaset one method of query need to be chosen
    if (!document.getElementById("randomQuery").checked && !document.getElementById("smartQuery").checked) {
        alert("Please choose at least one method of query");
        return;
    }

    // push list of markers to location list
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

    // get countermeasure, cm value, google map options, trade off curve options
    if (document.getElementById('cmopt0').checked) {
        countermeasure.push("no");
        cmVal.push(-1);
        if (document.getElementById('gmno').checked) {
            gmapNO = true;
        }
        else {
            gmapNO = false;
        }
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
        countermeasure.push("an");
        cmVal.push(val);
        if (document.getElementById('tradeOff1').checked) {
            tradeOffAD = true;
        }
        else {
            tradeOffAD = false;
        }
        if (document.getElementById('gmad').checked) {
            gmapAD = true;
        }
        else {
            gmapAD = false;
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
        countermeasure.push("tf");
        cmVal.push(val);
        if (document.getElementById('tradeOff2').checked) {
            tradeOffTF = true;
        }
        else {
            tradeOffTF = false;
        }
        if (document.getElementById('gmtf').checked) {
            gmapTF = true;
        }
        else {
            gmapTF = false;
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
        countermeasure.push("ka");
        cmVal.push(val);
        if (document.getElementById('tradeOff3').checked) {
            tradeOffKA = true;
        }
        else {
            tradeOffKA = false;
        }
        if (document.getElementById('gmka').checked) {
            gmapKA = true;
        }
        else {
            gmapKA = false;
        }
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
        countermeasure.push("kc");
        cmVal.push(val);
        if (document.getElementById('tradeOff4').checked) {
            tradeOffKC = true;
        }
        else {
            tradeOffKC = false;
        }
        if (document.getElementById('gmkc').checked) {
            gmapKC = true;
        }
        else {
            gmapKC = false;
        }
    }

    // get query number
    if (!isNumeric(document.getElementById('queryInput').value)) {
        alert("Number of query is not a valid number!");
        return;
    }
    numberOfQueries = document.getElementById("queryInput").value;
    if (!numberOfQueries) {
        alert("Please specify number of queries!");
        return;
    }
    if (numberOfQueries <= 0 || numberOfQueries % 1 != 0) {
        alert("Number of queries should be a positive integer");
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
    else {
        inputParams = false;
    }

    // formatting params
    // cell size
    args = "-cd " + cellSize + " ";
    // NE lat, SW lng, SW lat, NE lng
    args += "-a " + recRegion.getNorthEast().lat() + " " + recRegion.getSouthWest().lng() + " " + recRegion.getSouthWest().lat() + " " + recRegion.getNorthEast().lng() + " ";
    // number of channels
    args += "-c " + numberOfChannels + " ";
    // location of pu
    for (var i = 0; i < location_PU.length; i++) {
        args += "-C ";
        for (var j = 0; j < location_PU[i].length; j++) {
            args += location_PU[i][j].position.lat() + " " + location_PU[i][j].position.lng() + " ";
        }
    }
    // countermeasure
    args += "-cm ";
    for (var i = 0; i < countermeasure.length; i++) {
        if (countermeasure[i] == "no") {
            args += "no -1 ";
        }
        else if (countermeasure[i] == "an") {
            args += "an " + cmVal[i] + " ";
        }
        else if (countermeasure[i] == "tf") {
            args += "tf " + cmVal[i] + " ";
        }
        else if (countermeasure[i] == "ka") {
            args += "ka " + cmVal[i] + " ";
        }
        else {
            args += "kc " + cmVal[i] + " ";
        }
    }
    // google map plots
    args += "-gm ";
    if (gmapNO) {
        args += 'no ';
    }
    if (gmapAD) {
        args += 'ad ';
    }
    if (gmapTF) {
        args += 'tf ';
    }
    if (gmapKA) {
        args += 'ka ';
    }
    if (gmapKC) {
        args += 'kc ';
    }
    // trade off curve
    args += "-tr ";
    if (tradeOffAD) {
        args += 'ad '
    }
    if (tradeOffTF) {
        args += 'tf '
    }
    if (tradeOffKA) {
        args += 'ka '
    }
    if (tradeOffKC) {
        args += 'kc '
    }
    // queries
    args += "-q "
    if (document.getElementById("randomQuery").checked) {
        args += numberOfQueries + " ";
    }
    args += "-sq "
    if (document.getElementById("smartQuery").checked) {
        args += numberOfQueries + " ";
    }
    // email
    args += "-e " + email + " ";
    args += "-opt ";
    if (inputParams) {
        args += "pa ";
    }

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
        pustr += "<p>";
        pustr += "Primary user on channel " + i + "<br>";
        for (var j = 0; j < location_PU[i].length; j++) {
            pustr += "( " + location_PU[i][j].position.lat() + ", " + location_PU[i][j].position.lng() + " ) <br>";
        }
        pustr += "</p>";
    }
    document.getElementById("wellpu").innerHTML = pustr;
    // countermeasure
    var cmstr = "";
    for (var i = 0; i < countermeasure.length; i++) {
        cmstr += "<p>";
        if (countermeasure[i] == "no") {
            cmstr += "No countermeasure";
            if (gmapNO) {
                cmstr += ". Plot inferred location of primary users on Google Maps";
            }
        }
        else if (countermeasure[i] == "an") {
            cmstr += "Additive noise. Noise level: " + cmVal[i];
            if (gmapAD) {
                cmstr += ". Plot inferred location of primary users on Google Maps";
            }
            if (tradeOffAD) {
                cmstr += ". Plot trade-off curve";
            }
        }
        else if (countermeasure[i] == "tf") {
            cmstr += "Transfiguration. Sides of polygon: " + cmVal[i];
            if (gmapTF) {
                cmstr += ". Plot inferred location of primary users on Google Maps";
            }
            if (tradeOffTF) {
                cmstr += ". Plot trade-off curve";
            }
        }
        else if (countermeasure[i] == "ka") {
            cmstr += "K Anonymity. K: " + cmVal[i];
            if (gmapKA) {
                cmstr += ". Plot inferred location of primary users on Google Maps";
            }
            if (tradeOffKA) {
                cmstr += ". Plot trade-off bar";
            }
        }
        else {
            cmstr += "K Clustering. K: " + cmVal[i];
            if (gmapKC) {
                cmstr += ". Plot inferred location of primary users on Google Maps";
            }
            if (tradeOffKC) {
                cmstr += ". Plot trade-off bar";
            }
        }
        cmstr += "</p>";
    }
    document.getElementById("wellcm").innerHTML = cmstr;
    // query
    var querystr = "";
    if (document.getElementById("randomQuery").checked) {
        querystr = "Randomly generated location<br>";
    }
    if (document.getElementById("smartQuery").checked) {
        querystr += "Using smart query algorithm to determine query location<br>";
    }
    querystr += "Number of queries: " + numberOfQueries;
    document.getElementById("wellquery").innerHTML = querystr;
    // email
    var emstr = "";
    emstr += "<p>Results will be send to " + email;
    if (inputParams) {
        emstr += ". Email will also include parameters from above";
    }
    emstr += "</p>";
    document.getElementById("wellemail").innerHTML = emstr;

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
                alert("Simulation has started successfully!\nWe will send simulation results to you shortly.\nThanks for using!");
            }
            else {
                alert("Simulation failed. " + res);
            }
            // reload page
            window.location = 'index.html';
        }
        if (xmlhttp.readyState == 4 && xmlhttp.status != 200)
        {
            alert("Sorry. Failed to start demo. " + xmlhttp.status);
            // reload page
            window.location = 'index.html';
        }
    }
    xmlhttp.open("POST","result.php", true);
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

/**
 * Once the launch button is clicked, change the text to launching
 */
$(document).ready(function(){
    $("#launchButton").click(function(){
        $("#launchButton").html('Launching...');   // change the text to be launching
        $("#launchButton").prop('disabled', true); // disable the button
    });
});