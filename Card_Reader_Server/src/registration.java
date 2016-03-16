import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.*;
import java.util.*;
import java.time.*;
import java.io.*;
import java.net.*;


public class registration  extends Thread {
private String connection;
private String userName;
private String password;
private String room;
private int serverPort;
private int clientPort;
private int minAt;
private int early;
private int lateTime;

//TCP
//ServerSocket serverSocket = null;
//static public String str = "?";
//static public int taskCount = 0;

public registration(String connection, String user, String pass, String room, int minAt, int sPort, int cPort, int early, int late)
{
	
	this.connection=connection;
	this.userName=user;
	this.password=pass;
	this.room=room;
	this.serverPort=sPort;
	this.clientPort=cPort;
	this.early=early;
	this.lateTime=late;
	this.minAt = minAt;
	
	
}

public void run()
{
	//SQL var
	Connection con = null;
    PreparedStatement onTime = null;
    PreparedStatement late = null;
    PreparedStatement read = null;
    PreparedStatement session = null;
    PreparedStatement checkCorrect = null;
    PreparedStatement getRelatedSessions = null;
    PreparedStatement checkWrong = null;
    PreparedStatement wrongOnTime = null;
    PreparedStatement wrongLate = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    ResultSet rs3 = null;
    ResultSet rs4 = null;
    
    //TCP var
	 PrintWriter out=null;
	 ServerSocket serverSocket=null;
	 Socket clientSocket=null;
	 BufferedReader in=null;
	 String inputLine=null;
	 String roomID = null;
 	String studID = null;
 	String operation = null;
 	String sessionID = null;
 	String sesCode = null;
 	String wSessionID = null;
 	String timeTableID = null;
 	
 	 try {
   		con = DriverManager.getConnection(this.connection, this.userName, this.password);
   	} catch (SQLException e1) {
   		// TODO Auto-generated catch block
   		e1.printStackTrace();
   	}

 	
	while((!Thread.currentThread().isInterrupted()))
	{
		//System.out.println("p1");
	 try
	 {
		serverSocket = new ServerSocket (this.serverPort);
     clientSocket = serverSocket.accept ();
    
     out = new PrintWriter (clientSocket.getOutputStream (), true);
     in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ()));
	 }
	 catch (IOException e)
	 {
		 
	 }
     
	// System.out.println("p2");
     try
     {
    	//System.out.println("while_running");
			
			//gets date/time
	    	//time is in loop to keep it updated.
	    	
	    	//gets current time
	    	LocalTime localTime = LocalTime.now();
	    	
	    	//get's current minute and hour
	    	//int min = localTime.getMinute();
	    //	int hour = localTime.getHour()+1;
	    	//int sec = localTime.getSecond();
	    	int min= 20;
	    	int hour=19;
	    	int cHour=18;
	    	int sec = 0;
	    	
	    	
	    //	System.out.println(hour);
	    //	System.out.println(min);
	    	
	    	String t = hour+":00:00";
	    	
	    	//System.out.println(t);
	    	
	    	
	    	//gets current day
	    	Calendar calendar = Calendar.getInstance();
	    	Date date = calendar.getTime();
	    	//String day = new SimpleDateFormat("E", Locale.ENGLISH).format(date.getTime());
	    	String day = "Mon";
	    	 
	    	//System.out.println(day);
    	 
    	 String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    	 
    	 inputLine=in.readLine ();
   //  while ((inputLine = in.readLine ()) != null)
    // {
         System.out.println ("Server < " + inputLine);
         System.out.println(inputLine);
         
    // }
         
         System.out.println(inputLine);
         
         roomID = inputLine.substring(0,3);
         operation = inputLine.substring(3,6);
         studID = inputLine.substring(6,14);
         
         System.out.println(roomID);
         System.out.println(operation);
         System.out.println(studID);

         //SQL
         
         if (operation.equals("REG"))
         { 
         try {
        	System.out.println("try1");
        	 session = con.prepareStatement("SELECT session_id, ses_code FROM sessions WHERE room_id = '"+roomID+"' && time = '"+t+"' && day = '"+day+"' ");

     		//onTime = con.prepareStatement("UPDATE attendances SET absent=0, on_time=1, time='"+timeStamp+"' WHERE attendance_id = 621 ");
     		// late = con.prepareStatement("UPDATE attendances SET absent=0, on_time=0, late=1, time='"+timeStamp+"' WHERE attendance_id = 621 ");
     		// read = con.prepareStatement("SELECT (abset, on_time, late) FROM attendances WHERE attendance_id = 621");
        	 
        	 rs = session.executeQuery();
        	 
        	
        	 
        	 while(rs.next())
        	 {
        		 System.out.println(rs.getString(1));
         		System.out.println(rs.getString(2));
        		 
        		 System.out.println("while1");
        		 sessionID = rs.getString(1);
        		 sesCode = rs.getString(2);
        		 
        		 checkCorrect  = con.prepareStatement("SELECT timetable_id FROM timetables WHERE session_id = '"+sessionID+"' AND student_id = '"+studID+"'");
        		 System.out.println("pre-rs2");
        		 rs2 = checkCorrect.executeQuery();
        		 
        		 if(rs2.next())
        		 {
        			 timeTableID = rs2.getString(1);
        			 System.out.println(timeTableID);
        			 if ((((min >= early) && (min <= 59))) || ((min <= lateTime) && (min >= 0)))
        			 {
        				 System.out.println("if1");
	        			 onTime = con.prepareStatement("UPDATE attendances SET absent=0, on_time=1, time='"+timeStamp+"' WHERE timetable_id = '"+timeTableID+"' ");
	        			 
	        			 onTime.executeUpdate();
	        			 
	        			 System.out.println("on time");
        			 }
	        		else
	        			 {
	        			System.out.println("else1");
	        				 late = con.prepareStatement("UPDATE attendances SET absent=0, late=1, time='"+timeStamp+"' WHERE timetable_id = '"+timeTableID+"' ");
	        				late.executeUpdate();
	        				 System.out.println("late");
	        			 }
        		 }
        		 else
        		 {
        			 System.out.println("else rs failed");
        			 getRelatedSessions = con.prepareStatement("SELECT session_id FROM sessions WHERE ses_code = '"+sesCode+"'");
        			 
        			
        			 
        			 rs3 = getRelatedSessions.executeQuery();
        			 System.out.println("rs3_exec");
        			 if(rs3.next())
        			 {
        				 wSessionID = rs3.getString(1);
        				 
        				 checkWrong = con.prepareStatement("SELECT timetable_id FROM timetables WHERE session_id = '"+wSessionID+"' AND student_id = '"+studID+"'");
        				 
        				 rs4 = checkWrong.executeQuery();
        				 System.out.println("rs4_exec");
        				 if (rs4.next())
        				 {
        					 System.out.println("if2");
        					 if (((min >= early) && (min < 59)) || ((min < lateTime) && (min > 0)))
                			 {
        	        			 wrongOnTime = con.prepareStatement("UPDATE attendances SET absent=0, on_time=1, time='"+timeStamp+"', wrong_ses=1 WHERE timetable_id = '"+timeTableID+"' ");
        	        			 
        	        			 onTime.executeUpdate();
        	        			 
        	        			 System.out.println("one time wrong session");
                			 }
        	        		else
        	        			 {
        	        			System.out.println("else2");
        	        				 wrongLate = con.prepareStatement("UPDATE attendances SET absent=0, on_time=0, late=1, wrong_ses=1 time='"+timeStamp+"', wrong_ses=1 WHERE timetable_id = '"+timeTableID+"' ");
        	        				 System.out.println("late on time");
        	        			 }
        				 }
        			 }
        			 
        		 }
        		 
        		 
        	 }
        	 
     	 } catch (SQLException e) {
     		 System.out.println(e);
 		}
         }
       /*  try
    	 {
    		 onTime.executeUpdate();
    		 
    		
    			 String outPut = "On Time";
    			 out.println(outPut);
    			 
    		 
    		 
    		
    	 }
    	 catch (SQLException e)
    	 {
    		 System.out.println(e);
    	 }*/
        

    
    
     }
     catch (IOException e)
     {
    	 
     }
     
   
     try{
    	// System.out.println("hell");
    	 out.close();
    	    in.close();
    	    clientSocket.close();
    	    serverSocket.close();
    	 //   System.out.println("close");
     }
     catch(IOException e)
     {
    	 
     }
	}
   
 }





}

	

