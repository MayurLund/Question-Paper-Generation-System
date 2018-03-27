package com.engg.questionpaper.managers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.engg.questionpaper.models.UserBean;

@ManagedBean(name="configurationManager")
@SessionScoped
public class ConfigurationManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int configurationId;
	private int subjectId;
	private List<UserBean> allUsers;
	private List<Integer> selectedUserIds;
	private List<SelectItem> userNames;//for display purpose
	private List<SelectItem> selectedNames;
	public List<SelectItem> getSelectedNames() {
		return selectedNames;
	}

	public void setSelectedNames(List<SelectItem> selectedNames) {
		this.selectedNames = selectedNames;
	}



	private int noOfGenerates;
	private Date startDate;
	private Date endDate;
	
	Connection conn=ConnectionManager.getConnection();
	
	public int getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(int configurationId) {
		this.configurationId = configurationId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public List<UserBean> getAllUsers() {
		//write the logic for getting the faculty users
		PreparedStatement ps = null;
		ResultSet rs = null;
		allUsers = new ArrayList<UserBean>();
		UserBean userBean;
		int psCount=1;
		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "SELECT user_id, user_name FROM users where rowstate!=-1 and user_role =?";
			   ps = conn.prepareStatement(sql);
			   ps.setString(psCount++, "Faculty");
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	 userBean = new UserBean();
			    	 userBean.setUserId(rs.getInt("user_id"));
			    	 userBean.setUserName(rs.getString("user_name"));
			         allUsers.add(userBean);
			      }
			      /*//add the items in List<SelectItem>
			      userNames = new ArrayList<SelectItem>();
			      for(UserBean user:allUsers){
			    	  userNames.add(new SelectItem(user, user.getUserName()));
			      }*/
			     
			      rs.close();
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
			      }catch(SQLException se){
			      }// do nothing
			   }//end try

		return allUsers;
	}

	public void setAllUsers(List<UserBean> allUsers) {
		this.allUsers = allUsers;
	}
    
	public List<Integer> getSelectedUsers() {
		return selectedUserIds;
	}

	public void setSelectedUsers(List<Integer> selectedUserIds) {
		this.selectedUserIds = selectedUserIds;
	}

	public int getNoOfGenerates() {
		return noOfGenerates;
	}

	public void setNoOfGenerates(int noOfGenerates) {
		this.noOfGenerates = noOfGenerates;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	
	public String addConfiguration(){
		System.out.println("Inside addConfiguration");
		PreparedStatement ps = null;
		ResultSet rs = null;
		int psCount=1, configurationId =0;
		try{
			   ps = conn.prepareStatement("insert into qb_configuration(subject_id, start_date, end_date, no_of_generates, created_by, created_at, modified_by, modified_at, rowstate)  " +
			   		"values(?,?,?,?,?,now(),?,now(),?)", Statement.RETURN_GENERATED_KEYS);
			   ps.setInt(psCount++, getSubjectId());
			   ps.setDate(psCount++, new java.sql.Date(getStartDate().getTime()));
			   ps.setDate(psCount++,new java.sql.Date (getEndDate().getTime()));
			   ps.setInt(psCount++, getNoOfGenerates());
			   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
			   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
			   ps.setInt(psCount++, 0);
			   ps.executeUpdate();
			   rs= ps.getGeneratedKeys();
		       System.out.println("Inserted records into the table qb_configuration_master");
		       if(rs.next()){
		    	   configurationId = rs.getInt(1);
			   }
		     //insert into qb_configuration_users=
		       //String userids[] = selectedUserIds.split(",");
		       for(int i=0; i<=getSelectedUsers().size(); i++)
				{
					psCount=1;
					ps = conn.prepareStatement("insert into qb_configuration_users(configuration_id, user_id, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,now(),?,now(),?)");
					ps.setInt(psCount++, configurationId);
					ps.setInt(psCount++, getSelectedUsers().get(i));
					ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
				    ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
					ps.setInt(psCount++, 0);
					ps.executeUpdate();   
				}
				
		       
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }
		 FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Congratulations!! Configuration Created Successfully "));
		return "mainPage";
	}
}
