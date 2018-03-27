package com.engg.questionpaper.models;

import java.util.List;

public class QuestionPaperBean {
	
	private int paperId;
	private String paperName;
	private int templateId;
	private int subjectId;
	private int totalMarks;
	private String subject;
	private List<QuestionMasterBean> questionList;
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
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public int getTotalMarks() {
		return totalMarks;
	}
	public void setTotalMarks(int totalMarks) {
		this.totalMarks = totalMarks;
	}
	public List<QuestionMasterBean> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<QuestionMasterBean> questionList) {
		this.questionList = questionList;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
