package com.engg.questionpaper.models;

import java.io.Serializable;

public class DisplayObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sequenceNo;
	private String displayText;
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	
	

}
