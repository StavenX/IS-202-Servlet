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
/**
 *
 * @author Frank
 */
public class MessageHelper {
    
        /**
     * Inserts a student into the student table.
     * 
     * TODO: Currently prone to SQL injection, needs to use
     * prepareStatement() instead
     * 
     * @param name The student name
     * @param edu The student's education
     * @param conn The connection object
     * @param out The printwriter, for printing errors etc
     */
    public static void insertMessage( String senderId, String title, String content, Connection conn, PrintWriter out) {
        
        try {
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO message (mess_senderId, mess_title, mess_content) values ( ?, ?, ?);");
            
            prepInsert.setString(1, senderId);
            prepInsert.setString(2, title);            
            prepInsert.setString(3, content);            
            
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            // The button that prints all students
            out.println(
                "<form action=\"getMessage\" method=\"post\">\n" +
"                   <input type=\"Submit\" name=\"get\" value=\"Get all Messages from Database\">   \n" +
"               </form>");
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
    public static void printMessages(PrintWriter out, Connection conn) {

        HtmlHelper site = new HtmlHelper(out);
        PreparedStatement getMessage; 
        
        try {
            getMessage = conn.prepareStatement("SELECT * FROM messages ORDER BY ?");
            getMessage.setString(1, "mess_senderId");
                       
            ResultSet rset = getMessage.executeQuery();
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns
                String mess_id = rset.getString("mess_id");
                String mess_senderId = rset.getString("mess_senderId");
                String mess_title = rset.getString("mess_title");
                String mess_content = rset.getString("mess_content");

                
                out.println("<div class=\"student-container\">");
                
                //form containing student information
                out.println("<div class=\"student-container-item\">");
                out.println("<form  action=\"oneStudent\">");
                out.println("<input class=\"invisible\" name=\"stid\" value=\"" + mess_id + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div name=\"stid\">Student Id:" + mess_id + "</div>");
                out.println("<div>Name:" + mess_senderId + "</div>");
                out.println("<div>Education:" + mess_title + "</div>");
                out.println("<div>Education:" + mess_content + "</div>");

                out.println("</div>");
                
                //more info button
                out.println("<div class=\"student-container-item\">");
                out.println("<input type=\"submit\" value=\"Details\" class=\"more-info-button\">");
                out.println("</div>");
                out.println("</form>");
                
                //delete buttons
                out.println("<div class=\"student-container-item\">");
                
                out.println("<form name=\"delete-form-" + mess_id + "\" action=\"deleteStudent\">");
                site.printDeleteButton("deleteStudent", "student_id", mess_id);
                out.println("</div>");
                
                out.println("</div>");
                rowCount++;
            }
            out.println("Total number of records: " + rowCount);
            
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
     * @param stid 
     */
    public static void printOneMessage(PrintWriter out, Connection conn, String stid) {
        PreparedStatement getOneMessage;
        
        try {
            //sql statement
            getOneMessage = conn.prepareStatement("SELECT * FROM message WHERE mess_id = ?");
            getOneMessage.setString(1, stid);
            ResultSet rset = getOneMessage.executeQuery();
            
            //loop only executes once but is necessary?
            while (rset.next()) {
                String mess_id = rset.getString("mess_id");
                String mess_senderId = rset.getString("mess_senderId");
                String mess_title = rset.getString("mess_title");
                String mess_content = rset.getString("mess_content");

                out.println("<div>");
                out.println(mess_id + mess_senderId + mess_title + mess_content);
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
}
