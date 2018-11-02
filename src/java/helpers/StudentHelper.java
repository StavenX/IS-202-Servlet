/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
/**
 *
 * @author Staven
 */
public class StudentHelper {
    
        /**
     * Inserts a student into the student table.
     * 
     * TODO: Currently prone to SQL injection, needs to use
     * prepareStatement() instead
     * 
     * @param username The student name
     * @param password The student's education
     * @param role
     * @param fname
     * @param lname
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
    public static void insertUser(String username, String password, String role, String fname, String lname, Connection conn, PrintWriter out) {
        
        try {
            HtmlHelper site = new HtmlHelper(out);
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO users (user_username, user_password, user_role, user_fname, user_lname) VALUES (?, ?, ?, ?, ?);");
            prepInsert.setString(1, site.checkIfValidText(username));
            prepInsert.setString(2, site.checkIfValidText(password));
            prepInsert.setString(3, site.checkIfValidText(role));
            prepInsert.setString(4, site.checkIfValidText(fname));
            prepInsert.setString(5, site.checkIfValidText(lname));
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            // The button that prints all students
            out.println(
                "<form action=\"getUser\" method=\"get\">\n" +
"                   <input class=\"button\" type=\"Submit\" value=\"Get all Students from Database\">   \n" +
"               </form>");
        }
        catch (SQLIntegrityConstraintViolationException ex) {
            out.println("One or more mandatory fields were empty, please try again");
            out.println("<button class=\"button\" onclick=\"window.history.back();\">Go back</button>");
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
    
    /**
     * Prints all the students located in the student
     * table.
     * 
     * @param out The printwriter to write with
     * @param conn The connection to use
     */
    public static void printUsers(PrintWriter out, Connection conn) {

        HtmlHelper site = new HtmlHelper(out);
        PreparedStatement getModules; 
        
        try {
            getModules = conn.prepareStatement("SELECT * FROM users ORDER BY user_id");
                       
            ResultSet rset = getModules.executeQuery();
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns
                
                String user_id = rset.getString("user_id");
                String user_username = rset.getString("user_username");
                String user_fname = rset.getString("user_fname");
                String user_lname = rset.getString("user_lname");
                
                out.println("<div class=\"student-container\">");
                
                //form containing student information
                out.println("<div class=\"student-container-item\">");
                out.println("<form action=\"oneUser\">");
                out.println("<input type=\"hidden\" name=\"user_id\" value=\"" + user_id + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div>User Id:" + user_id + "</div>");
                out.println("<div>Username:" + user_username + "</div>");
                out.println("<div>First name:" + user_fname + "</div>");
                out.println("<div>Last name:" + user_lname + "</div>");
                out.println("</div>");
                
                //"more info"-button
                out.println("<div class=\"student-container-item\">");
                out.println("<input class=\"button more-info-button\" type=\"submit\" value=\"Details\">");
                out.println("</div>");
                out.println("</form>");
                
                //delete-buttons
                out.println("<div class=\"student-container-item\">");
                out.println("<form name=\"delete-form-" + user_id + "\" action=\"deleteUser\" method=\"get\">");
                site.printDeleteButton("deleteUser", "user_id", user_id);
                out.println("</div>");
                out.println("</div>");
                rowCount++;
            }
            out.println("Total number of records: " + rowCount);
            
            //prints javascript
            site.printJsForDeleteButton();
            
            conn.close();
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
    }
    
    /**
     * Prints more details about one student
     * @param out
     * @param conn
     * @param userid 
     */
    public static void printOneUser(PrintWriter out, Connection conn, String userid) {
        PreparedStatement getOneUser;
        
        try {
            //sql statement
            getOneUser = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            getOneUser.setString(1, userid);
            ResultSet rset = getOneUser.executeQuery();
            
            //loop only executes once but is necessary?
            while (rset.next()) {
                String user_id = rset.getString("user_id");
                String user_username = rset.getString("user_username");
                String user_fname = rset.getString("user_fname");
                String user_lname = rset.getString("user_lname");
                out.println("<div>");
                out.println(user_id + user_username + user_fname + user_lname);
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
}
