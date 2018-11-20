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
import org.json.simple.JSONArray;
import java.util.Calendar;

/**
 *
 * @author adria_000
 */

public class CalendarHelper {
      public static void getEvent (PrintWriter out, Connection conn, String reqDate, String reqDegree) {
        
        Statement stmt;
        Calendar cal = Calendar.getInstance();
        
        try {

            cal.setTimeInMillis(Long.parseLong(reqDate));
            int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
            System.out.println(reqDegree);
            
            JSONArray jsonA = new JSONArray();
            String query = ("SELECT * FROM calendar_event WHERE ce_weekOfYear =" + weekOfYear + ";");
            
            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
            int i = 0;
            while (rset.next()) {
                JSONObject jsonResponse = new JSONObject(); 
                jsonResponse.put("description", rset.getString("ce_description")); 
                jsonResponse.put("startDate", rset.getString("ce_sDate")); 
                jsonResponse.put("lecturers", rset.getString("ce_lecturers")); 
                jsonResponse.put("courseID", rset.getString("ce_CourseID")); 
                jsonA.add(i, jsonResponse);
                i++;
            }
            out.print(jsonA);
        }
        catch (Exception e) {
            out.println(e);
        }
    }
}
