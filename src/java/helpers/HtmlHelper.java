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
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"theme.css\">");
        out.println("<title>Servlet getStudent</title>");            
        out.println("</head>");
        out.println("<body id=\"" + bodyId + "\">");
        out.println("<a href=\"http://localhost:8084/WEB/\">Go home</a>");
        //printNav();
    }
    
    public void printJsForDeleteButton() {
        //javascript for handling delete buttons
            out.println("<script>"
                        //gets the buttons and uses the flip function
                        + "function makeSure(stid) { \n"
                            + "var items = document.getElementsByClassName(\'makesure-\' + stid); \n"
                            + "for (var i = 0; i < items.length; i++) { \n"
                                + "flip(items[i]);  \n"
                            + "} \n"
                        + "} \n"
                    
                    //changes display based on existing value
                        + "function flip(item) { \n"
                            + "if (item.style.display === \'inline-block\') { \n"
                                + "item.style.display = \'none\'; \n"
                            + "} else { \n"
                                + "item.style.display = \'inline-block\'; \n"
                            + "} \n"
                    + "}</script>");
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
