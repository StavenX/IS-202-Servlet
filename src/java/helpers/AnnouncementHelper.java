/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tobia
 */
public class AnnouncementHelper {
    
    
    
    
    public static ResultSet getAnnouncements (Connection conn, String course_id, int amount) {
        try {
            String sqlString = "SELECT * FROM announcement WHERE course_id LIKE ? ORDER BY announcement_id DESC LIMIT ?";
            PreparedStatement getAnnouncements = conn.prepareStatement(sqlString);
            getAnnouncements.setString(1, course_id);
            getAnnouncements.setInt(2, amount);
            ResultSet rset = getAnnouncements.executeQuery();
            
            return rset;
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
}
