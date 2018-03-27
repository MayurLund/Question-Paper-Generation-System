package com.engg.questionpaper.managers;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.engg.questionpaper.models.SubjectBean;

@ManagedBean(name="subjectManager" )
@SessionScoped
public class SubjectManager implements Serializable{
	/**
	 * 
	 */
	private String currentSubjectCodeValue="Hi";
	private String currentSubjectNameValue="Hello";
	//private SubjectBean subject;
	private String subjectCode;
	private String subjectName;
	
	
	
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName)  {
		this.subjectName = subjectName;
	}
	public String getCurrentSubjectCodeValue() {
		return currentSubjectCodeValue;
	}
	public void setCurrentSubjectCodeValue(String currentSubjectCodeValue) {
		this.currentSubjectCodeValue = currentSubjectCodeValue;
	}
	public String getCurrentSubjectNameValue() {
		return currentSubjectNameValue;
	}
	public void setCurrentSubjectNameValue(String currentSubjectNameValue) {
		this.currentSubjectNameValue = currentSubjectNameValue;
	}
	private List<SubjectBean> subjectList = new ArrayList<SubjectBean>();
	private static final long serialVersionUID = 1L;
	
//	
//	public SubjectBean getSubject() {
//		return subject;
//	}
//	public void setSubject(SubjectBean subject) {
//		this.subject = subject;
//	}
	Connection conn=ConnectionManager.getConnection();
	//to add the subject 
		public String addSubject(){
			System.out.println("Inside addSubject");
			PreparedStatement ps = null;
			int psCount=1;
			SubjectBean subjectBean;
			try{
				   ps = conn.prepareStatement("insert into subject_master(subject_code, subject_name, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,now(),?,now(),?)");
				   ps.setString(psCount++, getSubjectCode());
				   ps.setString(psCount++, getSubjectName());
				   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
				   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
				   ps.setInt(psCount++, 0);
				   ps.executeUpdate();
			       System.out.println("Inserted records into the table subject_master");
			     //add the subject to the list
			        subjectBean = new SubjectBean();
			    	subjectBean.setSubjectCode(getSubjectCode());
			    	subjectBean.setSubjectName(getSubjectName());
			    	subjectList.add(subjectBean);
			   }catch(SQLException se){
			      //Handle errors for JDBC
			      se.printStackTrace();
			   }catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }
			   return null;
		}
		//to get the subject list 
				public List<SubjectBean> getSubjectList(){
					PreparedStatement ps = null;
					ResultSet rs = null;
					subjectList = new ArrayList<SubjectBean>();
					SubjectBean subjectBean;
					try{
						  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
						   String sql = "SELECT subject_code, subject_name, subject_id  FROM subject_master where rowstate!=-1";
						   ps = conn.prepareStatement(sql);
						   rs = ps.executeQuery(sql);
						      //STEP 5: Extract data from result set
						      while(rs.next()){
						         //Retrieve by column name
						    	 subjectBean = new SubjectBean();
						    	 subjectBean.setSubjectCode(rs.getString("subject_code"));
						    	 subjectBean.setSubjectName(rs.getString("subject_name"));
						         subjectBean.setSubjectId(rs.getInt("subject_id"));
						    	 subjectList.add(subjectBean);
						      }
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
					return subjectList;
				}
				/*public List<SubjectBean> getSubjectList() {
					return subjectList;
				}*/
				public void setSubjectList(List<SubjectBean> subjectList) {
					this.subjectList = subjectList;
				}
				
}
