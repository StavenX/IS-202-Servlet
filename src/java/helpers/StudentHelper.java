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
 * @author Staven
 */
public class StudentHelper {
    
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
    public static void insertStudent(String name, String edu, Connection conn, PrintWriter out) {
        
        try {
            
            PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO student (student_name, student_education) values (?, ?);");
            prepInsert.setString(1, name);
            prepInsert.setString(2, edu);            
            
            System.out.println("The SQL query is: " + prepInsert.toString() ); // debug
            int countInserted = prepInsert.executeUpdate();         
            System.out.println(countInserted + " records inserted.\n");  
            out.println(countInserted + " records inserted.\n");  
            
            // The button that prints all students
            out.println(
                "<form action=\"getStudent\" method=\"post\">\n" +
"                   <input type=\"Submit\" name=\"get\" value=\"Get all Students from Database\">   \n" +
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
    public static void printStudents(PrintWriter out, Connection conn) {

        PreparedStatement getModules; 
        
        try {
            getModules = conn.prepareStatement("SELECT * FROM student ORDER BY ?");
            getModules.setString(1, "student_id");
                       
            ResultSet rset = getModules.executeQuery();
            
            out.println("the records selected are:" + "<br>");
            int rowCount = 0; 
            
            // While there exists more entries (rows?)
            while (rset.next()) {               
                // The different columns
                String studentID = rset.getString("student_id");
                String studentName = rset.getString("student_name");
                String studentEducation = rset.getString("student_education");
                
                out.println("<div class=\"student-container\">");
                
                //form containing student information
                out.println("<div class=\"student-container-item\">");
                out.println("<form  action=\"oneStudent\">");
                out.println("<input class=\"invisible\" name=\"stid\" value=\"" + studentID + "\">");
                out.println("<div>Row " + rowCount + "</div>");
                out.println("<div name=\"stid\">Student Id:" + studentID + "</div>");
                out.println("<div>Name:" + studentName + "</div>");
                out.println("<div>Education:" + studentEducation + "</div>");
                out.println("</div>");
                
                //more info button
                out.println("<div class=\"student-container-item\">");
                out.println("<input type=\"submit\" value=\"Details\" class=\"more-info-button\">");
                out.println("</div>");
                out.println("</form>");
                
                //delete buttons
                out.println("<div class=\"student-container-item\">");
                out.println("<form name=\"delete-form-" + studentID + "\" action=\"deleteStudent\">");
                out.println("<input class=\"invisible\" name=\"student_id\" value=\"" + studentID + "\">");
                out.println("<input type=\"button\" value=\"Delete\" onclick=\"makeSure(" + studentID + ");\" class=\"makesure-" + studentID + "\" style=\"display: inline-block\">");
                out.println("<p class=\"invisible makesure-" + studentID + "\">Really delete?<br></p>");
                out.println("<input type=\"submit\" value=\"Yes\" class=\"invisible makesure-" + studentID + "\">");
                out.println("<input type=\"button\" value=\"No\" onclick=\"makeSure(" + studentID + ");\" class=\"invisible makesure-" + studentID + "\">");
                out.println("</form>");
                out.println("</div>");
                
                out.println("</div>");
                rowCount++;
            }
            out.println("Total number of records: " + rowCount);
            
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
    public static void printOneStudent(PrintWriter out, Connection conn, String stid) {
        PreparedStatement getOneStudent;
        
        try {
            //sql statement
            getOneStudent = conn.prepareStatement("SELECT * FROM student WHERE student_id = ?");
            getOneStudent.setString(1, stid);
            ResultSet rset = getOneStudent.executeQuery();
            
            //loop only executes once but is necessary?
            while (rset.next()) {
                String studentID = rset.getString("student_id");
                String studentName = rset.getString("student_name");
                String studentEducation = rset.getString("student_education");
                out.println("<div>");
                out.println(studentID + studentName + studentEducation);
                out.println("</div>");
                
            }
                
        }
        
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
    }
}
