/**
 * This script is for selecting channels
 */

var numberOfChannels = 1;  // number of channels [1, 3]

/**
 * JQuery that bind to dropdown list
 */
$(document).ready(function(){
    $('#dp li').on('click', function(){
        setChannels($(this).text());
        $('#channelButton').html($(this).text());
    });
});

/**
 * Specify number of channels. 
 * Dispalying google map area in accordance with user specified number of channels
 */
function setChannels(args) {
    resetAllMarkers();
    numberOfChannels = parseInt(args);
    switch (numberOfChannels) {
    case 1:
        $("#butnChannel1").show();
        $("#butnChannel2").hide();
        $("#butnChannel3").hide();
        break;
    case 2:
        $("#butnChannel1").hide();
        $("#butnChannel2").show();
        $("#butnChannel3").hide();
        break;
    case 3:
        $("#butnChannel1").hide();
        $("#butnChannel2").hide();
        $("#butnChannel3").show();
        break;
    }
}