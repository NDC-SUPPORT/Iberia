package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipal;
import com.VentanaPrincipalIberiaPay;

import beans.BeanSheetExcel;

public class TimeOut_NDC_DIST_9009 {

	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
	
	public void datosDelPagoIBPay(BeanSheetExcel bean) 
	{
		try
		{
			//Obtenemos los datos de pmt-ppm.iberiapay.payments-in.1.debug
			String responseKibana = MyUtil.ejecutarShellScript("scriptIberiaPay.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					for(int i=0; i<hitsArray.length(); i++)
					{
						JSONObject objArray = hitsArray.getJSONObject(i);		
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject parameters = kpi.getJSONObject("parameters");
						JSONObject message = parameters.getJSONObject("message");
						
						if (message.has("payload.string")) 
						{
						    String payload = message.getString("payload.string");
						    if (!payload.isEmpty()) 
						    {
							    JSONObject jsonPayload = new JSONObject(payload);
	
							    //Datos del fraude (ACCEPT / REVIEW / REJECT)		
							    if (jsonPayload.has("provider") && "CYBERSOURCE".equals(jsonPayload.getString("provider"))) {
									String codigoRazon = jsonPayload.getString("reasonCode");
									String decision = jsonPayload.getString("decision");
									bean.setComentarios(bean.getComentarios() + " | " + "FRAUDE: " + codigoRazon + " - " + decision);
									VentanaPrincipal.showInfo("FRAUDE: " + codigoRazon + " - " + decision);
								}
							
								//Transacción
								if (jsonPayload.has("transactionId")) {
									String transaccion = jsonPayload.getString("transactionId");
									bean.setComentarios(bean.getComentarios() + " | " + "TRANSACCION: " + transaccion);
									VentanaPrincipal.showInfo("TRANSACCION: " + transaccion);
								}
							
								//Datos del pago
								if (jsonPayload.has("items")) {
									JSONArray items = jsonPayload.getJSONArray("items");
									JSONObject item = items.getJSONObject(0);
									if (item.has("transactionPath") && !item.isNull("transactionPath"))
									{
										JSONObject transactionPath = item.getJSONObject("transactionPath");
										String provider = transactionPath.getString("provider");
										if ("ONESAIT".equals(provider)) {
											//estadoDelPago
											String status = item.getString("status");
											//resultadoDelPAgo
											JSONObject result = item.getJSONObject("result");
											String code = result.getString("code");
											String desc = result.getString("message");

											bean.setComentarios(bean.getComentarios() + " | " + "PAGO: " + status + " (" + code + " - " + desc + ")");
											VentanaPrincipal.showInfo("PAGO: " + status + "(" + code + " - " + desc + ")");
										}
									}
								}
						    }
						    else
						    {
						    	String responseCode = message.getString("responseCode.string");
						    	bean.setComentarios(bean.getComentarios() + " | " + "WARNING !!! (HttpCode = " + responseCode + ") ");
						    	VentanaPrincipal.showWarning("WARNING !!! -> payload vacío.");
						    }
						}
					}
				}
				else 
				{
					VentanaPrincipal.showInfo("TimeOut_NDC_DIST_9009.datosDelPagoIBPay(): Array de resultados vacío en scriptIberiaPay.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipal.showError("TimeOut_NDC_DIST_9009.datosDelPagoIBPay() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipal.showError(e.getMessage());
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
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptIssueOrderDaoError.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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

					bean.setComentarios("exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}");
					VentanaPrincipal.showInfo("TimeOut_NDC_DIST_9009: " + bean.getComentarios());
					
					switch(bean.getTipoPago()) 
					{
						case "CASH": {
							bean.setTipoError("TIME-OUT");
							bean.setComentarios("Tipo de Pago = CASH" + "\n" + bean.getComentarios());
							break;
						}
						
						case "CreditCard": {
							bean.setTipoError("TIME-OUT");
							bean.setComentarios("Tipo de Pago = CREDIT_CARD" + "\n" + bean.getComentarios());
							String pnr = MyUtil.obtenerPNR(bean);
							bean.setComentarios(bean.getComentarios() + "\n" + "PNR = " + pnr);
							datosDelPagoIBPay(bean); 
							break;
						}
					}
				}
				else 
				{
					System.out.println("TimeOut_NDC_DIST_9009.analizar(): Array de resultados vacío en scriptPaymentInfo.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError("TimeOut_NDC_DIST_9009.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
	
}
