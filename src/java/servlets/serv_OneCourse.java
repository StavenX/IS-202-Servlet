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
@WebServlet(name = "oneCourse", urlPatterns = {"/oneCourse"})
public class serv_OneCourse extends HttpServlet {
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
            
            String course_id = request.getParameter("course_id");
            String course_name = request.getParameter("course_name");
            
            HtmlHelper site = new HtmlHelper(out, request);
            site.printHead(course_name, "single-course");
            
            out.println("You are now viewing course " + course_name);
            
            out.println("<form action=\"addToCourse\"><input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
            out.println("<input type=\"text\" name=\"student_id\" placeholder=\"student id\">");
            out.println("<button class=\"button\">Add to course</button>");
            out.println("</form>");
            
            AccessTokenHelper a = new AccessTokenHelper(request);
            String role = a.getUserRole();
            
            Connection conn = login.loginToDB(out);
            
            ModuleHelper.printModules(out, conn, "", role, course_id);
            
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
