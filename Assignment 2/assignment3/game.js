function setPositions() {
	var positions=[];
	var snakePositions   =[13,38,46,73,81,87]
	var snakeNewPositions=[2,17,26,43,51,67]

	var ladderPositions   =[3,9,32,48,56,78]
	var ladderNewPositions=[4,28,72,59,86,89]
	
	var snakes_or_ladders_Positions   =[25,65,70]
	var snakes_or_ladders_NewPositions=["5 or 45","44 or 74","49 or 90"]

 
	for (var i = 1; i <=90 ; i++) {
	 positions[i]=new Object();
	 positions[i].from=i;
	 
	  
	 if(snakePositions.indexOf(i)!=-1){
	   positions[i].to=snakeNewPositions[snakePositions.indexOf(i)];
	   positions[i].type="Snake";
	 }
	 else if(ladderPositions.indexOf(i)!=-1){
	   positions[i].to=ladderNewPositions[ladderPositions.indexOf(i)];
	   positions[i].type="Ladders";
	 }
	 else if(snakes_or_ladders_Positions.indexOf(i)!=-1){
	   positions[i].to=snakes_or_ladders_NewPositions[snakes_or_ladders_Positions.indexOf(i)];
	   positions[i].type="Snake or Ladders";
	 }
	 else if(i===16 || i===47 || i===68 || i===84){
		positions[i].to="Other Player position";
		positions[i].type="Sheep";   
	 }
	 else if(i===21 || i===40 || i===57 || i===75){
		positions[i].to="1 with "+(100-i)+"% possibility or 90 with "+i+" % possibility";
		positions[i].type="ALL IN";
	 }
	 else{
	   positions[i].to=i;
		positions[i].type="Normal";   
	   
	 }
	}
	 return positions; 
	}

var cells=setPositions();
for (var i = 1; i <=90 ; i++) {
    console.log("Cell: "+i+" type: "+cells[i].type+" From: "+cells[i].from+" To: "+cells[i].to)
}