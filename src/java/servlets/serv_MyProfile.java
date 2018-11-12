/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.AccessTokenHelper;
import helpers.HtmlHelper;
import helpers.ModuleHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;

/**
 *
 * @author tobia
 */
@WebServlet(name = "myProfile", urlPatterns = {"/myProfile"})
public class serv_MyProfile extends HttpServlet {
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
        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("My Profile", "my-profile");
            
            AccessTokenHelper a = new AccessTokenHelper(request);
            String username = a.getUsername();
            
            String orderBy = request.getParameter("orderBy");
            orderBy = (orderBy == null) ? "" : orderBy;
            
            Connection conn = login.loginToDB(out);
            
            String sqlString = "SELECT * FROM users WHERE user_username = ?";
            
            try {
                PreparedStatement getUser = conn.prepareStatement(sqlString);
                getUser.setString(1, username);
                ResultSet rset = getUser.executeQuery();
                
                while (rset.next()) {
                    String user_id = rset.getString("user_id");
                    String fname = rset.getString("user_fname");
                    String lname = rset.getString("user_lname");
                    String pic_url = "images/profiles/" + rset.getString("user_pic_url");
                    
                    out.println("<p> Name: " + fname + " " + lname + "</p>");
                    out.println("<img class=\"profile-pic-medium\" src=\"" + pic_url + "\" alt=\"Profile picture for " + fname + "\">");
                    
                    out.println("<h2>My modules: </h2>");
                    
                    ModuleHelper.printStudentsModules(out, conn, orderBy, user_id);
                }
                
            } catch (SQLException ex) {
                out.println(ex);
            }
            
            
            site.useJS("somebackgrounds.js");
            site.useJS("submitform.js");
            site.closeAndPrintEnd(login);
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
        try (PrintWriter out = response.getWriter()) {
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
