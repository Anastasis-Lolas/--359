//a) we have to get the 2 id's in a function pass1 conf pass2 
//next take these 2 strings and compare them if they are different
//if they are different show a message next to these 2 fields
//also in the password section add a show password button

function CheckPass(){
    const pass1 = document.getElementById("pass1").value;
    const pass2 = document.getElementById("pass2").value;


    const msg = document.getElementById("msg2");

    if (pass1.length === 0 || pass2.length === 0) {
        msg.style.display = "block";
        msg.innerText = "Please fill out both password fields.";
        msg.style.color = "orange"; // Or another color to indicate missing fields
        return false;
    } else if (pass1 === pass2) {
        msg.style.display = "block";
        msg.innerText = "Passwords Match";
        msg.style.color = "green";
        return true;
    } else {
        msg.style.display = "block";
        msg.innerText = "Passwords Mismatch";
        msg.style.color = "red";
        return false;
    }
    
    
}


function ShowPass(){
    let password = document.getElementById("pass1");
    let eye_icon = document.getElementById("img");

    if(password.type === "password"){
        eye_icon.src="eye-close.png";
        password.type = "text" ;
    }else if(password.type === "text"){
        eye_icon.src="eye-open.png";
        password.type = "password";
    }

}

//b)Make a function that checks out the security of the password
//make a variable pass the value from the index 
//check if this string is eqaul to any of the prohibited strings
//check also if the 50 of the password is numbers sue isNumber() etc
//check if it has at least 1 capital 1 number and 1 letter show the strong message 
//make a bar for it  and in every other case show the medium message 
// dont let the button submit if the password is weak

function PassSecurity(){
    const password = document.getElementById("pass1");
    
    if(checkWord(password) && checkNumberPercentage(password.value))
        return true;
    else 
        return false;
    
    
}

function checkWord(password) {
    const new_pass = password.value.toLowerCase();

    if (["fire", "fotia", "ethelontis", "volunteer"].includes(new_pass)) {
        password.value = ""; // Clear the password if it's a prohibited word
        return false; 
        alert("The password contains a prohibited word.");
    }

    return true;
}




function checkNumberPercentage(password){

    let counter = 0,Cflag=0,sflag=0,cflag=0;
    const message1  = document.getElementById("msg1");
    const message2 = document.getElementById("msg2");
    
    

    for(let i=0; i<password.length; i++){
        if(!isNaN(password[i])){
            counter++;
        }

        if (password[i].match(/[A-Z]/)) {
            Cflag = 1;
        } else if (password[i].match(/[a-z]/)) {
            cflag = 1;
        } else if (password[i].match(/[!@#$%^&*(),.?":{}|<>]/)) {
            sflag = 1;
        }
    }
    
    let percent = (counter / password.length) * 100;
   
    
    if(percent >= 50){

        message1.textContent = "Weak";
        message1.style.color = "red";

        message2.textContent = "Weak password more numbers than Letters!"
        message2.style.color = "red";
        return false;
    }else if(counter >= 1 && Cflag == 1 && cflag == 1 && sflag == 1){
        
        message1.textContent = "Strong";
        message1.style.color = "green";
        return true;
        
    }else{
        message1.textContent = "Medium";
        message1.style.color = "yellow";
        return true;
    }


}

//c) Create a function that will show the extra forms for the firefighter to fill out!
//start by getting the element id to acknowledge that we are dealing with a firefight
//inside here get the ids of the simple and the firefighter form and make it go back to normal if the user changes the option of the type.

function FirefighterExtraForms(){

    const fire = document.getElementById("FireFighter");
    const normal = document.getElementById("NormalUser");
    
    const form = document.getElementById("Fire_Form");
    
    let TermText = "*Απαγορεύεται η άσκοπη χρήση της εφαρμογής. Συμφωνώ πως η άσκοπη χρήση της θα διώκεται ποινικά."
    document.getElementById('TermText').textContent = TermText;

    const fire_element = fire ? fire.checked : false;
    const normal_element = normal ? normal.checked : false;
    

    if(fire_element){
        form.style.display = "block";
        TermText = "Δηλώνω υπεύθυνα ότι ανήκω στο ενεργό δυναμικό των εθελοντών πυροσβεστών";
        document.getElementById('TermText').textContent = TermText;

    }else{
        form.style.display = "none";
        txt = TermText;
    }

    if(normal_element)
        form.style.display = "none";
    else 
        form.style.display = "block";

    
}


//d)Make sure that if the afe is 18< or 55> dont let the volunteer submit 
//the second part of this question was done in the previous function

function CalcAge() {
    const fire = document.getElementById("FireFighter");
    const btn = document.getElementById('reg');
    const d1 = document.getElementById('birthdate').value;

    // Only run age validation if the Firefighter option is selected
    if (fire.checked && d1) {
        const birthDate = new Date(d1);
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();

        // Adjust age if birthday hasn't occurred yet this year
        if (today.getMonth() < birthDate.getMonth() || 
            (today.getMonth() === birthDate.getMonth() && today.getDate() < birthDate.getDate())) {
            age--;
        }

        // Disable the submit button if age is less than 18 or greater than 55
        if (age < 18 || age > 55) {
            btn.disabled = true;
        } else {
            btn.disabled = false;
        }
    } else {
        // If Firefighter is not selected, don't apply age validation
        btn.disabled = false;
    }
}




function getSelectedRadioValue(name) {
    const radios = document.getElementsByName(name);
    for (let i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            return radios[i].value; 
        }
    }
    return null; 
}



var lat = 35.0000;
var lon = 25.00000;
var verificator = false;


function VerifyLoc(){
    var country = document.getElementById("country").value;
    var mun = document.getElementById("municipality").value;
    var address = document.getElementById("address").value;

    var FullAddress = address + " " + mun + " " + country;
    var resultElement = document.getElementById("result");



    const xhr = new XMLHttpRequest();
    xhr.withCredentials = true;
    

    xhr.addEventListener('readystatechange', function () {
        if (this.readyState === this.DONE) {
            console.log(this.responseText);

            var response = JSON.parse(this.responseText);
        

            if( response && response.length > 0 ){
                
                var location = response[0];

                if(location && location.display_name && location.display_name.includes("Crete")){

                    lat = location.lat;
                    lon = location.lon;

                    verificator = true;
                    
                    resultElement.textContent = "Found: " + location.display_name;
                    resultElement.style.color = "green";
                    resultElement.classList.add("visible", "success");  // Make visible and apply success styles
                }else{
                    resultElement.style.color = "orange";
                    resultElement.textContent = "This service is only avaiable in Crete!";
                    
                }
                
            }else{
                document.getElementById("result").innerHTML = "Not Found";
                resultElement.style.color = "red";
            }
        }
    });

    
    

    xhr.open('GET',"https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q="+FullAddress+"&accept-language=en&polygon_threshold=0.0" );
    xhr.setRequestHeader('x-rapidapi-key', '99c85300e4msh0371cbb25566cc4p1dd483jsn8b273ed351df');
    xhr.setRequestHeader('x-rapidapi-host', 'forward-reverse-geocoding.p.rapidapi.com');

    xhr.send();

    
}

// Reset function
function Reset() {
    // Clear input fields
    document.getElementById("address").value = "";

    // Hide the map
    document.getElementById("Map").style.display = "none";

    // Clear any previous state variables
    FullAddress = "";
    verificator = false; // Reset the verificator flag
    lat = null; // Clear latitude value
    lon = null; // Clear longitude value

    // Clear result text or styles
    var resultElement = document.getElementById("result");
    resultElement.textContent = "";
    resultElement.style.color = "";
    MapShow = 0;
    // If you have OpenLayers map instances, clear or reset them
    if (typeof map !== 'undefined' && map instanceof OpenLayers.Map) {
        map.destroy();  
    }
}


var MapShow = 0;
// Define these functions outside MakeMarker to make them reusable
function setPosition(lat, lon) {
    var fromProjection = new OpenLayers.Projection("EPSG:4326"); // WGS 1984
    var toProjection = new OpenLayers.Projection("EPSG:900913"); // Spherical Mercator Projection
    var position = new OpenLayers.LonLat(lon, lat).transform(fromProjection, toProjection);
    return position;
}

function handler(position, message) {
    var popup = new OpenLayers.Popup.FramedCloud("Popup", 
        position, null, message, null, true // <-- true for a close (X) button
    );
    map.addPopup(popup);
    var div = document.getElementById('divID');
    
}

function MakeMarker() {
    if (verificator && MapShow == 0) {
        // Initialize Map
        MapShow++;
        map = new OpenLayers.Map("Map");
        var mapnik = new OpenLayers.Layer.OSM();
        map.addLayer(mapnik);
        document.getElementById("Map").style.display = "block";
        // Create Marker Layer
        var markers = new OpenLayers.Layer.Markers("Markers");
        map.addLayer(markers);

        // Define position using global lat and lon
        var position = setPosition(lat, lon);  // Use global lat and lon
        var mar = new OpenLayers.Marker(position);
        markers.addMarker(mar);

        // Add event handler
        mar.events.register('mousedown', mar, function(evt) { 
            handler(position, 'Your House');
        });

        // Set zoom level
        const zoom = 10;
        map.setCenter(position, zoom);
    }
}

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];

        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;

}


function HandleExec(event){
    var jsonOutput;
    var jsonparse;

    const fire = document.getElementById("FireFighter");
    const normal = document.getElementById("NormalUser");

    if(CheckPass() && PassSecurity()){

        if(fire.checked == false){

            var FormData = {
                username : document.getElementById("username").value,
                email: document.getElementById("mail").value,
                password : document.getElementById("pass1").value,
                firstname :  document.getElementById("firstname").value,
                lastname: document.getElementById("lastname").value,
                birthdate : document.getElementById("birthdate").value,
                gender : getSelectedRadioValue("gender"),
                afm : document.getElementById("afm").value,
                type : document.getElementById("NormalUser").value,
                country: document.getElementById("country").value,
                municipality: document.getElementById("municipality").value,
                prefecture : document.getElementById("prefecture").value,
                address: document.getElementById("address").value,
                job : document.getElementById("job").value,
                telephone : document.getElementById("telephone").value,
                lat : lat,
                lon : lon
            };

        }else if(normal.checked == false) {



            var FormData = {
                username : document.getElementById("username").value,
                email: document.getElementById("mail").value,
                password : document.getElementById("pass1").value,
                firstname :  document.getElementById("firstname").value,
                lastname: document.getElementById("lastname").value,
                birthdate : document.getElementById("birthdate").value,
                gender : getSelectedRadioValue("gender"),
                afm : document.getElementById("afm").value,
                type : document.getElementById("FireFighter").value,
                volunteer_type : getSelectedRadioValue("volunteer_type"),
                height : document.getElementById("height").value,
                weight : document.getElementById("weight").value,
                country: document.getElementById("country").value,
                municipality: document.getElementById("municipality").value,
                prefecture : document.getElementById("prefecture").value,
                address: document.getElementById("address").value,
                job : document.getElementById("job").value,
                telephone : document.getElementById("telephone").value,
                lat : lat,
                lon : lon
            };
        }

        jsonOutput = JSON.stringify(FormData,null,4);
        jsonparse = JSON.parse(jsonOutput);
        console.log(jsonOutput);


        /* Make a POST request */
        var xhr = new XMLHttpRequest();
        xhr.onload = function () {

            if (xhr.readyState === 4 && xhr.status === 200) {
                /* Load a new message and Say it is suceesful */

                $('#Log').html("<h1>Successful Registration</h1>");
                $('#ajaxContent').html("<h1>Your Data</h1>");
                $('#ajaxContent').append(createTableFromJSON(jsonparse));

            } else if (xhr.status === 409) {
                const response = JSON.parse(xhr.responseText);
                alert('Request failed. Returned status of 409 ' + response.message);

            }else{
                alert('Request failed. Returned status of ' + xhr.status);
            }
        };



        xhr.open('POST','Ass3Servlet');
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(jsonOutput);
        /* The POST request has been created and we asserted it in the correct Servlet */
    }


}