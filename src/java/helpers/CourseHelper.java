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
    
    
    public static ResultSet getAllCourses (PrintWriter out, Connection conn) {
        return getCourses("admin", out, conn);
    }
    
    public static ResultSet getCourses (String user_id, PrintWriter out, Connection conn) {
        
        PreparedStatement getCourses; 
        try {
            
            //base string for sql preparedstatement
            String sqlString = "SELECT * FROM course\n";
            
            if (!user_id.equals("admin")) {
                sqlString += "INNER JOIN course_details ON course.course_id = course_details.course_id\n"
                + "WHERE course_details.user_id = ?\n";
                sqlString += "ORDER BY course.course_id";
                getCourses = conn.prepareStatement(sqlString);
                getCourses.setString(1, user_id);
            } else {
                out.println("hei");
                sqlString += " ORDER BY course.course_id";
                getCourses = conn.prepareStatement(sqlString);
            }
            ResultSet rset = getCourses.executeQuery();
            return rset;
        }
        catch (SQLException ex) {
            out.println("SQL error: " + ex);
        }
        catch (Exception e) {
            out.println("Something wrong happened: " + e);
        }       
        return null;
    }
}
