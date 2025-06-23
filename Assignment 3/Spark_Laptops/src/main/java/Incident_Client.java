import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.http.HttpHeaders.ACCEPT;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class Incident_Client {
    private HttpPost addIncident;
    private HttpClient client;
    private HttpGet allIncidents;
    private HttpPut updateIncident;
    private HttpDelete deleteIncident;
    private String serviceName;


    private static final String URL =
            "http://localhost:4567/IncidentsApi/reports";

    public Incident_Client() {client = HttpClientBuilder.create().build();}


    public void addIncidents(String json) throws IOException {
        try {
            serviceName = "incident";
            addIncident = new HttpPost(URL + "/"  + serviceName);
            System.out.println(addIncident.getURI().toString());
            addIncident.setHeader(ACCEPT, "application/json");
            addIncident.setHeader(CONTENT_TYPE, "application/json");
            StringEntity entity = new StringEntity(json);
            addIncident.setEntity(entity);
            HttpResponse response = client.execute(addIncident);
            int responseCode = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

        }catch(Exception ex) {
            Logger.getLogger(Incident_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getIncidents_status_type(String type,String status,String municipality) throws IOException {
        try {
            serviceName = "incidents/"+type+ "/" +status;

            if(!"".equals(municipality))
                serviceName +="?municipality="+municipality;
            System.out.println(serviceName);
            allIncidents= new HttpGet(URL + "/"+serviceName);
            System.out.println(allIncidents.getURI().toString());
            allIncidents.addHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(allIncidents);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }

        }catch (Exception ex) {
            Logger.getLogger(Incident_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdateIncident(int incident_id,String status) throws IOException {
        try {
            serviceName = "incidentStatus";
            updateIncident = new HttpPut(URL + "/"+serviceName + "/" + incident_id + "/"+status);
            System.out.println(updateIncident.getURI().toString());
            updateIncident.setHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(updateIncident);
            int responseCode = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception ex) {
            Logger.getLogger(Incident_Client.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void DeleteIncident(int incident_id) throws IOException {
        try {
            serviceName = "incidentDeletion";
            deleteIncident = new HttpDelete(URL + "/" + serviceName + "/" + incident_id);
            deleteIncident.addHeader(ACCEPT, "application/json");
            HttpResponse response = client.execute(deleteIncident);
            int responseCode = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception ex) {
            Logger.getLogger(Incident_Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        Incident_Client client = new Incident_Client();
        System.out.println("New Incident maybe!");
        String json =  "{\n"
                + "    \"incident_type\":\"fire\",\n"
                + "    \"description\":\"Fire in Niaousta\",\n"
                + "    \"user_type\":\"guest\",\n"
                + "    \"user_phone\":\"6977771111\",\n"
                + "    \"address\":\"Agia Pelagia\",\n"
                + "    \"lat\":35.12345,\n" // Replace "X" with a valid latitude value
                + "    \"lon\":25.12345,\n" // Replace "Y" with a valid longitude value
                + "    \"prefecture\":\"heraklion\",\n"
                + "    \"municipality\":\"Malevizi\"\n"
                + "}";

        System.out.println("adding it!");
        client.addIncidents(json);
        System.out.println("getting incidents !!!");
        client.getIncidents_status_type("fire","running","");
        System.out.println("getting another incident with municipality!");
        client.getIncidents_status_type("accident","running","Heraklion");
        System.out.println("Updating Incident!");
        client.UpdateIncident(2,"running");
         client.UpdateIncident(6,"finished");
        System.out.println("Now deleting !");
       client.DeleteIncident(6);

    }
}
