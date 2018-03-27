package com.engg.questionpaper.managers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.engg.questionpaper.models.QuestionMasterBean;
import com.engg.questionpaper.models.TagReportBean;

@ManagedBean(name="reportsManager")
@SessionScoped
public class ReportsManager implements Serializable {
	
	/**
	 * 
	 */
	private List<QuestionMasterBean> questionList = new ArrayList<QuestionMasterBean>();
	private String tagType;
	
	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	private static final long serialVersionUID = 1L;
	Connection conn= ConnectionManager.getConnection();
	
	public String writeTagWiseExcel()throws IOException
	{
		String tagType= getTagType();
		System.out.println("Tag Type "+ tagType);
		if(tagType.equalsIgnoreCase("section")){
			writeSectionExcel();
		}else if(tagType.equalsIgnoreCase("topic")){
			writeTopicExcel();
		}
		else if(tagType.equalsIgnoreCase("term")){
			writeTermExcel();
		}
		else if(tagType.equalsIgnoreCase("year")){
			writeYearExcel();
		}
		else if(tagType.equalsIgnoreCase("class")){
			writeClassExcel();
		}
		else if(tagType.equalsIgnoreCase("others")){
			writeOthersExcel();
		}

		return "mainPage";
	}
	
	//section Excel
	public String writeSectionExcel()throws IOException
	{
	
		//writing the download excel code		
		XSSFWorkbook workbook = new XSSFWorkbook();      
        //Create another sheet with columns
        XSSFSheet tagSheet =  workbook.createSheet("Tag Report");
        //create the first row
        Row row = tagSheet.createRow(0);
        //create the map for column names
        Map<Integer, String> columnMap = new HashMap<Integer, String>();
        columnMap.put(0, "Question Id");
        columnMap.put(1, "Question Text");
        columnMap.put(2, "Subject");
        columnMap.put(3, "Marks");
        columnMap.put(4, "Section");

        //create the column headings
        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
        	Cell cell = row.createCell(cellNum);
        	cell.setCellValue(columnMap.get(cellNum));
        }
        //fetch the distinct values of tag: section
        Map<String, List<TagReportBean>> tagValuesMap = new HashMap();
        tagValuesMap.put("section", getSectionValues());
       /* tagValuesMap.put("topic", getTopicValues());
        tagValuesMap.put("term", getTermValues());
        tagValuesMap.put("year", getYearValues());
        tagValuesMap.put("others", getOthersValues());
        tagValuesMap.put("class", getClassValues());*/
        //iterate through the values
        int rownum = 1;
        //for every tag value pair fetch the questions in the form of Object Array
       
        
        List<TagReportBean> tagReportList = tagValuesMap.get("section");
       // Object[] objArray = new Object[tagReportList.size()];
        Object[] objArray;
        Map<String, Object[]> data = new HashMap<String, Object[]>();
        int i=1;	
        for (TagReportBean tagReportBean : tagReportList) 
        {
            
        	 objArray = new Object[]{tagReportBean.getQuestionId(),tagReportBean.getQuestionText(),
        			 tagReportBean.getSubjectName(), tagReportBean.getMarks(), tagReportBean.getSectionValue()};
        	 data.put(i+"", objArray);
        	 i=i+1;
        	 
        }
        Set<String> keyset = data.keySet();
        for (String key : keyset) {
            Row dataRow = tagSheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = dataRow.createCell(cellnum++);
                if(obj instanceof Date)
                    cell.setCellValue((Date)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
                else if(obj instanceof Boolean)
                    cell.setCellValue((Boolean)obj);
                else if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Double)
                    cell.setCellValue((Double)obj);
            }
        }
        
        
        FileOutputStream fileOut;
		try {
			File questionExcel = new File("D:\\TagReportExcel.xlsx");
			fileOut = new FileOutputStream(questionExcel);
			//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
			//FileOutputStream fos = new FileOutputStream(pdfFile);
			workbook.write(fileOut);
		    //fileOut.close();
		    //download part
			 HttpServletResponse response =
				        (HttpServletResponse) FacesContext.getCurrentInstance()
				            .getExternalContext().getResponse();
			ServletOutputStream out = response.getOutputStream();
	        FileInputStream in = new FileInputStream("D:\\TagReportExcel.xlsx");

	        response.setContentType("application/vnd.ms-excel");
	        response.addHeader("content-disposition",
	                "attachment; filename=" + "TagReportExcel.xlsx");

	        int octet;
	        while((octet = in.read()) != -1)
	            out.write(octet);
	        
	        in.close();
	        out.close();
		    FacesContext.getCurrentInstance().responseComplete();
			} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return "mainPage";
	}
	//section Excel
		public String writeTopicExcel()throws IOException
		{
		
			//writing the download excel code		
			XSSFWorkbook workbook = new XSSFWorkbook();      
	        //Create another sheet with columns
	        XSSFSheet tagSheet =  workbook.createSheet("Tag Report");
	        //create the first row
	        Row row = tagSheet.createRow(0);
	        //create the map for column names
	        Map<Integer, String> columnMap = new HashMap<Integer, String>();
	        columnMap.put(0, "Question Id");
	        columnMap.put(1, "Question Text");
	        columnMap.put(2, "Subject");
	        columnMap.put(3, "Marks");
	        columnMap.put(4, "Topic");

	        //create the column headings
	        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
	        	Cell cell = row.createCell(cellNum);
	        	cell.setCellValue(columnMap.get(cellNum));
	        }
	        //fetch the distinct values of tag: section
	        Map<String, List<TagReportBean>> tagValuesMap = new HashMap();
	        tagValuesMap.put("topic", getTopicValues());
	       /* tagValuesMap.put("topic", getTopicValues());
	        tagValuesMap.put("term", getTermValues());
	        tagValuesMap.put("year", getYearValues());
	        tagValuesMap.put("others", getOthersValues());
	        tagValuesMap.put("class", getClassValues());*/
	        //iterate through the values
	        int rownum = 1;
	        //for every tag value pair fetch the questions in the form of Object Array
	       
	        
	        List<TagReportBean> tagReportList = tagValuesMap.get("topic");
	       // Object[] objArray = new Object[tagReportList.size()];
	        Object[] objArray;
	        Map<String, Object[]> data = new HashMap<String, Object[]>();
	        int i=1;	
	        for (TagReportBean tagReportBean : tagReportList) 
	        {
	            
	        	 objArray = new Object[]{tagReportBean.getQuestionId(),tagReportBean.getQuestionText(),
	        			 tagReportBean.getSubjectName(), tagReportBean.getMarks(), tagReportBean.getSectionValue()};
	        	 data.put(i+"", objArray);
	        	 i=i+1;
	        	 
	        }
	        Set<String> keyset = data.keySet();
	        for (String key : keyset) {
	            Row dataRow = tagSheet.createRow(rownum++);
	            Object [] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr) {
	                Cell cell = dataRow.createCell(cellnum++);
	                if(obj instanceof Date)
	                    cell.setCellValue((Date)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	                else if(obj instanceof Boolean)
	                    cell.setCellValue((Boolean)obj);
	                else if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Double)
	                    cell.setCellValue((Double)obj);
	            }
	        }
	        
	        
	        FileOutputStream fileOut;
			try {
				File questionExcel = new File("D:\\TagReportExcel.xlsx");
				fileOut = new FileOutputStream(questionExcel);
				//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
				//FileOutputStream fos = new FileOutputStream(pdfFile);
				workbook.write(fileOut);
			    //fileOut.close();
			    //download part
				 HttpServletResponse response =
					        (HttpServletResponse) FacesContext.getCurrentInstance()
					            .getExternalContext().getResponse();
				ServletOutputStream out = response.getOutputStream();
		        FileInputStream in = new FileInputStream("D:\\TagReportExcel.xlsx");

		        response.setContentType("application/vnd.ms-excel");
		        response.addHeader("content-disposition",
		                "attachment; filename=" + "TagReportExcel.xlsx");

		        int octet;
		        while((octet = in.read()) != -1)
		            out.write(octet);
		        
		        in.close();
		        out.close();
			    FacesContext.getCurrentInstance().responseComplete();
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return "mainPage";
		}
		//Term Excel
		public String writeTermExcel()throws IOException
		{
		
			//writing the download excel code		
			XSSFWorkbook workbook = new XSSFWorkbook();      
	        //Create another sheet with columns
	        XSSFSheet tagSheet =  workbook.createSheet("Tag Report");
	        //create the first row
	        Row row = tagSheet.createRow(0);
	        //create the map for column names
	        Map<Integer, String> columnMap = new HashMap<Integer, String>();
	        columnMap.put(0, "Question Id");
	        columnMap.put(1, "Question Text");
	        columnMap.put(2, "Subject");
	        columnMap.put(3, "Marks");
	        columnMap.put(4, "Term");

	        //create the column headings
	        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
	        	Cell cell = row.createCell(cellNum);
	        	cell.setCellValue(columnMap.get(cellNum));
	        }
	        //fetch the distinct values of tag: section
	        Map<String, List<TagReportBean>> tagValuesMap = new HashMap();
	        tagValuesMap.put("term", getTermValues());
	       /* tagValuesMap.put("topic", getTopicValues());
	        tagValuesMap.put("term", getTermValues());
	        tagValuesMap.put("year", getYearValues());
	        tagValuesMap.put("others", getOthersValues());
	        tagValuesMap.put("class", getClassValues());*/
	        //iterate through the values
	        int rownum = 1;
	        //for every tag value pair fetch the questions in the form of Object Array
	       
	        
	        List<TagReportBean> tagReportList = tagValuesMap.get("term");
	       // Object[] objArray = new Object[tagReportList.size()];
	        Object[] objArray;
	        Map<String, Object[]> data = new HashMap<String, Object[]>();
	        int i=1;	
	        for (TagReportBean tagReportBean : tagReportList) 
	        {
	            
	        	 objArray = new Object[]{tagReportBean.getQuestionId(),tagReportBean.getQuestionText(),
	        			 tagReportBean.getSubjectName(), tagReportBean.getMarks(), tagReportBean.getSectionValue()};
	        	 data.put(i+"", objArray);
	        	 i=i+1;
	        	 
	        }
	        Set<String> keyset = data.keySet();
	        for (String key : keyset) {
	            Row dataRow = tagSheet.createRow(rownum++);
	            Object [] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr) {
	                Cell cell = dataRow.createCell(cellnum++);
	                if(obj instanceof Date)
	                    cell.setCellValue((Date)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	                else if(obj instanceof Boolean)
	                    cell.setCellValue((Boolean)obj);
	                else if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Double)
	                    cell.setCellValue((Double)obj);
	            }
	        }
	        
	        
	        FileOutputStream fileOut;
			try {
				File questionExcel = new File("D:\\TagReportExcel.xlsx");
				fileOut = new FileOutputStream(questionExcel);
				//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
				//FileOutputStream fos = new FileOutputStream(pdfFile);
				workbook.write(fileOut);
			    //fileOut.close();
			    //download part
				 HttpServletResponse response =
					        (HttpServletResponse) FacesContext.getCurrentInstance()
					            .getExternalContext().getResponse();
				ServletOutputStream out = response.getOutputStream();
		        FileInputStream in = new FileInputStream("D:\\TagReportExcel.xlsx");

		        response.setContentType("application/vnd.ms-excel");
		        response.addHeader("content-disposition",
		                "attachment; filename=" + "TagReportExcel.xlsx");

		        int octet;
		        while((octet = in.read()) != -1)
		            out.write(octet);
		        
		        in.close();
		        out.close();
			    FacesContext.getCurrentInstance().responseComplete();
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return "mainPage";
		}
		//year Excel
		public String writeYearExcel()throws IOException
		{
		
			//writing the download excel code		
			XSSFWorkbook workbook = new XSSFWorkbook();      
	        //Create another sheet with columns
	        XSSFSheet tagSheet =  workbook.createSheet("Tag Report");
	        //create the first row
	        Row row = tagSheet.createRow(0);
	        //create the map for column names
	        Map<Integer, String> columnMap = new HashMap<Integer, String>();
	        columnMap.put(0, "Question Id");
	        columnMap.put(1, "Question Text");
	        columnMap.put(2, "Subject");
	        columnMap.put(3, "Marks");
	        columnMap.put(4, "Year");

	        //create the column headings
	        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
	        	Cell cell = row.createCell(cellNum);
	        	cell.setCellValue(columnMap.get(cellNum));
	        }
	        //fetch the distinct values of tag: section
	        Map<String, List<TagReportBean>> tagValuesMap = new HashMap();
	        tagValuesMap.put("year", getYearValues());
	       /* tagValuesMap.put("topic", getTopicValues());
	        tagValuesMap.put("term", getTermValues());
	        tagValuesMap.put("year", getYearValues());
	        tagValuesMap.put("others", getOthersValues());
	        tagValuesMap.put("class", getClassValues());*/
	        //iterate through the values
	        int rownum = 1;
	        //for every tag value pair fetch the questions in the form of Object Array
	       
	        
	        List<TagReportBean> tagReportList = tagValuesMap.get("year");
	       // Object[] objArray = new Object[tagReportList.size()];
	        Object[] objArray;
	        Map<String, Object[]> data = new HashMap<String, Object[]>();
	        int i=1;	
	        for (TagReportBean tagReportBean : tagReportList) 
	        {
	            
	        	 objArray = new Object[]{tagReportBean.getQuestionId(),tagReportBean.getQuestionText(),
	        			 tagReportBean.getSubjectName(), tagReportBean.getMarks(), tagReportBean.getSectionValue()};
	        	 data.put(i+"", objArray);
	        	 i=i+1;
	        	 
	        }
	        Set<String> keyset = data.keySet();
	        for (String key : keyset) {
	            Row dataRow = tagSheet.createRow(rownum++);
	            Object [] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr) {
	                Cell cell = dataRow.createCell(cellnum++);
	                if(obj instanceof Date)
	                    cell.setCellValue((Date)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	                else if(obj instanceof Boolean)
	                    cell.setCellValue((Boolean)obj);
	                else if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Double)
	                    cell.setCellValue((Double)obj);
	            }
	        }
	        
	        
	        FileOutputStream fileOut;
			try {
				File questionExcel = new File("D:\\TagReportExcel.xlsx");
				fileOut = new FileOutputStream(questionExcel);
				//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
				//FileOutputStream fos = new FileOutputStream(pdfFile);
				workbook.write(fileOut);
			    //fileOut.close();
			    //download part
				 HttpServletResponse response =
					        (HttpServletResponse) FacesContext.getCurrentInstance()
					            .getExternalContext().getResponse();
				ServletOutputStream out = response.getOutputStream();
		        FileInputStream in = new FileInputStream("D:\\TagReportExcel.xlsx");

		        response.setContentType("application/vnd.ms-excel");
		        response.addHeader("content-disposition",
		                "attachment; filename=" + "TagReportExcel.xlsx");

		        int octet;
		        while((octet = in.read()) != -1)
		            out.write(octet);
		        
		        in.close();
		        out.close();
			    FacesContext.getCurrentInstance().responseComplete();
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return "mainPage";
		}
		//class Excel
		public String writeClassExcel()throws IOException
		{
		
			//writing the download excel code		
			XSSFWorkbook workbook = new XSSFWorkbook();      
	        //Create another sheet with columns
	        XSSFSheet tagSheet =  workbook.createSheet("Tag Report");
	        //create the first row
	        Row row = tagSheet.createRow(0);
	        //create the map for column names
	        Map<Integer, String> columnMap = new HashMap<Integer, String>();
	        columnMap.put(0, "Question Id");
	        columnMap.put(1, "Question Text");
	        columnMap.put(2, "Subject");
	        columnMap.put(3, "Marks");
	        columnMap.put(4, "Class");

	        //create the column headings
	        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
	        	Cell cell = row.createCell(cellNum);
	        	cell.setCellValue(columnMap.get(cellNum));
	        }
	        //fetch the distinct values of tag: section
	        Map<String, List<TagReportBean>> tagValuesMap = new HashMap();
	        tagValuesMap.put("class", getClassValues());
	       /* tagValuesMap.put("topic", getTopicValues());
	        tagValuesMap.put("term", getTermValues());
	        tagValuesMap.put("year", getYearValues());
	        tagValuesMap.put("others", getOthersValues());
	        tagValuesMap.put("class", getClassValues());*/
	        //iterate through the values
	        int rownum = 1;
	        //for every tag value pair fetch the questions in the form of Object Array
	       
	        
	        List<TagReportBean> tagReportList = tagValuesMap.get("class");
	       // Object[] objArray = new Object[tagReportList.size()];
	        Object[] objArray;
	        Map<String, Object[]> data = new HashMap<String, Object[]>();
	        int i=1;	
	        for (TagReportBean tagReportBean : tagReportList) 
	        {
	            
	        	 objArray = new Object[]{tagReportBean.getQuestionId(),tagReportBean.getQuestionText(),
	        			 tagReportBean.getSubjectName(), tagReportBean.getMarks(), tagReportBean.getSectionValue()};
	        	 data.put(i+"", objArray);
	        	 i=i+1;
	        	 
	        }
	        Set<String> keyset = data.keySet();
	        for (String key : keyset) {
	            Row dataRow = tagSheet.createRow(rownum++);
	            Object [] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr) {
	                Cell cell = dataRow.createCell(cellnum++);
	                if(obj instanceof Date)
	                    cell.setCellValue((Date)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	                else if(obj instanceof Boolean)
	                    cell.setCellValue((Boolean)obj);
	                else if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Double)
	                    cell.setCellValue((Double)obj);
	            }
	        }
	        
	        
	        FileOutputStream fileOut;
			try {
				File questionExcel = new File("D:\\TagReportExcel.xlsx");
				fileOut = new FileOutputStream(questionExcel);
				//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
				//FileOutputStream fos = new FileOutputStream(pdfFile);
				workbook.write(fileOut);
			    //fileOut.close();
			    //download part
				 HttpServletResponse response =
					        (HttpServletResponse) FacesContext.getCurrentInstance()
					            .getExternalContext().getResponse();
				ServletOutputStream out = response.getOutputStream();
		        FileInputStream in = new FileInputStream("D:\\TagReportExcel.xlsx");

		        response.setContentType("application/vnd.ms-excel");
		        response.addHeader("content-disposition",
		                "attachment; filename=" + "TagReportExcel.xlsx");

		        int octet;
		        while((octet = in.read()) != -1)
		            out.write(octet);
		        
		        in.close();
		        out.close();
			    FacesContext.getCurrentInstance().responseComplete();
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return "mainPage";
		}
		//Others Excel
		public String writeOthersExcel()throws IOException
		{
		
			//writing the download excel code		
			XSSFWorkbook workbook = new XSSFWorkbook();      
	        //Create another sheet with columns
	        XSSFSheet tagSheet =  workbook.createSheet("Tag Report");
	        //create the first row
	        Row row = tagSheet.createRow(0);
	        //create the map for column names
	        Map<Integer, String> columnMap = new HashMap<Integer, String>();
	        columnMap.put(0, "Question Id");
	        columnMap.put(1, "Question Text");
	        columnMap.put(2, "Subject");
	        columnMap.put(3, "Marks");
	        columnMap.put(4, "Others");

	        //create the column headings
	        for(int cellNum=0; cellNum<columnMap.size(); cellNum++){
	        	Cell cell = row.createCell(cellNum);
	        	cell.setCellValue(columnMap.get(cellNum));
	        }
	        //fetch the distinct values of tag: section
	        Map<String, List<TagReportBean>> tagValuesMap = new HashMap();
	        tagValuesMap.put("others", getOthersValues());
	       /* tagValuesMap.put("topic", getTopicValues());
	        tagValuesMap.put("term", getTermValues());
	        tagValuesMap.put("year", getYearValues());
	        tagValuesMap.put("others", getOthersValues());
	        tagValuesMap.put("class", getClassValues());*/
	        //iterate through the values
	        int rownum = 1;
	        //for every tag value pair fetch the questions in the form of Object Array
	       
	        
	        List<TagReportBean> tagReportList = tagValuesMap.get("others");
	       // Object[] objArray = new Object[tagReportList.size()];
	        Object[] objArray;
	        Map<String, Object[]> data = new HashMap<String, Object[]>();
	        int i=1;	
	        for (TagReportBean tagReportBean : tagReportList) 
	        {
	            
	        	 objArray = new Object[]{tagReportBean.getQuestionId(),tagReportBean.getQuestionText(),
	        			 tagReportBean.getSubjectName(), tagReportBean.getMarks(), tagReportBean.getSectionValue()};
	        	 data.put(i+"", objArray);
	        	 i=i+1;
	        	 
	        }
	        Set<String> keyset = data.keySet();
	        for (String key : keyset) {
	            Row dataRow = tagSheet.createRow(rownum++);
	            Object [] objArr = data.get(key);
	            int cellnum = 0;
	            for (Object obj : objArr) {
	                Cell cell = dataRow.createCell(cellnum++);
	                if(obj instanceof Date)
	                    cell.setCellValue((Date)obj);
	                else if(obj instanceof Integer)
	                    cell.setCellValue((Integer)obj);
	                else if(obj instanceof Boolean)
	                    cell.setCellValue((Boolean)obj);
	                else if(obj instanceof String)
	                    cell.setCellValue((String)obj);
	                else if(obj instanceof Double)
	                    cell.setCellValue((Double)obj);
	            }
	        }
	        
	        
	        FileOutputStream fileOut;
			try {
				File questionExcel = new File("D:\\TagReportExcel.xlsx");
				fileOut = new FileOutputStream(questionExcel);
				//File pdfFile = File.createTempFile("C:\\Users\\Priya\\Desktop\\QuestionExcel.xlsx", ".xlsx");
				//FileOutputStream fos = new FileOutputStream(pdfFile);
				workbook.write(fileOut);
			    //fileOut.close();
			    //download part
				 HttpServletResponse response =
					        (HttpServletResponse) FacesContext.getCurrentInstance()
					            .getExternalContext().getResponse();
				ServletOutputStream out = response.getOutputStream();
		        FileInputStream in = new FileInputStream("D:\\TagReportExcel.xlsx");

		        response.setContentType("application/vnd.ms-excel");
		        response.addHeader("content-disposition",
		                "attachment; filename=" + "TagReportExcel.xlsx");

		        int octet;
		        while((octet = in.read()) != -1)
		            out.write(octet);
		        
		        in.close();
		        out.close();
			    FacesContext.getCurrentInstance().responseComplete();
				} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			return "mainPage";
		}
	
	public List<TagReportBean> getSectionValues(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> sectionValues = new ArrayList<String>();
		List<TagReportBean> tagReportList = new ArrayList<TagReportBean>();
		TagReportBean tagReportBean;

		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "select ques_id,  question_text, subject_name, marks, section from ques_tag_mapping t, subject_master s, question_master q " +
			   		" where t.ques_id = q.question_id and q.subject_id = s.subject_id and" +
			   		" section in(select distinct section from ques_tag_mapping) order by section";
			   ps = conn.prepareStatement(sql);
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  tagReportBean = new TagReportBean();
			    	  tagReportBean.setQuestionId(rs.getInt("ques_id"));
			    	  tagReportBean.setQuestionText(rs.getString("question_text"));
			    	  tagReportBean.setSubjectName(rs.getString("subject_name"));
			    	  tagReportBean.setMarks(rs.getInt("marks"));			    	  
			    	  tagReportBean.setSectionValue(rs.getString("section"));
			    	  tagReportList.add(tagReportBean);
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
		return tagReportList;
	}
	public List<TagReportBean> getTopicValues(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> sectionValues = new ArrayList<String>();
		List<TagReportBean> tagReportList = new ArrayList<TagReportBean>();
		TagReportBean tagReportBean;

		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "select ques_id,  question_text, subject_name, marks, topic from ques_tag_mapping t, subject_master s, question_master q " +
			   		" where t.ques_id = q.question_id and q.subject_id = s.subject_id and" +
			   		" section in(select distinct topic from ques_tag_mapping) order by topic";
			   ps = conn.prepareStatement(sql);
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  tagReportBean = new TagReportBean();
			    	  tagReportBean.setQuestionId(rs.getInt("ques_id"));
			    	  tagReportBean.setQuestionText(rs.getString("question_text"));
			    	  tagReportBean.setSubjectName(rs.getString("subject_name"));
			    	  tagReportBean.setMarks(rs.getInt("marks"));			    	  
			    	  tagReportBean.setSectionValue(rs.getString("topic"));
			    	  tagReportList.add(tagReportBean);
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
		return tagReportList;
	}
	public List<TagReportBean> getTermValues(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> sectionValues = new ArrayList<String>();
		List<TagReportBean> tagReportList = new ArrayList<TagReportBean>();
		TagReportBean tagReportBean;

		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "select ques_id,  question_text, subject_name, marks, term from ques_tag_mapping t, subject_master s, question_master q " +
			   		" where t.ques_id = q.question_id and q.subject_id = s.subject_id and" +
			   		" section in(select distinct term from ques_tag_mapping) order by term";
			   ps = conn.prepareStatement(sql);
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  tagReportBean = new TagReportBean();
			    	  tagReportBean.setQuestionId(rs.getInt("ques_id"));
			    	  tagReportBean.setQuestionText(rs.getString("question_text"));
			    	  tagReportBean.setSubjectName(rs.getString("subject_name"));
			    	  tagReportBean.setMarks(rs.getInt("marks"));			    	  
			    	  tagReportBean.setSectionValue(rs.getString("term"));
			    	  tagReportList.add(tagReportBean);
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
		return tagReportList;
	}
	public List<TagReportBean> getYearValues(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> sectionValues = new ArrayList<String>();
		List<TagReportBean> tagReportList = new ArrayList<TagReportBean>();
		TagReportBean tagReportBean;

		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "select ques_id,  question_text, subject_name, marks, year from ques_tag_mapping t, subject_master s, question_master q " +
			   		" where t.ques_id = q.question_id and q.subject_id = s.subject_id and" +
			   		" section in(select distinct year from ques_tag_mapping) order by year";
			   ps = conn.prepareStatement(sql);
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  tagReportBean = new TagReportBean();
			    	  tagReportBean.setQuestionId(rs.getInt("ques_id"));
			    	  tagReportBean.setQuestionText(rs.getString("question_text"));
			    	  tagReportBean.setSubjectName(rs.getString("subject_name"));
			    	  tagReportBean.setMarks(rs.getInt("marks"));			    	  
			    	  tagReportBean.setSectionValue(rs.getString("year"));
			    	  tagReportList.add(tagReportBean);
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
		return tagReportList;
	}
	public List<TagReportBean> getClassValues(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> sectionValues = new ArrayList<String>();
		List<TagReportBean> tagReportList = new ArrayList<TagReportBean>();
		TagReportBean tagReportBean;

		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "select ques_id,  question_text, subject_name, marks, class from ques_tag_mapping t, subject_master s, question_master q " +
			   		" where t.ques_id = q.question_id and q.subject_id = s.subject_id and" +
			   		" section in(select distinct class  from ques_tag_mapping) order by class ";
			   ps = conn.prepareStatement(sql);
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  tagReportBean = new TagReportBean();
			    	  tagReportBean.setQuestionId(rs.getInt("ques_id"));
			    	  tagReportBean.setQuestionText(rs.getString("question_text"));
			    	  tagReportBean.setSubjectName(rs.getString("subject_name"));
			    	  tagReportBean.setMarks(rs.getInt("marks"));			    	  
			    	  tagReportBean.setSectionValue(rs.getString("class"));
			    	  tagReportList.add(tagReportBean);
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
		return tagReportList;
	}
	public List<TagReportBean> getOthersValues(){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//List<String> sectionValues = new ArrayList<String>();
		List<TagReportBean> tagReportList = new ArrayList<TagReportBean>();
		TagReportBean tagReportBean;

		try{
			  // ps = conn.prepareStatement("select subject_master(question_type, question_text, subject_id, marks, created_by, created_at, modified_by, modified_at, rowstate)  values(?,?,?,?,?,now(),?,now(),?)");
			   String sql = "select ques_id,  question_text, subject_name, marks, others from ques_tag_mapping t, subject_master s, question_master q " +
			   		" where t.ques_id = q.question_id and q.subject_id = s.subject_id and" +
			   		" section in(select distinct others  from ques_tag_mapping) order by others";
			   ps = conn.prepareStatement(sql);
			   rs = ps.executeQuery();
			      //STEP 5: Extract data from result set
			      while(rs.next()){
			         //Retrieve by column name
			    	  tagReportBean = new TagReportBean();
			    	  tagReportBean.setQuestionId(rs.getInt("ques_id"));
			    	  tagReportBean.setQuestionText(rs.getString("question_text"));
			    	  tagReportBean.setSubjectName(rs.getString("subject_name"));
			    	  tagReportBean.setMarks(rs.getInt("marks"));			    	  
			    	  tagReportBean.setSectionValue(rs.getString("others"));
			    	  tagReportList.add(tagReportBean);
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
		return tagReportList;
	}
}
