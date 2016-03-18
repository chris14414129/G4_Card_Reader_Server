import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.*;
import java.util.*;
import java.time.*;

public class autoAbsent  extends Thread{
private String connection;
private String userName;
private String password;

//minAt is the minute at which even will occur
private int minAt;
//doOnce prevents loop from occuring multiple times
private boolean doOnce =false ;


public autoAbsent(String connection, String user, String pass, int minAt)
{
	this.connection=connection;
	this.userName=user;
	this.password=pass;
	this.minAt = minAt;
    
}

//main code
public void run()
{
	//Have variables and initialize at top (try/catch causes issues)
	//note the use of "null"
	
	//connection to server
	Connection con = null;
	//prepared statements
    PreparedStatement pst = null;
    PreparedStatement students = null;
    PreparedStatement attendance = null;
 
    
    //Where the results are stored
    ResultSet rs = null;
    ResultSet rs2 = null;
    
    //vars for prepared statement
    String session = null;
    String timetable = null;
   
    
    //connection to server
    //connection must exist outside of any loops, too many connections causes server issues
    try {
		con = DriverManager.getConnection(this.connection, this.userName, this.password);
	
		//note: SQLException not IOException
    } catch (SQLException e1) {
		// 
		e1.printStackTrace();
	}
  
    //any looped code use this as thread.stop is not recommended
    while (!Thread.currentThread().isInterrupted())
    {
    	//System.out.println("while");
    	
    	//gets date/time
    	//time is in loop to keep it updated.
    	
    	//gets current time
    	LocalTime localTime = LocalTime.now();
    	
    	//get's current minute and hour
    	int min = localTime.getMinute();
    	int hour = localTime.getHour()+1;
    	//int min= 45;
    	//int hour=19;
    	
    	//System.out.println(hour);
    	//System.out.println(min);
    	
    	
    	//gets current day
    	Calendar calendar = Calendar.getInstance();
    	Date date = calendar.getTime();
    	//String day = new SimpleDateFormat("E", Locale.ENGLISH).format(date.getTime());
    	String day = "Mon";
    	 
    	//System.out.println(day);
   
    	//minAt is the minute at which even will occur
    	if ((this.minAt == min))
    	{
    		//doOnce prevents loop from occuring multiple times
    		if(!doOnce)
    		{
    			
		    try{
		        //creates hour found in database for session
		    	String t = hour+":00:00";
		    	
		    	String timeStamp = new SimpleDateFormat("yyyy-MM-dd "+t).format(Calendar.getInstance().getTime());
		    	
		    	
		    	
		    	//System.out.println(timeStamp);
		        
		    	//prepared statement
		
		       pst = con.prepareStatement("SELECT session_id FROM sessions WHERE day = '"+day+"' && time ='"+t+"'");
		      // students = con.prepareStatement("SELECT timetable_id from timetables WHERE session_id = '"+session+"'");
		     //  attendance = con.prepareStatement("INSERT INTO attendances (timetable_id, absent, time) VALUES ('"+timetable+"',"+1+",'"+timeStamp+"')");
		       //runs query
		       rs = pst.executeQuery();
		  
		        
		     
		       //looks through results
		       while (rs.next()) {
		    	System.out.println("rs session_id: "+ rs.getString(1));
		    
		    	  //used for subquery
		    	   //note: can be placed above with other prepared queries
		    	session = rs.getString(1);
		    	
		    	students = con.prepareStatement("SELECT timetable_id from timetables WHERE session_id = '"+session+"'");
		    	  rs2 = students.executeQuery();
		    	  	//sub query can use previous results 
		    	  // think for loop inside for loop
		    
			    	  while (rs2.next())
			    	  {
			    		  System.out.println("rs2");
			    		  System.out.println("timetable_id : "+ rs2.getString(1) );
			    		  System.out.println("time: "+timeStamp);
			    		
			    		  timetable = rs2.getString(1);
			    		
			    		  //can use results from inner results
			    		  attendance = con.prepareStatement("INSERT INTO attendances (timetable_id, absent, time) VALUES ('"+timetable+"',"+1+",'"+timeStamp+"')");
			    		  attendance.executeUpdate();
			    	
			    	  }
		    	  
		    	  
		       }
		       
		       
		       
		
		      //sets doOnce to true 
		      doOnce = true;
		    System.exit(0);
		  
		    }
		    
		   
		    catch(SQLException e)
		    {
		    	System.out.println(e);
		    }
    		}
    		
    		
	
    	}
    	
    	//resets doOnce
    	if ((this.minAt != min))
		{
		doOnce=false;
		}
	 
    	 
	
	
	 
    }


}
}
	

