/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import Exceptions.AFMExists;
import Exceptions.TelephoneExists;
import Exceptions.UsernameExists;
import com.google.gson.Gson;
import mainClasses.User;
import mainClasses.Volunteer;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Exceptions.UsernameExists;
import Exceptions.TelephoneExists;
import Exceptions.AFMExists;

import static java.lang.System.out;

/**
 *
 * @author Mike
 */
public class EditVolunteersTable {

 
    public void addVolunteerFromJSON(String json) throws ClassNotFoundException, SQLException, TelephoneExists, AFMExists, UsernameExists {
         Volunteer user=jsonToVolunteer(json);

        CheckDuplicateUsers(user);
        CheckDuplicateTelephone(user);
        CheckDuplicateMail(user);


        addNewVolunteer(user);
    }
    
     public Volunteer jsonToVolunteer(String json){
         Gson gson = new Gson();

        Volunteer user = gson.fromJson(json, Volunteer.class);
        return user;
    }
    
    public String volunteerToJSON(Volunteer user){
         Gson gson = new Gson();

        String json = gson.toJson(user, Volunteer.class);
        return json;
    }

    public void  CheckDuplicateUsers(Volunteer user) throws SQLException, ClassNotFoundException, UsernameExists {

        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();


        String dupQuery = "SELECT COUNT(*) FROM users WHERE username = '" + user.getUsername() + "'";

        ResultSet rs = stmt.executeQuery(dupQuery);

        rs.next();
        if (rs.getInt(1) > 0) {
            out.println("# The username '" + user.getUsername() + "' already exists in the database.");
            throw new UsernameExists("The Username Already exists!");
        }

    }

    public void CheckDuplicateMail(Volunteer user) throws SQLException, ClassNotFoundException, AFMExists {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();


        String dupQuery = "SELECT COUNT(*) FROM users WHERE email = '" + user.getEmail() + "'";

        ResultSet rs = stmt.executeQuery(dupQuery);

        rs.next();
        if (rs.getInt(1) > 0) {
            throw new AFMExists("This Email Already exists!");
        }
    }

    public void CheckDuplicateTelephone(Volunteer user) throws  SQLException, ClassNotFoundException,TelephoneExists {
        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();

        String dupQuery = "SELECT COUNT(*) FROM users WHERE telephone = '" + user.getTelephone() + "'";

        ResultSet rs = stmt.executeQuery(dupQuery);

        rs.next();
        if (rs.getInt(1) > 0) {
            out.println("# The username '" + user.getUsername() + "' already exists in the database.");
            throw new TelephoneExists("The Telephone Already exists!");
        }
    }
    
    public void updateVolunteer(String username,String personalpage) throws SQLException, ClassNotFoundException{
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        String update="UPDATE volunteers SET personalpage='"+personalpage+"' WHERE username = '"+username+"'";
        stmt.executeUpdate(update);
    }
    
    public void printVolunteerDetails(String username) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM volunteers WHERE username = '" + username + "'");
            while (rs.next()) {
                System.out.println("===Result===");
                DB_Connection.printResults(rs);
            }

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
    
    public Volunteer databaseToVolunteer(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM volunteers WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            Volunteer user = gson.fromJson(json, Volunteer.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    

    
    
    public ArrayList<Volunteer> getVolunteers(String type) throws SQLException, ClassNotFoundException {
        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();
        ArrayList<Volunteer> volunteers = new ArrayList<Volunteer>();
        ResultSet rs = null;
        try {
            if("simple".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM volunteers WHERE volunteer_type= '" + "simple" + "'");
            else if ("driver".equals(type))
                 rs = stmt.executeQuery("SELECT * FROM volunteers WHERE volunteer_type= '" + "driver" + "'");

           
            while (rs.next()) {
                String json = DB_Connection.getResultsToJSON(rs);
                Gson gson = new Gson();
                Volunteer volunteer = gson.fromJson(json, Volunteer.class);
                volunteers.add(volunteer);
            }
            return volunteers;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    
    public String databaseVolunteerToJSON(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM volunteers WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


     public void createVolunteersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE volunteers "
                  + "(volunteer_id INTEGER not NULL AUTO_INCREMENT, "
                + "    username VARCHAR(30) not null unique,"
                + "    email VARCHAR(50) not null unique,	"
                + "    password VARCHAR(32) not null,"
                + "    firstname VARCHAR(30) not null,"
                + "    lastname VARCHAR(30) not null,"
                + "    birthdate DATE not null,"
                + "    gender  VARCHAR (7) not null,"
                + "    afm  VARCHAR (10) not null,"
                + "    country VARCHAR(30) not null,"
                + "    address VARCHAR(100) not null,"
                + "    municipality VARCHAR(50) not null,"
                + "    prefecture VARCHAR(15) not null,"
                + "    job VARCHAR(200) not null,"
                + "    telephone VARCHAR(14) not null unique,"
                  + "    lat DOUBLE,"
                + "    lon DOUBLE,"
                + "    volunteer_type VARCHAR(10) not null,"
                + "    height DOUBLE,"
                + "    weight DOUBLE,"
                + " PRIMARY KEY (volunteer_id))";
        stmt.execute(query);
        stmt.close();
    }
    
    
    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewVolunteer(Volunteer user) throws ClassNotFoundException {
        try {

            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();

            String checkQuery = "SELECT COUNT(*) FROM volunteers WHERE username = '" + user.getUsername() + "'";

            ResultSet rs = stmt.executeQuery(checkQuery);

            rs.next();
            if (rs.getInt(1) > 0) {
                out.println("# The username '" + user.getUsername() + "' already exists in the database.");
                throw new UsernameExists("The Username Already exists!");
            }

            checkQuery = "SELECT COUNT(*) FROM volunteers WHERE email = '" + user.getEmail() + "'";

            rs = stmt.executeQuery(checkQuery);

            rs.next();
            if (rs.getInt(1) > 0) {
                out.println("# The username '" + user.getUsername() + "' already exists in the database.");
                throw new AFMExists("The Email Already exists!");
            }

            checkQuery = "SELECT COUNT(*) FROM volunteers WHERE telephone= '" + user.getTelephone() + "'";

            rs = stmt.executeQuery(checkQuery);

            rs.next();
            if (rs.getInt(1) > 0) {
                out.println("# The username '" + user.getUsername() + "' already exists in the database.");
                throw new TelephoneExists("The telephone Already exists!");
            }

            String insertQuery = "INSERT INTO "
                    + " volunteers (username,email,password,firstname,lastname,birthdate,gender,afm,country,address,municipality,prefecture,"
                    + "job,telephone,lat,lon,volunteer_type,height,weight)"
                    + " VALUES ("
                    + "'" + user.getUsername() + "',"
                    + "'" + user.getEmail() + "',"
                    + "'" + user.getPassword() + "',"
                    + "'" + user.getFirstname() + "',"
                    + "'" + user.getLastname() + "',"
                    + "'" + user.getBirthdate() + "',"
                    + "'" + user.getGender() + "',"
                    + "'" + user.getAfm() + "',"
                    + "'" + user.getCountry() + "',"
                    + "'" + user.getAddress() + "',"
                    + "'" + user.getMunicipality() + "',"                    
                    + "'" + user.getPrefecture() + "',"
                     + "'" + user.getJob() + "',"
                    + "'" + user.getTelephone() + "',"
                    + "'" + user.getLat() + "',"
                    + "'" + user.getLon() + "',"
                    + "'" + user.getVolunteer_type() + "',"
                    + "'" + user.getHeight()+ "',"
                    + "'" + user.getWeight() + "'"
                    + ")";
            //stmt.execute(table);
            System.out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            System.out.println("# The volunteer was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(EditVolunteersTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UsernameExists | TelephoneExists | AFMExists e) {
            throw new RuntimeException(e);
        }
    }

   

}
