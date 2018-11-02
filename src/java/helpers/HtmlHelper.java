/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import network.Login;

/**
 *
 * @author Tobias
 */
public class HtmlHelper {
    private PrintWriter out;
    private HttpServletRequest request;
    
    
    public HtmlHelper (PrintWriter out) {
        this.out = out;
    }
    
    public HtmlHelper (PrintWriter out, HttpServletRequest request) {
        this.request = request;
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
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<link rel=\"icon\" href=\"images/Placeholder_v2.png\" type=\"image/png\">");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/theme.css\">");
        out.println("<title>" + title + "</title>");            
        out.println("</head>");
        //out.println(nav());
        //printNav();
        out.println("<body id=\"" + bodyId + "\">");
        out.println("<form action=\"http://localhost:8084/WEB/\"> <button class=\"button button-home\">Go home</button> </form>");
        String loggedUser;
        try {
            AccessTokenHelper a = new AccessTokenHelper(request);
            loggedUser = a.getUsername() + a.getUserRole();
        } catch (Exception ex) {
            loggedUser = "Not logged in / not implemented in this servlet | " + ex;
        }
        out.println("<p>" + loggedUser + "</p>");
    }
    
    public void printDeleteButton (String servletName, String entityPK, String entityID) {
                out.println("<form name=\"delete-form-" + entityID + "\" action=\"" + servletName + "\">");
                out.println("<input class=\"invisible\" name=\"" + entityPK + "\" value=\"" + entityID + "\">");
                out.println("<input class=\"button makesure-" + entityID + "\" type=\"button\" value=\"Delete\" onclick=\"makeSure(" + entityID + ");\"  style=\"display: inline-block\">");
                out.println("<p class=\"invisible makesure-" + entityID + "\">Really delete?<br></p>");
                out.println("<input class=\"invisible button makesure-" + entityID + "\" type=\"submit\" value=\"Yes\">");
                out.println("<input class=\"invisible button makesure-" + entityID + "\" type=\"button\" value=\"No\" onclick=\"makeSure(" + entityID + ");\">");
                out.println("</form>");
    }
        
    //javascript for handling delete buttons
    public void printJsForDeleteButton() {
        out.println("<script src=\"buttons-for-delete.js\"></script>");
    }
    
    public void useJS(String filename) {
        out.println("<script src=\"" + filename + "\"></script>");
    }
    
    public String checkIfValidText(String toCheck) {
        //returns null if the string is empty, to prevent empty strings being inserted
        //into database (columns have 'NOT NULL' property
        if (toCheck.equals("")) {
            return null;
        }
        String checked = "";
        String[] letters = toCheck.split("");
            //replaces '<' and '>' with unicode symbols, so the page doesn't treat them as html code
            //(prevents images etc being posted instead of text
            for (String letter : letters) {
                if (letter.equals("<")) {
                    letter = "&#x003C";
                }
                if (letter.equals(">")) {
                    letter = "&#x003E";
                }
                //adds each letter to a new string to be used later
                checked += letter;
            }
        return checked;
    }
    
    //only works with absolute path
    public String nav() {
        String contents = "heihei";
        try {
            contents = new String(Files.readAllBytes(Paths.get("C:\\Users\\tobia\\Documents\\IS-202-Servlet\\web\\nav.html")));
        } catch (IOException ex) {
            out.println(ex);
        }
        return contents;
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
    
    /**
     * Prints the closing tag of body and html and closes connection
     * @param login the connection to be closed
     */
    public void closeAndPrintEnd(Login login) {
        login.close();
        printEnd();
    }
}
