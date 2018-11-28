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
 * @author tobia
 */
@WebServlet(name = "updateModule", urlPatterns = {"/updateModule"})
public class serv_UpdateModule extends HttpServlet {
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
            HtmlHelper site = new HtmlHelper(out, request);
            
            //body id 'invisible' makes no content on the page visible, and it should auto load
            //due to javascript 
            site.printHead("Updating module...", "invisible");
            
            out.println("<h1>Module updated</h1>");
            
            String id = request.getParameter("module_id");
            String name = request.getParameter("module_name");
            String desc = request.getParameter("module_desc");
            String points = request.getParameter("module_points");
            
            Connection conn = login.loginToDB(out);
            ModuleHelper.updateModule(id, name, desc, points, conn, out);
            
            //form that takes you back to the module you just edited
            out.println("<form name=\"auto\" action=\"oneModule\" method=\"get\">");
            out.println("<input name=\"module_id\" type=\"text\" value=\"" + id + "\">");
            out.println("<input class=\"button\" type=\"submit\">");
            out.println("</form>");
            
            //auto submits the form so the page auto loads
            out.println("<script>window.onload=document.forms[\'auto\'].submit();</script>");
            
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
