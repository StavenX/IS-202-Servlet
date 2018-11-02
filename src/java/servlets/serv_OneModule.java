/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.StudentHelper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import network.Login;
import helpers.ModuleHelper;
/**
 *
 * @author Tobias
 */
@WebServlet(name = "oneModule", urlPatterns = {"/oneModule"})
public class serv_OneModule extends HttpServlet {
    Statement stmt;
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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            HtmlHelper site = new HtmlHelper(out);
            site.printHead("Single module", "one-module-container");
            
            String singleMod_id = request.getParameter("module_id");
            
            Connection conn;
            conn = login.loginToDB(out);

            out.println("<h2>Viewing a single module</h2>");
            
            ModuleHelper.printOneModule(out, conn, singleMod_id);
            
            //TODO box containing students
            out.println("<div class=\"module-student-list\"");
            out.println("<div class=\"module-student-list-item\">");
            out.println("<div>TODO: Table of students</div>");
            out.println("</div>");
            out.println("</div>");
            
            site.useJS("editmodule.js");
            
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
        response.getWriter().println("hei");
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
