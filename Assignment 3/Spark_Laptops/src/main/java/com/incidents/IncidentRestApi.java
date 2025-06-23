package com.incidents;


import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.path;
import static spark.Spark.port;

import com.mycompany.spark_laptops.StandardResponse;
import database.tables.EditIncidentsTable;
import mainClasses.Incident;



public class IncidentRestApi {
    static String apiPath = "IncidentsApi/reports";
    public static void main(String[] args) {
        // we will check later if we need to add an incident or not !

        // add new incident POST function as requested
        post(apiPath + "/incident",(request, response) -> {
            // we first create a incident object and we parse the data from the request
            try {
                // than we create an instance for the DB->incidentTable

                Incident incident = new Gson().fromJson(request.body(), Incident.class);
                String json = request.body();
                EditIncidentsTable newIncident = new EditIncidentsTable();

                // check if the phone is 10-14 numbers

                String u_phone = incident.getUser_phone();
                if (u_phone.length() < 10 || u_phone.length() > 14) {
                    System.out.println("Invalid phone number");
                    response.status(400);
                    return "Invalid phone type";
                }

                // check if the user type holds one of these 3 values

                String u_type = incident.getUser_type();
                if (!(u_type.equals("guest") || u_type.equals("admin") || u_type.equals("user"))) {
                    System.out.println("Invalid type");
                    response.status(400);
                    return "Invalid user type";
                }

                String i_type = incident.getIncident_type();
                System.out.println("incident : " + i_type);
                // check if the incident type is 1 of these 2

                if (!(i_type.equals("fire") || i_type.equals("accident"))) {
                    System.out.println("Invalid accident type");
                    response.status(400);
                    return "Invalid accident type";
                }

                String mun = incident.getPrefecture();

                // check if it is being placed in 1 of the 4 municipalities of Crete

                if (!(mun.equals("heraklion") || mun.equals("chania")
                        || mun.equals("rethymno") || mun.equals("lasithi"))) {
                    System.out.println("Invalid prefecutre");
                    response.status(400);
                    return "Invalid Prefecture type";
                }

                // for every check that is wrong we want to send a 400 error code
                // to signify that the data that was passed are not correct ( user-input problem)

                // if nothing went wrong than we can add the incident to the DB
                String modifiedJson = json.substring(0, json.length() - 1) + ",\n"
                        + "    \"status\":\"submitted\",\n"  // Adding status
                        + "    \"danger\":\"unknown\"\n"   // Adding danger
                        + "}";
                newIncident.addIncidentFromJSON(modifiedJson);
                response.status(200);
                return new Gson().toJson(new StandardResponse(new Gson().toJson("Success: Incident Added to the DataBase!")));
            }catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Internal Server Error"+e.getMessage();
            }

        });

        // get an incident from the database with parameters . NO UPDATE
        get(apiPath + "/incidents/:type/:status",(request, response) ->{
            try {
                response.type("application/json");
                ArrayList<Incident> incidentsStats = new ArrayList<Incident>();
                String type = request.params(":type");
                String status = request.params(":status");
                String municipality = request.queryParams("municipality");


                EditIncidentsTable getTypeStats = new EditIncidentsTable();
                // we dont have to check if the status and the type is all
                // the EditInceTable already checks that
                // the main difference in the change of all is the Query String

                // Use the alreaady made function from the incident Table
                // and put the incidents that match these strings in an
                // ArrayList. After that check if it is empty or not
                // and send the proper status code!
                incidentsStats = getTypeStats.databaseToIncidentsSearch(type, status, municipality);

                if (!incidentsStats.isEmpty()) {
                    String json = new Gson().toJson(incidentsStats);
                    response.status(200);
                    return new Gson().toJson(new StandardResponse(new Gson().toJsonTree(incidentsStats)));
                } else {
                    response.status(403);
                    return new Gson().toJson(new StandardResponse("No incidents found matching the criteria"));
                }
            }catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Internal Server Error "+e.getMessage();
            }
        });

        // get an incident_id and a status that you want to update .
        put(apiPath + "/incidentStatus/:incident_id/:status",(request, response) -> {
            response.type("application/json");
            String incident_id = request.params(":incident_id");
            String status = request.params(":status");

            // Instance of the incident table
            EditIncidentsTable updateIncidentStats = new EditIncidentsTable();

            // We now have to create a HashMap that will represent the status key-value mapping
            HashMap<String,String> updates = new HashMap<>();

            if (status.equals("running") || status.equals("fake")) {
                updates.put("status", status); // Update the status field
            } else if (status.equals("finished")) {
                updates.put("status", "finished"); // Update the status field
                // Add the current datetime as end_datetime for finished status
                String currentDateTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                updates.put("end_datetime", currentDateTime); // Add the end_datetime field
            }

            try{
                updateIncidentStats.updateIncident(incident_id,updates);
                response.status(200); // Respond with success status
                return new Gson().toJson(new StandardResponse("Incident status updated successfully."));
            }catch (Exception e) {
                response.status(500); // Internal server error
                return new Gson().toJson(new StandardResponse("Error updating incident status: " + e.getMessage()));
            }


        });

        // Delete an incident from the database
        delete(apiPath + "/incidentDeletion/:incident_id", (request, response) -> {

            response.type("application/json");
            String incident_id = request.params(":incident_id");

            EditIncidentsTable deleteIncident = new EditIncidentsTable();
            // search first if the incident_id exists
            try{
                deleteIncident.deleteIncident(incident_id);
                response.status(200);
                return new Gson().toJson(new StandardResponse(new Gson().toJson("Incident Deleted")));
            }catch (Exception e) {
                response.status(403);
                return "Error incident_id does not exist";
            }

        });
    }
}
