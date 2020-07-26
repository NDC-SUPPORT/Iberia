package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.MyUtil;

import beans.BeanSheetExcel;

public class Resiber_SSE_ORM_9007 {
	
	private BeanSheetExcel bean = null;
	private String request    = "";
	private String responseId = "";
	private String fchIni     = "";
	private String fchFin     = "";

	private void detalleCaso126() 
	{
		try
		{
			//Por FlightPrice !!! ¿Y si realiza la consulta por OfferPrice?
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberOrmError.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);

			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				//String comentarios = "\n" + "Vuelos, rutas, compañias y clase implicadas en la reserva: ";
				String comentarios = "";
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject parameters1 = parameters.getJSONObject("parameter1");
					JSONObject order = parameters1.getJSONObject("order");
					JSONArray slicesArray = order.getJSONArray("slices.list.object");
										
					for (int j=0; j<slicesArray.length(); j++)
					{
						JSONObject slice = slicesArray.getJSONObject(j);
											
						JSONArray segments = slice.getJSONArray("segments.list.object");
						JSONObject segment = segments.getJSONObject(0);
						
						//Segmento
						String segmento = segment.getString("segmentId.string");
						//Salida
						JSONObject departure = segment.getJSONObject("departure");
						String origen = departure.getString("airportCode.string");
						//Llegada
						JSONObject arrival = segment.getJSONObject("arrival");
						String destino = arrival.getString("airportCode.string");
						//Operador
						JSONObject flight = segment.getJSONObject("flight");
						JSONObject operationalCarrier = flight.getJSONObject("operationalCarrier");
						String codCompany = operationalCarrier.getString("companyCode.string");
						String descripcionCompany = operationalCarrier.getString("companyName.string");
						String operador = codCompany + " - " + descripcionCompany;
						//Clase
						JSONObject cabina = segment.getJSONObject("cabin");
						String clase = cabina.getString("rbd.string");
												
						comentarios = comentarios + "\n" + "F" + (j+1) + ": " + segmento + " " + origen + "-" + destino + " > " + operador + " & Clase " + clase;
					}

					bean.setTipoError("CODE-SHARE");
					bean.setComentarios(this.bean.getComentarios() + comentarios);
					System.out.println(this.bean.getComentarios());
				}
				else
				{
					System.out.println("Resiber_SSE_ORM_9007.analizar(): Array de resultados vacío en scriptResiberOrmError.sh");
				}
			}
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> Resiber_SSE_ORM_9007.detalleCaso126() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
		}
	}
	
	
	private void detalleCaso457() 
	{
		try
		{
			//Por FlightPrice !!! ¿Y si realiza la consulta por OfferPrice?
			String responseKibana = MyUtil.ejecutarShellScript("scriptFlightPriceResponseID.sh" + " " + this.responseId + " " + this.fchIni + " " + this.fchFin);

			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				String comentarios = "\n" + "Compañias operadoras: ";
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject dataLists = response.getJSONObject("dataLists");
					JSONObject flightSegmentList = dataLists.getJSONObject("flightSegmentList");
					JSONArray flightSegment = flightSegmentList.getJSONArray("flightSegment.list.object");
					
					for (int j=0; j<flightSegment.length(); j++)
					{
						JSONObject fsElement = flightSegment.getJSONObject(j);
						
						//Segmento
						String segmento = fsElement.getString("segmentKey.string");
						//Salida (ruta)
						JSONObject departure = fsElement.getJSONObject("departure");
						JSONObject airportCodeOrigen = departure.getJSONObject("airportCode");
						String origen = airportCodeOrigen.getString("value.string");
						//Llegada (ruta)
						JSONObject arrival = fsElement.getJSONObject("arrival");
						JSONObject airportCodeDestino = arrival.getJSONObject("airportCode");
						String destino = airportCodeDestino.getString("value.string");

						JSONObject operatingCarrier = fsElement.getJSONObject("operatingCarrier");
						JSONObject airlineID = operatingCarrier.getJSONObject("airlineID");
						JSONObject flightNumber = operatingCarrier.has("flightNumber")?operatingCarrier.getJSONObject("flightNumber"):null;
						
						comentarios = comentarios + "\n" + "F" + (j+1) + ": " + segmento + " " + origen + "-" + destino + " > " + airlineID.getString("value.string") + (flightNumber!=null?flightNumber.getString("value.string"):"") + " - " + operatingCarrier.getString("name.string");
					}

					bean.setTipoError("CODE-SHARE");
					bean.setComentarios(this.bean.getComentarios() + comentarios);
					System.out.println(this.bean.getComentarios());
				}
				else
				{
					System.out.println("Resiber_SSE_ORM_9007.analizar(): Array de resultados vacío en scriptFlightPriceResponseID.sh");
				}
			}
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> Resiber_SSE_ORM_9007.detalleCaso457() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
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
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberTktIn-desc.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
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
					
					bean.setComentarios((codResiber!=null?codResiber:"") + " - " + (descResiber!=null?descResiber:""));
					System.out.println("Resiber_SSE_ORM_9007: " + bean.getComentarios());
					
					switch(codResiber) {
					    case "126": detalleCaso126(); break;
						case "457": detalleCaso457(); break;
					}
					
				}
				else 
				{
					System.out.println("Resiber_SSE_ORM_9007.analizar(): Array de resultados vacío en scriptResiberTktIn-desc.sh");
				}
	        }
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> Resiber_SSE_ORM_9007.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
		}
	}
	
}