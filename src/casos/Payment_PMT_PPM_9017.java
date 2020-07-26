package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;

import beans.BeanSheetExcel;

public class Payment_PMT_PPM_9017 {

	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
	
	private void detalleCasoPaisNull()
	{
		try
		{
			String comentarios = "";
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptRequestOfferInformation.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					//kpi_sse-avm_getOfferInformation_ctrl_1.response.entity.offer.slices.segments
					JSONObject objArray = hitsArray.getJSONObject(0);
					JSONObject source = objArray.getJSONObject("_source");
					JSONObject kpi = source.getJSONObject("kpi_sse-avm_getOfferInformation_ctrl_1");
					JSONObject response = kpi.getJSONObject("response");
					JSONObject entity = response.getJSONObject("entity");
					JSONObject offer = entity.getJSONObject("offer");
					JSONArray slices = offer.getJSONArray("slices");
					
					for(int i=0; i<slices.length(); i++)
					{
						JSONObject o = slices.getJSONObject(i);
						JSONArray segments = o.getJSONArray("segments");
						for(int j=0; j<segments.length(); j++)
						{
							JSONObject segment = segments.getJSONObject(j);
							
							//Departure
							JSONObject departure = segment.getJSONObject("departure");
							JSONObject cityDeparturre = departure.getJSONObject("city");
							if(cityDeparturre.isNull("countryCode")) {
								comentarios = comentarios + "\n" + cityDeparturre.toString();
							}
							
							//Arraival
							JSONObject arraival = segment.getJSONObject("arrival");
							JSONObject arrival = segment.getJSONObject("arrival");
							JSONObject cityArrival = arrival.getJSONObject("city");
							if(cityArrival.isNull("countryCode")) {
								comentarios = comentarios + "\n" + cityArrival.toString();
							}
						}
					}

					this.bean.setTipoError("INTERNAL ERROR");
					this.bean.setComentarios(this.bean.getComentarios() + "\n" + "sse-avm.getofferinformation.xxx.countryCode = null: " + comentarios + "\n" + "(NDC-11302)");
				}
				else
				{
					System.out.println("Payment_PMT_PPM_9017.detalleCasoPaisNull(): Array de resultados vacío en scriptRequestOfferInformation.sh");
				}
			}
		}
		catch(Exception e) {
			System.out.println("Payment_PMT_PPM_9017.detalleCasoPaisNull() -> " + "fecha: " + bean.getFechaLocal() + " - request: " + bean.getCodRequest());
			e.printStackTrace();
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
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptPaymentInfo.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					JSONObject exception = source.getJSONObject("exception");
					
					String stackTrace = exception.isNull("stackTrace")?"":exception.getString("stackTrace");
					String errorCode = exception.isNull("errorCode")?"":exception.getString("errorCode");
					String errorDescription = exception.isNull("errorDescription")?"":exception.getString("errorDescription");

					bean.setComentarios("Payment -> exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}");
					System.out.println("Payment_PMT_PPM_9017: " + bean.getComentarios());
					
					switch(errorCode) {
						case "PAC_UTILS_E0001": {
							detalleCasoPaisNull();
							break;
						}
					}
				}
				else 
				{
					System.out.println("Payment_PMT_PPM_9017.analizar(): Array de resultados vacío en scriptPaymentInfo.sh");
				}
	        }
		}
		catch (Exception e) {
			System.out.println("Payment_PMT_PPM_9017.analizar() -> " + "fecha: " + bean.getFechaLocal() + " - request: " + bean.getCodRequest());
			e.printStackTrace();
		}
	}
	
}
