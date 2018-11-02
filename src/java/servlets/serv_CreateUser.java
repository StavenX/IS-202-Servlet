/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import exceptions.InvalidSymbolException;
import helpers.HtmlHelper;
import helpers.StudentHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;

/**
 *
 * @author Staven
 */
@WebServlet(name = "createUser", urlPatterns = {"/createUser"})
public class serv_CreateUser extends HttpServlet {

    Login login = new Login();

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
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8"); 
        
        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New user", "create-user");
            out.println("");
            out.println("<form action=\"createUser\" method=\"post\">");
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_name\" placeholder=\"Insert username\">");   
            out.println("<input class=\"student-input\" type=\"password\" name=\"user_password\" placeholder=\"Insert password\">");
            out.println("<select class=\"student-input\" name=\"user_role\">");
            out.println("<option value=\"Student\">Student</option>");
            out.println("<option value=\"Lecturer\">Lecturer</option>");
            out.println("</select>"); 
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_fname\" placeholder=\"Insert first name\">");
            out.println("<input class=\"student-input\" type=\"text\" name=\"user_lname\" placeholder=\"Insert last name\">");
            out.println("<input class=\"button\" type=\"Submit\" name=\"get\" value=\"Create\">");
            out.println("</form>");
            site.printEnd();
        }       
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
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8"); 
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("New user created", "create-user");
            
            String user_username = request.getParameter("user_username");
            String user_password = request.getParameter("user_password");
            
            try {
                
                isValid(user_username, "username");
                isValid(user_password, "password");
                
                Connection conn;
                conn = login.loginToDB(out);

                StudentHelper.insertUser(
                        user_username,
                        user_password,
                        request.getParameter("user_role"),
                        request.getParameter("user_fname"),
                        request.getParameter("user_lname"),
                        conn, 
                        out
                );
            } catch (InvalidSymbolException ex) {
                out.println("Field \"" + ex.getInvalidField() + "\" contained an invalid character, try again");
                out.println("<button class=\"button\" onclick=\"window.history.back();\">Go back</button>");
            } 
            site.closeAndPrintEnd(login);
        }
    }
    
    public void isValid(String toCheck, String fieldName) throws InvalidSymbolException{
        //all valid characters in a string
        String validString = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZzÆæØøÅå";
        //all valid characters as array
        String[] validChars = validString.split("");
        //the characters in the string to check
        String[] charsToCheck = toCheck.split("");
        
        //ends j-loop if the character is found to be valid
        boolean found = false;
        
        //ends i-loop if there is an invalid character
        boolean invalid = false;
        
        //for every letter in the string to check
        for (int i = 0; i < charsToCheck.length && !invalid; i++) {
            
            //for every valid letter
            for (int j = 0; j < validChars.length && !found; j++) {
                
                //if the character matches a valid one
                if (charsToCheck[i].contains(validChars[j])) {
                    found = true;
                }
            }
            
            //if the character doesn't match any valid ones
            if (!found) {
                invalid = true;
            }
        }
        
        //if there is an invalid character
        if (invalid) {
            throw new InvalidSymbolException(fieldName);
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
    }

}
