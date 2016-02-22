/**
 * Script for MTP parameters
 */

var d0 = 8;
var d1 = 14;
var d2 = 25;

function setd0fromInner0 () {
	if (document.getElementById("inner0").value == "") {
		alert("Range parameters can't be null");
	} else if (document.getElementById("inner0").value <= 0) {
		alert("Range parameters must be positive");
	} else {
		d0 = Number(document.getElementById("inner0").value);
	}
	var elements = document.getElementsByName("inner");
	for (var i = 0; i < elements.length; i++) {
		elements[i].value = d0;
	}
}

function setd0fromInner1 () {
	if (document.getElementById("inner1").value == "") {
		alert("Range parameters can't be null");
	} else if (document.getElementById("inner1").value <= 0) {
		alert("Range parameters must be positive");
	} else {
		d0 = Number(document.getElementById("inner1").value);
	}
	var elements = document.getElementsByName("inner");
	for (var i = 0; i < elements.length; i++) {
		elements[i].value = d0;
	}
}

function setd1fromMiddle0 () {
	if (document.getElementById("middle0").value == "") {
		alert("Range parameters can't be null");
	} else if (document.getElementById("middle0").value <= 0) {
		alert("Range parameters must be positive");
	} else {
		d1 = Number(document.getElementById("middle0").value);
	}
	var elements = document.getElementsByName("middle");
	for (var i = 0; i < elements.length; i++) {
		elements[i].value = d1;
	}
	if (d1 <= d0) {
		alert("Range parameters must be in increasing order");
	}
}

function setd1fromMiddle1 () {
	if (document.getElementById("middle1").value == "") {
		alert("Range parameters can't be null");
	} else if (document.getElementById("middle1").value <= 0) {
		alert("Range parameters must be positive");
	} else {
		d1 = Number(document.getElementById("middle1").value);
	}
	var elements = document.getElementsByName("middle");
	for (var i = 0; i < elements.length; i++) {
		elements[i].value = d1;
	}
	if (d1 <= d0) {
		alert("Range parameters must be in increasing order");
	}
}

function setd2fromOutter0 () {
	if (document.getElementById("outter0").value == "") {
		alert("Range parameters can't be null");
	} else if (document.getElementById("outter0").value <= 0) {
		alert("Range parameters must be positive");
	} else {
		d2 = Number(document.getElementById("outter0").value);
	}
	var elements = document.getElementsByName("outter");
	for (var i = 0; i < elements.length; i++) {
		elements[i].value = d2;
	}
	if (d2 <= d1 || d2 <= d0) {
		alert("Range parameters must be in increasing order");
	}
}

function setd2fromOutter1 () {
	if (document.getElementById("outter1").value == "") {
		alert("Range parameters can't be null");
	} else if (document.getElementById("outter1").value <= 0) {
		alert("Range parameters must be positive");
	} else {
		d2 = Number(document.getElementById("outter1").value);
	}
	var elements = document.getElementsByName("outter");
	for (var i = 0; i < elements.length; i++) {
		elements[i].value = d2;
	}
	if (d2 <= d1 || d2 <= d0) {
		alert("Range parameters must be in increasing order");
	}
}

function checkMTPParameters () {
	return d0 < d1 && d1 < d2 && d0 > 0 && d1 > 0 && d2 > 0
}
