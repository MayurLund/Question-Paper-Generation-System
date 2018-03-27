package com.engg.questionpaper.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name="TemplateBean")
@SessionScoped
	
public class TemplateBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int templateId;
	private String templateName;
	//private List<SubjectBean> subjectList= new ArrayList<SubjectBean>();
	private int subjectId;
	private String subjectName;
	private List<StaticTextBean> staticList = new ArrayList<StaticTextBean>();
	private List<RuleBean> ruleList = new ArrayList<RuleBean>();
	
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
	
}

