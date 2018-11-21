/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import errors.serv_ERROR403;
import helpers.CourseHelper;
import helpers.HtmlHelper;
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
import helpers.UserHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            
            String role = UserHelper.getUserRole(request);
            String module_id = request.getParameter("module_id");
            Connection conn = login.loginToDB(out);
            String user_id = UserHelper.getUserId(conn, UserHelper.getUserName(request));
            
            boolean hasModule = false;
            try {
                hasModule = ModuleHelper.studentHasModule(conn, module_id, user_id);
            } catch (SQLException ex) {
                out.println(ex);
            }
            
            //access only given to students with the module and to other roles (lecturer /admin)
            if (!hasModule && role.equals("Student")) {
                serv_ERROR403 e = new serv_ERROR403();
                e.doGet(request, response);
            }
            //prints page
            else {
                HtmlHelper site = new HtmlHelper(out, request);
                site.printHead("Single module", "one-module-container");

                String orderBy = request.getParameter("orderBy");
                orderBy = (orderBy == null) ? "" : orderBy;
                site.printBackButton();
                String course_id = "";
                String course_name = "";
                try {
                    course_id = CourseHelper.getCourseId(conn, module_id);
                    course_name = CourseHelper.getCourseName(course_id, conn);
                }
                catch (SQLException ex) {
                    out.println(ex);
                }
                out.println("<form action=\"oneCourse\" method=\"get\">");
                out.println("<input type=\"hidden\" name=\"course_id\" value=\"" + course_id + "\">");
                out.println("<input type=\"submit\" class=\"button\" value=\"To " + course_name + "\">");
                out.println("</form>");

                out.println("<h2>Viewing a single module</h2>");
                ModuleHelper.printOneModule(out, conn, module_id, role);
                try {
                    if (role.equals("Student")) {
                        ResultSet rset = ModuleHelper.getOneStudentModule(conn, module_id, user_id);

                        //returns true if rset isn't empty
                        if (rset.isBeforeFirst()) {
                            out.println("<form action=\"oneStudentModule\" method=\"post\">");
                            out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                            out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                            out.println("<button class=\"button\">View my response</button>");
                            out.println("</form>");
                        } 
                    }

                } catch (SQLException ex) {
                    out.println(ex);
                }


                if (role.equals("Lecturer")) {
                    String search = request.getParameter("search");
                    search = (search == null) ? "%" : "%" + search + "%";
                    try {
                    ModuleHelper.printOneModuleStudentTable(out, conn, module_id, orderBy, search);
                    } catch (SQLException ex) {
                        out.println(ex);
                    }
                }

                out.println("<h2 id=\"Comments\">Comments on module</h2>");

                out.println("<div class=\"new-comment-container\">");
                out.println("<form id=\"newComment\" action=\"createComment\" method=\"post\">");
                out.println("<input type=\"hidden\" name=\"module_id\" value=\"" + module_id + "\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("</form>");
                out.println("<textarea form=\"newComment\" name=\"module_comment_content\" class=\"new-comment-input\" placeholder=\"Write a comment...\"></textarea>");
                out.println("<button class=\"button\" onclick=\"submit(\'newComment\');\">Post comment</button>");
                out.println("</div>");

                ModuleHelper.printModuleComments(out, conn, module_id, request);
                site.useJS("somebackgrounds.js");
                site.useJS("editmodule.js");
                site.useJS("submitform.js");
                site.useJS("buttons-for-delete.js");

                site.closeAndPrintEnd(login);
            }
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
        try (PrintWriter out = response.getWriter()) {
            doGet(request, response);
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
