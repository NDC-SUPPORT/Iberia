package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.MyUtil;
import com.VentanaPrincipalYerros;

import beans.BeanSheetExcel;

public class CA_Resiber_SSE_ORM_9001 {
	
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
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptResiberTktIn-desc.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					//Obtenemos tickets
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject message = parameters.getJSONObject("message");
					String payload = message.getString("payload.string");
					
					Document doc = MyUtil.convertStringToXMLDocument(payload);
					String detallesResiber = MyUtil.obtenerDetallesDeXML(doc);
					String TKTResiber = MyUtil.obtenerTKTsDeXML(doc);
					
					//Obtenemos PNR					
					JSONObject objArray_B = hitsArray.getJSONObject(hitsArray.length()-1);
					JSONObject source_B = objArray_B.getJSONObject("_source");
					JSONObject kpi_B = source_B.getJSONObject("kpi");
					JSONObject parameters_B = kpi_B.getJSONObject("parameters");
					JSONObject message_B = parameters_B.getJSONObject("message");
					String payload_B = message_B.getString("payload.string");

					
					Document doc_B = MyUtil.convertStringToXMLDocument(payload_B);
					String locator = MyUtil.obtenerLocalizadorDeXML(doc_B);

					if (detallesResiber != null) {
						bean.setTipoError("RESIBER");
						bean.setComentarios("PNR: " + bean.getPnr() + " | " + detallesResiber + "\n" + "TKTs: " + TKTResiber);
						VentanaPrincipalYerros.showInfo("CA_Resiber_SSE_ORM_9001.analizar(): " + bean.getComentarios());
					}

				}
				else 
				{
					System.out.println("CA_Resiber_SSE_ORM_9001.analizar(): Array de resultados vac�o en scriptResiberRequestTktIn.sh");
				}
	        }
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> CA_Resiber_SSE_ORM_9001.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			e.printStackTrace();
		}
	}
	
}