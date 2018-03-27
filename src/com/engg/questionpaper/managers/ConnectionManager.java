package com.engg.questionpaper.managers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
	
	

		   /**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
		// JDBC driver name and database URL
		   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		   static final String DB_URL = "jdbc:mysql://localhost/mca";

		   //  Database credentials
		   static final String USER = "root";
		   static final String PASS = "mysql";
		   
		   public static Connection getConnection() {
		   Connection conn = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName("com.mysql.jdbc.Driver");

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

//		      //STEP 4: Execute a query
//		      System.out.println("Creating statement...");
//		      stmt = conn.createStatement();
//		      String sql;
//		      sql = "SELECT id, first, last, age FROM Employees";
//		      ResultSet rs = stmt.executeQuery(sql);
//
//		      //STEP 5: Extract data from result set
//		      while(rs.next()){
//		         //Retrieve by column name
//		         int id  = rs.getInt("id");
//		         int age = rs.getInt("age");
//		         String first = rs.getString("first");
//		         String last = rs.getString("last");
//
//		         //Display values
//		         System.out.print("ID: " + id);
//		         System.out.print(", Age: " + age);
//		         System.out.print(", First: " + first);
//		         System.out.println(", Last: " + last);
//		      }
//		      //STEP 6: Clean-up environment
//		      rs.close();
//		      stmt.close();
		     // conn.close();
		      
		   }
		   catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }
		   System.out.println("Connection Successfull!");
		   return conn;
		}//end main
		}//end FirstExample

