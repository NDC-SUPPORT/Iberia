package com;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import beans.BeanFormularioMailsTimeOut;
import beans.BeanSheetExcelMailsTimeOut;

public class ProcesoMailsTimeOut {
	
	private List<BeanSheetExcelMailsTimeOut> almacenarValores(String responseKibana)
	{
		try
		{
			List<BeanSheetExcelMailsTimeOut> myList = new ArrayList<BeanSheetExcelMailsTimeOut>();
			
			JSONObject jsonResponse = new JSONObject(responseKibana.toString());
			JSONObject hitsObj = jsonResponse.getJSONObject("hits");
			JSONArray hitsArray = hitsObj.getJSONArray("hits");
						
			for (int i = 0; i < hitsArray.length(); i++) 
			{
				JSONObject objArray = hitsArray.getJSONObject(i);
				JSONObject source = objArray.getJSONObject("_source");
				
				//@timestamp 
				String timestamp = source.getString("@timestamp");
				//request
			    String request = source.getString("request");
			    //user
			    String usuario = source.getString("user");
			    
			    JSONObject kpi = source.getJSONObject("kpi");
				JSONObject parameters = kpi.getJSONObject("parameters");
				JSONObject mailProviderRequest = parameters.getJSONObject("mailProviderRequest");
				JSONObject body = mailProviderRequest.getJSONObject("body");
				JSONObject salesforceRequest = body.getJSONObject("salesforceRequest");
				
				//Para obtener el PNR (+ idioma)
				String pnr = "¿?";
				JSONObject salesforceBodyRequest = salesforceRequest.getJSONObject("salesforceBodyRequest");
				String idioma = salesforceBodyRequest.getString("language.string");
				String jsonMailString = salesforceBodyRequest.getString("json_data.string");
				int comienzo = jsonMailString.indexOf("locator");
				if (comienzo != -1) {
					pnr = jsonMailString.substring(comienzo+10,comienzo+10+5);
				}
				
				//Destinatario
				JSONObject subscriberKeyRequest = salesforceRequest.getJSONObject("subscriberKeyRequest");
				String email = subscriberKeyRequest.getString("email.string");
				String firstName = "¿?";
				if (subscriberKeyRequest.has("firstName.string")) {
					firstName = subscriberKeyRequest.getString("firstName.string");
				} else {
					VentanaPrincipalMailsTimeOut.showWarning("WARNING !!! - No tiene firstName: " + request);
				}
				
				String lastName = "¿?";
				if (subscriberKeyRequest.has("lastName.string")) {
					lastName = subscriberKeyRequest.getString("lastName.string");
				} else {
					VentanaPrincipalMailsTimeOut.showWarning("WARNING !!! - No tiene lastName: " + request);
				}
				
				String nombre = firstName + " " + lastName;
			  			    
			    BeanSheetExcelMailsTimeOut bSE_Mails = new BeanSheetExcelMailsTimeOut(timestamp, request, usuario, pnr, nombre, email, idioma);
			    myList.add(bSE_Mails);
			}
			
			if (myList.size()==999) {
				VentanaPrincipalMailsTimeOut.showWarning("WARNING !!! - TOTAL Resquest encontradas (max 999): " + myList.size());
			} else {
				VentanaPrincipalMailsTimeOut.showInfo("TOTAL Resquest encontradas (max 999): " + myList.size());
			}
			
			return myList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalMailsTimeOut.showError(e.getMessage());
			return null;
		}
	}
	
	public void ejecutar(BeanFormularioMailsTimeOut bF_Mails) 
	{
		try
		{
			String fch = bF_Mails.getFecha();
			String hIni = bF_Mails.getHoraInicio();
			String hFin = bF_Mails.getHoraFin();
			
			MyUtil.ventanaEnEjecucion = "VentanaPrincipalMailsTimeOut";
			String responseKibana = MyUtil.ejecutarShellScript("Mails_TimeOut.sh" + " " + fch + " " + hIni + " " + hFin);
			
			if (responseKibana != null) 
			{
				List<BeanSheetExcelMailsTimeOut> listaBeans = almacenarValores(responseKibana.toString());

				if (listaBeans != null && !listaBeans.isEmpty()) 
				{	
					SendToLocalExcelMailsTimeOut stle_Mails = new SendToLocalExcelMailsTimeOut();
					stle_Mails.toExcel(listaBeans, bF_Mails);
				} 
				else 
				{
					VentanaPrincipalMailsTimeOut.showWarning("WARNING - ProcesoMailsTimeOut.ejecutar() : No hay resultados.");
				}
				
            } else {
            	
            }			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalMailsTimeOut.showError(e.getMessage());
		}
	}

}
