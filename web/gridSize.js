/**
 * Javascript for grid size
 */

function adjustGridSize () {
	var val = document.getElementById("cd").value;
	if (!isNumeric(val)) {
		document.getElementById("cd").value = 0.005;
	}
	else if (val < 0.005) {
		document.getElementById("cd").value = 0.005;
	}
	else if (val > 0.05) {
		document.getElementById("cd").value = 0.05;
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