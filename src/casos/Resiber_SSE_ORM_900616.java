package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipalYerros;

import beans.BeanSheetExcel;

public class Resiber_SSE_ORM_900616 {
	
	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
	
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
						bean.setTipoError("MANIPULACION EXTERNA");
						bean.setComentarios("Update: " + ciudadUpd + numAgente + " - Fecha y hora: " + fechaUpd + " " + horaUpd);
						VentanaPrincipalYerros.showInfo("Resiber_SSE_ORM_900616.analizar(): " + bean.getComentarios());
					} else {
						
					}		
				}
	        } 
		}
		catch (Exception e)	{
			e.printStackTrace();
			VentanaPrincipalYerros.showError("Resiber_SSE_ORM_900616.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}

}
