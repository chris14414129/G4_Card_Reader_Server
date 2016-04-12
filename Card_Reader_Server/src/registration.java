import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
//private String room;
//private int serverPort;
private int clientPort;
private String broadcastIP;
//private int minAt;
private int early;
private int lateTime;

public registration(String connection, String user, String pass, String room, int minAt, int sPort, int cPort, int early, int late)
{
	
	this.connection=connection;
	this.userName=user;
	this.password=pass;
	this.clientPort=cPort;
	this.early=early;
	this.lateTime=late;
	this.broadcastIP="127.0.0.1";
	
	
}

public void  response(String roomID, String type, String response)
{
	try
	{
		 DatagramSocket socket = new DatagramSocket ();
         byte[] buf = new byte[256];
		    String output = roomID+type+response;
		 
		    buf = output.getBytes ();
            InetAddress address = InetAddress.getByName (this.broadcastIP);
            DatagramPacket packet = new DatagramPacket (buf, buf.length, address, this.clientPort);
            socket.send(packet);
		 
	}
	catch(IOException e)
	{
		 
	}
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
    
    
    //UDP var
    DatagramSocket socket = null;
    DatagramPacket packet = null;
	//BufferedReader in=null;
	String inputLine=null;
	String roomID = null;
 	String studID = null;
 	String operation = null;
 	String sessionID = null;
 	String sesCode = null;
 	String wSessionID = null;
 	String timeTableID = null;
 	String sqlHour = null;
    int sqlHourInt = 0;
    
    boolean doOnce = true;
 	
    //establishes connection to DB server
 	 try 
 	 	{
   			con = DriverManager.getConnection(this.connection, this.userName, this.password);
 	 	} 
 	 		catch (SQLException e1) {
 	 		// TODO Auto-generated catch block
 	 		e1.printStackTrace();
 	 	}

 	
	while((!Thread.currentThread().isInterrupted()))
	{
		if (doOnce == true)
			{
				
				try
					{
						socket = new DatagramSocket (1234);
        
						byte[] buf2 = new byte[256];

						packet = new DatagramPacket (buf2, buf2.length);
						socket.receive (packet);
						inputLine = new String (packet.getData());
						// System.out.println ("Received packet: " + received);
						
						 //  System.out.println(inputLine);
		 
					}
				catch (IOException e)
				{
					System.out.println(e);
				}
     
		//gets current time
		LocalTime localTime = LocalTime.now();
	
		//get's current minute and hour
		int min = localTime.getMinute();
		 	int cHour = localTime.getHour()+1; //cHour means current Hour
		//int sec = localTime.getSecond();
		//int min=05;
		//int cHour=19;
		int hour=cHour+1;
		//int sec = 0;
	
	
		//	System.out.println(hour);
		//	System.out.println(min);
	
		//generates string based on time for SQL queries
		String t = hour+":00:00";
		String t2 = cHour+":00:00";
	
		//System.out.println(t);
	
	
		//gets current day
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		String day = new SimpleDateFormat("E", Locale.ENGLISH).format(date.getTime());
		//String day = "Mon";
		 
		//System.out.println(day);
	  	 
		//timestamp used for queries.
	  	String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
  	 
	 
       
    
       //creates vars 
       roomID = inputLine.substring(0,3);
       operation = inputLine.substring(3,6);
       studID = inputLine.substring(6,14);
       
       System.out.println(roomID);
       System.out.println(operation);
       System.out.println(studID);

       //SQL
       
       if (operation.equals("REG"))
       { 
    	   try 
    	   	{
    		   System.out.println("try1");
    		   session = con.prepareStatement("SELECT session_id, ses_code, duration, Time FROM sessions WHERE room_id = '"+roomID+"'AND day = '"+day+"' AND (time = '"+t+"' OR time = '"+t2+"') ");

   
	 
    		   rs = session.executeQuery();
	 
	
    		   if (rs.next())
    		   	{
    			   rs = session.executeQuery();
    			   while(rs.next())
    			   	{
    				   System.out.println("if"+rs.getString(1));
    				   System.out.println(rs.getString(1));
    				   System.out.println(rs.getString(2));
		 
    				   System.out.println("while1");
    				   sessionID = rs.getString(1);
    				   sesCode = rs.getString(2);
    				   
    				   Time sqlTime = rs.getTime(4);
    				   sqlHour = new SimpleDateFormat("HH", Locale.ENGLISH).format(sqlTime.getTime());
    				   System.out.println(sqlHour);
    				   sqlHourInt = Integer.parseInt(sqlHour);
    					//sqlHourInt = 19;
    					System.out.println(sqlHourInt);
    					
    				  
		 
    				   checkCorrect  = con.prepareStatement("SELECT timetable_id FROM timetables WHERE session_id = '"+sessionID+"' AND student_id = '"+studID+"'");
    				   System.out.println("pre-rs2");
    				   rs2 = checkCorrect.executeQuery();
    				   
    				  
    				   	
    				   if(rs2.next())
    				   	{
    					   System.out.println("rs2 next");
    					   timeTableID = rs2.getString(1);
    					   System.out.println(timeTableID);
    					   PreparedStatement onTimeCheck = con.prepareStatement("SELECT absent FROM attendances WHERE timetable_id = '"+timeTableID+"'");
			
    					   ResultSet oTCRS = onTimeCheck.executeQuery();
			
    					   if(oTCRS.next())
    					   	{

    						   int status = oTCRS.getInt(1);
    						   System.out.println("status1: "+status);
				
				 
    						   if (status == 1)
    						   	{
				 
					 
    							//  if (((((min >= early) && (min <= 59)))))
    								if (((cHour == (sqlHourInt-1)) && ((min >= early) && (min <=59))) )
    							   
    							   	{
    								   System.out.println("if1");
				 
    								   onTime = con.prepareStatement("UPDATE attendances SET absent=0, on_time=1, time='"+timeStamp+"' WHERE timetable_id = '"+timeTableID+"' ");
				 	
    								   System.out.println("ok");
    								   onTime.executeUpdate();
    								   response(roomID, "CON", "PASS");
    							   	}

    							   // System.out.println("on time");
	 
    							 //  else if ((min <= lateTime) && (min >= 0))
    								else if (((cHour == (sqlHourInt)) && ((min >= 0) && (min <= lateTime))) )
    							   	{
    								   System.out.println("else1");
    								   late = con.prepareStatement("UPDATE attendances SET absent=0, late=1, time='"+timeStamp+"' WHERE timetable_id = '"+timeTableID+"' ");
    								   late.executeUpdate();
    								   System.out.println("late");
    								   response(roomID, "CON", "LATE");
    							   	}
				 
    								
    							   else
    							   	{
    								   System.out.println("fail");
    								   response(roomID, "CON", "FAIL");
    							   	}
    						   	}
    					   	} 
    				   	} 
		 
		 
    				   else
    				   	{
    					   System.out.println("else rs failed");
    					   getRelatedSessions = con.prepareStatement("SELECT session_id FROM sessions WHERE ses_code = '"+sesCode+"'");
			  
    					   rs3 = getRelatedSessions.executeQuery();
    					   System.out.println("rs3_exec");

    					   while(rs3.next())
    					   	{
    						   System.out.println("rs3_next");
    						   wSessionID = rs3.getString(1);
				 
    						   System.out.println(wSessionID);

    						   checkWrong = con.prepareStatement("SELECT timetable_id FROM timetables WHERE session_id = '"+wSessionID+"' AND student_id= '"+studID+"'");
				 
    						   rs4 = checkWrong.executeQuery();
    						   System.out.println("rs4_exec");
				 
    						   if (rs4.next())
    						   	{
    							   rs4 = checkWrong.executeQuery();
    							   
    							   while (rs4.next())
    							   	{
    								   System.out.println(rs4.getString(1));
					 
    								   String wrongTimeTableID = rs4.getString(1);
				 
    								   System.out.println("if2");
    								   
    								   PreparedStatement onTimeCheck = con.prepareStatement("SELECT absent FROM attendances WHERE timetable_id = '"+wrongTimeTableID+"'");
						
    								   System.out.println("if2.1");
    								   ResultSet oTCRS = onTimeCheck.executeQuery();
    								   System.out.println("if");
					
    								   if (oTCRS.next()) 
    								   	{
    									   System.out.println("if2.2");
						
    									   System.out.println("if2.4");
    									   int status = oTCRS.getInt(1);
    									   System.out.println("if2.5");
    									   
    									   if (status == 1)
    									   	{
						 System.out.println("if2.3");
					// if ((((min >= early) && (min <= 59))) ) 
						 if (((cHour == (sqlHourInt-1)) && ((min >= early) && (min <=59))) )
							   
						 
	    			 {
						 System.out.println("if3");
     
	        			 wrongOnTime = con.prepareStatement("UPDATE attendances SET absent=0, on_time=1, time='"+timeStamp+"', wrong_ses=1 WHERE timetable_id = '"+wrongTimeTableID+"' ");
	        			 
	        			 wrongOnTime.executeUpdate();
	        			 
	        			 System.out.println("wrong time wrong session");
	        			 response(roomID, "CON", "PASS");
	    			 }
					 
						 
						// else if ((min <= lateTime) && (min >= 0))
						 else if (((cHour == (sqlHourInt)) && ((min >= 0) && (min <= lateTime))) )
	        			 {
	        			System.out.println("else2");
	        				 wrongLate = con.prepareStatement("UPDATE attendances SET absent=0, late=1, time='"+timeStamp+"', wrong_ses=1 WHERE timetable_id = '"+wrongTimeTableID+"' ");
	        				 System.out.println("late wrong session");
	        				 
	        				 wrongLate.executeUpdate();
	        				 response(roomID, "CON", "LATE");
	        			 }
				 
				 
				 else
				 {
					 System.out.println("inner FAILL");
					 response(roomID, "CON", "FAIL");
				 }
				 }
				 }
				 }
			 }
			 }
			 
		 }
		 
		 
	 }
	 }
	 else
	 { 
		 System.out.println("outer FAILL");
		 response(roomID, "CON", "FAIL");
	 }
      
	 
	 
   	 } catch (SQLException e) {
	 System.out.println(e);
	}
       }
  
    	 socket.close();
    
       
	} 
	
	}
	
	
	
   
 }





}

	

