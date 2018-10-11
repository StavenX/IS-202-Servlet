/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import classes.PasswordStorage;
import classes.PasswordStorage.CannotPerformOperationException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.HtmlHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import network.Login;

/**
 *
 * @author Tobias
 */
@WebServlet(name = "serv_CreateNewUser", urlPatterns = {"/serv_CreateNewUser"})
public class serv_CreateNewUser extends HttpServlet {
    Connection conn;
    Login login = new Login();

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
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("Create new user", "body");
            
            
            String users_username = request.getParameter("users_username");
            String plain_password = request.getParameter("users_password");
            if (users_username.length() > 20) {
                out.println("Username is too long");
            }
            else if (plain_password.length() > 20){
                out.println("Password is too long");
            }
            else {
                try {
                    String users_password = PasswordStorage.createHash(plain_password);
                    try {
                        conn = login.loginToDB(out);
                        PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO users (users_username, users_password) VALUES (?,?);");
                        prepInsert.setString(1, users_username);
                        prepInsert.setString(2, users_password);

                        System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
                        int countInserted = prepInsert.executeUpdate();   
                        out.println(countInserted);
                    } catch (SQLException ex) {
                        if (ex.getMessage().toLowerCase().contains("duplicate entry")) {
                            out.println("Username is taken, please choose another");
                        }
                        else {
                            out.println(ex);
                        }
                    }
                } catch (CannotPerformOperationException ex) {
                    out.println(ex);
                }
            }
            
            
            out.println("<a href=\"firstlogin.html\">Return to login page</a>");
            
            
            site.printEnd();
        }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
