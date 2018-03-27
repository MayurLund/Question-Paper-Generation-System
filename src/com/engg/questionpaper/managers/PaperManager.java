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
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.engg.questionpaper.models.QuestionMasterBean;
import com.engg.questionpaper.models.QuestionPaperBean;
import com.engg.questionpaper.models.RuleBean;
import com.engg.questionpaper.models.StaticTextBean;
import com.engg.questionpaper.models.TemplateBean;
@ManagedBean(name="paperManager" )
@SessionScoped

public class PaperManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionID = 1L;
	private int paperId;
	private String paperName;
	private int subjectId;
	private int templateId;
	
	
	public int getPaperId() {
		return paperId;
	}
	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	Connection conn=ConnectionManager.getConnection();
	private List<QuestionPaperBean> paperList = new ArrayList<QuestionPaperBean>();
	
	
	
	public void setPaperList(List<QuestionPaperBean> paperList) {
		this.paperList = paperList;
	}
	public String generatePaper(TemplateBean template){
		int templateId = template.getTemplateId();
		//insert in ques_paper_master
        QuestionPaperBean paperBean = new QuestionPaperBean();
        QuestionMasterBean questionBean;
        List<QuestionMasterBean> questionList = new ArrayList<QuestionMasterBean>();
		System.out.println("Inside generatePaper");
		//fetch the template information
		TemplateManager templateManager = new TemplateManager();
		TemplateBean templateBean = new TemplateBean();
		templateBean=templateManager.getTemplateInfo(templateId);
		//logic to arrange questions sequence wise
		for(StaticTextBean staticBean : templateBean.getStaticList()){
			questionBean = new QuestionMasterBean();
			questionBean.setStaticTextId(staticBean.getStaticTextId());
			questionBean.setQuestionText(staticBean.getStaticText());
			questionBean.setSequenceNo(staticBean.getSequenceNo());
			questionList.add(questionBean);
		}
        for(RuleBean ruleBean : templateBean.getRuleList()){
        	//evaluate the queries and create the question list
        	List<QuestionMasterBean> ruleQuestionList = new ArrayList<QuestionMasterBean>();
        	ruleQuestionList = templateManager.fetchQuestions(ruleBean);
        	questionList.addAll(ruleQuestionList);       				
		}
        //calculate the total marks
        //insert the values in ques_paper_master   
        paperBean.setTemplateId(templateId);
        paperBean.setPaperName(templateBean.getTemplateName() + "_"+new Date());
        paperBean.setSubjectId(templateBean.getSubjectId());
        paperBean.setQuestionList(questionList);	
        System.out.println("Subject Id::" +templateBean.getSubjectId() );
		PreparedStatement ps = null;
		ResultSet rs = null;
		int psCount=1;
		int paperId=0;
		templateBean = new TemplateBean();
		try{
			//add the template
			   ps = conn.prepareStatement("insert into ques_paper_master(template_id, subject_id, ques_paper_name, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,now(),?,now(),?)", Statement.RETURN_GENERATED_KEYS);
			   ps.setInt(psCount++, paperBean.getTemplateId());
			   ps.setInt(psCount++, paperBean.getSubjectId());
			   ps.setString(psCount++,paperBean.getPaperName());
			   ps.setInt(psCount++,1);//user id hardcoded(created_by)
			   ps.setInt(psCount++,1);//user id hardcoded(modified_by)
			   ps.setInt(psCount++, 0);
			   ps.executeUpdate();	
			   rs= ps.getGeneratedKeys();
			   if(rs.next()){
				   paperId = rs.getInt(1);
			   }
			   System.out.println("Successfully Inserted in the ques_paper_master");
		
			//add the question info in the question_paper table
			for(QuestionMasterBean question: paperBean.getQuestionList())
			{
				psCount=1;
				  ps = conn.prepareStatement("insert into question_paper(ques_paper_id, question_id, static_text_id, sequence_no, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
				   ps.setInt(psCount++, paperId);
				   ps.setInt(psCount++, question.getQuestionId());
				   ps.setInt(psCount++, question.getStaticTextId());
				   ps.setInt(psCount++, Integer.parseInt(question.getSequenceNo()));
				   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
				   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
				   ps.setInt(psCount++, 0);
				   ps.executeUpdate();   
			}
			
			System.out.println("Successfully Inserted in the question_paper");
			
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }
		 FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Congratulations!! Paper Created Successfully with id: "+paperId));
		 return "createQuestionPaper";		
	}
	//to get the question paper list
	public List<QuestionPaperBean> getPaperList(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		paperList = new ArrayList<QuestionPaperBean>();
		QuestionPaperBean paperBean;
		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   StringBuilder sql = new StringBuilder("select ques_paper_id, template_id, ques_paper_name, q.subject_id, subject_name from ques_paper_master q join subject_master s" +
			   		" where q.subject_id = s.subject_id and q.rowstate!=-1 and s.rowstate!=-1");
			   if(this.paperId!=0){
				   sql.append(" and ques_paper_id ="+this.paperId);
			   }
			   if(this.paperName!="" && this.paperName!=null){
				   sql.append(" and ques_paper_name like %"+this.paperName+"%");
			   }
			   if(this.subjectId!=0){
				   sql.append(" and s.subject_id ="+this.subjectId);
			   }
			   if(this.templateId!=0){
				   sql.append(" and template_id ="+this.templateId);
			   }
			   ps = conn.prepareStatement(sql.toString());
			   rs = ps.executeQuery();
			   System.out.println("Query:: " + sql.toString());
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  paperBean = new QuestionPaperBean();
			    	  paperBean.setPaperId(rs.getInt("ques_paper_id"));
			    	  paperBean.setTemplateId(rs.getInt("template_id"));
			    	  paperBean.setPaperName(rs.getString("ques_paper_name"));
			    	  paperBean.setSubjectId(rs.getInt("subject_id"));
			    	  paperBean.setSubject(rs.getString("subject_name"));
			    	 System.out.println("Subject id in getTemplateList()" + paperBean.getSubjectId());
			    	 paperList.add(paperBean);
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
		return paperList;
	}

	//redirect to viewQuestionPaper.jsp
		public String viewJsp(QuestionPaperBean paperBean){
	         try {  
	               
	             FacesContext context = FacesContext.getCurrentInstance();  
	             //get the request object and set the parameters
	             HttpServletRequest request = ((HttpServletRequest)context.getExternalContext().getRequest());  
	             HttpServletResponse response  = ((HttpServletResponse)context.getExternalContext().getResponse());  
	             RequestDispatcher requestDispatcher = request.getRequestDispatcher("/viewQuestionPaper.jsp");
	             request.setAttribute("paperId", paperBean.getPaperId()+"");
	             System.out.println("Paper Id:: "+ paperBean.getPaperId());
//	             response.sendRedirect(request.getContextPath()+"/viewQuestionPaper.jsp"); 
	             requestDispatcher.forward(request, response);
	             context.responseComplete();  
	             }  
	             catch (Exception ex) {  
	                 ex.printStackTrace();  
	             }  
			return null;
		}
		//redirect to viewQuestionPaper.jsp
		public String downloadPdf(QuestionPaperBean paperBean){
					
			return null;
		} 
        //to get the question list for displaying on the jsp	
		public List<QuestionMasterBean> getQuestionList(String paperId){
			PreparedStatement ps = null, ps1 = null, ps2= null, ps3=null;
			ResultSet rs = null, rs1= null, rs2 = null, rs3=null;
			List<QuestionMasterBean> questionList = new ArrayList<QuestionMasterBean>();
			QuestionMasterBean questionBean;
			int psCount = 1;
			int questionId =0;
			int sequenceNo =0;
			int staticTextId =0;
			try{
				  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
				   String sql = "select question_id, static_text_id, sequence_no from question_paper where ques_paper_id =? and rowstate !=-1  order by sequence_no";
				   ps = conn.prepareStatement(sql); 
				   ps.setInt(psCount++, Integer.parseInt(paperId));
				   rs = ps.executeQuery();
				      //STEP 5: Extract data from result set
				      while(rs.next()){
				    	  questionBean = new QuestionMasterBean();
				         //Retrieve by column name
				    	  questionId = rs.getInt("question_id");
				    	  sequenceNo = rs.getInt("sequence_no");
				    	  staticTextId = rs.getInt("static_text_id");
				    	  if(questionId == 0){
				    		 
							  //fetch the static_text from static_text_master
								   sql = "select static_text_master_id, static_text from static_text_master where static_text_master_id =?";
								   ps2 = conn.prepareStatement(sql);
								   ps2.setInt(1,staticTextId);
								   rs2 = ps2.executeQuery();
								   if(rs2.next()){
									   questionBean.setStaticTextId(rs2.getInt("static_text_master_id"));
									   questionBean.setQuestionText(rs2.getString("static_text"));
									   questionList.add(questionBean);
								   }
				    	  }else{
				    		  //fetch the question text and marks from question_master table
				    		  sql = "select question_id, question_text, marks  from question_master where question_id =?";
							   ps3 = conn.prepareStatement(sql);
							   ps3.setInt(1,questionId);
							   rs3 = ps3.executeQuery();
							   if(rs3.next()){
								   questionBean.setQuestionId(rs3.getInt("question_id"));
								   questionBean.setQuestionText(rs3.getString("question_text"));
								   questionBean.setMarks(rs3.getInt("marks"));
								   questionList.add(questionBean);
							   }
				    	  }
				    	
				      }
				      rs.close();
				      rs2.close();
				      rs3.close();
				      System.out.println("Question List:: " + questionList.toString());
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
			

}
