package com.engg.questionpaper.models;

import java.util.List;

public class QuestionMasterBean {
	private int questionId;
	private String questionType;
	private String questionText;
	private int subjectId;
	private String subjectName;
	private int marks;
	private List<TagBean> tagList;
	private String sequenceNo;//used for questionpaper
	private int staticTextId;//used for questionpaper
	
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
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public int getStaticTextId() {
		return staticTextId;
	}
	public void setStaticTextId(int staticTextId) {
		this.staticTextId = staticTextId;
	}
}
