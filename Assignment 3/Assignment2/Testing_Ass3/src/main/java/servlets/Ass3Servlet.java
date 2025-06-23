package servlets;

import Exceptions.AFMExists;
import Exceptions.TelephoneExists;
import Exceptions.UsernameExists;
import com.google.gson.Gson;
import com.sun.prism.PixelFormat;
import database.init.JSON_Converter;
import database.tables.EditParticipantsTable;
import database.tables.EditUsersTable;
import database.tables.EditVolunteersTable;
import mainClasses.User;
import Exceptions.UsernameExists;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.io.Writer;
import java.sql.SQLException;


@WebServlet(name = "Ass3Servlet", value = "/Ass3Servlet")
public class Ass3Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // We got the data from the xhr request now we have
        // To chagne them to JSON form

        JSON_Converter jc  = new JSON_Converter();
        String new_user_data = jc .getJSONFromAjax(request.getReader());
        System.out.println("Received JSON: " + new_user_data);

        // we have to find a way to declare if the data belong to a user
        // or if they belong to volunteer , in every case
        // we handle them accordingly !

        // if the json string from AJAX either contains Normal User
        if (new_user_data.contains("Normal User")) {
            EditUsersTable new_addition = new EditUsersTable();

            try {
                new_addition.addUserFromJSON(new_user_data);

                // Success response
                response.setContentType("application/json");
                response.getWriter().write("{\"status\": \"success\", \"message\": \"User added successfully.\"}");
                response.setStatus(200);
            } catch (UsernameExists e) {
                response.setStatus(409); // HTTP 409 Conflict
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Username already exists.\"}");
            } catch (AFMExists e) {
                response.setStatus(409); // HTTP 409 Conflict
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Email already exists.\"}");
            } catch (TelephoneExists e) {
                response.setStatus(409); // HTTP 409 Conflict
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Telephone number already exists.\"}");
            } catch (ClassNotFoundException | SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP 500 Internal Server Error
                response.getWriter().write("{\"status\": \"error\", \"message\": \"An unexpected error occurred.\"}");
            }

    }else if(new_user_data.contains("Firefighter")){ // Either it contains Firefighter

            EditVolunteersTable new_vol = new EditVolunteersTable();

            try {
                new_vol.addVolunteerFromJSON(new_user_data);

                // Success response
                response.setContentType("application/json");
                response.getWriter().write("{\"status\": \"success\", \"message\": \"User added successfully.\"}");
                response.setStatus(200);
            } catch (UsernameExists e) {
                response.setStatus(409); // HTTP 409 Conflict
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Username already exists.\"}");
            } catch (AFMExists e) {
                response.setStatus(409); // HTTP 409 Conflict
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Email already exists.\"}");
            } catch (TelephoneExists e) {
                response.setStatus(409); // HTTP 409 Conflict
                response.getWriter().write("{\"status\": \"error\", \"message\": \"Telephone number already exists.\"}");
            } catch (ClassNotFoundException | SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // HTTP 500 Internal Server Error
                response.getWriter().write("{\"status\": \"error\", \"message\": \"An unexpected error occurred.\"}");
            }
        }





    }
}