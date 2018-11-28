/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.AccessTokenHelper;
import helpers.HtmlHelper;
import helpers.ModuleHelper;
import helpers.UserHelper;
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
 * @author tobia
 */
@WebServlet(name = "myProfile", urlPatterns = {"/myProfile"})
public class serv_MyProfile extends HttpServlet {
    Login login = new Login();

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
        try (PrintWriter out = response.getWriter()) {
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("My Profile", "my-profile");
            
            AccessTokenHelper a = new AccessTokenHelper(request);
            String username = a.getUsername();
            
            String orderBy = request.getParameter("orderBy");
            orderBy = (orderBy == null) ? "" : orderBy;
            
            String orderDirection = request.getParameter("orderDirection");
            orderDirection = (orderDirection == null) ? "" : orderDirection;
            
            Connection conn = login.loginToDB(out);
            String user_id = UserHelper.getUserId(conn, username);
            UserHelper.printUserPage(out, conn, user_id);
            out.println("<h2>My modules: </h2>");

            ModuleHelper.printStudentsModules(out, conn, orderBy, orderDirection, user_id);
            
            site.useJS("somebackgrounds.js");
            site.useJS("submitform.js");
            site.closeAndPrintEnd(login);
        } catch (Exception ex) {
            login.close();
        }
    }

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
            doPost(request, response);
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
