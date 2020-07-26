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

import beans.BeanFormulario;
import beans.BeanSheetExcel;

public class SendToLocalExcel {

	public void toExcel(Object o, BeanFormulario bF)
	{
		String codServ = bF.getCodServicio();
		String hoja = MyUtil.getNombrePestaniaExcel(codServ);
		boolean agrupar = bF.isRespuestaAgrupada();
		boolean total = bF.isTotal();
		List<BeanSheetExcel> lista = (List<BeanSheetExcel>) o;
		//String textPath = "C:\\Users\\0015305\\Documents\\IBERIA\\Errores_20191028-20191103.xlsx";
		String textPath = bF.getRutaExcel().replace("\\", "\\\\");
		
		FileInputStream excelInStream = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		
		try 
		{ 
			if (textPath.isEmpty()) 
			{
				VentanaPrincipal.showError("Por favor, indica la ruta del Excel");
			}
			else
			{
				File file = new File(textPath);
				boolean fileIsNotLocked = file.renameTo(file);
				
				if (fileIsNotLocked) 
				{
					VentanaPrincipal.showInfo("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
					VentanaPrincipal.showInfo("1) Preparando datos para Excel...");
					
					excelInStream = new FileInputStream(new File(textPath));
					workbook = new XSSFWorkbook(excelInStream);
					sheet = workbook.getSheet(hoja);
					
					CellStyle styleString = workbook.createCellStyle();
					styleString.setWrapText(true);
					styleString.setVerticalAlignment(VerticalAlignment.CENTER);
					
					CellStyle styleNumerico = workbook.createCellStyle();
					styleNumerico.setAlignment(HorizontalAlignment.CENTER);
					styleNumerico.setVerticalAlignment(VerticalAlignment.CENTER);
					
					//This data needs to be written (Object[])
			        Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
					
			        int numCasos = 0;
					for (int i=0; i<lista.size(); i++) {
						BeanSheetExcel bean = (BeanSheetExcel) lista.get(i);
						String impCodError = bean.getCodError()[0] + (bean.getCodError()[1]!=null?"\n" + bean.getCodError()[1]:"");
						String impDesError = bean.getDescripcionError()[0] + (bean.getDescripcionError()[1]!=null?"\n" + bean.getDescripcionError()[1]:"");
						data.put(i, new Object[] {agrupar?bean.getFechaLocal().substring(0,10):bean.getFechaLocal(), agrupar?"":bean.getCodRequest(), impCodError, impDesError, bean.getNumDeCasos(), bean.getTipoError(), bean.getComentarios()});
						numCasos = numCasos + bean.getNumDeCasos();
					}
					
			        //data.put(1, new Object[] {"ID", "NAME", "LASTNAME"});
			        //data.put(2, new Object[] {1, "Amit", "Shukla"});
			        //data.put(3, new Object[] {2, "Lokesh", "Gupta"});
			        //data.put(4, new Object[] {3, "John", "Adwards"});
			        //data.put(5, new Object[] {4, "Brian", "Schultz"});
			          
					//https://stackoverflow.com/questions/48040638/how-to-insert-a-linebreak-as-the-data-of-a-cell
					
					VentanaPrincipal.showInfo("2) Enviando datos a: " + bF.getRutaExcel());
					
			        //Iterate over data and write to sheet
			        Set<Integer> keyset = data.keySet();
			        int rownum = sheet.getLastRowNum()==0?sheet.getLastRowNum()+1:sheet.getLastRowNum()+2;
			        int filaInicio = rownum;
			        for (Integer key : keyset)
			        {
			            Row row = sheet.createRow(rownum++);
			            Object [] objArr = data.get(key);
			            int cellnum = 0;
			            for (Object obj : objArr)
			            {
			               Cell cell = row.createCell(cellnum++);
			               if(obj instanceof String) {
			            	    cell.setCellStyle(styleString);
			                    cell.setCellValue((String)obj);
			               }
			               else if(obj instanceof Integer) {
			            	    cell.setCellStyle(styleNumerico);
			                    cell.setCellValue((Integer)obj);
			               }
			            }
			        }
			        
			        VentanaPrincipal.showInfo("3) A partir de la fila: " + (numCasos==0?"N/A":filaInicio+1));
			        
			        //Actualizamos la pestaña de contadores...
			        Row rowPestaniaCero = null;
			        Cell cellPestaniaCero = null;
			        if (total) 
			        {
			        	Integer numFila    = MyUtil.getUbicacionPestaniaCero_NumFila(codServ, bF.getFecha());
			        	Integer numColumna = MyUtil.getUbicacionPestaniaCero_NumColumna(codServ, bF.getFecha());
			        	
			        	XSSFSheet sheetPestaniaCero = workbook.getSheet("0");
			        	
			        	rowPestaniaCero = sheetPestaniaCero.getRow(numFila);
			        	if (rowPestaniaCero == null) {
		        	    	rowPestaniaCero = sheetPestaniaCero.createRow(numFila);
		        	    	cellPestaniaCero = rowPestaniaCero.createCell(numColumna);
		        	    } else {
		        	    	cellPestaniaCero = rowPestaniaCero.getCell(numColumna);
		        	    	if (cellPestaniaCero == null) {
		        	    		cellPestaniaCero = rowPestaniaCero.createCell(numColumna);
		        	    	}
		        	    }
		        	    
		        	    if (cellPestaniaCero != null) {
		        	    	cellPestaniaCero.setCellStyle(styleNumerico);
		        	    	cellPestaniaCero.setCellValue(numCasos);
		        	    }
			        }
			        		            
			        FileOutputStream excelOutStream = new FileOutputStream(new File(textPath));
			        workbook.write(excelOutStream);
			        workbook.close();
		            excelInStream.close();
		            excelOutStream.close();
		            
		            VentanaPrincipal.showInfo("*** FIN ***");
				} 
				else 
				{
					VentanaPrincipal.showError("Por favor, cierra el fichero Excel !!!");
				}
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
