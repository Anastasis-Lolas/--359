/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import mainClasses.JSONConverter;
import mainClasses.Person;

/**
 *
 * @author mountant
 */
public class Register extends HttpServlet {

    

   
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            JSONConverter jc = new JSONConverter();
            HttpSession session=request.getSession();
            Person p=Resources.registeredUsers.get(session.getAttribute("loggedIn").toString());
            String json = jc.JavaObjectToJSONRemoveElements(p, "password");//personToJSON(p);
            response.setStatus(200);
            response.getWriter().write(json);
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JSONConverter jc = new JSONConverter();
        Person p =jc.jsonToPerson(request.getReader());
        p.setValues();
        String JsonString = jc.personToJSON(p);
        
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if (Resources.registeredUsers.containsKey(p.getUsername())) {
            response.setStatus(409);
            Gson gson = new Gson();
            JsonObject jo = new JsonObject();
            jo.addProperty("error", "Username Already Taken");
            response.getWriter().write(jo.toString());
        } else {
            Resources.registeredUsers.put(p.getUsername(), p);
            response.setStatus(200);
            response.getWriter().write(JsonString);
  
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
