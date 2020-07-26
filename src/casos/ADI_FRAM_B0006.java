package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipal;

import beans.BeanSheetExcel;

public class ADI_FRAM_B0006 {
	
	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
	
	private boolean esCasoMail()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptADIMail.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
							bean.setComentarios("confirmationmail -> exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}");
							VentanaPrincipal.showInfo(bean.getComentarios());
							return true;
						}
					}
				}
	        } 

			return false;
		}
		catch (Exception e)	{
			e.printStackTrace();
			VentanaPrincipal.showError("ADI_FRAM_B0006.esCasoMail() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
			return false;
		}
	}
	
	private boolean esCasoManipulacionExt()
	{
		try
		{
			String responseKibana = MyUtil.ejecutarShellScript("scriptReserve.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					JSONObject response = kpi.getJSONObject("response");
					JSONObject pnr = response.getJSONObject("pnr");
					JSONObject update = pnr.getJSONObject("update");

					String ciudadUpd = update.isNull("ciudadUpd.string")?"":update.getString("ciudadUpd.string");
					Integer numAgente = update.isNull("numAgente.int")?-1:update.getInt("numAgente.int");
					String fechaUpd = update.isNull("fechaUpd.string")?"":update.getString("fechaUpd.string");
					String horaUpd = update.isNull("horaUpd.string")?"":update.getString("horaUpd.string");
					
					if ("HDQ".equals(ciudadUpd)) {
						bean.setComentarios("Update: " + ciudadUpd + numAgente + " - Fecha y hora: " + fechaUpd + " " + horaUpd);
						System.out.println("ADI_FRAM_B0006 casoManipulacionExt: " + bean.getComentarios());
						return true;
					} else {
						return false;
					}		
				}
	        } 

			return false;
		}
		catch (Exception e)	{
			e.printStackTrace();
			VentanaPrincipal.showError("ADI_FRAM_B0006.esCasoManipulacionExt() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
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
			
			if (esCasoMail()) {
				bean.setTipoError("INTERNAL ERROR");
			} else if(esCasoManipulacionExt()) {
				bean.setTipoError("MANIPULACION EXTERNA");
			} else {
				
			}
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> ADI_FRAM_B0006.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
		}
	}

}
