/**
 * Javascript for grid size
 */

// default size
var cellSize = 0.005;

$(document).ready(function(){
    $('#gsdp li').on('click', function(){
    	// set button text
        $('#gridSizeDisp').html($(this).text());
        // adjust grid size
        adjustGridSize($(this).text());
    });
});

/**
 * Adjust grid size
 * @param  {string} s 0.5 km, 1 km, 5 km
 */
function adjustGridSize (s) {
	console.log(s);
	var val = parseFloat(s.substr(0, s.length - 2));
	console.log(val);
	if (val == 1) {
		cellSize = 0.01;
		console.log(1);
	}
	else if (val == 5) {
		cellSize = 0.05;
		console.log(5);
	}
	else {
		cellSize = 0.005;
	}
	if ((rect != undefined && rect != null) && (recRegion != undefined && recRegion != null)) {
		clearGrids();
		if (!checkSize(recRegion)) {
			if (rect != undefined) rect.setMap(null);
			recRegion = undefined;
			return;
		}
		drawGrids(recRegion);
	}
}