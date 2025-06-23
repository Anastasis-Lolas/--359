package servlets;

import database.init.JSON_Converter;
import database.tables.EditUsersTable;
import mainClasses.User;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DataServlet", value = "/DataServlet")
public class DataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // make a new converter to pass the data
        JSON_Converter jc = new JSON_Converter();

        // Pass the data of the js -> findData string and than try access the DB
        String findData = jc.getJSONFromAjax(request.getReader());

        HttpSession session = request.getSession();

        System.out.println("Received Again : " + findData);

        EditUsersTable Data = new EditUsersTable();

        System.out.println("Session :" + session.getAttribute("loggedIn"));

        if((String)session.getAttribute("loggedIn") != null) {
            try {
                User user = new User();
                user = Data.jsonToUser(findData);

                findData = Data.databaseUserToJSON(user.getUsername(), user.getPassword());

                System.out.println("Gained : " + findData);


                response.setStatus(200);

                response.getWriter().write(findData);

            } catch (ClassNotFoundException | SQLException e) {
                response.setStatus(500);
                throw new RuntimeException(e);
            }

        }
    }
}