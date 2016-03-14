import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class roomSession {
	private String connection;
	private String userName;
	private String password;
	private String room;
	private int serverPort;
	private int minAt;
	private int early;
	private int late;
	
	public roomSession(String connection, String user, String pass, String room, int minAt, int port, int early, int late)
	{
		
		this.connection=connection;
		this.userName=user;
		this.password=pass;
		this.room=room;
		this.serverPort=port;
		this.early=early;
		this.late=late;
		this.minAt = minAt;
		
		
	}	
	
	public void run()
	{
		//SQL var
		Connection con = null;
	    PreparedStatement session = null;
	    ResultSet rs = null;
	    
	    //TCP var
		 PrintWriter out=null;
		 ServerSocket serverSocket=null;
		 Socket clientSocket=null;
		 BufferedReader in=null;
		 String inputLine="";
		 String roomID;
	 	
	 	 try {
	   		con = DriverManager.getConnection(this.connection, this.userName, this.password);
	   	} catch (SQLException e1) {
	   		// TODO Auto-generated catch block
	   		e1.printStackTrace();
	   	}

	 	
		while((!Thread.currentThread().isInterrupted()))
		{
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
	    	
	    	String t = hour+":00:00";
	    	
	    	
	    	//gets current day
	    	Calendar calendar = Calendar.getInstance();
	    	Date date = calendar.getTime();
	    	String day = new SimpleDateFormat("E", Locale.ENGLISH).format(date.getTime());
	    	//String day = "Mon";
	    	 
	    	//System.out.println(day);
	   
	    	//minAt is the minute at which even will occur
	    	if ((this.minAt == min))
	    	{
	    		
	    		
	    		try
	    		{
	    			session = con.prepareStatement("SELECT (session_code, ses_code, time, duration, room_id) FROM sessions WHERE time = '"+t+"'" );
	    			rs = session.executeQuery();
	    			
	    			while (rs.next())
		    		{
		    			
		    		}
	    			
	    		}
	    		catch (SQLException e)
	    		{
	    			
	    		}
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    	}
			
			
			
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
