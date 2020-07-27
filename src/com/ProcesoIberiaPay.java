package com;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import beans.BeanFormularioIberiaPay;
import beans.BeanSheetExcelIberiaPay;
import casos.Payment_IBPay;

public class ProcesoIberiaPay {
	
	private List<BeanSheetExcelIberiaPay> almacenarValores(String responseKibana)
	{
		try
		{
			List<BeanSheetExcelIberiaPay> myList = new ArrayList<BeanSheetExcelIberiaPay>();
			
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
			    //market
			    String mercado = source.getString("market");
			    //user
			    String usuario = source.getString("user");
			    
			    JSONObject kpi = source.getJSONObject("kpi");
				JSONObject parameters = kpi.getJSONObject("parameters");
				JSONObject parameter1 = parameters.getJSONObject("parameter1");
				JSONObject sender = parameter1.getJSONObject("sender");
				JSONObject paymentMethod = parameter1.getJSONObject("paymentMethod");
							    
			    //iataNumber
			    String codAgencia = sender.getString("iataNumber.string");
			    //agencyId
			    String agencia = sender.getString("agencyId.string");			    
			    //type
			    String tipoPago = paymentMethod.getString("type.string");
			    
			    //Errores... (Puede devolver varios errores...)
			    String[] cE  = new String[2]; //codigo error
			    String[] dE  = new String[2]; //descripción error
			    
			    Double totalPrecio = null;
				String moneda = "";
				String mascaraTarjeta = "";
				String pnr = "";
			    
			    if (kpi.has("response")) 
			    {
					JSONObject response = kpi.getJSONObject("response");
					JSONObject entity = response.getJSONObject("entity");
					
					if (entity.has("errors.list.object")) 
					{
						//kpi.response.entity.errors.list.object -> Errores
						JSONArray error = entity.getJSONArray("errors.list.object");
						
						for(int j=0; j<error.length(); j++)
						{
							JSONObject errorArray = error.getJSONObject(j);
							//Código de error y descripción
							cE[j] = errorArray.getString("code.string");
							dE[j] = errorArray.getString("reason.string");
							    
							//Actualmente sólo admitimos 2 valores
						    if(j==1) break;
						}
					} 
					else
					{
						//Solo es válido para los casos de IBERIA_PAY y hayan ido correctamente.
						if ("IBERIA_PAY".equals(tipoPago))
						{
							//kpi.response.entity.paymentItems.list.object -> Tarjeta + Precio
							JSONArray pilo = entity.getJSONArray("paymentItems.list.object");
							JSONObject piloCero = pilo.getJSONObject(0);
							totalPrecio = piloCero.getDouble("total.float");
							moneda = piloCero.getString("currency.string");
							mascaraTarjeta = piloCero.getString("maskedCardNumber.string");
							
							//kpi.response.entity.order.orderItems.list.object -> PNR
							JSONObject order = entity.getJSONObject("order");
							JSONArray olo = order.getJSONArray("orderItems.list.object");
							JSONObject oloUltimo = olo.getJSONObject(olo.length()-1);
							JSONArray brlo = oloUltimo.getJSONArray("bookingReferences.list.object");
							JSONObject brloCero = brlo.getJSONObject(0);
							pnr = brloCero.getString("reference.string");
						}
					}				
			    }
			    
			    BeanSheetExcelIberiaPay bSE_IBPay = new BeanSheetExcelIberiaPay(timestamp, request, mercado, usuario, codAgencia, agencia, tipoPago, cE, dE, totalPrecio, moneda, mascaraTarjeta, pnr);
			    myList.add(bSE_IBPay);
			}
			
			if (myList.size()==999) {
				VentanaPrincipalIberiaPay.showWarning("WARNING !!! - TOTAL Resquest encontradas (max 999): " + myList.size());
			} else {
				VentanaPrincipalIberiaPay.showInfo("TOTAL Resquest encontradas (max 999): " + myList.size());
			}
			
			return myList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError(e.getMessage());
			return null;
		}
	}
	
	private void analizarCasos(List<BeanSheetExcelIberiaPay> lista, BeanFormularioIberiaPay bF_IBPay)
	{
		try
		{
			for (BeanSheetExcelIberiaPay bean: lista) 
			{
				//Identificamos los casos de Iberia-Pay
				String tipoDePago = bean.getTipoPago();
				
				//Capa PMT
				switch(tipoDePago) 
				{
					case "IBERIA_PAY": 
						Payment_IBPay paymentIBPay = new Payment_IBPay();
						paymentIBPay.analizarPMT(bean);  
					break;
				}	
				
				//Capa NDC
				switch(tipoDePago) 
				{
					case "IBERIA_PAY":
					//case "TOKENIZEDCARD":
						Payment_IBPay paymentIBPay = new Payment_IBPay();
						paymentIBPay.analizarNDC(bean);  
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
	
	public void ejecutar(BeanFormularioIberiaPay bF_IBPay) 
	{
		try
		{
			boolean analizar = bF_IBPay.isAnalizar();
			String fch = bF_IBPay.getFecha();
			String hIni = bF_IBPay.getHoraInicio();
			String hFin = bF_IBPay.getHoraFin();
			
			MyUtil.ventanaEnEjecucion = "VentanaPrincipalIberiaPay";
			String responseKibana = MyUtil.ejecutarShellScript("IBPay_issueOrder.sh" + " " + fch + " " + hIni + " " + hFin);
			
			if (responseKibana != null) 
			{
				List<BeanSheetExcelIberiaPay> listaBeans = almacenarValores(responseKibana.toString());
				
				if (analizar) {
					analizarCasos(listaBeans, bF_IBPay);
				}
				
				if (listaBeans != null && !listaBeans.isEmpty()) 
				{	
					VentanaPrincipalIberiaPay.showInfo("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
					VentanaPrincipalIberiaPay.showInfo("1) Preparando datos para Excel...");
					SendToLocalExcelIberiaPay stle_IBPay = new SendToLocalExcelIberiaPay();
					stle_IBPay.toExcel(listaBeans, bF_IBPay);
				} 
				else 
				{
					VentanaPrincipalIberiaPay.showWarning("WARNING - ProcesoIberiaPay.ejecutar() : No hay resultados.");
				}
				
            } else {
            	
            }			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}

}
