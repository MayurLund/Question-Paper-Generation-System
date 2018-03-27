package com.engg.questionpaper.models;

import java.io.Serializable;

public class StaticTextBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int staticTextId;
	private String staticText;
	private String sequenceNo;
	
	public int getStaticTextId() {
		return staticTextId;
	}
	public void setStaticTextId(int staticTextId) {
		this.staticTextId = staticTextId;
	}
	public String getStaticText() {
		return staticText;
	}
	public void setStaticText(String staticText) {
		this.staticText = staticText;
	}
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	
	
	
	
}
