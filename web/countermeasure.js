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
	if (document.getElementById(arg).disabled) {
		document.getElementById(arg).disabled = false;
	}
    else {
    	document.getElementById(arg).disabled = true;
    	if (arg == "cmval1") {
    		document.getElementById("tradeOff1").checked = false;
    	}
    	if (arg == "cmval2") {
    		document.getElementById("tradeOff2").checked = false;
    	}
    }
}

/**
 * When checkbox is clicked, enable related input field for countermeasure parameter if it is disabled
 * @param  {String} arg id of trade-off checkboxes, tradeOff1, tradeOff2
 * @return {void}     [description]
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