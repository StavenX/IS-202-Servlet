/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import helpers.HtmlHelper;
import helpers.ModuleHelper;
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
@WebServlet(name = "createModule", urlPatterns = {"/createModule"})
public class serv_CreateModule extends HttpServlet {

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
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("New module", "create-module");
            
            out.println("<h1> Create a new module </h1>");  
            out.println("<div class =\"form1\">");  
            out.println("<form action=\"createModule\" method=\"post\">");
            
            out.println("<select style=\"display:list-item\" name=\"course_id\">");
            out.println("<option value=\"1\">IS-200</option>");
            out.println("<option value=\"2\">IS-201</option>");
            out.println("<option value=\"3\">IS-202</option>");
            out.println("<option value=\"4\">ORG-110</option>");
            out.println("<option value=\"5\">ENG-205</option>");
            out.println("</select>");
            
            out.println("<input type=\"text\" name=\"module_name\" placeholder=\"Insert module name\">");  
            out.println("<input type=\"text\" name=\"module_desc\" placeholder=\"Insert module description\">");
            out.println("<input type=\"text\" name=\"module_points\" placeholder=\"Insert module points\">");
            out.println("<input class=\"button\" type=\"Submit\" name=\"get\" value=\"Create\">"); 
            out.println("</form>");
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
            
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead("New module created", "create-module");
            
            Connection conn;
                conn = login.loginToDB(out);
                
                ModuleHelper.insertModule(
                        request.getParameter("module_name"),
                        request.getParameter("module_desc"),
                        request.getParameter("module_points"),
                        request.getParameter("course_id"),
                        conn, 
                        out
                );                
                site.closeAndPrintEnd(login);
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
