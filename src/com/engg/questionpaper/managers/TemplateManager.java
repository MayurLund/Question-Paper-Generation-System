package com.engg.questionpaper.managers;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.engg.questionpaper.models.DisplayObject;
import com.engg.questionpaper.models.QuestionMasterBean;
import com.engg.questionpaper.models.RuleBean;
import com.engg.questionpaper.models.StaticTextBean;
import com.engg.questionpaper.models.TemplateBean;

@ManagedBean(name="templateManager" )
@SessionScoped
public class TemplateManager implements Serializable{
	
	private TemplateBean templateBean;
	private int templateId;
	private String templateName;
	private int subjectId;
	private String subjectName;
	private List<StaticTextBean> staticList = new ArrayList<StaticTextBean>();
	private List<RuleBean> ruleList = new ArrayList<RuleBean>();
	private List<DisplayObject> displayList= new ArrayList<DisplayObject>();
	private static final long serialVersionUID = 1L;
	
	//Rule attributes
	private int noOfQuestions;
	private int selectedSubjectId;// template subject id
	private int selectedRuleSubjectId;//rule subject id
	private int marks;
	private String ruleQuery;
	private String selectedTagColumnName;
	private String tagValue;
	private String selectedOperator;
	private String selectedSign;
	private String condition;
	private Boolean ruleWrong = true;
	
	
	
	private StaticTextBean staticTextBean;
	private RuleBean ruleBean;
	private String sequenceNo ="0";
	private String staticText;
	private String ruleText;
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public List<StaticTextBean> getStaticList() {
		return staticList;
	}
	public void setStaticList(List<StaticTextBean> staticList) {
		this.staticList = staticList;
	}
	public List<RuleBean> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<RuleBean> ruleList) {
		this.ruleList = ruleList;
	}
	public List<DisplayObject> getDisplayList() {
		return displayList;
	}
	public void setDisplayList(List<DisplayObject> displayList) {
		this.displayList = displayList;
	}
	public StaticTextBean getStaticTextBean() {
		return staticTextBean;
	}
	public void setStaticTextBean(StaticTextBean staticTextBean) {
		this.staticTextBean = staticTextBean;
	}
	public RuleBean getRuleBean() {
		return ruleBean;
	}
	public void setRuleBean(RuleBean ruleBean) {
		this.ruleBean = ruleBean;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getStaticText() {
		return staticText;
	}
	public void setStaticText(String staticText) {
		this.staticText = staticText;
	}
	public String getRuleText() {
		return ruleText;
	}
	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public TemplateBean getTemplateBean() {
		return templateBean;
	}
	public void setTemplateBean(TemplateBean templateBean) {
		this.templateBean = templateBean;
	}
	public int getNoOfQuestions() {
		return noOfQuestions;
	}
	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getRuleQuery() {
		return ruleQuery;
	}
	public void setRuleQuery(String ruleQuery) {
		this.ruleQuery = ruleQuery;
	}
	public String getSelectedTagColumnName(){
		return selectedTagColumnName;
	}
	public void setSelectedTagColumnName(String selectedTagColumnName) {
		this.selectedTagColumnName = selectedTagColumnName;
	}
	public int getSelectedSubjectId() {
		return selectedSubjectId;
	}
	public void setSelectedSubjectId(int selectedSubjectId) {
		this.selectedSubjectId = selectedSubjectId;
	}
	public int getSelectedRuleSubjectId() {
		return selectedRuleSubjectId;
	}
	public void setSelectedRuleSubjectId(int selectedRuleSubjectId) {
		this.selectedRuleSubjectId = selectedRuleSubjectId;
	}
	public String getSelectedOperator() {
		return selectedOperator;
	}
	public void setSelectedOperator(String selectedOperator) {
		this.selectedOperator = selectedOperator;
	}
	
	public String getSelectedSign() {
		return selectedSign;
	}
	public void setSelectedSign(String selectedSign) {
		this.selectedSign = selectedSign;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	


	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}



	public Boolean getRuleWrong() {
		return ruleWrong;
	}
	public void setRuleWrong(Boolean ruleWrong) {
		this.ruleWrong = ruleWrong;
	}



	private List<TemplateBean> templateList = new ArrayList<TemplateBean>();
	
	private List<TemplateBean> configuredTemplateList = new ArrayList<TemplateBean>();//for configuration part
	
	Connection conn=ConnectionManager.getConnection();
	LoginManager loginManager = new LoginManager();
	//to get the template list
				public List<TemplateBean> getTemplateList(){
					PreparedStatement ps = null;
					ResultSet rs = null;
					templateList = new ArrayList<TemplateBean>();
					TemplateBean templateBean;
					try{
						  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
						StringBuilder sql = new StringBuilder("select template_id, template_name, q.subject_id, subject_name from template_master q join subject_master s" +
			   		   " where q.subject_id = s.subject_id and q.rowstate!=-1 and s.rowstate!=-1");
						   ps = conn.prepareStatement(sql.toString());
						   if(this.templateId!=0){
							   sql.append(" and ques_paper_id ="+this.templateId);
						   }
						   if(this.templateName!=""){
							   sql.append(" and template_name like %"+this.templateName+"%");
						   }
						   if(this.subjectId!=0){
							   sql.append(" and s.subject_id ="+this.subjectId);
						   }
						   rs = ps.executeQuery();
						      //STEP 5: Extract data from result set
						      while(rs.next()){
						         //Retrieve by column name
						    	 templateBean = new TemplateBean();
						    	 templateBean.setTemplateId(rs.getInt("template_id"));
						    	 templateBean.setTemplateName(rs.getString("template_name"));
						    	 templateBean.setSubjectId(rs.getInt("subject_id"));
						    	 templateBean.setSubjectName(rs.getString("subject_name"));
						    	 System.out.println("Subject id in getTemplateList()" + templateBean.getSubjectId());
						    	 templateList.add(templateBean);
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
					return templateList;
				}
				
				
				
				public void setConfiguredTemplateList(List<TemplateBean> configuredTemplateList) {
					this.configuredTemplateList = configuredTemplateList;
				}
				public List<TemplateBean> getConfiguredTemplateList(){
					PreparedStatement ps = null;
					ResultSet rs = null;
					templateList = new ArrayList<TemplateBean>();
					TemplateBean templateBean;
					try{
						  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
						   String sql = "select template_id, template_name, subject_id from template_master where rowstate !=-1";
						   ps = conn.prepareStatement(sql);
						   rs = ps.executeQuery(sql);
						      //STEP 5: Extract data from result set
						      while(rs.next()){
						         //Retrieve by column name
						    	 templateBean = new TemplateBean();
						    	 templateBean.setTemplateId(rs.getInt("template_id"));
						    	 templateBean.setTemplateName(rs.getString("template_name"));
						    	 templateBean.setSubjectId(rs.getInt("subject_id"));
						    	 System.out.println("Subject id in getTemplateList()" + templateBean.getSubjectId());
						    	 templateList.add(templateBean);
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
					return templateList;
				}
				//to get the configured template list
				/*public List<TemplateBean> getConfiguredTemplateList(){
					PreparedStatement ps, ps1,ps2, ps3, ps4 = null;
					ResultSet rs, rs1, rs2, s,rs3, rs4 = null;
					templateList = new ArrayList<TemplateBean>();
					TemplateBean templateBean;
					int psCount=1; 
					int userId = loginManager.getUserId();
					int configurationId=0;
					try{
						//validations to be performed before displaying the template list
						int subjectId = this.getSubjectId();
						int noOfGeneratesAllowed, noOfGenerated =0;
						Date startDate, endDate;
						System.out.println("Subject Id:: " + subjectId);
						//fetch the configuration id through subject_id
						 String sql = "select configuration_id from qb_configuration where subject_id=? and rowstate !=-1";
						   ps1 = conn.prepareStatement(sql);
						   ps1.setInt(psCount++, subjectId);
						   rs1 = ps1.executeQuery(sql);
						      //STEP 5: Extract data from result set
						      if(rs1.next()){
						    	  sql = "	select configuration_id from qb_configuration_users where user_id = ? and configuration_id in" +
						    	  		"(select configuration_id from qb_configuration where subject_id=?)";
								   ps2 = conn.prepareStatement(sql);
								   ps2.setInt(psCount++, userId);
								   ps2.setInt(psCount++, subjectId);
								   rs2 = ps2.executeQuery();
								   if(rs2.next()){
									   configurationId = rs2.getInt(1);
									   psCount=1;
									   sql = "	select no_of_generates_allowed, no_of_generated from qb_configuration c join qb_configuration_users u on " +
									   		"c.configuration_id = u.configuration_id and user_id =? and c.configuration_id=?";
										   ps3 = conn.prepareStatement(sql);
										   ps3.setInt(psCount++, userId);
										   ps3.setInt(psCount++, configurationId);
										   rs3 = ps3.executeQuery();
										   if(rs3.next()){
											   noOfGeneratesAllowed=rs3.getInt(1);
											   noOfGenerated = rs3.getInt(2);
											   psCount=1;
											   if(noOfGenerated< noOfGeneratesAllowed)
											   //fetch the dates
											   {
												   sql = "select start_date, end_date from qb_configuration where configuration_id =?";
												   ps4 = conn.prepareStatement(sql);
												   ps4.setInt(psCount++, configurationId);
												   rs4 = ps4.executeQuery(sql); 
												    if(rs4.next()){
												    	//write logic of comparing the dates
												    	//if current date doesn't fall in the range, throw the error
												    	  sql = "select template_id, template_name, subject_id from template_master where rowstate !=-1";
														   ps = conn.prepareStatement(sql);
														   rs = ps.executeQuery(sql);
														      //STEP 5: Extract data from result set
														      while(rs.next()){
														         //Retrieve by column name
														    	 templateBean = new TemplateBean();
														    	 templateBean.setTemplateId(rs.getInt("template_id"));
														    	 templateBean.setTemplateName(rs.getString("template_name"));
														    	 templateBean.setSubjectId(rs.getInt("subject_id"));
														    	 System.out.println("Subject id in getTemplateList()" + templateBean.getSubjectId());
														    	 templateList.add(templateBean);
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

*/
											/*	    else{
												    	FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Sorry!! The generation date is over"));
												    }
											   }else
												   FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Sorry!! The allowed limit of generating question paper is over"));
											   }
											   
										   }
										   
									   
								   }else{
									   FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Sorry!! No Template is configured for you for this subject"));
								   }
								
						      }else{
						    	  FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Sorry!! No Template is configured for this subject"));
						      }
						
						 
				}*/
	
				
				public TemplateBean getTemplateInfo(int templateId){
					PreparedStatement ps = null;
					ResultSet rs = null;
					TemplateBean templateBean = new TemplateBean();
					StaticTextBean staticBean;
					RuleBean ruleBean;
					List<StaticTextBean> staticList = new ArrayList<StaticTextBean>();
					List<RuleBean> ruleList = new ArrayList<RuleBean>();
					int psCount =1;
					try{
						  //fetch the template name
						   String sql = "select template_name, subject_id from template_master where template_id = ?";
						   ps = conn.prepareStatement(sql);
						   ps.setInt(psCount++, templateId);
						   rs = ps.executeQuery();
						   //STEP 5: Extract data from result set
						    if(rs.next()){
						         //Retrieve by column name
						    	 templateBean.setTemplateName(rs.getString("template_name"));
						    	 templateBean.setSubjectId(rs.getInt("subject_id"));
						      
						      }
						    //fetch the static text info
						    psCount =1;
						    sql = "select static_text, sequence_no, static_text_master_id from static_text_master where rowstate !=-1 and template_id =?";
							   ps = conn.prepareStatement(sql);
							   ps.setInt(psCount++, templateId);
							   rs = ps.executeQuery();
							   //STEP 5: Extract data from result set
							    while(rs.next()){
							         //Retrieve by column name
							    	staticBean = new StaticTextBean();
							    	staticBean.setSequenceNo(rs.getString("sequence_no"));
							    	staticBean.setStaticText(rs.getString("static_text"));	
							    	staticBean.setStaticTextId(rs.getInt("static_text_master_id"));
							    	staticList.add(staticBean);
							      }
							   templateBean.setStaticList(staticList);
							 //fetch the rule info
							   psCount =1;
							    sql = "select rule_query, sequence_no from rule_master where rowstate !=-1 and template_id =?";
								   ps = conn.prepareStatement(sql);
								   ps.setInt(psCount++, templateId);
								   rs = ps.executeQuery();
								   //STEP 5: Extract data from result set
								    while(rs.next()){
								         //Retrieve by column name
								    	ruleBean = new RuleBean();
								    	ruleBean.setSequenceNo(rs.getString("sequence_no"));
								    	ruleBean.setRuleQuery(rs.getString("rule_query"));							    	
								    	ruleList.add(ruleBean);
								      }
								   templateBean.setRuleList(ruleList);
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
					return templateBean;
				}
				/*public List<SubjectBean> getSubjectList() {
					return subjectList;
				}*/
				public void setTemplateList(List<TemplateBean > templateList) {
					this.templateList = templateList;
				}
				
				//pseudocode for trial
				public String addStaticText(){
					System.out.println("Inside addStatcText");
					StaticTextBean staticBean = new StaticTextBean();
					sequenceNo= Integer.parseInt(sequenceNo)+ 1 +"";
					staticBean.setSequenceNo(sequenceNo);
					staticBean.setStaticText(staticText);
					staticList.add(staticBean);
					//Add the static bean in the display list
					DisplayObject dispObj = new DisplayObject();
					dispObj.setDisplayText(staticBean.getStaticText());
					dispObj.setSequenceNo(staticBean.getSequenceNo());
					displayList.add(dispObj);
					System.out.println("End of addStatcText");
					
					return null;
				}
				public String addCondition(){
					System.out.println("Inside addCondition");
				    //String conditionString = "";
				    if(getCondition()==""){
				    	setCondition(getSelectedTagColumnName() + " "+ getSelectedSign() +" '"+ getTagValue()+"' ");
				    }else{
				    	setCondition(getCondition()+" "+getSelectedOperator()+" "+ getSelectedTagColumnName() + " "+ getSelectedSign() +" '"+ getTagValue()+"' " );
				    }
					System.out.println("End of addCondition ::" + getCondition());
					
					return getCondition();
				}
				public String addRule(){
					System.out.println("Inside addRule");
				    //write the logic for executing the rule
					
					RuleBean ruleBean = new RuleBean();
					//arrange the questions according to sequence
					String sequenceList="";
					for(int i =1; i<= getNoOfQuestions(); i++){
						sequenceNo= Integer.parseInt(sequenceNo)+ 1 +"";
						if(sequenceList==""){
							
							sequenceList = sequenceNo;
						}else{
							sequenceList = sequenceList + ", "+  sequenceNo;
						}
					}
					ruleBean.setSequenceNo(sequenceList);
					ruleBean.setMarks(getMarks());
					ruleBean.setNoOfQuestions(getNoOfQuestions());
					ruleBean.setSelectedSubjectId(getSelectedRuleSubjectId());
					setRuleText("Select "+ getNoOfQuestions()+" with Subject "+ getSelectedRuleSubjectId()+" having Marks "+getMarks()+" And Tags "+ getCondition() );
					ruleBean.setRuleText(getRuleText());
					//form the actual query
					ruleBean.setRuleQuery("select * from question_master q join ques_tag_mapping t"+ 
					" where q.question_id = t.ques_id and subject_id ="+getSelectedRuleSubjectId()+" and marks= "+getMarks()+" and "+getCondition() +" limit "+ getNoOfQuestions());
					System.out.println("Actual Query:: "+ getRuleQuery());
					//setRuleQuery("Select "+ getNoOfQuestions()+" with Subject "+ getSelectedRuleSubjectId()+" having Marks "+getMarks()+" And Tags "+ getCondition());
					ruleList.add(ruleBean);
					//Add the rule bean in the display list
					DisplayObject dispObj = new DisplayObject();
					dispObj.setDisplayText(ruleBean.getRuleText());
					dispObj.setSequenceNo(ruleBean.getSequenceNo());
					displayList.add(dispObj);
					//disable the addRule button
					setRuleWrong(true);
					
					System.out.println("End of addRule");
					
					return null;
				}
				public String checkRule(){
					System.out.println("Inside checkRule");
					//form the query and execute it
					PreparedStatement ps = null;
					ResultSet rs = null;
				    String ruleQuery ="select * from question_master q join ques_tag_mapping t"+ 
					" where q.question_id = t.ques_id and subject_id ="+getSelectedRuleSubjectId()+" and marks= "+getMarks()+" and "+getCondition() +" limit "+ getNoOfQuestions();
				    try {
						ps = conn.prepareStatement(ruleQuery);
						rs = ps.executeQuery(ruleQuery);
						if(rs.next()){
							setRuleWrong(false);  
					    }
					} catch (SQLException e) {
						 FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Please check the syntax of the rule."));
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					      //STEP 5: Extract data from result set
					    
					System.out.println("End of checkRule");
					//if rule is correct, set addRule to true
					
					
					return null;
				} 
				//to get the question list based on rule query
				public List<QuestionMasterBean> fetchQuestions(RuleBean ruleBean){
					List<QuestionMasterBean> questionList = new ArrayList<QuestionMasterBean>();
					QuestionMasterBean questionBean;
					System.out.println("Inside checkRule");
					//form the query and execute it
					PreparedStatement ps = null;
					ResultSet rs = null;
				    String ruleQuery = ruleBean.getRuleQuery();
				    String[] sequenceArray = ruleBean.getSequenceNo().split(",");
				    try {
						ps = conn.prepareStatement(ruleQuery);
						rs = ps.executeQuery(ruleQuery);
					    for(int i=0; rs.next(); i++){
					    	questionBean = new QuestionMasterBean();
					    	questionBean.setQuestionId(rs.getInt("question_id"));
					    	questionBean.setMarks(rs.getInt("marks"));
					    	questionBean.setQuestionText(rs.getString("question_text"));
					    	questionBean.setSequenceNo(sequenceArray[i]);
					    	//set the static text id to zero
					    	questionBean.setStaticTextId(0);
					    	System.out.println("Sequence of Question "+ i+ "is "+ questionBean.getSequenceNo());
							questionList.add(questionBean);						  
					    }
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					      //STEP 5: Extract data from result set
					    
					System.out.println("End of fetch Questions");
					//if rule is correct, set addRule to true
					
					
					return questionList;
				}
				
				//to add the template
				public String addTemplate(){
					System.out.println("Inside addTemplate");
					int templateId = 0;
					PreparedStatement ps = null;
					ResultSet rs = null;
					int psCount=1;
					templateBean = new TemplateBean();
					System.out.println("Template NAme "+ getTemplateName());
					templateBean.setTemplateName(getTemplateName());
					templateBean.setSubjectId(getSelectedSubjectId());
					templateBean.setStaticList(getStaticList());
					templateBean.setRuleList(getRuleList());
					try{
						//add the template
						   ps = conn.prepareStatement("insert into template_master(template_name, subject_id, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,now(),?,now(),?)", Statement.RETURN_GENERATED_KEYS);
						   ps.setString(psCount++, templateBean.getTemplateName());
						   ps.setInt(psCount++, templateBean.getSubjectId());
						   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
						   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
						   ps.setInt(psCount++, 0);
						   ps.executeUpdate();	
						   rs= ps.getGeneratedKeys();
						   if(rs.next()){
							   templateId = rs.getInt(1);
						   }
					
						//add the static text
						for(StaticTextBean staticBean: templateBean.getStaticList())
						{
							psCount=1;
							  ps = conn.prepareStatement("insert into static_text_master(static_text, sequence_no, template_id, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,now(),?,now(),?)");
							   ps.setString(psCount++, staticBean.getStaticText());
							   ps.setString(psCount++, staticBean.getSequenceNo());
							   ps.setInt(psCount++, templateId);
							   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
							   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
							   ps.setInt(psCount++, 0);
							   ps.executeUpdate();   
						}
						
						//add the rule
						for(RuleBean ruleBean: templateBean.getRuleList())
						{
							psCount=1;
							  ps = conn.prepareStatement("insert into rule_master(rule_text, rule_query, sequence_no, template_id, no_of_questions, marks, subject_id, created_by, created_at, modified_by, modified_at, rowstate)  " +
							  		"values(?,?,?,?,?,?,?,?,now(),?,now(),?)");
							   ps.setString(psCount++, ruleBean.getRuleText());
							   ps.setString(psCount++, ruleBean.getRuleQuery());
							   ps.setString(psCount++, ruleBean.getSequenceNo());
							   ps.setInt(psCount++, templateId);
							   ps.setInt(psCount++, ruleBean.getNoOfQuestions());
							   ps.setInt(psCount++, ruleBean.getMarks());
							   ps.setInt(psCount++, ruleBean.getSelectedSubjectId());
							   ps.setString(psCount++,"Admin");//user id hardcoded(created_by)
							   ps.setString(psCount++,"Admin");//user id hardcoded(modified_by)
							   ps.setInt(psCount++, 0);
							   ps.executeUpdate();   
						}			
						psCount=1;
						
					   }catch(SQLException se){
					      //Handle errors for JDBC
					      se.printStackTrace();
					   }catch(Exception e){
					      //Handle errors for Class.forName
					      e.printStackTrace();
					   }
					 FacesContext.getCurrentInstance().addMessage(null, new  FacesMessage("Congratulations!! Template Created Successfully with id: "+templateId));
					 return "mainPage"; 
				}
			
				
				
				
}
