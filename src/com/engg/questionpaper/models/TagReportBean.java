package com.engg.questionpaper.models;

public class TagReportBean {
	private int questionId;
	private String questionText;
	private int marks;
	private String subjectName;
	private String sectionValue;
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public int getMarks() {
		return marks;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getSectionValue() {
		return sectionValue;
	}
	public void setSectionValue(String sectionValue) {
		this.sectionValue = sectionValue;
	}

}
