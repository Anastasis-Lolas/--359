/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.tables;

import Exceptions.*;
import mainClasses.User;
import com.google.gson.Gson;
import mainClasses.User;
import database.DB_Connection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Participant;
import Exceptions.TelephoneExists;
import Exceptions.AFMExists;

import javax.servlet.http.HttpServletResponse;

import static java.lang.System.out;

/**
 *
 * @author Mike
 */
public class EditUsersTable {

    public int LoginCheck(String json) throws ClassNotFoundException,SQLException{


        User user  = jsonToUser(json);

        Connection con = DB_Connection.getConnection();

        Statement stmt = con.createStatement();

        String dupQuery = "SELECT COUNT(*) FROM users " + "WHERE username = '" + user.getUsername() + "' " + "AND password = '" + user.getPassword() + "'";

        ResultSet rs = stmt.executeQuery(dupQuery);

        rs.next();
        if (rs.getInt(1) > 0) {

            System.out.println("LOGIN SUCCESSFUL!");

            return  1;
        }

        return 0;

    }
 
    public void addUserFromJSON(String json) throws ClassNotFoundException, UsernameExists, TelephoneExists, AFMExists, SQLException {
         User user=jsonToUser(json);


         CheckDuplicateUsers(user);
         CheckDuplicateTelephone(user);
         CheckDuplicateMail(user);


        addNewUser(user);
    }
    
     public User jsonToUser(String json){
         Gson gson = new Gson();

        User user = gson.fromJson(json, User.class);
        return user;
    }
    
    public String userToJSON(User user){
         Gson gson = new Gson();

        String json = gson.toJson(user, User.class);
        return json;
    }
    
   public void  CheckDuplicateUsers(User user) throws SQLException, ClassNotFoundException, UsernameExists {

       Connection con = DB_Connection.getConnection();

       Statement stmt = con.createStatement();


        String dupQuery = "SELECT COUNT(*) FROM volunteers WHERE username = '" + user.getUsername() + "'";

       ResultSet rs = stmt.executeQuery(dupQuery);

       rs.next();
       if (rs.getInt(1) > 0) {
           out.println("# The username '" + user.getUsername() + "' already exists in the database.");
           throw new UsernameExists("The Username Already exists!");
       }

   }

   public void CheckDuplicateMail(User user) throws SQLException, ClassNotFoundException, AFMExists {
       Connection con = DB_Connection.getConnection();

       Statement stmt = con.createStatement();


       String dupQuery = "SELECT COUNT(*) FROM volunteers WHERE email = '" + user.getEmail() + "'";

       ResultSet rs = stmt.executeQuery(dupQuery);

       rs.next();
       if (rs.getInt(1) > 0) {
           throw new AFMExists("This Email Already exists!");
       }
   }

   public void CheckDuplicateTelephone(User user) throws  SQLException, ClassNotFoundException,TelephoneExists {
       Connection con = DB_Connection.getConnection();

       Statement stmt = con.createStatement();


       String dupQuery = "SELECT COUNT(*) FROM volunteers WHERE telephone = '" + user.getTelephone() + "'";

       ResultSet rs = stmt.executeQuery(dupQuery);

       rs.next();
       if (rs.getInt(1) > 0) {
           out.println("# The username '" + user.getUsername() + "' already exists in the database.");
           throw new TelephoneExists("The Username Already exists!");
       }
   }

   public int CheckDupUpdate(String username,String key,String value) throws SQLException,ClassNotFoundException{
           Connection con = DB_Connection.getConnection();
           Statement stmt = con.createStatement();

           // SQL query to check if the key-value exists for a different username
       String CheckUpdate= "SELECT COUNT(*) FROM users WHERE " + key + " ='" + value + " = ? AND username != ?";


       ResultSet rs = stmt.executeQuery(CheckUpdate);

           if (rs.next() && rs.getInt(1) > 0) {
               // Duplicate found
               System.out.println("This key-value pair already exists for a different user.");
               return 0;
           } else {
               // No duplicate, safe to update
               System.out.println("No duplicate found. Safe to proceed with update.");
           }

           rs.close();
           stmt.close();
           con.close();

           return 1;

   }


    
    public void updateUser(String username,String key,String value) throws SQLException, ClassNotFoundException {

            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String update = "UPDATE users SET " + key + "='" + value + "' WHERE username = '" + username + "'";
            stmt.executeUpdate(update);
            stmt.close();
            con.close();


    }
   
    
    public User databaseToUsers(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            Gson gson = new Gson();
            User user = gson.fromJson(json, User.class);
            return user;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public String databaseUserToJSON(String username, String password) throws SQLException, ClassNotFoundException{
         Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + username + "' AND password='"+password+"'");
            rs.next();
            String json=DB_Connection.getResultsToJSON(rs);
            return json;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }


     public void createUsersTable() throws SQLException, ClassNotFoundException {

        Connection con = DB_Connection.getConnection();
        Statement stmt = con.createStatement();

        String query = "CREATE TABLE users "
                + "(user_id INTEGER not NULL AUTO_INCREMENT, "
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
                + " PRIMARY KEY (user_id))";
        stmt.execute(query);
        stmt.close();
    }
    
    
    /**
     * Establish a database connection and add in the database.
     *
     * @throws ClassNotFoundException
     */
    public void addNewUser(User user) throws ClassNotFoundException {
        try {



            Connection con = DB_Connection.getConnection();

            Statement stmt = con.createStatement();


            String insertQuery = "INSERT INTO "
                    + " users (username,email,password,firstname,lastname,birthdate,gender,afm,country,address,municipality,prefecture,"
                    + "job,telephone,lat,lon)"
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
                    + "'" + user.getLon() + "'"
                    + ")";
            //stmt.execute(table);
            out.println(insertQuery);
            stmt.executeUpdate(insertQuery);
            out.println("# The user was successfully added in the database.");

            /* Get the member id from the database and set it to the member */
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditUsersTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   

}
