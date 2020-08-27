package com;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import beans.BeanFormulario;
import beans.BeanSheetExcel;
import casos.ADI_FRAM_B0006;
import casos.CA_Resiber_SSE_ORM_9001;
import casos.CodeShareOperatingCarrier;
import casos.InternalError_PMTPPM9017_PMTORM9004;
import casos.InternalError_PMT_PPM_8006;
import casos.InternalError_PMT_PPM_8030;
import casos.ModifSimultaneas_SSE_ORM_9340;
import casos.Payment_PMT_PPM_12005;
import casos.Resiber_SSE_ORM_9001;
import casos.Resiber_SSE_ORM_900616;
import casos.Resiber_SSE_ORM_9007;
import casos.Sara_SSE_ORM_900606;
import casos.TimeOut_NDC_DIST_9009;
import casos.TimeOut_PMT_ORM_9002;
import casos.VueloCompleto_SSE_ORM_9339;

public class Proceso {
		
	private List<BeanSheetExcel> almacenarValores(String responseKibana, String codServ)
	{
		try
		{
			List<BeanSheetExcel> myList = new ArrayList<BeanSheetExcel>();
			
			JSONObject jsonResponse = new JSONObject(responseKibana.toString());
			JSONObject hitsObj = jsonResponse.getJSONObject("hits");
			JSONArray hitsArray = hitsObj.getJSONArray("hits");
						
			for (int i = 0; i < hitsArray.length(); i++) 
			{
				String tipoPago = "";
				String rId = "";
				String pnr = "";
				JSONObject objArray = hitsArray.getJSONObject(i);
				JSONObject source = objArray.getJSONObject("_source");
				
				//@timestamp 
				String fH = source.getString("@timestamp");
				//request
			    String cR = source.getString("request");
			    //version
			    Integer v = Integer.valueOf(source.getString("version"));
				
			    String[] cE  = new String[2];
			    String[] dE  = new String[2];
			    if (source.has("exception")) 
			    {
			    	JSONObject exception = source.getJSONObject("exception");
			    	//Código de error y descripción
					cE[0] = exception.getString("errorCode");
				    if (exception.has("errorDescription")) dE[0] = exception.getString("errorDescription");
			    } 
			    else 
			    {
			    	JSONObject kpi = source.getJSONObject("kpi");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject errors = response.getJSONObject("errors");
					JSONArray error = errors.getJSONArray("error.list.object");
					
					//WARNING !!! -> puede devolver varios errores...
					for(int j=0; j<error.length(); j++)
					{
						JSONObject errorArray = error.getJSONObject(j);
						//Código de error y descripción
						cE[j] = errorArray.getString("shortText.string");
					    dE[j] = errorArray.getString("value.string");
					    
					    //Actualmente sólo admitimos 2 valores
					    if(j==1) break;
					}
			    }
			    
			    if ("ADI".equals(codServ)) {
			    	JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject parameter1 = parameters.getJSONObject("parameter1");
					JSONObject query = parameter1.getJSONObject("query");
					JSONArray tdilo = query.getJSONArray("ticketDocInfo.list.object");
					JSONObject tdiloCero = tdilo.getJSONObject(0);
					JSONObject payments = tdiloCero.getJSONObject("payments");
					JSONArray plo = payments.getJSONArray("payment.list.object");
					JSONObject ploCero = plo.getJSONObject(0);
					JSONObject type = ploCero.getJSONObject("type");
					String cs = type.getString("code.string");
					if ("CA".equals(cs)) tipoPago = "CASH";
					if ("CC".equals(cs)) tipoPago = "CreditCard";
			    } 			    
			    
			    if ("OC".equals(codServ)) {
				    JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					
					if (v != 17) {
						
						JSONObject parameter1 = parameters.getJSONObject("parameter1");
						JSONObject query = parameter1.getJSONObject("query");
					
						//--- v16 ---
						if (query.has("orderItems")) { 
							JSONObject orderItems = query.getJSONObject("orderItems");
							JSONObject shoppingResponse = orderItems.getJSONObject("shoppingResponse");
							JSONObject responseID = shoppingResponse.getJSONObject("responseID");
							rId = responseID.getString("value.string");
							//Obtenemos el tipo de pago
							if (query.has("payments")) {
								JSONObject payments = query.getJSONObject("payments");
								JSONArray plo = payments.getJSONArray("payment.list.object");
								JSONObject ploCero = plo.getJSONObject(0);
								JSONObject method = ploCero.getJSONObject("method");
								if (method.has("cash")) tipoPago = "CASH";
								if (method.has("paymentCard")) tipoPago = "CreditCard";
							}
						}
					
					} else {
						
						//********************************
						// ESTO SE DEBE A QUE EN VERSIÓN 17 EL KPI NO SE ESCRIBE IGUAL EN TODAS LAS PETICIONES (¿SE ARREGLARÍA CON EL PASE?)
						//********************************
						
						JSONObject query = null;
						if (parameters.has("parameter1")) {
							//Opcion 1 - Respuesta tiene "parameter1"
							JSONObject parameter1 = parameters.getJSONObject("parameter1");
							query = parameter1.getJSONObject("query");
						} else {
							//Opcion 2 - Respuesta NO tiene "parameter1"
							query = parameters.getJSONObject("query");
						}
					
						//--- v17 ---
						if (query.has("order")) { 
							JSONObject order = query.getJSONObject("order");
							JSONArray oListObject = order.getJSONArray("offer.list.object");
							JSONObject itemOLO = oListObject.getJSONObject(0);
							rId = itemOLO.getString("responseID.string");
							//Obtenemos el tipo de pago
							if (query.has("payments")) {
								JSONObject payments = query.getJSONObject("payments");
								JSONArray plo = payments.getJSONArray("payment.list.object");
								JSONObject ploCero = plo.getJSONObject(0);
								JSONObject method = ploCero.getJSONObject("method");
								if (method.has("cash")) tipoPago = "CASH";
								if (method.has("paymentCard")) tipoPago = "CreditCard";
							}
						}
					
					}
					
			    }
			    
			    if ("CA".equals(codServ)) {
			    	JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject parameter1 = parameters.getJSONObject("parameter1");
					JSONObject query = parameter1.getJSONObject("query");
					if (query.has("bookingReferences")) {
						JSONObject bookingReferences = query.getJSONObject("bookingReferences");
						JSONArray blo = bookingReferences.getJSONArray("bookingReference.list.object");
						JSONObject bloCero = blo.getJSONObject(0);
						pnr = bloCero.getString("id.string");
					}
			    }
			    
			    BeanSheetExcel bSG = new BeanSheetExcel(fH, cR, cE, dE, rId, tipoPago, pnr, v, 1);
			    myList.add(bSG);
			}
			
			if (myList.size()==999) {
				VentanaPrincipalYerros.showWarning("WARNING !!! - TOTAL Resquest encontradas (max 999): " + myList.size());
			} else {
				VentanaPrincipalYerros.showInfo("TOTAL Resquest encontradas (max 999): " + myList.size());
				if (myList.size() == 0) {
					VentanaPrincipalYerros.showWarning("WARNING - Ejecutar(" + codServ + ") / " + MyUtil.getNombrePestaniaExcel(codServ) + ": No hay resultados.");
				}
			}			
			
			return myList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError(e.getMessage());
			return null;
		}
	}
	
	private List<BeanSheetExcel> leerTablaResumenAirShopping(String responseKibana, BeanFormulario bF)
	{
		try
		{
			List<BeanSheetExcel> myList = new ArrayList<BeanSheetExcel>();
			
			JSONObject jsonResponse = new JSONObject(responseKibana.toString());
			JSONObject aggregations = jsonResponse.getJSONObject("aggregations");
			JSONObject dataResults = aggregations.getJSONObject("dataResults");
			JSONObject buckets = dataResults.getJSONObject("buckets");
			JSONObject aggreg = buckets.getJSONObject("aggreg");
			JSONObject erroresTecnicosCodigo = aggreg.getJSONObject("erroresTecnicosCodigo");
			JSONArray bucketsArray = erroresTecnicosCodigo.getJSONArray("buckets");
						
			for (int i = 0; i < bucketsArray.length(); i++) 
			{
				String[] cE  = new String[2];
				
				JSONObject objArray = bucketsArray.getJSONObject(i);
				
				cE[0] = objArray.getString("key");
				
				JSONObject erroresTecnicosDescripcion = objArray.getJSONObject("erroresTecnicosDescripcion");
				JSONArray bucketsArrayII = erroresTecnicosDescripcion.getJSONArray("buckets");
				
				for (int j = 0; j < bucketsArrayII.length(); j++)
				{
					
				    String[] dE  = new String[2];
					JSONObject objArrayII = bucketsArrayII.getJSONObject(j);
					//Descripción - Puede tener diferentes descripciones para un mismo código debido a los diferentes idiomas
					dE[0] = objArrayII.getString("key");
					Integer numCasos = objArrayII.getInt("doc_count");

					BeanSheetExcel bSG = new BeanSheetExcel(bF.getFecha() + " 00:00:00.00000", "", cE, dE, "", "", "", null, numCasos);
					myList.add(bSG);
				}
			}
			
			return myList;
		} 
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError(e.getMessage());
			return null;
		}
	}
	
	private void agruparPorCodigoYDescripcion(List<BeanSheetExcel> lista)
	{

		for (int i=0; i<lista.size(); i++) {
			
			BeanSheetExcel beanRef = lista.get(i);
			
			String codRef  = beanRef.getCodError()[0];
			String descRef = beanRef.getDescripcionError()[0];
			
			for (int j=i+1; j<lista.size();) {
				
				BeanSheetExcel beanCom = lista.get(j);
				
				String codCom = beanCom.getCodError()[0];
				String descCom = beanCom.getDescripcionError()[0];
				
				if (codRef.equals(codCom) && descRef.equals(descCom)) {
					Integer n = beanRef.getNumDeCasos();
					beanRef.setNumDeCasos(n+1);
					lista.remove(j);
					j--;
				}
				
				j++;
			}
		}
		
		Collections.sort(lista, new Comparator<BeanSheetExcel>() {
		    @Override
		    public int compare(BeanSheetExcel o1, BeanSheetExcel o2) {
		        return o1.getCodError()[0].compareTo(o2.getCodError()[0]);
		    }
		});
	}
	
	private void analizarCasos(List<BeanSheetExcel> lista, BeanFormulario bF)
	{
		try
		{
			for (BeanSheetExcel bean: lista) 
			{
				//Analizaremos el código de error que primero se produzca... (parece que se almacenan con un PUSH)
				String codError  = bean.getCodError()[0];
				String codError2 = bean.getCodError()[1]; 
				
				switch(codError) 
				{	
					case "SSE_ORM_9340":
						ModifSimultaneas_SSE_ORM_9340 modifSimultaneas = new ModifSimultaneas_SSE_ORM_9340();
					    modifSimultaneas.analizar(bean);
					break;
				
					case "SSE_ORM_9339":
					case "SSE_ORM_9017": // -> Corresponde a Vuelo completo "Sorry, flight full". Se analiza por posibles discrepancias entre QPX y Resiber. 
						VueloCompleto_SSE_ORM_9339 vueloCompleto = new VueloCompleto_SSE_ORM_9339();
						vueloCompleto.analizar(bean);
					break;
				
				    case "PMT_PPM_8030":
				    	InternalError_PMT_PPM_8030 internalErrorPMT8030 = new InternalError_PMT_PPM_8030();
						internalErrorPMT8030.analizar(bean);
					break;
				
					case "PMT_PPM_8006":
						InternalError_PMT_PPM_8006 internalErrorPMT8006 = new InternalError_PMT_PPM_8006();
						internalErrorPMT8006.analizar(bean);
					break;
					
					case "PMT_PPM_9017":
						if ("SSE_ORM_9004".equals(codError2)) {
							InternalError_PMTPPM9017_PMTORM9004 internalErrorPMT9017_PMT9004 = new InternalError_PMTPPM9017_PMTORM9004();
							internalErrorPMT9017_PMT9004.analizar(bean);
						}
					break;
				
					case "PMT_ORM_9002":
						TimeOut_PMT_ORM_9002 timeOutPMT9002 = new TimeOut_PMT_ORM_9002();
						timeOutPMT9002.analizar(bean);
					break;	
										
					case "PMT_PPM_12005":
						Payment_PMT_PPM_12005 payNetplus = new Payment_PMT_PPM_12005();
						payNetplus.analizar(bean);
					break;
					
					case "NDC_DIST_9009":
						if ("OC".equals(bF.getCodServicio())) {
							InternalError_PMT_PPM_8006 OC_timeOutPMT_8006 = new InternalError_PMT_PPM_8006();
							OC_timeOutPMT_8006.analizar(bean);
						} else {
							TimeOut_NDC_DIST_9009 timeOutOrm = new TimeOut_NDC_DIST_9009();
							timeOutOrm.analizar(bean);
						}
				    break;
				
					case "SSE_ORM_900616": 
						Resiber_SSE_ORM_900616 manipulacionExterna = new Resiber_SSE_ORM_900616();
						manipulacionExterna.analizar(bean);
					break;
				
					case "SSE_ORM_1000601": 
						CodeShareOperatingCarrier csoc = new CodeShareOperatingCarrier();
						csoc.analizar(bean);  
					break;
					
					case "SSE_ORM_9001":
						switch(bF.getCodServicio())
						{
						   case "CA":
							   CA_Resiber_SSE_ORM_9001 oc_r9001 = new CA_Resiber_SSE_ORM_9001();
							   oc_r9001.analizar(bean);  
						   break;
						   default:
							   Resiber_SSE_ORM_9001 r9001 = new Resiber_SSE_ORM_9001();
							   r9001.analizar(bean);  
						   break;
						}
					break;
					
					case "SSE_ORM_9007": 
						Resiber_SSE_ORM_9007 r9007 = new Resiber_SSE_ORM_9007();
						r9007.analizar(bean);  
					break;
					
					case "SSE_ORM_900606":
						Sara_SSE_ORM_900606 sara = new Sara_SSE_ORM_900606();
						sara.analizar(bean, bF);
					break;
					
					//case "PMT_PPM_9017":
					//	Payment_PMT_PPM_9017 pay = new Payment_PMT_PPM_9017();
					//	pay.analizar(bean);
					//break;
				
					case "FRAM_B0006":
					{
						switch(bF.getCodServicio())
						{
						   case "ADI":
							   ADI_FRAM_B0006 adiFram = new ADI_FRAM_B0006();
							   adiFram.analizar(bean);
						   break;
						}
					}
					break;
				}
		
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
	public void ejecutar(List<BeanFormulario> listCheckServ) 
	{
		try
		{
			for (BeanFormulario beanFormulario: listCheckServ) 
			{
				String codServ = beanFormulario.getCodServicio();
				boolean agrupar = beanFormulario.isRespuestaAgrupada();
				boolean analizar = beanFormulario.isAnalizar();
				String fch = beanFormulario.getFecha();
				String hIni = beanFormulario.getHoraInicio();
				String hFin = beanFormulario.getHoraFin();
				
				String responseKibana = MyUtil.ejecutarShellScript(MyUtil.getNombreScript_AA(codServ) + " " + fch + " " + hIni + " " + hFin);
				
				if (responseKibana != null) 
				{
					//AirShopping - Tiene un script "Tabla - Buckets - Aggregation"
					if ("AS".equals(codServ)) 
					{
						List<BeanSheetExcel> listaBeansAS = leerTablaResumenAirShopping(responseKibana.toString(), beanFormulario);
						
						if (listaBeansAS != null) 
						{
							SendToLocalExcel stle = new SendToLocalExcel();
							stle.toExcel(listaBeansAS, beanFormulario);
						}
					} 
					else 
					{
						List<BeanSheetExcel> listaBeans = almacenarValores(responseKibana.toString(), codServ);

						if (agrupar) {
							agruparPorCodigoYDescripcion(listaBeans);
						} else if (analizar) {
							analizarCasos(listaBeans, beanFormulario);
						}
						
						if (listaBeans != null) 
						{
							SendToLocalExcel stle = new SendToLocalExcel();
							stle.toExcel(listaBeans, beanFormulario);
						}
					}
															
	            } else {
	            	
	            }			
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}

}
