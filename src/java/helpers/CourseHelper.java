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
 * @author tobia
 */
public class CourseHelper {
    
    /**
     * shortcut method for selecting all courses
     * @param out
     * @param conn
     * @return 
     */
    public static ResultSet getAllCourses (PrintWriter out, Connection conn) {
        return getCourses("admin", out, conn);
    }
    
    /**
     * gets courses the user is participating in
     * @param user_id
     * @param out
     * @param conn
     * @return 
     */
    public static ResultSet getCourses (String user_id, PrintWriter out, Connection conn) {
        
        //initialises the preparedstatement
        PreparedStatement getCourses; 
        try {
            
            //base string for sql preparedstatement
            String sqlString = "SELECT * FROM course\n";
            
            //the query is set up differently to retrieve all courses when logged in as admin
            if (user_id.equals("admin")) {
                out.println("All courses listed below, as you are admin");
                sqlString += " ORDER BY course.course_id";
                getCourses = conn.prepareStatement(sqlString);
            }
            
            //gets courses the user is participating in according to the
            //course_details table
            else {
                sqlString += "INNER JOIN course_details ON course.course_id = course_details.course_id\n"
                        + "WHERE course_details.user_id = ?\n";
                sqlString += "ORDER BY course.course_id";
                getCourses = conn.prepareStatement(sqlString);
                getCourses.setString(1, user_id);
            }
            
            //exectues the query set up in the if-else-statement
            ResultSet rset = getCourses.executeQuery();
            return rset;
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }
        
        //program shouldn't reach this ever
        return null;
    }
}
