package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.MyUtil;
import com.VentanaPrincipalYerros;

import beans.BeanFormulario;
import beans.BeanSheetExcel;

public class Sara_SSE_ORM_900606 
{
	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
			
	public boolean esCasoSSE() 
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptSaraSseOrm.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				String comentarios = "";
				if (hitsArray.length() != 0)
				{
					comentarios = "Resultado/s validación por pasajero:";
					for(int i=0; i<hitsArray.length(); i++)
					{
						String codSara = "";
						String descSara = "";
						
						JSONObject objArray = hitsArray.getJSONObject(i);
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject parameters = kpi.getJSONObject("parameters");
						JSONObject message = parameters.getJSONObject("message");
						String payload = message.getString("payload.string");
						
						Document doc = MyUtil.convertStringToXMLDocument(payload);
						
						NodeList nlCode = doc.getElementsByTagName("ax21:codRespuesta");
						Node nodoCode = nlCode.item(0);
						codSara = nodoCode!=null?nodoCode.getTextContent():"";
						
						NodeList nlDocs = doc.getElementsByTagName("ax21:descripcion");
						Node nodoDocs = nlDocs.item(0);
						descSara = nodoDocs!=null?nodoDocs.getTextContent():"";
						
						//Cuando no tenemos Codigo-Descripcion -> Faultcode-FaultString
						if (codSara.isEmpty() && descSara.isEmpty())
						{
							NodeList nlFaultCode = doc.getElementsByTagName("faultcode");
							Node nodoFaultCode = nlFaultCode.item(0);
							codSara = nodoFaultCode!=null?nodoFaultCode.getTextContent():"";
							
							NodeList nlFaultString= doc.getElementsByTagName("faultstring");
							Node nodoFaultString = nlFaultString.item(0);
							descSara = nodoFaultString!=null?nodoFaultString.getTextContent():"";
						}
						
						if (!codSara.isEmpty() || !descSara.isEmpty())
						{
							comentarios = comentarios + "\n" + "P"+(i+1) + ": " + codSara + " - " + descSara;
						}
						else
						{
							comentarios = "Respuesta no esperada:" + "\n" + payload;
						}
					}
					
					this.bean.setComentarios(comentarios);
					VentanaPrincipalYerros.showInfo(this.bean.getComentarios());
					return true;
				} 
				else {
					VentanaPrincipalYerros.showInfo("Sara_SSE_ORM_900606.esCasoSSE() = false");
				}
			}
			
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("Sara_SSE_ORM_900606.esCasoSSE() -> " + "fecha: " + bean.getFechaLocal() + " - request: " + bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
			return false;
		}
	}
	
	public boolean esCasoResiberOrmProvider() 
	{
		try
		{
			//String responseKibanaOut = MyUtil.ejecutarShellScript("scriptSaraResiberOrmProviderIn.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			//JSONObject jsonResponse = new JSONObject(responseKibanaOut.toString());						
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptSaraResiberOrmProviderIn.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null)
			{
				JSONObject jsonResponseIn = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponseIn.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				String comentarios = "";
				if (hitsArray.length() != 0)
				{
					comentarios = "Resultado/s validación por pasajero:";
					for(int i=0; i<hitsArray.length(); i++)
					{
						String codSara = "";
						String descSara = "";
						
						JSONObject objArray = hitsArray.getJSONObject(i);
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject parameters = kpi.getJSONObject("parameters");
						JSONObject message = parameters.getJSONObject("message");
						String payload = message.getString("payload.string");
						
						Document doc = MyUtil.convertStringToXMLDocument(payload);
						
						NodeList nlCode = doc.getElementsByTagName("ax21:codRespuesta");
						Node nodoCode = nlCode.item(0);
						codSara = nodoCode!=null?nodoCode.getTextContent():"";
						
						NodeList nlDocs = doc.getElementsByTagName("ax21:descripcion");
						Node nodoDocs = nlDocs.item(0);
						descSara = nodoDocs!=null?nodoDocs.getTextContent():"";
						
						//Cuando no tenemos Codigo-Descripcion -> Faultcode-FaultString
						if (codSara.isEmpty() && descSara.isEmpty())
						{
							NodeList nlFaultCode = doc.getElementsByTagName("faultcode");
							Node nodoFaultCode = nlFaultCode.item(0);
							codSara = nodoFaultCode!=null?nodoFaultCode.getTextContent():"";
							
							NodeList nlFaultString= doc.getElementsByTagName("faultstring");
							Node nodoFaultString = nlFaultString.item(0);
							descSara = nodoFaultString!=null?nodoFaultString.getTextContent():"";
						}
						
						if (!codSara.isEmpty() || !descSara.isEmpty())
						{
							comentarios = comentarios + "\n" + "P"+(i+1) + ": " + codSara + " - " + descSara;
						}
						else
						{
							comentarios = "Respuesta no esperada:" + "\n" + payload;
						}
					}
					
					this.bean.setComentarios(comentarios);
					VentanaPrincipalYerros.showInfo(this.bean.getComentarios());
					return true;
				} 
				else {
					VentanaPrincipalYerros.showInfo("Sara_SSE_ORM_900606.esCasoResiberOrmProvider() = false");
				}
			}
			
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("Sara_SSE_ORM_900606.esCasoResiberOrmProvider() -> " + "fecha: " + bean.getFechaLocal() + " - request: " + bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
			return false;
		}
	}
	
	private boolean esCasoAcreditacionError()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptSaraSseOrmError.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					
					if (source.has("exception")) 
					{
						JSONObject exception = source.getJSONObject("exception");
	
						String stackTrace = exception.isNull("stackTrace")?"":exception.getString("stackTrace");
						String errorCode = exception.isNull("errorCode")?"":exception.getString("errorCode");
						String errorDescription = exception.isNull("errorDescription")?"":exception.getString("errorDescription");
	
						if (stackTrace.isEmpty() && errorCode.isEmpty() && errorDescription.isEmpty()) {
							return false;
						} else {
							bean.setComentarios("exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}");
							VentanaPrincipalYerros.showInfo(bean.getComentarios());
							return true;
						}
					}
				}
	        } 

			return false;
		}
		catch (Exception e)	{
			e.printStackTrace();
			VentanaPrincipalYerros.showError("Sara_SSE_ORM_900606.esCasoAcreditacionError() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
			return false;
		}
	}
	
	public void analizar(BeanSheetExcel bean, BeanFormulario bF) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			
			if (esCasoResiberOrmProvider()) {
				bean.setTipoError("SARA");
			} else if(esCasoSSE()) {
				bean.setTipoError("SARA");
			} else if(esCasoAcreditacionError()) {
				bean.setTipoError("SARA");
			} else {
				String comentario = "WARNING !!! -> No existe causística contemplada";
				bean.setComentarios(comentario);
				VentanaPrincipalYerros.showWarning(comentario);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("Sara_SSE_ORM_900606.analizar() -> " + "fecha: " + bean.getFechaLocal() + " - request: " + bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
}
