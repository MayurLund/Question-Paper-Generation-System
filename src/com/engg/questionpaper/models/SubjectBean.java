package com.engg.questionpaper.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="subjectBean")
@SessionScoped
	
public class SubjectBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int subjectId;
	private String subjectCode;
	private String subjectName;
	//private List<SubjectBean> subjectList= new ArrayList<SubjectBean>();
	
	public int getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	/*public List<SubjectBean> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<SubjectBean> subjectList) {
		this.subjectList = subjectList;
	}
*/

}
