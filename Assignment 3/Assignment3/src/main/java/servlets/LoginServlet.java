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

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // We have to get the session for the LOGIN
        // We will use HttpAPI and not Cookies

        HttpSession session = request.getSession(true);

        JSON_Converter jc = new JSON_Converter();
        String check_login = jc.getJSONFromAjax(request.getReader());

        System.out.println("Received : " + check_login);

        JsonObject jsonObject = JsonParser.parseString(check_login).getAsJsonObject();
        String username = jsonObject.get("username").getAsString();

        System.out.println("Username to be saved for the session : " + username);




        EditUsersTable Login = new EditUsersTable();
        int res ;
        try {
            res = Login.LoginCheck(check_login);

            if(res == 1){
                response.setStatus(200);
                session.setAttribute("loggedIn", username);
            }else
                response.setStatus(401);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }


    };
}