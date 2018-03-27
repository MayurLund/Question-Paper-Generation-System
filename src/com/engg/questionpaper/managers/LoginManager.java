package com.engg.questionpaper.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name="loginManager")
@SessionScoped

public class LoginManager {
	private int userId;
	private String userName;
	private String userPassword;
	private String userRole;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	
	public String validateLogin(){
		//fetch the user role and accordingly redirect
		PreparedStatement ps = null;
		ResultSet rs = null;
		String userRole="";
		int userId=0;
		Connection conn=ConnectionManager.getConnection();
		int psCount =1;
		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "SELECT user_name, user_pwd, user_id, user_role FROM users where rowstate!=-1 and user_name=? and user_pwd=?";
			   ps = conn.prepareStatement(sql);
			   ps.setString(psCount++, getUserName());
			   ps.setString(psCount++, getUserPassword());
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      if(rs.next()){
			         //Retrieve by column name
			    	  userRole = rs.getString("user_role");
			    	  userId = rs.getInt("user_id");
			      }else
			      {
			    	  FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Username or Password is Incorrect"));
			      }
			      this.userId= userId;
			      if(userRole.equalsIgnoreCase("Admin")){
			    	return  "mainPage";
			      }else if(userRole.equalsIgnoreCase("Faculty")){
			    	 return "facultyPage";
			      }else if(userRole.equalsIgnoreCase("Content Creator")){
				    	 return "ccPage";
				  }
			      else{
                     return "login"; 			    	  
			      }
			      
			   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }finally{
			      //finally block used to close resources
			      try{
			         if(ps!=null)
			            ps.close();
			         if(rs!=null)
			            rs.close();
			      }catch(SQLException se){
			      }// do nothing
			   }//end try
		
		return null;
	}
	public String logout(){
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    return "logout?faces-redirect=true";
	}

}
