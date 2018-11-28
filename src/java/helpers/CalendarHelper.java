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
import java.sql.Statement;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    public static Calendar newCalendar (Date date, Date time) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal;
    }
    public static void insertEvents (PrintWriter out, Connection conn, String classroom, String lecturer,
                                    String courseCode, String degree, String sc, String ec, String cw) {
        try {
            System.out.println(ec);
        PreparedStatement prepStmt = conn.prepareStatement ("INSERT INTO calendar_event (ce_room, ce_lecturers, ce_courseID,"
                                                                       + "ce_degreeNandS, ce_sDate, ce_eDate, ce_weekOfYear)"
                                                                       + "VALUES (?, ?, ?, ?, ?, ?, ?);");
                    prepStmt.setString(1, classroom);
                    prepStmt.setString(2, lecturer);
                    prepStmt.setString(3, courseCode);
                    prepStmt.setString(4, degree);
                    prepStmt.setString(5, sc);
                    prepStmt.setString(6, ec);
                    prepStmt.setString(7, cw);
                    
                    prepStmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
