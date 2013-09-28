package com.controller; 

import java.io.*;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;


public class ReadWriteXLSXFiles {

	public String path;	
	public FileInputStream fis=null;
	public FileOutputStream fileOut=null;
    
	private  XSSFWorkbook workbook=null;
	private XSSFSheet sheet = null;
	private XSSFRow row   =null;
	private XSSFCell cell = null;
	private String cellData[][];
	private String cellDataStr;
	ReadWriteXLSXFiles(String path){
			 this.path=path;
			 try{
			 fis=new FileInputStream(path);
			 workbook=new XSSFWorkbook(fis);
			 sheet=workbook.getSheetAt(0);
			 } catch (Exception e) {
				e.printStackTrace();
			 }
	}
	
	//Function to return no. of rows from the specified sheet
	public int getRowCount(String sheetName){
	
		int index=workbook.getSheetIndex(sheetName);
		if (index==-1)
		return 0;
		else {
			sheet=workbook.getSheetAt(index);
			int rowNum=sheet.getLastRowNum()+1;
			return rowNum;
		}
	}
	//*******************End of Function to return no. of rows from the specified sheet ********************
	
	// Returns the column count in a sheet
	public int getColumnCount(String sheetName){
		
		int index=workbook.getSheetIndex(sheetName);
		if (index==-1)
		return 0;
		else {
			sheet=workbook.getSheetAt(index);
			row=sheet.getRow(0);
			int colNum=row.getLastCellNum();
			return colNum;	
		}
	}
	//************************************End of returning the column count in a sheet *******************************
	// Returns all data from a sheet
	public String[][] getSheetData(String sheetName) {
		try{
		sheet=workbook.getSheet(sheetName);
		if (sheet==null){
			System.out.println("Invalid sheet has been provided");
			String nullCellData[][]=new String [0][0];
			return nullCellData;
		}
		// create a string array to store sheet data storing sheet data into string
		cellData=new String[getRowCount(sheetName)][getColumnCount(sheetName)];
		System.out.println(getRowCount(sheetName)+ "  "+getColumnCount(sheetName));
		for (int i=0;i<getRowCount(sheetName);i++)			
			for (int j=0;j<getColumnCount(sheetName);j++){
				row=sheet.getRow(i);
				if(row==null){
					System.out.println("Null Row data");
					cellData[i][j]="";
				}
				cell=row.getCell(j);
				if(cell==null){
					System.out.println("Null Cell data");
					cellData[i][j]="";
					continue;
				}
				
				if (cell.getCellType()==cell.CELL_TYPE_STRING)
				cellData[i][j]=cell.getStringCellValue();
									
				else if (cell.getCellType()==cell.CELL_TYPE_NUMERIC || cell.getCellType()==cell.CELL_TYPE_FORMULA){
					cellData[i][j]=String.valueOf(cell.getNumericCellValue());
				
					if(HSSFDateUtil.isCellDateFormatted(cell)){
						 // format in form of DD/MM/YYYY
						double d=cell.getNumericCellValue();
						Calendar cal=Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						String strYear=(String.valueOf(cal.get(Calendar.YEAR)));
						cellData[i][j]=cal.get(Calendar.DAY_OF_MONTH) + "/" +(cal.get(Calendar.MONTH)+1) +"/"+ strYear;
					}
					
				}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
					System.out.println("Blank Cell");
					cellData[i][j]="";	
				}else cellData[i][j]=String.valueOf(cell.getBooleanCellValue());
						
			}
		
		return cellData;
		
	}catch (Exception e){
		e.printStackTrace();
		String nullCellData[][]=new String [0][0];
		return nullCellData;
	}
	}
	//*********************************End of returning all data from a sheet********************************
	
	//Return specified cell data	
	@SuppressWarnings("static-access")
	public String getCellData(String sheetName,String colNum,int rowNum){
		try{
		if(rowNum <=0){
			System.out.println("Invalid Row Number has been provided");
			return "";
		}
				
		//validating sheet
		sheet=workbook.getSheet(sheetName);
		if (sheet==null){
			System.out.println("Invalid Sheet name has been provided");
			return "";
		}
		//validating column name
		int col_num=-1;
		boolean col_check=false;
		row=sheet.getRow(0);
		for(int i=0;i<getColumnCount(sheetName);i++){
			cell=row.getCell(i);			
			if(cell.getStringCellValue().equals(colNum)){
				++col_num;
				col_check=true;
				break;
			}
			++col_num;
		}
		
		if(col_check==false){
			System.out.println("Invalid column name has been provided");
			return "";
		}
		//validating row
		 row = sheet.getRow(rowNum-1);		 
		 if(row==null){
			 System.out.println("The specified Row does not exist in Excel file");
			 return "";
		 }
		 //returning the cell data
		 cell=row.getCell(col_num);
		 if(cell==null){
				System.out.println("Null Cell data");
			return "";
		}
		 
			if (cell.getCellType()==cell.CELL_TYPE_STRING){
				cellDataStr=cell.getStringCellValue();
				return cellDataStr;
			}
			else if (cell.getCellType()==cell.CELL_TYPE_NUMERIC || cell.getCellType()==cell.CELL_TYPE_FORMULA){
				cellDataStr=String.valueOf(cell.getNumericCellValue());
			
				if(HSSFDateUtil.isCellDateFormatted(cell)){
					 // format in form of DD/MM/YYYY
					double d=cell.getNumericCellValue();
					Calendar cal=Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					String strYear=(String.valueOf(cal.get(Calendar.YEAR)));
					cellDataStr=cal.get(Calendar.DAY_OF_MONTH) + "/" +(cal.get(Calendar.MONTH)+1) +"/"+ strYear;
				}
				return cellDataStr;
			}else if(cell.getCellType()==Cell.CELL_TYPE_BLANK){
				System.out.println("Blank Cell");
				return "";	
			}else cellDataStr=String.valueOf(cell.getBooleanCellValue()); 
			return cellDataStr;
		} catch (Exception e){
			e.printStackTrace();
			return "row "+rowNum+" or column "+colNum +" does not exist  in xls";
		}
	}
	//*************************End of returning specified cell data**************************************//
	
	// Set cell data into an excel
	
	public boolean setCellData(String sheetName,String colName,String TCID,String Pass_Fail){
		try{
		   fis=new FileInputStream(path);
		    sheet=workbook.getSheet(sheetName);
		    if (sheet==null){
		    	System.out.println("Sheet does not exist");
		    	return false;
		    }
		    
	   
		 //validating column name
			int col_num=-1;
			boolean col_check=false;
			row=sheet.getRow(0);
			for(int i=0;i<getColumnCount(sheetName);i++){
				cell=row.getCell(i);			
				if(cell.getStringCellValue().equals(colName)){
					++col_num;
					col_check=true;
					break;
				}
				++col_num;
			}
			
			if(col_check==false){
				System.out.println("Invalid column name has been provided to set data into excel");
				return false;
			}
		    
		    //find row number for the provided TCID
		    boolean isTCExists=false;
		   for (int row_num=1;row_num<getRowCount(sheetName);row_num++){
			   row=sheet.getRow(row_num);
			   System.out.println(row.getCell(0).getStringCellValue() + " TCID:  "+TCID);
			   if(row.getCell(0).getStringCellValue().equals(TCID)){
				   isTCExists=true;
				   System.out.println("TC found");
				   break;
			   }
		   }
		   if(!isTCExists){
			System.out.println("TCID does not exist");
			   return false;
		   }
		   //setting value in the excel
		    cell=row.getCell(col_num);
		    if (cell == null){
		        cell = row.createCell(col_num);
		        System.out.println("Creating cell");
		    }
		    //setting value
		    cell.setCellValue(Pass_Fail);
		   
		   /* XSSFCreationHelper createHelper = workbook.getCreationHelper();

		    //cell style for hyperlinks
		    //by default hypelrinks are blue and underlined
		    CellStyle hlink_style = workbook.createCellStyle();
		    XSSFFont hlink_font = workbook.createFont();
		    hlink_font.setUnderline(XSSFFont.U_SINGLE);
		    hlink_font.setColor(IndexedColors.BLUE.getIndex());
		    hlink_style.setFont(hlink_font);
		    //hlink_style.setWrapText(true);
String url="www.gmail.com";
		    XSSFHyperlink link = createHelper.createHyperlink(XSSFHyperlink.LINK_FILE);
		    link.setAddress(url);
		    cell.setHyperlink(link);
		    cell.setCellStyle(hlink_style); */
		    
		    CellStyle font_Color=workbook.createCellStyle();
		    if(Pass_Fail=="Fail"){
		    font_Color.setFillForegroundColor(IndexedColors.RED.getIndex());
		    font_Color.setFillPattern(CellStyle.SOLID_FOREGROUND);
		}else {
			font_Color.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		    font_Color.setFillPattern(CellStyle.SOLID_FOREGROUND);    
		}
		    cell.setCellStyle(font_Color);
		    
		    
		    fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			System.out.println("Closing connection after writing data to excel");
		   fileOut.close();	
		   
		// Reload the workbook, workaround for bug 49940
		   workbook=new XSSFWorkbook(fis);
			sheet=workbook.getSheet(sheetName);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
	} 
	
	} 