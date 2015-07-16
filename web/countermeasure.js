/* 
 * This script is used for selecting countermeasures 
 */

function clearCounterMeasure () {
    document.getElementById("countermeasure").innerHTML = "";
}

function counterFunc1() {
    var str = "";
    str += '<form class="form-inline" role="form">';
    str += '<div class="form-group" style="width: 50%">';
    str += '<label for="additive_noise">Noise Level:  </label>';
    str += '<input type="number" class="form-control" id="cmval" style="width: 30%" min="0.0" max="1.0" step="0.1" placeholder="0.5">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}
function counterFunc2 () {
    var str = "";
    str += '<form class="form-inline" role="form">';
    str += '<div class="form-group">';
    str += '<label for="polygon">Sides for convex polygon:  </label>';
    str += '<input type="number" class="form-control" id="cmval" min="3" placeholder="3">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}

function counterFunc3 () {
    var str = "";
    str += '<form class="form-inline" role="form">';
    str += '<div class="form-group">';
    str += '<label for="ka">K for K-Anonymity:  </label>';
    str += '<input type="number" class="form-control" id="cmval" min="1" placeholder="2">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}

function counterFunc4 () {
    var str = "";
    str += '<form class="form-inline" role="form">';
    str += '<div class="form-group">';
    str += '<label for="kc">K for K-Clustering:  </label>';
    str += '<input type="number" class="form-control" id="cmval" min="1" placeholder="2">';
    str += '</div></form>';
    document.getElementById("countermeasure").innerHTML = str;
}