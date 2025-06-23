package servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.init.JSON_Converter;
import database.tables.EditUsersTable;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UpdateServlet", value = "/UpdateServlet")
public class UpdateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSON_Converter jc = new JSON_Converter();
        String updateData = jc.getJSONFromAjax(request.getReader());

        System.out.println("Received for Update : " + updateData);

        HttpSession session = request.getSession();

        if (session.getAttribute("loggedIn") != null) {  // Check if the user is logged in
            String username = (String) session.getAttribute("loggedIn");
            EditUsersTable updaterUser = new EditUsersTable();

            JsonObject jsonObject = JsonParser.parseString(updateData).getAsJsonObject();
            // Dynamically extracting the key-value pair from the JSON object
            String key = jsonObject.keySet().iterator().next(); // Get the first key in the JSON
            String value = jsonObject.get(key).getAsString();
            System.out.println("Received key: " + key + ", value: " + value);

            try {
                // Update the user's data
                updaterUser.updateUser(username, key, value);
                response.setStatus(200); // Successfully updated
            } catch (SQLException | ClassNotFoundException e) {
                response.setStatus(500); // Internal Server Error
                e.printStackTrace();
            } catch(IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
                response.getWriter().write(e.getMessage());
            }
        } else {
            response.setStatus(403);  // Unauthorized if session is not found
        }
    }
}