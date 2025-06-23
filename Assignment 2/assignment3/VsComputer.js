
//player 1 = red player2 = white 
var players = ["Red", "White"];
//Array for turn of the players 
var turn = "";
var RedPosition =0  ,WhitePosition = 0;
//dice 
var dice,message = "";
var prevWhite,prevRed;


function stopAllSounds() {
    var allAudio = document.querySelectorAll('audio');
    allAudio.forEach(audio => {
        audio.pause();
        audio.currentTime = 0; // Reset playback to start
    });
}


function playStartSound(){
    var StartAudio = document.getElementById("StartSound");
    StartAudio.play();
    StartAudio.volume = 0.5;
}

function playDiceSound(){
    
    var diceAudio = document.getElementById("diceSound");
    diceAudio.play();
    diceAudio.volume = 0.6;
}

function playWinSound(){
    stopAllSounds();
    var winSound = document.getElementById("winSound");
    winSound.play();
    winSound.volume = 0.6;

    winSound.play().catch(err => console.error("Audio playback error:", err));

}

function playLoseSound(){
    stopAllSounds();
    var loseSound= document.getElementById("loseSound");
    loseSound.play();
    loseSound.volume = 0.6;

    loseSound.play().catch(err => console.error("Audio playback error:", err));

}
//This function will start A game and 
//it will randomly give player 1 or 2 the first move
function NewGame(){

    playStartSound();
    var info = document.getElementById("InfoBox");
   info.textContent = "Game has started ! ";

    var first = Math.floor(Math.random() * 2) + 1;
    console.log(first);

    setTimeout(function(){
        info.innerHTML +=  "<br>" + players[first - 1] + " is going first";
    },2000);

    setTurn(first - 1);

    
}

function setTurn(player){
    turn = players[player];
    
}

function getPlayerTurn(){
    return turn;
}

function diceTranslator(dice){
    if(dice == 1){
        return "one";
    }else if(dice == 2){
        return "two";
    }else if(dice == 3){
        return "three";
    }else if(dice == 4){
        return "four";
    }else if(dice == 5){
        return "five";
    }else if(dice == 6){
        return "six";
    }
}

function SnakeLadder(player,loss,win){
    var test  = Math.floor(Math.random() * 10);

    if(test < 5){
        if(player == "Red"){
            RedPosition = loss;
        }else{
            WhitePosition = loss;
        }

        message = player + " failed the test and is being transferred to " + loss + ".";
        console.log(player + " failed the test and is being transferred to " + loss + ".");
    }else if(test >= 5 && test <= 10){
        if(player == "Red"){
            RedPosition  = win;
        }else{
            WhitePosition = win;
        }

        message = player + "  passed the test and is being transferred to " + win + ".";
        console.log(player + "  passed the test and is being transferred to " + win + ".");
    }
}

function resetImage(prev){
    document.getElementById("position"+prev).innerHTML="<img  src='images/"+prev+".png'  height=70 width=80></div>";	
}

function resetDupImage(prev,duprev){
    resetImage(prev);
    document.getElementById("position"+duprev).innerHTML="<img  src='images/"+duprev+".png'  height=70 width=80></div>";
}



function all_in(position){
    var BlackJack = Math.floor(Math.random() * 100)+1;
    console.log(BlackJack);


    if(BlackJack <= position){
        console.log("A player has won the BlackJack");
        message = "<br>" + turn + " has won the BlackJack";
        position = 90;
        alert(getPlayerTurn() + " WON");
        playWinSound();
    }else if(BlackJack > position) {
        message ="<br>" + turn + " has lost the BlackJack";
        console.log("A player has lost the BlackJack");
        position = 1;          
        playLoseSound();
    }

    return position;
}
    

function HasWon(player,position,prev){
    if(position == 90){
        return true;
    }else{
        return false;
    }

}


function ShowMove(dice, position1, position2, prev) {

    var info = document.getElementById("InfoBox");
    var currentTurn = getPlayerTurn();
    var diceImg = document.getElementById("dice");
    


    diceImg.src = "ImagesDice/"+ diceTranslator(dice) + ".png";

    info.innerHTML = turn + " rolled " + dice ; 

    
    setTimeout(function(){
        info.innerHTML += "<br>" + message;
    },300);

    if(position1 != position2)
        changePosition(currentTurn,position1);
    else{
        changePosition("Both",position1);
    }

    if(prev != 0){
        resetImage(prev);
    }
    
        
    
        
   
}


function play() {
    var currentTurn = getPlayerTurn();
    var previousTurn = getPlayerTurn();
    var prev=0;
    var winner = 0 ;
    

    if(winner == 0){
        dice = Math.floor(Math.random() * 6) + 1;
        playDiceSound();
        console.log(previousTurn + " rolled " + dice);

        if(currentTurn == "Red"){
            
            prev = RedPosition;
            RedPosition += dice;

            if(HasWon(currentTurn,RedPosition)){
                playWinSound();
                alert("Red WON !");
                winner = 1;
            }   

            if(RedPosition > 90){
                RedPosition = RedPosition -  90;
                RedPosition = 90 - RedPosition;
            }
            

            console.log(" R E D  P O S I T I O N = " + RedPosition);

          

            if(cells[RedPosition].type == "Normal"){
                console.log("Normal Move");
                
                message = "Normal Move";
            
            }else if(cells[RedPosition].type == "Ladders") {
                console.log(previousTurn + " climbed the ladder from " + cells[RedPosition].from + " to " + cells[RedPosition].to);

                message = previousTurn + " climbed the ladder from " + cells[RedPosition].from + " to " + cells[RedPosition].to ;
                RedPosition = cells[RedPosition].to;
            
            }else if(cells[RedPosition].type == "Snake"){

                console.log(previousTurn + " Got Snaked from " + cells[RedPosition].from + " to " + cells[RedPosition].to);


                message = previousTurn + " Got Snaked from " + cells[RedPosition].from + " to " + cells[RedPosition].to
                RedPosition = cells[RedPosition].to
                

            }else if(cells[RedPosition].type == "Snake or Ladders"){

                var [win,loss] = cells[RedPosition].to.split(" or ");
                SnakeLadder("Red",win,loss);
                
              
                
            }else if(cells[RedPosition].type == "Sheep"){
                
                message = "Red got into a sheep and is being transfered to the White Player" ;
                RedPosition = WhitePosition ;
            
            }else if(cells[RedPosition].type == "ALL IN"){
                var decision = window.confirm("Do you want to go All In?");
                
                if (decision) {
                    RedPosition = all_in(RedPosition);
                } else {
                    
                    console.log("Red chose not to go All In. Position remains at " + RedPosition);
                }

                
            }

            ShowMove(dice,RedPosition,WhitePosition,prev);

            if(dice == 6){
                setTurn(0);
                
            }else 
                setTurn(1);
           
            
            console.log("it is " + turn + "'s turn " );

        }else if(currentTurn == "White"){
        
            prev = WhitePosition;
            WhitePosition += dice;

            if(HasWon(currentTurn, WhitePosition)){
                playWinSound();
                alert("White WON !");
                winner = 1;
            }

            if(WhitePosition > 90){
                WhitePosition = WhitePosition -  90;
                WhitePosition= 90 - WhitePosition;
            }

            console.log(" W H I T E   P O S I T I O N = " + WhitePosition);

            


        
            if(cells[WhitePosition].type == "Normal"){
                
                console.log("Normal Move");

                message = "Normal Move";

            }else if(cells[WhitePosition].type == "Ladders"){
            
                console.log(previousTurn + " climbed the ladder from " + cells[WhitePosition].from + " to " + cells[WhitePosition].to);

                message = previousTurn + " climbed the ladder from " + cells[WhitePosition].from + " to " + cells[WhitePosition].to;
                WhitePosition = cells[WhitePosition].to;
                
            

            }else if(cells[WhitePosition].type == "Snake"){
                
                console.log(previousTurn + " Got Snaked from " + cells[WhitePosition].from + " to " + cells[WhitePosition].to);

                message = previousTurn + " Got Snaked from " + cells[WhitePosition].from + " to " + cells[WhitePosition].to;

                WhitePosition = cells[WhitePosition].to;
                
            

            }else if(cells[WhitePosition].type== "Snakes or Ladders"){
                var [loss,win] = cells[WhitePosition].to.split(" or ");
                SnakeLadder("White",loss,win);
        
            }else if(cells[WhitePosition].type == "Sheep"){
                console.log("White got into a sheep and is being transfered to the Red Player" );

                message = "White got into a sheep and is being transfered to the Red Player" ;
                WhitePosition = RedPosition;

            }else if(cells[WhitePosition].type == "ALL IN"){
                var decision = window.confirm("Do you want to go All In?");
                
                if (decision) {
                    WhitePosition= all_in(WhitePosition);

                } else {
                    
                    console.log("Red chose not to go All In. Position remains at " + RedPosition);
                }
                
            }

            ShowMove(dice,WhitePosition,RedPosition,prev);

            if(dice == 6){
                setTurn(1);             
            }else 
                setTurn(0);
        
            console.log("it is " + turn + "'s turn " );
        }
    }
}


