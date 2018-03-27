package com.engg.questionpaper.managers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.engg.questionpaper.models.TagBean;

@ManagedBean(name="tagManager" )
@SessionScoped
public class TagManager implements Serializable{
	/**
	 * 
	 */

	private String tagColumnName;

	private List<TagBean> tagList = new ArrayList<TagBean>();
	private static final long serialVersionUID = 1L;
	
//	
//	public SubjectBean getSubject() {
//		return subject;
//	}
//	public void setSubject(SubjectBean subject) {
//		this.subject = subject;
//	}
	Connection conn=ConnectionManager.getConnection();
	
	public String getTagColumnName() {
		return tagColumnName;
	}
	public void setTagColumnName(String tagColumnName) {
		this.tagColumnName = tagColumnName;
	}
	public void setTagList(List<TagBean> tagList) {
		this.tagList = tagList;
	}
		//to add the tag
		public String addTag(){
			PreparedStatement ps= null;
			int psCount=1;
			TagBean tagBean;
			try{
				   ps = conn.prepareStatement("insert into tag_master(tag_column_name, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,now(),?,now(),?)");
				   ps.setString(psCount++, getTagColumnName());
				   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
				   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
				   ps.setInt(psCount++, 0);
				   ps.executeUpdate();
			      
				//alter table ques_tag_mapping
					Statement stmt = conn.createStatement();
					stmt.execute("ALTER TABLE ques_tag_mapping ADD "+getTagColumnName()+" varchar(100)");
		       
				   System.out.println("Inserted records into the table tag_master");
			       //write alter table logic for ques_tag_mapping
			       
			     //add the tag to the list
			       tagBean = new TagBean();
			       tagBean.setTagColumnName(getTagColumnName());
			       setTagColumnName(getTagColumnName());
			       tagList.add(tagBean);
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
				public List<TagBean> getTagList(){
					PreparedStatement ps = null;
					ResultSet rs = null;					
					tagList = new ArrayList<TagBean>();
					TagBean tagBean;
					try{
						  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
						   String sql = "SELECT tag_column_name, tag_id FROM tag_master where rowstate!=-1";
						   ps = conn.prepareStatement(sql);
						   rs = ps.executeQuery(sql);
						      //STEP 5: Extract data from result set
						      while(rs.next()){
						         //Retrieve by column name
						    	 tagBean = new TagBean();
						    	 tagBean.setTagColumnName(rs.getString("tag_column_name"));
						    	 tagBean.setTagId(rs.getInt("tag_id"));
						    	 tagList.add(tagBean);
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
					return tagList;
				}
				
}
