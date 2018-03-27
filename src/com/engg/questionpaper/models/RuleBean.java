package com.engg.questionpaper.models;

import java.io.Serializable;

public class RuleBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sequenceNo;//comma-separated values
	private int noOfQuestions;
	private int selectedSubjectId;
	private int marks;
	private String ruleQuery;//that is to be executed
	private String ruleText;//that is to be displayed
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public int getNoOfQuestions() {
		return noOfQuestions;
	}
	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}
	public int getSelectedSubjectId() {
		return selectedSubjectId;
	}
	public void setSelectedSubjectId(int selectedSubjectId) {
		this.selectedSubjectId = selectedSubjectId;
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
	public String getRuleText() {
		return ruleText;
	}
	public void setRuleText(String ruleText) {
		this.ruleText = ruleText;
	}

}
