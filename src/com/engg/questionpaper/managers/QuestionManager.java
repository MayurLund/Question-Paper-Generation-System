package com.engg.questionpaper.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.engg.questionpaper.models.QuestionMasterBean;
import com.engg.questionpaper.models.TagBean;

@ManagedBean(name="questionManager")
@SessionScoped
public class QuestionManager implements Serializable {
	
	/**
	 * 
	 */
	private List<QuestionMasterBean> questionList = new ArrayList<QuestionMasterBean>();
	private static final long serialVersionUID = 1L;
	Connection conn=ConnectionManager.getConnection();
	LoginManager loginManager = new LoginManager();
	
	public String writeQuestionExcel()throws IOException
	{
	
		//writing the download excel code		
		XSSFWorkbook workbook = new XSSFWorkbook();      
        //Create a help sheet
      //  XSSFSheet helpSheet =  workbook.createSheet("Help");//commenting while time being
        //Create another sheet with columns
        XSSFSheet questionSheet =  workbook.createSheet("Question Sheet");
        //create the first row
        Row row = questionSheet.createRow(0);
        //create the map for column names
        Map<Integer, String> columnMap = new HashMap<Integer, String>();
        columnMap.put(0, "Question Id");
        columnMap.put(1, "Question Type");
        columnMap.put(2, "Question Text");
        columnMap.put(3, "Subject");
        columnMap.put(4, "Marks");
        columnMap.put(5, "Section");
        columnMap.put(6, "Topic");
        columnMap.put(7, "Term");
        columnMap.put(8, "Year");
        columnMap.put(9, "Others");
        //create the columns
        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
        	Cell cell = row.createCell(cellNum);
        	cell.setCellValue(columnMap.get(cellNum));
        }

        FileOutputStream fileOut;
		try {
			File questionExcel = new File("D:\\QuestionExcel.xlsx");
			fileOut = new FileOutputStream(questionExcel);
			//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
			//FileOutputStream fos = new FileOutputStream(pdfFile);
			workbook.write(fileOut);
		    //fileOut.close();
		    //download part
			 HttpServletResponse response =
				        (HttpServletResponse) FacesContext.getCurrentInstance()
				            .getExternalContext().getResponse();
			ServletOutputStream out = response.getOutputStream();
	        FileInputStream in = new FileInputStream("D:\\QuestionExcel.xlsx");

	        response.setContentType("application/vnd.ms-excel");
	        response.addHeader("content-disposition",
	                "attachment; filename=" + "QuestionExcel.xlsx");

	        int octet;
	        while((octet = in.read()) != -1)
	            out.write(octet);
	        
	        in.close();
	        out.close();
		    FacesContext.getCurrentInstance().responseComplete();
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "mainPage";
	}

	//writing code to upload the excel
	public String readQuestionExcel(String filePath)
	{
	
		 try
	        {
	            FileInputStream file = new FileInputStream(filePath);
	 
	            //Create Workbook instance holding reference to .xlsx file
	            XSSFWorkbook workbook = new XSSFWorkbook(file);
	 
	            //Get first/desired sheet from the workbook
	            XSSFSheet sheet = workbook.getSheetAt(0);
	            XSSFRow row = sheet.getRow(1);
	            List <QuestionMasterBean> questionList = new ArrayList<QuestionMasterBean>();
	            QuestionMasterBean questionBean;
	 
	            //Iterate through each rows one by one
	            //Iterator<Row> rowIterator = sheet.iterator();
	            for(int rowNum =1; rowNum< sheet.getPhysicalNumberOfRows(); rowNum++){
	            	row= sheet.getRow(rowNum);
	                //create Question Bean
	                questionBean = new QuestionMasterBean();
	                    //Check the cell type and format accordingly
	            	   questionBean.setQuestionId((int)row.getCell(0).getNumericCellValue());
	            	   questionBean.setQuestionType(row.getCell(1).getStringCellValue());
	            	   questionBean.setQuestionText(row.getCell(2).getStringCellValue());
	            	   questionBean.setSubjectName(row.getCell(3).getStringCellValue());
	            	   //write code to fetch the subject id
	            	   questionBean.setSubjectId(1);//hardcoded subject id
	            	   questionBean.setMarks((int)row.getCell(4).getNumericCellValue());
	            	  //create a tag list
	            	   //change the logic since tables have changed
	            	   List<TagBean> tagList = new ArrayList<TagBean>();
	            	   for(int cellNum= 5; cellNum<=9; cellNum++){
	            		   if(row.getCell(cellNum)!=null){
	            			   TagBean tag = new TagBean();
	            			   tag.setTagId(cellNum);//hardcoded tag id
                               tag.setTagValue(row.getCell(cellNum).getStringCellValue());
	            			   tagList.add(tag);
	            		   }
	            	   }
                      //add the tag list
	            	   questionBean.setTagList(tagList);
	            	   questionList.add(questionBean);	                
	            }
	            conn= ConnectionManager.getConnection();
	            //insert all questions in the database
	            for(QuestionMasterBean question: questionList){
	            	addQuestion(question,conn);
	            }
	            
	          
	            file.close();
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
		 FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Congratulations!! Questions uploaded successfully"));	
		 return "mainPage";
	    }
	public static void main(String args[]) throws IOException{
		QuestionManager questionManager = new QuestionManager();
		//questionManager.readQuestionExcel();
		questionManager.writeQuestionExcel();
	}
		
	//to add the question 
	public void addQuestion(QuestionMasterBean question, Connection con){
		PreparedStatement ps = null;
		int psCount=1, generatedQuestionId=0;
		ResultSet rs = null;
		try{
			   ps = conn.prepareStatement("insert into question_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   ps.setString(psCount++, question.getQuestionType());
			   ps.setString(psCount++, question.getQuestionText());
			   ps.setInt(psCount++, question.getSubjectId());
			   ps.setInt(psCount++, question.getMarks());
			   ps.setInt(psCount++,loginManager.getUserId());//user id hardcoded(created_by)
			   ps.setInt(psCount++,loginManager.getUserId());//user id hardcoded(modified_by)
			   ps.setInt(psCount++, 0);
			   ps.executeUpdate();
			   String query = "select max(question_id) from question_master";
			   rs =  ps.executeQuery(query);
			   if(rs.next()){
				   generatedQuestionId=	rs.getInt(1);	   
			   }
			   
		       System.out.println("Inserted records into the table question_master with id :: "+generatedQuestionId );
		      //insert into ques_tag_mapping table      
		      if(question.getTagList()!=null){
		    	 	   ps = conn.prepareStatement("insert into ques_tag_mapping(ques_id, section, topic, term, year, others, created_by, created_at, modified_by, modified_at, rowstate) values(?,?,?,?,?,?,?,now(),?,now(),?)");
		    		   psCount=1;
					   ps.setInt(psCount++, generatedQuestionId);
					   ps.setString(psCount++, question.getTagList().get(0).getTagValue());
					   ps.setString(psCount++, question.getTagList().get(1).getTagValue());
					   ps.setString(psCount++, question.getTagList().get(2).getTagValue());
					   ps.setString(psCount++, question.getTagList().get(3).getTagValue());
					   ps.setString(psCount++, question.getTagList().get(4).getTagValue());
					   ps.setInt(psCount++, loginManager.getUserId());//user id hardcoded(created_by)
					   ps.setInt(psCount++, loginManager.getUserId());//user id hardcoded(modified_by)
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
		   
	}

	//delete the question
	public String deleteQuestion(int questionId){
		PreparedStatement ps = null;
		int psCount=1;
		try{
			   ps = conn.prepareStatement("update question_master set rowstate=-1, modified_by =?, modified_at=now() where question_id =? ");
			   ps.setInt(psCount++, 1);
			   ps.setInt(psCount++, questionId);//user_id
			   System.out.println("Query:: " + ps);
			   ps.executeUpdate();
			   FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Question deleted successfully with id: "+questionId));	   
		}catch(SQLException se){
				      //Handle errors for JDBC
				      se.printStackTrace();
		 }catch(Exception e){
				      //Handle errors for Class.forName
		      e.printStackTrace();
		 }
		return null;
	}
	//edit the question
		public String editQuestion(int questionId){
			PreparedStatement ps = null;
			ResultSet rs= null;
			int psCount=1;
			try{
				   ps = conn.prepareStatement("select question_id, question_text, marks, subject_id from question_master " +
				   		" where question_id =? ");
				   ps.setInt(psCount++, questionId);//user_id
				   System.out.println("Select Query:: " + ps);
				   rs = ps.executeQuery();
				      //STEP 5: Extract data from result set
				      if(rs.next()){
				         //Retrieve by column name
				    	  this.setQuestionId(rs.getInt("question_id"));
				    	  this.setQuestionText(rs.getString("question_text"));
				    	  this.setSubjectId(rs.getInt("subject_id"));
				    	  this.setMarks(rs.getInt("marks"));
				      }
				      return "editQuestion";
				  }catch(SQLException se){
					      //Handle errors for JDBC
					      se.printStackTrace();
			 }catch(Exception e){
					      //Handle errors for Class.forName
			      e.printStackTrace();
			 }
			return null;
		}

		//update the question
		public String updateQuestion(int questionId){
			PreparedStatement ps = null;
			int psCount=1;
			try{
				   ps = conn.prepareStatement("update question_master set question_text=?, subject_id=?, marks=?, modified_by =?, modified_at=now() where question_id =? ");
				   ps.setString(psCount++, this.questionText);
				   ps.setInt(psCount++, this.subjectId);
				   ps.setInt(psCount++, this.marks);
				   ps.setInt(psCount++, 1);//user_id
				   ps.setInt(psCount++, questionId);
				   System.out.println("Update Query:: " + ps);
				   ps.executeUpdate();
				   //reset the data
				   this.setQuestionText("");
				   this.setMarks(0);
				   this.setSubjectId(1);
				   FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Question updated successfully with id: "+questionId));
			}catch(SQLException se){
					      //Handle errors for JDBC
					      se.printStackTrace();
			 }catch(Exception e){
					      //Handle errors for Class.forName
			      e.printStackTrace();
			 }
			return "SearchQuestion";
		}
	
	public List<QuestionMasterBean> getQuestionList(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		questionList = new ArrayList<QuestionMasterBean>(); 
		System.out.println("QuestionId "+ this.questionId);
		QuestionMasterBean quetionMasterBean;
		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   StringBuilder sql = new StringBuilder("select question_id, question_text, marks, subject_name from question_master q join subject_master s" +
			   		" where q.subject_id = s.subject_id and q.rowstate!=-1 and s.rowstate!=-1") ;
			   if(this.questionId!=0){
				   sql.append(" and question_id ="+this.questionId);
			   }
			   if(this.marks!=0){
				   sql.append(" and question_id ="+this.marks);
			   }
			   if(this.subjectId!=0){
				   sql.append(" and s.subject_id ="+this.subjectId);
			   }
			   ps = conn.prepareStatement(sql.toString());
			   System.out.println("Query:: " + sql.toString());
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  quetionMasterBean = new QuestionMasterBean();
			    	  quetionMasterBean.setQuestionId(rs.getInt("question_id"));
			    	  quetionMasterBean.setQuestionText(rs.getString("question_text"));
			    	  quetionMasterBean.setSubjectName(rs.getString("subject_name"));
			    	  quetionMasterBean.setMarks(rs.getInt("marks"));
			    	  questionList.add(quetionMasterBean);
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
		return questionList;
	}
	/*public List<SubjectBean> getSubjectList() {
		return subjectList;
	}*/
	public void setQuestionList(List<QuestionMasterBean > questionList) {
		this.questionList= questionList;
	}
	
	private int questionId;
	private String questionType;
	private String questionText;
	private int subjectId;
	private String subjectName;
	private int marks;
	private List<TagBean> tagList;
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public List<TagBean> getTagList() {
		return tagList;
	}
	public void setTagList(List<TagBean> tagList) {
		this.tagList = tagList;
	}


}	
	

