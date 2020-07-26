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

import beans.BeanFormularioIberiaPay;
import beans.BeanSheetExcelIberiaPay;

public class SendToLocalExcelIberiaPay {

	public void toExcel(Object o, BeanFormularioIberiaPay bF)
	{
		try 
		{ 
			List<BeanSheetExcelIberiaPay> lista = (List<BeanSheetExcelIberiaPay>) o;
			//String textPath = "C:\\Users\\0015305\\Documents\\IBERIA\\IberiaPay.xlsx";
			String textPath = bF.getRutaExcel().replace("\\", "\\\\");
			
			if (textPath.isEmpty()) 
			{
				VentanaPrincipalIberiaPay.showError("Por favor, indica la ruta del Excel !!!");
			}
			else
			{
				File file = new File(textPath);
				boolean fileIsNotLocked = file.renameTo(file);
				
				if (fileIsNotLocked) 
				{
					FileInputStream excelInStream = new FileInputStream(new File(textPath));
					XSSFWorkbook workbook = new XSSFWorkbook(excelInStream);
					XSSFSheet sheet = workbook.getSheet("Emisiones");
					
					CellStyle styleString = workbook.createCellStyle();
					styleString.setWrapText(true);
					styleString.setVerticalAlignment(VerticalAlignment.CENTER);
					
					CellStyle styleNumerico = workbook.createCellStyle();
					styleNumerico.setAlignment(HorizontalAlignment.RIGHT);
					styleNumerico.setVerticalAlignment(VerticalAlignment.CENTER);
					
					//This data needs to be written (Object[])
			        Map<Integer, Object[]> data = new TreeMap<Integer, Object[]>();
					
			        int numCasos = 0;
					for (int i=0; i<lista.size(); i++) {
						BeanSheetExcelIberiaPay bean = (BeanSheetExcelIberiaPay) lista.get(i);
						String impCodError = bean.getCodError()[0]!=null?bean.getCodError()[0]:"" + (bean.getCodError()[1]!=null?"\n" + bean.getCodError()[1]:"");
						String impDesError = bean.getDescripcionError()[0]!=null?bean.getDescripcionError()[0]:"" + (bean.getDescripcionError()[1]!=null?"\n" + bean.getDescripcionError()[1]:"");
						data.put(i, new Object[] {bean.getFechaLocal(), 
												  bean.getServicioNDC(),
												  bean.getCodRequest(),
								                  bean.getMercado(), 
								                  bean.getUsuario(),
								                  bean.getCodAgencia(),
								                  bean.getAgencia(),
								                  bean.getTipoPago(),
								                  bean.getPnr(),
								                  bean.getTotalPrecio(),
								                  bean.getMoneda(),
								                  bean.getMascaraTarjeta(),
								                  bean.getRazonFraude(),
								                  bean.getDecisionFraude(),
								                  bean.getTransaccion(),
								                  bean.getEstadoDelPago(),
								                  bean.getResultadoDelPago(),
								                  impCodError,
								                  impDesError
						});
					}
					
					VentanaPrincipalIberiaPay.showInfo("2) Enviando datos a: " + bF.getRutaExcel());
					
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
			               else if(obj instanceof Double) {
			            	    cell.setCellStyle(styleNumerico);
			                    cell.setCellValue((Double)obj);
			               }
			            }
			        }
			        
			        VentanaPrincipalIberiaPay.showInfo("3) A partir de la fila: " + (filaInicio+1));
           
			        FileOutputStream excelOutStream = new FileOutputStream(new File(textPath));
			        workbook.write(excelOutStream);
			        workbook.close();
		            excelInStream.close();
		            excelOutStream.close();
			          
		            VentanaPrincipalIberiaPay.showInfo("*** FIN ***");
				} 
				else 
				{
					VentanaPrincipalIberiaPay.showError("Por favor, cierra el fichero Excel !!!");
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
		}
	}
	
}
