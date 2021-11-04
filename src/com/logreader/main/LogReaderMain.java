package com.logreader.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.logreader.model.ServerLogs;
import com.logreader.utils.LogFormatter;

public class LogReaderMain {
	public static Logger logger ;
	public static FileHandler fileHandler;
	static {
		try {
		      boolean append = false;
		      fileHandler = new FileHandler("Application.log", append);
		      //fh.setFormatter(new XMLFormatter());
		      fileHandler.setFormatter(new SimpleFormatter());
		      logger = Logger.getLogger("ApplicationLog");
		      logger.addHandler(fileHandler);
		      System.setProperty("hsqldb.reconfig_logging", "false");
		    }
		    catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	

	public static void main(String[] args) throws Exception{
		
		
		// TODO Auto-generated method stub
		File file = new File(args[0]);
		logger.info( "Establishing Connection to Database");
		//java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/AletEvents --dbname.0 AlertEventsDB
		Connection connection = makeConnection();
		logger.info("Connection to Database Established!");
		boolean createTable = createTable(connection);
		if(!createTable) {
			logger.log(Level.SEVERE, "Not able to create Table..");
		}
		logger.info("Table succesfully created or already exists!");
		logger.info("Starting to read logfile.txt...");
		try(
//		InputStream input = Files.newInputStream(Path.of("src\\com\\logreader\\main\\logfile.txt"));
				
		BufferedReader br = new BufferedReader(new FileReader(file));
		JsonReader reader = new JsonReader(br);
				){
			HashMap<String,ServerLogs> hmap = new HashMap<String,ServerLogs>();
			reader.beginArray();
			while(reader.hasNext()) {
				Boolean alertFlag=false;
				ServerLogs serverLogs = new Gson().fromJson(reader, ServerLogs.class);
				if(hmap.containsKey(serverLogs.getId())){
					long eventDuration=Math.abs(serverLogs.getTimestamp()-hmap.get(serverLogs.getId()).getTimestamp());
					if(eventDuration>4) {
						alertFlag = true;
					}
					saveToDatabase(serverLogs.getId(),eventDuration,connection,alertFlag);
					continue;
				}
				hmap.put(serverLogs.getId(), serverLogs);
			}
			reader.endArray();
			boolean breakConnection = breakConnection(connection);
		}
		logger.info("Completed Reading logfile.txt");
		
	}
	public static Connection makeConnection() throws ClassNotFoundException,SQLException{
		Connection con = null;	  
	      try {
	    	  
	         Class.forName("org.hsqldb.jdbc.JDBCDriver");
	         con = DriverManager.getConnection("jdbc:hsqldb:file:AlertEvents", "SA", "");
	      }  catch (Exception e) {
	    	  logger.info("Exception");
		         throw e;
		  }
		  return con;
	}
	
	public static Boolean createTable(Connection con) throws SQLException{
	      Statement stmt = null;
	      int result = 0;
	      
	      try {
	          stmt = con.createStatement();
	         
	         result = stmt.executeUpdate("CREATE TABLE IF NOT EXISTS AlertEvents ("+
	            "id VARCHAR(20) NOT NULL, EventDuration INT NOT NULL, ALERT_STATUS BOOLEAN DEFAULT FALSE)"
	           );
//	          result = stmt.executeUpdate("ALTER TABLE AlertEvents ADD ALERT_STATUS BOOLEAN DEFAULT FALSE");
//				con.commit();
	      }  catch (Exception e) {
	         throw e;
	      }finally {
	    	  if(stmt!=null) {
	    		  try {
	    		  stmt.close();
	    		  }finally {
	    			  stmt=null;
	    		  }
	    	  }
	      }
	      return true;
	   }
	
	public static Boolean breakConnection (Connection con) throws SQLException {
		try {
		if(con!=null) {
			con.close();
		}
		}
		catch(Exception exception) {
			throw exception;
		}
		finally {
			con=null;
		}
		return true;
	}
	
	public static Boolean saveToDatabase(String eventId, long eventDuration,Connection connection,Boolean alertFlag) throws SQLException{
		String insertQuery = "INSERT INTO AlertEvents (id, EventDuration,ALERT_STATUS) values (?, ?, ?)";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(insertQuery);
			stmt.setString(1, eventId);
			stmt.setLong(2, eventDuration);
			stmt.setBoolean(3, alertFlag);
			stmt.executeUpdate();
			connection.commit();
		}catch (Exception e) {
	         throw e;
	      }finally {
	    	  if(stmt!=null) {
	    		  try {
	    		  stmt.close();
	    		  }finally {
	    			  stmt=null;
	    		  }
	    	  }
	      }
	      return true;
	}
	}
