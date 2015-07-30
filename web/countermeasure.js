/* 
 * This script is used for selecting countermeasures 
 */

function disableInputs () {
    document.getElementById("cmval1").disabled = true;
    document.getElementById("cmval2").disabled = true;
    document.getElementById("cmval3").disabled = true;
    document.getElementById("cmval4").disabled = true;
}

function enableInput(arg) {
    disableInputs();
    var str = "cmval" + arg
    document.getElementById(str).disabled = false;
}