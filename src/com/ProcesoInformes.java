package com;
import java.util.List;

import beans.BeanFormularioInformes;
import beans.BeanSheetExcelLookToBook;
import casos.LookToBook;

public class ProcesoInformes {
	
	public void ejecutar(BeanFormularioInformes bF_Informes) 
	{
		try
		{
			MyUtil.ventanaEnEjecucion = "VentanaPrincipalInformes";
			LookToBook lookToBook = new LookToBook();
			List<BeanSheetExcelLookToBook> listaBeans = lookToBook.obtenerDatos(bF_Informes);
			
			SendToLocalExcelLookToBook enviarExcel = new SendToLocalExcelLookToBook();
			enviarExcel.toExcel(listaBeans, bF_Informes);
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalInformes.showError(e.getMessage());
		}
	}

}
