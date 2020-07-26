package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;

import beans.BeanSheetExcel;

public class CodeShareOperatingCarrier {
	
	public void analizar(BeanSheetExcel bean) 
	{
		try
		{
			String orderId = bean.getResponseId();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			String fchIni = sdf.format(c.getTime());
			String fchFin = sdf.format(c.getTime());
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptFlightPriceResponseID.sh" + " " + orderId + " " + fchIni + " " + fchFin);

			if (responseKibana != null)
			{
				JSONObject jsonResponse = new JSONObject(responseKibana.toString());
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi_ndc-dist_flightPrice_soap_2");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject dataLists = response.getJSONObject("dataLists");
					JSONObject flightSegmentList = dataLists.getJSONObject("flightSegmentList");
					JSONArray flightSegment = flightSegmentList.getJSONArray("flightSegment");
					
					boolean existeVY = false;
					for (int j=0; j<flightSegment.length(); j++)
					{
						JSONObject fsElement = flightSegment.getJSONObject(j);
						JSONObject operatingCarrier = fsElement.getJSONObject("operatingCarrier");
						JSONObject airlineID = operatingCarrier.getJSONObject("airlineID");
						//VY = Vueling Airlines
						if ("VY".equals(airlineID.getString("value"))) {
							bean.setTipoError("CODE-SHARE");
							bean.setComentarios(airlineID.getString("value") + " - " + operatingCarrier.getString("name"));
							existeVY = true;
							break;
						}
					}
					
					if (!existeVY) {
						bean.setTipoError("CODE-SHARE");
						bean.setComentarios("WARNING !!! -> No se ha encontrado el operador VY - Vueling Airlines");
					}
					
					System.out.println(bean.getComentarios());
				}
				else
				{
					System.out.println("CodeShareOperatingCarrier.analizar(): Array de resultados vacío en scriptFlightPriceResponseID.sh");
				}
			}
		}
		catch (Exception e) {
			System.out.println("ERROR !!! -> CodeShareOperatingCarrier.analizar() -> " + "fecha: " + bean.getFechaLocal() + " - request: " + bean.getCodRequest());
			e.printStackTrace();
		}
	}
}
