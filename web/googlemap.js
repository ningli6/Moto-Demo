var myCenter=new google.maps.LatLng(37.227799, -80.422054);
var map;

function initialize() {
  var mapProp = {
    center:myCenter,
    zoom:5,
    mapTypeId:google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
	var ulla = localStorage.getItem("upperLeftLat");
    var ullg = localStorage.getItem("upperLeftLng");
    var lrla = localStorage.getItem("lowerRightLat");
    var lrlg = localStorage.getItem("lowerRightLng");
	if (ulla == null) {
		ulla = 38;
		localStorage.setItem("upperLeftLat", ulla);
	}
	if (ullg == null) {
		ullg = -82;
		localStorage.setItem("upperLeftLng", ullg);
	}
	if (lrla == null) {
		lrla = 36;
		localStorage.setItem("lowerRightLat", lrla);
	}
	if (lrlg == null) {
		lrlg = -79;
		localStorage.setItem("lowerRightLng", lrlg);
	}
    var ul = new google.maps.LatLng(ulla, ullg);
	var ur = new google.maps.LatLng(ulla, lrlg);
	var ll = new google.maps.LatLng(lrla, ullg);
	var lr = new google.maps.LatLng(lrla, lrlg);
	// console.log(ul, ur, ll, lr);
	var bounds=[ul,ur,lr,ll,ul];
	var path=new google.maps.Polyline({
	  path:bounds,
	  strokeColor:"#0000FF",
	  strokeOpacity:0.8,
	  strokeWeight:2
	  });
	path.setMap(map);
}
google.maps.event.addDomListener(window, 'load', initialize);

function setRecBounds (form) {
    var ulla = form.ulla.value;
    var ullg = form.ullg.value;
    var lrla = form.lrla.value;
    var lrlg = form.lrlg.value;
    var n1 = ulla.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    var n2 = ullg.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    var n3 = lrla.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    var n4 = lrlg.search(/-{0,1}[0-9]+(.[0-9]+){0,1}/i);
    if (n1 != 0 || n2 != 0 || n3 != 0 || n4 != 0 || 
    	(countDot(ulla) > 1) ||
    	(countDot(ullg) > 1) ||
    	(countDot(lrla) > 1) ||
    	(countDot(lrlg) > 1)) {
	    alert("Invalid coordinates!");
		return;
	}
	localStorage.setItem("upperLeftLat", parseFloat(ulla));
	localStorage.setItem("upperLeftLng", parseFloat(ullg));
	localStorage.setItem("lowerRightLat", parseFloat(lrla));
	localStorage.setItem("lowerRightLng", parseFloat(lrlg));
}

function countDot(str) {
	var count = 0;
	for (var i = 0; i < str.length; i++) {
		if (str.charAt(i) == '.') count++;
	}
	return count;
}