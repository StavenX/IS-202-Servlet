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
import java.text.SimpleDateFormat;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Calendar;
import java.util.Date;

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
            
            JSONArray jsonA = new JSONArray();
            String query = ("SELECT * FROM calendar_event WHERE ce_weekOfYear =" + weekOfYear + " AND ce_degreeNandS ='" + reqDegree + "';");

            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
            int i = 0;
            while (rset.next()) {
                JSONObject jsonResponse = new JSONObject(); 
                jsonResponse.put("startDate", rset.getString("ce_sDate")); 
                jsonResponse.put("lecturers", rset.getString("ce_lecturers")); 
                jsonResponse.put("courseID", rset.getString("ce_CourseID")); 
                jsonResponse.put("startTime", rset.getString("ce_sDate"));
                jsonResponse.put("endTime", rset.getString("ce_eDate"));
                jsonResponse.put("room", rset.getString("ce_room"));
                jsonA.add(i, jsonResponse);
                i++;
            }
            out.print(jsonA);
        }
        catch (Exception e) {
            out.println(e);
        }
    }
    public static void createEvent (String courseCode, String classroom, String lecturer, String date, String startTime, String endTime, 
                                      String mon, String tue, String wed, String thu, String fri, String sat, String sun) {
        try {
            SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat hourMinutes = new SimpleDateFormat("HH:mm");
            
            Date parsedEndTime = hourMinutes.parse(endTime);
            Date parsedStartTime = hourMinutes.parse(startTime);
            Date parsedDate = yearMonthDay.parse(date);
            
            parsedDate.setHours(parsedStartTime.getHours());
            parsedDate.setMinutes(parsedStartTime.getMinutes());
            
            System.out.println(parsedDate);

        
        }
        catch (Exception e) {
            System.out.println(e);
        }
          
    }
}
