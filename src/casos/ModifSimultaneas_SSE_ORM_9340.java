package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.MyUtil;
import com.VentanaPrincipal;

import beans.BeanSheetExcel;

public class ModifSimultaneas_SSE_ORM_9340 {

	private BeanSheetExcel bean = null;
	private String request      = "";
	private String fchIni       = "";
	private String fchFin       = "";
		
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
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberResIn-desc.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
				VentanaPrincipal.showInfo(this.bean.getComentarios());
			}
			else
			{
				VentanaPrincipal.showInfo("ModifSimultaneas_SSE_ORM_9340.analizar: Array de resultados vacío en scriptResiberResIn-desc.sh");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("ERROR !!! -> ModifSimultaneas_SSE_ORM_9340.analizar -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
		}
	}
	
}
