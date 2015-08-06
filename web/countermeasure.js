/* 
 * This script is used for selecting countermeasures 
 */

/**
 * When checkbox is selected, enable/disable related input field for countermeasure parameter.
 * For additive noise and transfiguration, uncheck trade-off curve at the same time
 * @param  {String} arg id of input for countermeasure parameters, cmval1, cmval2, cmval3, cmval4
 * @return {void}
 */
function enableInput(arg) {
	if (arg == "cmval0") {
		if (!document.getElementById("cmopt0").checked) {
    		document.getElementById("gmno").checked = false;
    	}
    	return;
    }
	if (document.getElementById(arg).disabled) {
		document.getElementById(arg).disabled = false;
		if (arg == "cmval1") {
			document.getElementById("cmval1").value = 0.5;
		}
		if (arg == "cmval2") {
			document.getElementById("cmval2").value = 3;
		}
		if (arg == "cmval3") {
			document.getElementById("cmval3").value = 1;
		}
		if (arg == "cmval4") {
			document.getElementById("cmval4").value = 1;
		}
	}
    else {
    	document.getElementById(arg).disabled = true;
    	if (arg == "cmval1") {
    		document.getElementById("tradeOff1").checked = false;
    		document.getElementById("gmad").checked = false;
    	}
    	if (arg == "cmval2") {
    		document.getElementById("tradeOff2").checked = false;
    		document.getElementById("gmtf").checked = false;
    	}
    	if (arg == "cmval3") {
    		document.getElementById("gmka").checked = false;
    	}
    	if (arg == "cmval4") {
    		document.getElementById("gmkc").checked = false;
    	}
    }
}

/**
 * When checkbox is clicked, enable related input field for countermeasure parameter if it is disabled
 * @param  {String} arg id of trade-off checkboxes, tradeOff1, tradeOff2
 */
function tradeOffCurve(arg) {
	if (arg == "tradeOff1") {
		if (!document.getElementById('cmopt1').checked) {
			document.getElementById('cmopt1').checked = true
			enableInput('cmval1');
		}
	}
	if (arg == "tradeOff2") {
		if (!document.getElementById('cmopt2').checked) {
			document.getElementById('cmopt2').checked = true
			enableInput('cmval2');
		}
	}
}

/**
 * Control plot google map checkbox
 * @param  {String} argument checkbox id
 */
function plotGMap (argument) {
	if (argument == "gmad") {
		if (!document.getElementById('cmopt1').checked) {
			document.getElementById('cmopt1').checked = true
			enableInput('cmval1');
		}
	}
	else if (argument == "gmtf") {
		if (!document.getElementById('cmopt2').checked) {
			document.getElementById('cmopt2').checked = true
			enableInput('cmval2');
		}
	}
	else if (argument == "gmka") {
		if (!document.getElementById('cmopt3').checked) {
			document.getElementById('cmopt3').checked = true
			enableInput('cmval3');
		}
	}
	else if (argument == "gmkc") {
		if (!document.getElementById('cmopt4').checked) {
			document.getElementById('cmopt4').checked = true
			enableInput('cmval4');
		}
	}
	else {
		if (!document.getElementById('cmopt0').checked) {
			document.getElementById('cmopt0').checked = true
		}
	}
}

/**
 * Check if the input is a valid numeric number
 * @param  {[type]}  n input number
 * @return {Boolean}   true if input is valid
 */
function isNumeric(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

/**
 * Value of noise level shoud be within 0 and 1
 */
function adjustNoiseLevel() {
	if (!isNumeric(document.getElementById("cmval1").value)) {
		document.getElementById("cmval1").value = 0;
		return;
	}
	var val = document.getElementById("cmval1").value;
	if (val < 0) {
		document.getElementById("cmval1").value = 0;
	}
	else if (val > 1) {
		document.getElementById("cmval1").value = 1;
	}
}

/**
 * Number of sides should be a positive integer no less than 3
 */
function adjustNumberOfSides () {
	if (!isNumeric(document.getElementById("cmval2").value)) {
		document.getElementById("cmval2").value = 3;
		return;
	}
	var val = document.getElementById("cmval2").value;
	if (val < 3) {
		document.getElementById("cmval2").value = 3;
	}
	else {
		document.getElementById("cmval2").value = Math.round(val);
	}
}

/**
 * K for k anonymity should be an integer no less than 1
 */
function adjustKAnonymity () {
	if (!isNumeric(document.getElementById("cmval3").value)) {
		document.getElementById("cmval3").value = 1;
		return;
	}
	var val = document.getElementById("cmval3").value;
	if (val < 1) {
		document.getElementById("cmval3").value = 1;
	}
	else {
		document.getElementById("cmval3").value = Math.round(val);
	}
}

/**
 * K for k clustering should be an integer no less than 1
 */
function adjustKClustering () {
	if (!isNumeric(document.getElementById("cmval4").value)) {
		document.getElementById("cmval4").value = 1;
		return;
	}
	var val = document.getElementById("cmval4").value;
	if (val < 1) {
		document.getElementById("cmval4").value = 1;
	}
	else {
		document.getElementById("cmval4").value = Math.round(val);
	}
}