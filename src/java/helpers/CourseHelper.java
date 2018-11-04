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
    
    
    public static ResultSet getCourses (PrintWriter out, Connection conn) {
        
        PreparedStatement getCourses; 
        try {
            
            //base string for sql preparedstatement
            String sqlString = "SELECT * FROM course ORDER BY course_id";
            
            
            //preparedstatement is prepared and executed
            getCourses = conn.prepareStatement(sqlString);
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
