function initBoard(){
	var table = document.getElementById('mainTable');
	var tr = document.createElement('tr');

	for (var i = 9; i >=1; i--) {
	  var tr = document.createElement('tr');
	  for (var j = 9; j >=0; j--) {
	  var td1 = document.createElement('td');
	  var num=i*10-j;
	  td1.innerHTML="<div id='position"+num+"'><img  src='images/"+num+".png'  height=70 width=80></div>";
	  
	  tr.appendChild(td1);
	  
	  }
	  table.appendChild(tr);
	}
	setPositions();
}

function changePosition(player, newPosition) {
	var positionElement = document.getElementById("position" + newPosition);
    
    if (positionElement) {
        // Only set innerHTML if the element exists
        positionElement.innerHTML = "<img src='images" + player + "/" + newPosition + ".png' height=70 width=80>";
    } else {
        // Log an error if the element is not found
        console.error("Element with id 'position" + newPosition + "' not found.");
    }
}
