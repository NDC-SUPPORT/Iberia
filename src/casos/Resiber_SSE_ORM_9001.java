package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.MyUtil;
import com.VentanaPrincipal;

import beans.BeanSheetExcel;

public class Resiber_SSE_ORM_9001 {
	
	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
	private boolean isFirstErrorFound = true;

	private void detalleCaso12000039()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberTktOut.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					
					NodeList nlDocs = doc.getElementsByTagName("doc_id");
					String annadir = "";
					for(int i=0; i<nlDocs.getLength(); i++) {  
					   Node nodo = nlDocs.item(i);  
					   if (nodo != null && nodo instanceof Element) {
						   String incluir = "Doc " + (i + 1) + " : "  + nodo.getTextContent();
						   annadir += "\n" + incluir;
						   VentanaPrincipal.showInfo(incluir);
					   }  
					}
					
					this.bean.setTipoError("INCORRECT INPUT");
					this.bean.setComentarios(bean.getComentarios() + annadir);
				}
				else
				{
					VentanaPrincipal.showInfo("Resiber_SSE_ORM_9001.detalleCaso12000039(): Array de resultados vacío en scriptResiberTktOut.sh");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.detalleCaso12000039() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
	private void detalleCaso11200003()
	{
		try
		{
			this.bean.setTipoError("FUNCIONAL");
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	private void detalleCaso11000011()
	{
		try
		{
			this.bean.setTipoError("RESIBER");
			//this.bean.setComentarios(this.bean.getComentarios() + "\n" + "> INC000001265280");
			this.bean.setComentarios(this.bean.getComentarios());
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	private void detalleCaso14500001()
	{
		try
		{
			this.bean.setTipoError("RESIBER");
			this.bean.setComentarios(this.bean.getComentarios() + "\n" + "> INC000001265285");
		}
		catch (Exception e)	{
			e.printStackTrace();
		}
	}
	
	private boolean obtenerCodDesDeResiberResIn()
	{
		boolean hayError = false;
		
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberResIn-desc.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					int numError = 1;
					for (int i=0; i<hitsArray.length(); i++)
					{	
						JSONObject objArray = hitsArray.getJSONObject(i);
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject parameters = kpi.getJSONObject("parameters");
						JSONObject message = parameters.getJSONObject("message");
						String payload = message.getString("payload.string");
						
						Document doc = MyUtil.convertStringToXMLDocument(payload);
						
						NodeList nlError = doc.getElementsByTagName("error");
						Node nodoError = nlError.item(0);
						NodeList nlErr = doc.getElementsByTagName("err");
						Node nodoErr = nlErr.item(0);
						if( nodoError != null || nodoErr != null ) {
						
							String codResiber = MyUtil.obtenerCodigoDeXML(doc);
							String descResiber = MyUtil.obtenerDescripcionDeXML(doc);
							
							this.bean.setComentarios(this.bean.getComentarios() + (this.isFirstErrorFound?"":"\n") + "( "+numError+" ) " + (codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
							VentanaPrincipal.showInfo((codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
									
							this.isFirstErrorFound = false;
							hayError = true;
							numError++;
							this.bean.setTipoError("RESIBER");
						}
					}
				}
				else {
					VentanaPrincipal.showInfo("Resiber_SSE_ORM_9001.analizar(): Array de resultados vacío en scriptResiberResIn-desc.sh");
				}
			}
			
			return hayError;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.obtenerCodDesDeResiberResIn() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
			return false;
		}
		
	}
	
	public boolean obtenerCodDesDeResiberTktIn() 
	{
		boolean hayError = false;
		
		try
		{
			this.request = bean.getCodRequest();
			
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
					int numError = 1;
					for (int i=0; i<hitsArray.length(); i++)
					{
						JSONObject objArray = hitsArray.getJSONObject(i);
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject parameters = kpi.getJSONObject("parameters");
						JSONObject message = parameters.getJSONObject("message");
						String payload = message.getString("payload.string");
						
						Document doc = MyUtil.convertStringToXMLDocument(payload);
						
						NodeList nlError = doc.getElementsByTagName("error");
						Node nodoError = nlError.item(0);
						NodeList nlErr = doc.getElementsByTagName("err");
						Node nodoErr = nlErr.item(0);
						if( nodoError != null || nodoErr != null ) {
						
							String codResiber = MyUtil.obtenerCodigoDeXML(doc);
							String descResiber = MyUtil.obtenerDescripcionDeXML(doc);
							
							this.bean.setComentarios(this.bean.getComentarios() + (this.isFirstErrorFound?"":"\n") + "( "+numError+" ) " + (codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
							VentanaPrincipal.showInfo((codResiber!=null?codResiber:"null") + " - " + (descResiber!=null?descResiber:"null"));
							
							this.isFirstErrorFound = false;
							hayError = true;
							numError++;

							switch(codResiber) 
							{
						    	case "11000011": detalleCaso11000011(); break;
						    	case "11200003": detalleCaso11200003(); break;
						    	case "12000039": detalleCaso12000039(); break;
						    	case "14500001": detalleCaso14500001(); break;
							}

						}
					}
				}
				else {
					System.out.println("Resiber_SSE_ORM_9001.obtenerCodDesDeResiberTktIn(): Array de resultados vacío en scriptResiberTktIn.sh");
				}
	        }
			
			return hayError;
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.obtenerCodDesDeResiberTktIn() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
			
			return false;
		}
	}
	
	private boolean esCaso_ContensError()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptInfoSecurity.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					
					if (source.has("exception") && !source.get("exception").equals(null)) 
					{
						JSONObject exception = source.getJSONObject("exception");
	
						String stackTrace = exception.isNull("stackTrace")?"":exception.getString("stackTrace");
						String errorCode = exception.isNull("errorCode")?"":exception.getString("errorCode");
						String errorDescription = exception.isNull("errorDescription")?"":exception.getString("errorDescription");
	
						if (stackTrace.isEmpty() && errorCode.isEmpty() && errorDescription.isEmpty()) {
							//return false;
						} else {
							bean.setComentarios("exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}");
							VentanaPrincipal.showInfo(bean.getComentarios());
							//return true;
						}
					}
					
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject response = kpi.getJSONObject("response");
					
					if (response.has("entity")) {
						
						JSONObject entity = response.getJSONObject("entity");	
					
						if (entity.has("errors.list.object")) 
						{
							JSONArray errorsArray = entity.getJSONArray("errors.list.object");
							
							String comentarios = "\n" + "Errores: " + "\n";
							for(int i=0; i<errorsArray.length(); i++)
							{
								JSONObject objError = errorsArray.getJSONObject(i);
								String codigo = objError.getString("code.string");
								String descripcion = objError.getString("reason.string");		
								comentarios += codigo + " - " + descripcion;
								if ((i+1)<errorsArray.length()) comentarios += "\n";
							}
							bean.setComentarios(bean.getComentarios() + comentarios);
							return true;
						}
					}
				}
	        } 

			return false;
		}
		catch (Exception e)	{
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.esCaso_ContensError() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
			return false;
		}
	}
	
	public void analizar(BeanSheetExcel bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
						
			if (obtenerCodDesDeResiberTktIn()) {
				//OK
			} else if(obtenerCodDesDeResiberResIn()) {
				//OK
			} else if (esCaso_ContensError()) {
				//WCS CONTENTS_ERROR
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("Resiber_SSE_ORM_9001.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
}
