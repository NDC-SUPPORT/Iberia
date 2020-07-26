package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import beans.BeanFormularioInformes;
import beans.BeanSheetExcelLookToBook;

public class SendToLocalExcelLookToBook {

	public void toExcel(Object o, BeanFormularioInformes bF)
	{
		List<BeanSheetExcelLookToBook> lista = (List<BeanSheetExcelLookToBook>) o;
		String textPath = "C:\\Users\\0015305\\Documents\\IBERIA\\eclipse\\NDCErroresTecnicosNew\\LookToBook.xlsx";
		//String textPath = bF.getRutaExcel().replace("\\", "\\\\");
		
		FileInputStream excelInStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		
		try 
		{ 
			
			File file = new File(textPath);
			boolean fileIsNotLocked = file.renameTo(file);
			
			if (fileIsNotLocked) 
			{
				VentanaPrincipalInformes.showInfo("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
				VentanaPrincipalInformes.showInfo("1) Preparando datos para Excel...");
				
				excelInStream = new FileInputStream(new File(textPath));
				workbook = new XSSFWorkbook(excelInStream);
				sheet = workbook.getSheet("DispoVsBook");
				
				CellStyle styleString = workbook.createCellStyle();
				styleString.setWrapText(true);
				styleString.setVerticalAlignment(VerticalAlignment.CENTER);
				
				CellStyle styleNumerico = workbook.createCellStyle();
				styleNumerico.setAlignment(HorizontalAlignment.RIGHT);
				styleNumerico.setVerticalAlignment(VerticalAlignment.CENTER);
				
				//This data needs to be written (Object[])
		        Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
				
				for (int i=0; i<lista.size(); i++) {
					BeanSheetExcelLookToBook bean = (BeanSheetExcelLookToBook) lista.get(i);
					data.put(i, new Object[] {bean.getUser(), bean.getLook()});
				}
				
		        //data.put(1, new Object[] {"ID", "NAME", "LASTNAME"});
		        //data.put(2, new Object[] {1, "Amit", "Shukla"});
		        //data.put(3, new Object[] {2, "Lokesh", "Gupta"});
		        //data.put(4, new Object[] {3, "John", "Adwards"});
		        //data.put(5, new Object[] {4, "Brian", "Schultz"});
		          
				//https://stackoverflow.com/questions/48040638/how-to-insert-a-linebreak-as-the-data-of-a-cell
				
				VentanaPrincipalInformes.showInfo("2) Generando Excel: " + bF.getRutaExcel());
				
		        //Iterate over data and write to sheet
		        Set<Integer> keyset = data.keySet();
		        //int rownum = sheet.getLastRowNum()==0?sheet.getLastRowNum()+1:sheet.getLastRowNum()+2;
		        //int filaInicio = rownum;
		        int rownum = 2;
		        for (Integer key : keyset)
		        {
		            Row row = sheet.getRow(rownum++);
		            Object [] objArr = data.get(key);
		            int cellnum = 0;
		            for (Object obj : objArr)
		            {
		               Cell cell = row.createCell(cellnum++);
		               if(obj instanceof String) {
		            	    cell.setCellStyle(styleString);
		                    cell.setCellValue((String)obj);
		               }
		               else if(obj instanceof Long) {
		            	    cell.setCellStyle(styleNumerico);
		                    cell.setCellValue((Long)obj);
		               }
		            }
		        }
		        
		        VentanaPrincipalInformes.showInfo("3) Abriendo...");
		        		            
		        FileOutputStream excelOutStream = new FileOutputStream("TEMP\\2020_LookToBook.xlsx");
		        workbook.write(excelOutStream);
		        workbook.close();
	            excelInStream.close();
	            excelOutStream.close();
	            
	            Runtime.getRuntime().exec("cmd /c start " + "TEMP\\2020_LookToBook.xlsx");
	            
	            VentanaPrincipalInformes.showInfo("*** FIN ***");
			}			
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
		    e.printStackTrace();
		}
		finally {
			try {workbook.close();excelInStream.close();} catch (Exception e) {}
		}
	}
	
}
