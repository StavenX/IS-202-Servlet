/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.simple.JSONObject;

/**
 *
 * @author adria_000
 */

public class CalendarHelper {
      public static void getEvent (PrintWriter out, Connection conn) {
        
        Statement stmt;
        
        try {
            JSONObject jsonResponse = new JSONObject();
            String query = ("SELECT * FROM calendar_event;");
            
            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
    
            while (rset.next()) {
                jsonResponse.put("description", rset.getString("ce_description"));
                jsonResponse.put("startDate", rset.getString("ce_sDate"));
                jsonResponse.put("lecturers", rset.getString("ce_lecturers"));
                jsonResponse.put("courseID", rset.getString("ce_CourseID"));
                out.print(jsonResponse);
            }
        }
        catch (Exception e) {
            out.println(e);
        }
    }
}
