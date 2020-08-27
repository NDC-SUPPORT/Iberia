package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.MyUtil;
import com.VentanaPrincipalYerros;

import beans.BeanSheetExcel;

public class VueloCompleto_SSE_ORM_9339 {

	private BeanSheetExcel bean = null;
	private String responseId   = "";
	private String request      = "";
	private String fchIni       = "";
	private String fchFin       = "";
	
	private String horaLocal(String tS)
	{
		try
		{
			//UTC = GMT = Zulu
			TimeZone timeZone = TimeZone.getTimeZone("UTC");
			Calendar c = GregorianCalendar.getInstance(timeZone);
			//tS = YYYY-MM-DDTHH:MM:SS.SSSZ
			//-> Advertencia !!! En este campo perdemos los milisegundos
			c.set(Integer.parseInt(tS.substring(0,4)),    //Año
	              Integer.parseInt(tS.substring(5,7))-1,  //Mes
	              Integer.parseInt(tS.substring(8,10)),   //Dia
	              Integer.parseInt(tS.substring(11,13)),  //Hora
	              Integer.parseInt(tS.substring(14,16)),  //Minutos
	              Integer.parseInt(tS.substring(17,19))); //Segundos
		
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
			sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
			return sdf.format(c.getTime()).substring(0,23);
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError(e.getMessage());
			return "";
		}
	}
		
	private String obtenerRequestEnFP(String responseId)
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptFlightPriceResponseID.sh " + responseId + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
	
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					return source.getString("request");
				}
			}
			else
			{
				VentanaPrincipalYerros.showInfo("VueloCompleto_SSE_ORM_9339.obtenerRequestEnFP: Array de resultados vacío en scriptFlightPriceResponseID.sh");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("ERROR !!! -> VueloCompleto_SSE_ORM_9339.obtenerRequestEnFP -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
		
		return null;
	}
	
	private String obtenerRequestEnOP(String responseId)
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptOfferPriceResponseID.sh " + responseId + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
	
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					return source.getString("request");
				}
			}
			else
			{
				VentanaPrincipalYerros.showInfo("VueloCompleto_SSE_ORM_9339.obtenerRequestEnOP: Array de resultados vacío en scriptFlightPriceResponseID.sh");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("ERROR !!! -> VueloCompleto_SSE_ORM_9339.obtenerRequestEnOP -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
		
		return null;
	}
	
	private void obtenerErrorResiber()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberTktIn-desc.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			//Hay respuesta (correcta o no) !!!
			JSONObject jsonResponse = new JSONObject(responseKibana);
			JSONObject hitsObj = jsonResponse.getJSONObject("hits");
			JSONArray hitsArray = hitsObj.getJSONArray("hits");
			
			if (hitsArray.length() != 0) 
			{
				JSONObject objArray = hitsArray.getJSONObject(0);
				JSONObject source = objArray.getJSONObject("_source");
				JSONObject kpi = source.getJSONObject("kpi");
				JSONObject parameters = kpi.getJSONObject("parameters");
				JSONObject message = parameters.getJSONObject("message");
				String payload = message.getString("payload.string");
				
				Document doc = MyUtil.convertStringToXMLDocument(payload);
				
				String codResiber = MyUtil.obtenerCodigoDeXML(doc);
				String descResiber = MyUtil.obtenerDescripcionDeXML(doc);
				
				this.bean.setComentarios((codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
				VentanaPrincipalYerros.showInfo(this.bean.getComentarios());
			}
			else
			{
				VentanaPrincipalYerros.showInfo("VueloCompleto_SSE_ORM_9339.obtenerErrorResiber: Array de resultados vacío en scriptResiberTktIn-desc.sh.sh");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("ERROR !!! -> VueloCompleto_SSE_ORM_9339.obtenerErrorResiber -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}	
	
	public void analizar(BeanSheetExcel bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			this.responseId = bean.getResponseId();
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			
			//Informamos del cod-descripción del error de Resiber
			obtenerErrorResiber();
			
			//Obtenemos el cod-request del FlightPrice/OfferPrice para obtener los microservicios que intervienen en el FlightPrice/OfferPrice (vengo de un error en OrderCreate).
			String requestFPoOP = obtenerRequestEnFP(this.responseId);
			if (requestFPoOP == null) {requestFPoOP = obtenerRequestEnOP(this.responseId);}
			
			if (requestFPoOP!=null && !requestFPoOP.isEmpty()) 
			{
				String responseKibana = MyUtil.ejecutarShellScript("scriptGetFareCtrlInfo.sh " + requestFPoOP + " " + this.fchIni + " " + this.fchFin);
				
				if (responseKibana != null) 
				{
					String comentarios = "";
					
					JSONObject jsonResponse = new JSONObject(responseKibana);
					JSONObject hitsObj = jsonResponse.getJSONObject("hits");
					JSONArray hitsArray = hitsObj.getJSONArray("hits");
					
					if (hitsArray.length() != 0) 
					{
						JSONObject objArray = hitsArray.getJSONObject(0);
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject fareResponse = kpi.getJSONObject("fareResponse");
						JSONObject entity = fareResponse.getJSONObject("entity");
						JSONArray entityArray = entity.getJSONArray("offers.list.object");
						JSONObject objArrayII = entityArray.getJSONObject(0);
						JSONArray slicesArray = objArrayII.getJSONArray("slices.list.object");
						
						comentarios += "\n" + "FlightPrice/OfferPrice ejecutado aprox. : " + horaLocal(source.getString("logDate"));
						
						for (int j=0; j<slicesArray.length(); j++)
						{
							JSONObject objArrayIII = slicesArray.getJSONObject(j);
							JSONArray segmentsArray = objArrayIII.getJSONArray("segments.list.object");
							
							comentarios += "\n" + "Slice " + (j+1) + ":";
							for (int k=0; k<segmentsArray.length(); k++)
							{
								JSONObject segment = segmentsArray.getJSONObject(k);
								
								//Segmento
								String segmento = segment.getString("id.string");
								//Salida
								JSONObject departure = segment.getJSONObject("departure");
								String origen = departure.getString("code.string");
								//Llegada
								JSONObject arrival = segment.getJSONObject("arrival");
								String destino = arrival.getString("code.string");
								//Operador
								JSONObject flight = segment.getJSONObject("flight");
								JSONObject operationalCarrier = flight.getJSONObject("operationalCarrier");
								String codCompany = operationalCarrier.getString("code.string");
								String descripcionCompany = operationalCarrier.getString("name.string");
								String operador = codCompany + " - " + descripcionCompany;
								//Clase
								JSONObject cabina = segment.getJSONObject("cabin");
								String clase = cabina.getString("rbd.string");
								Integer asientosLibres = cabina.getInt("remainingSeats.int");
								
								comentarios += "\n" + "F" + (k+1) + ": " + segmento + " " + origen + "-" + destino + " > " + operador + " & Clase " + clase + " (" + asientosLibres + " asientos libres)";
							}
						}
						
						bean.setTipoError("");
						bean.setComentarios(this.bean.getComentarios() + comentarios);
					}
					else 
					{
						VentanaPrincipalYerros.showInfo("VueloCompleto_SSE_ORM_9339.analizar(): Array de resultados vacío en scriptGetFareCtrlInfo.sh");
					}
		        }
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("VueloCompleto_SSE_ORM_9339.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
}
