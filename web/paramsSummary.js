/* fill in wells */
function showParamsSummary () {
    // number of channels
    document.getElementById("wellchannel").innerHTML = numberOfChannels;
    // grid size
    var gridsizeStr = "";
    if (cellSize == 0.005) gridsizeStr += "0.5 km";
    else if (cellSize == 0.01) gridsizeStr += "1 km";
    else gridsizeStr += "5 km";
    document.getElementById("wellgridsize").innerHTML = gridsizeStr;
    // mtp parameters
    var mtpStr = "Let <var>d</var> be the distance between attacker and primary user. <var>R</var> is the full transmit power available, <var>r</var> is the actual transmit power that attacker can use.<br>Define the MTP parameters in the simulation:<br><br>";
    mtpStr += "<p align='center'><i>r = 0 if d &le; " + d0 + " km<br>";
    mtpStr += "r = 0.5 R if " + d0 + " km &lt; d &le; " + d1 + " km<br>";
    mtpStr += "r = 0.75 R if " + d1 + " km &lt; d &le; " + d2 + " km<br>";
    mtpStr += "r = R if d &gt; " + d2 + " km</i></p>";
    document.getElementById("wellmtp").innerHTML = mtpStr;
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
    emstr += "<p>Simulation results will be sent to " + email + "</p>";
    document.getElementById("wellemail").innerHTML = emstr;
}
