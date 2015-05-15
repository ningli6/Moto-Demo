var myCenter=new google.maps.LatLng(37.227799, -80.422054);
var map;
// var upperLeft=new google.maps.LatLng(38,-82);
// var upperRight=new google.maps.LatLng(38,-79);
// var lowerLeft=new google.maps.LatLng(36,-82);
// var lowerRight=new google.maps.LatLng(36, -79);
function initialize() {
  var mapProp = {
    center:myCenter,
    zoom:10,
    mapTypeId:google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
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
	var ul = new google.maps.LatLng(parseFloat(ulla), parseFloat(ullg));
	var ur = new google.maps.LatLng(parseFloat(ulla), parseFloat(lrlg));
	var ll = new google.maps.LatLng(parseFloat(urla), parseFloat(ullg));
	var lr = new google.maps.LatLng(parseFloat(lrla), parseFloat(lrlg));
	var bounds=[ul,ur,lr, ll];
	var path=new google.maps.Polyline({
	  path:bounds,
	  strokeColor:"#0000FF",
	  strokeOpacity:0.8,
	  strokeWeight:2
	  });
	path.setMap(map);
}

function countDot(str) {
	var count = 0;
	for (var i = 0; i < str.length; i++) {
		if (str.charAt(i) == '.') count++;
	}
	return count;
}