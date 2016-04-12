import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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

public class roomSession extends Thread {
	private String connection;
	private String userName;
	private String password;
	//private String room;
	private int serverPort;
	private String broadcastIP;
	private int minAt;
	private int early;
	private int late;
	//doOnce prevents loop from occuring multiple times
	private boolean doOnce =false ;
	
	
	public roomSession(String connection, String user, String pass, int minAt, int port,  String broadcastIP)
	{
		
		this.connection=connection;
		this.userName=user;
		this.password=pass;
		this.serverPort=port;
		this.minAt = minAt;
		this.broadcastIP=broadcastIP;
		
	}	
	
	public void run()
	{
		System.out.println("roomSession running");
		
		//SQL var
		Connection con = null;
	    PreparedStatement session = null;
	   // PreparedStatement room = null;
	    ResultSet rs = null;
	    ResultSet rs2 = null;
	    
	    //TCP var
	    DatagramSocket socket = null;
		// String roomID;
		 
	 	
	 	 try {
	 	//	System.out.println("try");
	   		con = DriverManager.getConnection(this.connection, this.userName, this.password);
	   	//	System.out.println("try2");
	   	} catch (SQLException e) {
	   		// TODO Auto-generated catch block
	   		e.printStackTrace();
	   		System.out.println(e);
	   	}

	 	 try
	 	 {
	 		socket = new DatagramSocket ();
	         
	 	 }
	 	 catch(IOException e)
	 	 {
	 		 
	 	 }
	 	
		while((!Thread.currentThread().isInterrupted()))
	 	 
		{
			//System.out.println("while_running");
			
			//gets date/time
	    	//time is in loop to keep it updated.
	    	
	    	//gets current time
	    	LocalTime localTime = LocalTime.now();
	    	
	    	//get's current minute and hour
	    	//int min = localTime.getMinute();
	    	//int hour = localTime.getHour()+1;
	    	int min= 45;
	    	int hour=19;
	    	
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
	   
	    	//minAt is the minute at which even will occur
	    	if (min==minAt)
	    	{
	    		//System.out.println(doOnce);
	    		if(!doOnce)
	    		{
	    		
	    		try
	    		{
	    			//System.out.println(t);
	    		session = con.prepareStatement("SELECT ses_code, ses_name, time, duration, room_id FROM sessions WHERE time = '"+t+"'" );
	    		//session = con.prepareStatement("SELECT  time from sessions WHERE room_id=10");
	    			//session = con.prepareStatement("SELECT * FROM sessions");
	    			//room = con.prepareStatement("SELECT (room) FROM rooms WHERE room_id = '"+rs.getString(5)+"'");
	    			rs = session.executeQuery();
	    		//	rs2 = room.executeQuery();
	    		
	    			//boolean isMoreThanOneRow = rs.first() && rs.next();
	    			
	    			//System.out.println(isMoreThanOneRow);
	    			
	    			while (rs.next())
		    		{
	    				
	    				//System.out.println(rs);
	    				/*System.out.println("Sessions_code :"+rs.getString(1));
	    				System.out.println("session_name: "+rs.getString(2));
	    				System.out.println("start_time: "+rs.getString(3));
	    				System.out.println("duration: "+rs.getString(4));
	    				System.out.println("room_id: "+rs.getString(5));*/
	    				
	    				
	    				
	    				//System.out.println(rs.getString(1));
	    				
	    				
	    				int curRoomInt = rs.getInt(5);
	    				
	    				//int curRoomInt = 9;
	    				
	    				String curRoom = "";
	    				
	    				if (curRoomInt < 10)
	    				{
	    					curRoom = "00"+curRoomInt;
	    				}
	    				else if ((curRoomInt < 100) && (curRoomInt >10))
	    				{
	    					curRoom = "0"+curRoomInt;
	    				}
	    				
	    				//System.out.println(curRoom);
	    				
	    				//System.exit(0);
	    				
	    				String startTime = rs.getString("time");
	    				
	    				
	    				//add padding to names
	    				String sCode = rs.getString("ses_code");
	    				String sName = rs.getString("ses_name");
	    				//String sName = "This is a test";
	    				
	    				//http://www.dotnetperls.com/padding-java
	    				//http://stackoverflow.com/questions/388461/how-can-i-pad-a-string-in-java
	    				String sCodeFormatted = String.format("%1$-11s", sCode).replace(" ", "*");
	    				String sNameFormatted = String.format("%1$-30s", sName).replace(" ", "*");
	    				
	    				
	    				
	    				//System.out.println(sCodeFormatted);
	    			//	System.out.println(sNameFormatted);
	    				
	    				
	    				//calculate and produce end hour using duration
	    				int endHour = hour+rs.getInt("duration");
	    				String endTime = endHour+":00:00";
	    		//		System.out.println("endTime: "+endTime);
	    				
	    				
	    				
	    				try
		    			{
	    					byte[] buf = new byte[256];
		    		         String output = curRoom+"UPD"+sCodeFormatted+sNameFormatted+startTime+endTime;
		    		         
		    		        
							buf = output.getBytes ();
		    		            InetAddress address = InetAddress.getByName (this.broadcastIP);
		    		            DatagramPacket packet = new DatagramPacket (buf, buf.length, address, this.serverPort);
		    		            socket.send(packet);
		    		            
		    		       
		    		       
		    		            
		    		           
		    			}
		    			catch(IOException e)
		    			{
		    				e.printStackTrace();
		    			}
	    				
	    				
	  	    			
	    				
		    		}
	    		
	    		
	    			
	    		
		    		}
	    		catch (SQLException e)
	    		{
	    			e.printStackTrace();
	    		}
	    		//  System.exit(0);
  				
   			 //sets doOnce to true 
 	  		    //  doOnce = true;
 	  		      
 	  		    
	    		
	    		}
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    		
	    	}
	    	//resets doOnce
        	if ((this.minAt != min))
    		{
    		doOnce=false;
    		}
			
			
		}
		
	    
	    	  try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (session != null) {
	                    session.close();
	                }
	                if (con != null) {
	                    con.close();
	                }
	    }
	    catch(SQLException e)
	    {
	    	
	    }
	    	  
	    	  
	    	
		 /*try{
		    	// System.out.println("hell");
		    	 out.close();
		    	    in.close();
		    	    clientSocket.close();
		    	    serverSocket.close();
		    	 //   System.out.println("close");
		     }
		     catch(IOException e)
		     {
		    	 
		     }*/
		
	}
	
	
	
	
}
