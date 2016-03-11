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
private int minAt;

//TCP
ServerSocket serverSocket = null;
static public String str = "?";
static public int taskCount = 0;

public registration(String connection, String user, String pass, String room, int minAt)
{
	
	this.connection=connection;
	this.userName=user;
	this.password=pass;
	this.room=room;
	this.minAt = minAt;
	//TCP
	
}

public void run()
{
	//SQL var
	Connection con = null;
    PreparedStatement onTime = null;
    PreparedStatement late = null;
    PreparedStatement read = null;
    ResultSet rs = null;
    
    //TCP var
	 PrintWriter out=null;
	 ServerSocket serverSocket=null;
	 Socket clientSocket=null;
	 BufferedReader in=null;
	 String inputLine="";
	 String roomID;
 	String studID;
 	
 	 try {
   		con = DriverManager.getConnection(this.connection, this.userName, this.password);
   	} catch (SQLException e1) {
   		// TODO Auto-generated catch block
   		e1.printStackTrace();
   	}

 	
	while((!Thread.currentThread().isInterrupted()))
	{
	 try
	 {
		serverSocket = new ServerSocket (4444);
     clientSocket = serverSocket.accept ();
    
     out = new PrintWriter (clientSocket.getOutputStream (), true);
     in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream ()));
	 }
	 catch (IOException e)
	 {
		 
	 }
     

     try
     {
    	// LocalTime localTime = LocalTime.now();
    	 
    	 String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    	 
    	 inputLine=in.readLine ();
   //  while ((inputLine = in.readLine ()) != null)
    // {
         System.out.println ("Server < " + inputLine);
         System.out.println(inputLine);
         
    // }
         
         roomID = inputLine.substring(0,3);
         studID = inputLine.substring(3,11);
         
         //System.out.println(studID);

         //SQL
         
         try {
        	
     		
     		onTime = con.prepareStatement("UPDATE attendances SET absent=0, on_time=1, time='"+timeStamp+"' WHERE attendance_id = 621 ");
     		 late = con.prepareStatement("UPDATE attendances SET absent=0, on_time=0, late=1, time='"+timeStamp+"' WHERE attendance_id = 621 ");
     		 read = con.prepareStatement("SELECT (abset, on_time, late) FROM attendances WHERE attendance_id = 621");
     	 } catch (SQLException e) {
     		 System.out.println(e);
 		}

         try
    	 {
    		 onTime.executeUpdate();
    		 
    		
    			 String outPut = "On Time";
    			 out.println(outPut);
    			 
    		 
    		 
    		
    	 }
    	 catch (SQLException e)
    	 {
    		 System.out.println(e);
    	 }
        

    
    
     }
     catch (IOException e)
     {
    	 
     }
     
   
     try{
    	 System.out.println("hell");
    	 out.close();
    	    in.close();
    	    clientSocket.close();
    	    serverSocket.close();
    	    System.out.println("close");
     }
     catch(IOException e)
     {
    	 
     }
	}
   
 }





}

	

