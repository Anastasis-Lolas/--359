/**
 *
 *  This function allows the user to see it's password in both login and logged in pages
 *  Also it is being called when an img is clicked .
 *
 *  @param The id element of password and of the img .
 */
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




// Create a HTTP request for access for the servlet
// After that check in the Servlet if the username,pass exists and return the correct number(200,400)
/**
 *
 *  Checks if a user can login after it presses the login button .
 *  It creates a Http Request to a specified LoginServlet .
 *  It sends the data ( username password ) and after that
 *  The servlet checks in the database if there is a match .
 *  And if there is a HttpSession with these credentials is being set .
 *
 *  @param username  password
 *
 *
 * @constructor
 */
function CheckLogin(){

    // Here we can create a request for acces for the server !
    // Take username,password as elements

    var username = document.getElementById("username").value;
    var password = document.getElementById("pass1").value;

    var FormData = {
        "username": username,
        "password": password
    };

    var JsonOutput = JSON.stringify(FormData);
    console.log(JsonOutput);

    var xhr = new XMLHttpRequest();
    xhr.onload = function(){

        if(xhr.readyState === 4 && xhr.status === 200){
            // Load a new page that shows the data of the certain user
            CheckData(username, password);

            $('body').empty();

            // Load new content from "logout.html" into the body
            $('body').load("logout.html");


        }else{
            // Here create an alert message that will say that the credentials are not viable
            alert("Login failed this user does not exist!");

        }
    }

    xhr.open("POST", 'LoginServlet', true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JsonOutput);



}


/**
 *  When the logout button is pressed it sends a request to LogoutServlet .
 *  It checks if a session exists and if it does it cancles it .
 *  Than it loads the login page .
 *
 *  @param No Param
 *
 * @constructor
 */
function Logout(){
    // Same as login -> Make a request http
    // Invalidate the session in the servlet  thats all !



    var xhr = new XMLHttpRequest();
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            // Load a new page that shows the data of the certain user



            // Load new content from "logout.html" into the body
            $('body').load("index.html");

            alert("Logout Sucessfully!");
        }else{
            // Here create an alert message that will say that the credentials are not viable
            alert("Login failed this user does not exist!");

        }
    }


    xhr.open("POST", 'LogoutServlet', true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send();
}

function createTableFromJSON(data) {
    var html = "<table><tr><th>Category</th><th>Value</th></tr>";
    for (const x in data) {
        var category = x;
        var value = data[x];
        if (value.endsWith('jpg') || value.endsWith("png")) {
            value = "<img height=300 src='" + value + "'/>";
        }
        html += "<tr><td>" + category + "</td><td>" + value + "</td></tr>";
    }
    html += "</table>";
    return html;

}


/**
 *
 *  After the login, takes the credentials where the session is created and than
 *  Goes to the Data Servlet where there it asks for access in the DB to take all the data of this certain user .
 *  Than it gives them back to the client in JSON Form and after that a new page is being shown with the data
 *  of the user .
 *
 * @param username
 * @param password
 * @constructor
 */
function CheckData(username, password){

    var FormData = {
        "username": username,
        "password": password
    };



    var JsonOutput = JSON.stringify(FormData);

    xhr = new XMLHttpRequest();
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){

            if (xhr.responseText) {
                var userData = JSON.parse(xhr.responseText);

                console.log(userData);
                document.getElementById("username").value = userData["username"];
                document.getElementById("mail").value = userData["email"];
                document.getElementById("pass1").value = password;
                document.getElementById("firstname").value = userData["firstname"];
                document.getElementById("lastname").value = userData["lastname"];
                document.getElementById("birthdate").value = userData["birthdate"];
                document.getElementById("gender").value = userData["gender"];
                document.getElementById("afm").value = userData["afm"];
                document.getElementById("country").value = userData["country"];
                document.getElementById("municipality").value = userData["municipality"];
                document.getElementById("prefecture").value = userData["prefecture"];
                document.getElementById("address").value = userData["address"];
                document.getElementById("job").value = userData["job"];
                document.getElementById("telephone").value = userData["telephone"];
                document.getElementById("lat").value = userData["lat"];
                document.getElementById("lon").value = userData["lon"];


            } else {
                console.error("Empty response from the server");
            }

        }else{
            // Here create an alert message that will say that the credentials are not viable
            alert("Ayo")

        }
    }

    xhr.open("POST", 'DataServlet', true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JsonOutput);
}

var verifyResult = 0 ;
/**
 *  Takes the element id and its value when an Update button is pressed . Than the Update Servlet
 *  goes to the database and Updates the current key : value that was given according to the username of
 *  the user which is stored in the Http session . Of course in every ocation we check if a session exists
 *  else we return an 40X code . If it works the DB is updated and we upload a success messsage .
 *
 * @param elementId
 * @constructor
 */
function Update(elementId){
    // take the data that were shown in the screen and check if something changed
    // if it does update the values in the database
    // else make an alert(no updates)

    // make form data to send to a new Servlet
    var jsonOutput ;

    console.log(elementId + " : " + document.getElementById(elementId).value);

    if(elementId==="address" && verifyResult !== 1){
        alert("You have to verify your address first ! ");
        return ;
    }

    var FormData = {
         [elementId] : document.getElementById(elementId).value
    };

     jsonOutput = JSON.stringify(FormData,null,4);
     console.log(jsonOutput);
    // Make a request

    var xhr = new XMLHttpRequest();
    xhr.onload = function(){
        if(xhr.readyState === 4 && xhr.status === 200){
            document.getElementById("Log").innerHTML = "Update Successfully!";


        }else{
            alert("Update Failed");
        }
    }

    xhr.open("POST", 'UpdateServlet', true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(jsonOutput);
}




function VerifyLoc(){
    var country = document.getElementById("country").value;
    var mun = document.getElementById("municipality").value;
    var address = document.getElementById("address").value;

    var FullAddress = address + " " + mun + " " + country;
    var resultElement = document.getElementById("result");



    const xhr = new XMLHttpRequest();
    xhr.withCredentials = false;


    xhr.addEventListener('readystatechange', function () {
        if (this.readyState === this.DONE) {
            console.log(this.responseText);

            var response = JSON.parse(this.responseText);


            if( response && response.length > 0 ){

                var location = response[0];

                if(location && location.display_name && location.display_name.includes("Crete")){

                    lat = location.lat;
                    lon = location.lon;


                    resultElement.textContent = "Found: " + location.display_name;
                    resultElement.style.color = "green";
                    resultElement.classList.add("visible", "success");  // Make visible and apply success styles

                    verifyResult = 1;

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



