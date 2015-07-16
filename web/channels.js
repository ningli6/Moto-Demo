/**
 * This script is for selecting channels
 */


/**
 * Specify number of channels. 
 * Dispalying google map area in accordance with user specified number of channels
 */
function setChannels() {
    console.log("Set channels!");
    resetAllMarkers();
    var e = document.getElementById("selc");
    numberOfChannels = parseInt(e.options[e.selectedIndex].value);
    switch (numberOfChannels) {
    case 1:
        console.log("Number of channels: 1");
        chanls = 1
        var str = "<button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += '<span class="help-block">Click rectangle icon to draw analysis area</span>'
        str += "<div id='map-canvas' style='width:100%; height:500px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 2:
        console.log("Number of channels: 2");
        var str = "<button type='button' class='btn btn-info' onclick='chanls = 0;'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='chanls = 1;'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += '<span class="help-block">Click rectangle icon to draw analysis area</span>'
        str += "<div id='map-canvas' style='width:100%; height:500px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    case 3:
        console.log("Number of channels: 3");
        var str = "<button type='button' class='btn btn-info' onclick='chanls = 0;'>Select location of PU(s) for channel 0</button>";
        str += " <button type='button' class='btn btn-info' onclick='chanls = 1;'>Select location of PU(s) for channel 1</button>";
        str += " <button type='button' class='btn btn-info' onclick='chanls = 2;'>Select location of PU(s) for channel 2</button>";
        str += " <button type='button' class='btn btn-warning' onclick='resetAllMarkers();'>Reset</button>";
        str += '<span class="help-block">Click rectangle icon to draw analysis area</span>'
        str += "<div id='map-canvas' style='width:100%; height:500px;'></div>";
        document.getElementById("mapArea").innerHTML = str;
        window.onload = initialize();
        break;
    }
}