/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.PrintWriter;

/**
 *
 * @author Tobias
 */
public class HtmlHelper {
    private PrintWriter out;
    
    
    public HtmlHelper (PrintWriter out) {
        this.out = out;
    }
    
    /**
     * Prints the start of the html page
     * @param title the title visible in the tab of your browser
     * @param bodyId the html class the body will be assigned
     */
    public void printHead (String title, String bodyId) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<link rel=\"icon\" href=\"placeholder_v1.png\" type=\"image/png\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/theme.css\">");
        out.println("<title>Servlet getStudent</title>");            
        out.println("</head>");
        out.println("<body id=\"" + bodyId + "\">");
        out.println("<a href=\"http://localhost:8084/WEB/\">Go home</a>");
        //printNav();
    }
    
    public void printDeleteButton (String servletName, String entityPK, String entityID) {
                out.println("<form name=\"delete-form-" + entityID + "\" action=\"" + servletName + "\">");
                out.println("<input class=\"invisible\" name=\"" + entityPK + "\" value=\"" + entityID + "\">");
                out.println("<input type=\"button\" value=\"Delete\" onclick=\"makeSure(" + entityID + ");\" class=\"makesure-" + entityID + "\" style=\"display: inline-block\">");
                out.println("<p class=\"invisible makesure-" + entityID + "\">Really delete?<br></p>");
                out.println("<input type=\"submit\" value=\"Yes\" class=\"invisible makesure-" + entityID + "\">");
                out.println("<input type=\"button\" value=\"No\" onclick=\"makeSure(" + entityID + ");\" class=\"invisible makesure-" + entityID + "\">");
                out.println("</form>");
    }
    
    public void printJsForDeleteButton() {
        //javascript for handling delete buttons
        out.println("<script src=\"FirstScripts.js\"></script>");
    }
    
    public void printNav () {
        out.println("\n" +
"        <div class=\"nav-container\" id=\"nav-container\">\n" +
"            <div class=\"nav-button\">\n" +
"                <input onclick=\"hide()\" type=\"button\" value=\"<<\" id=\"nav-button\">\n" +
"            </div>\n" +
"            <div class=\"home-nav\" id=\"nav\">\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"#\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            My profile\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"student.html\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            Students\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"module.html\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            Modules\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"#\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            My profile\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"student.html\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            Students\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"module.html\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            Modules\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"#\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            My profile\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"student.html\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            Students\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"                <div class=\"nav-item\">\n" +
"                    <a href=\"module.html\">\n" +
"                        <div class=\"nav-img\">\n" +
"                            <img src=\"http://via.placeholder.com/50x50\" alt=\"lol\">\n" +
"                        </div>\n" +
"                        <div class=\"nav-text\">\n" +
"                            Modules\n" +
"                        </div>\n" +
"                    </a>\n" +
"                </div>\n" +
"            </div>\n" +
"        </div>");
    }
    
    /**
     * Prints the closing tag of body and html
     */
    public void printEnd () {
        out.println("</body>");
        out.println("</html>");
    }
}
