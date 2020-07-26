package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipalIberiaPay;

import beans.BeanSheetExcelIberiaPay;

public class Payment_IBPay {

	private BeanSheetExcelIberiaPay bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
		
	public void analizarPMT(BeanSheetExcelIberiaPay bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			this.bean.setRazonFraude("¿?");
			this.bean.setDecisionFraude("¿?");
			this.bean.setTransaccion("¿?");
			this.bean.setEstadoDelPago("¿?");
			this.bean.setResultadoDelPago("¿?");
			//this.bean.setPnr("¿?");
			//this.bean.setMoneda("¿?");
			//this.bean.setMascaraTarjeta("¿?");
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			
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
						    if (!payload.isEmpty()) {
							    JSONObject jsonPayload = new JSONObject(payload);
	
							    //Datos del fraude (ACCEPT / REVIEW / REJECT)
								if (jsonPayload.has("provider") && "CYBERSOURCE".equals(jsonPayload.getString("provider"))) {
									String codigoRazon = jsonPayload.getString("reasonCode");
									String decision = jsonPayload.getString("decision");
									bean.setRazonFraude(codigoRazon);
									bean.setDecisionFraude(decision);
									VentanaPrincipalIberiaPay.showInfo("FRAUDE: " + codigoRazon + " - " + decision);
								}
							
								//Transacción
								if (jsonPayload.has("transactionId")) {
									String transaccion = jsonPayload.getString("transactionId");
									//transaccion = !transaccion.isEmpty()?"OK":"KO";
									bean.setTransaccion(transaccion);
									VentanaPrincipalIberiaPay.showInfo("TRANSACCION: " + transaccion);
								}
							
								//Datos del pago
								if (jsonPayload.has("items")) {
									JSONArray items = jsonPayload.getJSONArray("items");
									JSONObject item = items.getJSONObject(0);
									if (item.has("transactionPath")) {
										JSONObject transactionPath = item.getJSONObject("transactionPath");
										String provider = transactionPath.getString("provider");
										if ("ONESAIT".equals(provider)) {
											//estadoDelPago
											String status = item.getString("status");
											bean.setEstadoDelPago(status);
											//resultadoDelPAgo
											JSONObject result = item.getJSONObject("result");
											String code = result.getString("code");
											String desc = result.getString("message");
											bean.setResultadoDelPago(code + " - " + desc);
											VentanaPrincipalIberiaPay.showInfo("ONESAIT: " + code + " - " + desc);
										}
									}
								}
						    }
						    else
						    {
						    	VentanaPrincipalIberiaPay.showWarning("WARNING !!! -> payload vacío.");
						    }
						}
					}
				}
				else 
				{
					VentanaPrincipalIberiaPay.showInfo("Payment_IBPay.analizarPMT(): Array de resultados vacío en scriptIberiaPay.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError("Payment_IBPay.analizarPMT() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
	
	public void analizarNDC(BeanSheetExcelIberiaPay bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getCodRequest();
			this.bean.setServicioNDC("¿?");
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			
			//Obtenemos los datos de pmt-ppm.iberiapay.payments-in.1.debug
			String responseKibana = MyUtil.ejecutarShellScript("scriptAdiOcIberiaPay.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
					String servicio = source.getString("service");
					
					switch(servicio) 
					{
						case "createOrder":
						{
							this.bean.setServicioNDC("OC");
							
							String[] cE = this.bean.getCodError();
							
							if (cE[0]!=null) {

								JSONObject kpi = source.getJSONObject("kpi");
								JSONObject parameters = kpi.getJSONObject("parameters");
								JSONObject parameter1 = parameters.getJSONObject("parameter1");
								JSONObject query = parameter1.getJSONObject("query");
								JSONObject payments = query.getJSONObject("payments");
								JSONArray plo = payments.getJSONArray("payment.list.object");
								JSONObject ploCero = plo.getJSONObject(0);	
								
								JSONObject amount = ploCero.getJSONObject("amount");
								//Precio
								if (amount.has("value.int")) {
									Integer valorInteger = amount.getInt("value.int");
									this.bean.setTotalPrecio(valorInteger.doubleValue());
								} else if (amount.has("value.float")) {
									Double valorDouble = amount.getDouble("value.float");
									this.bean.setTotalPrecio(valorDouble);
								}
								//Moneda
								String moneda = amount.getString("code.string");
								this.bean.setMoneda(moneda);
								
								//Mascara tarjeta
								JSONObject metodo = ploCero.getJSONObject("method");
								JSONObject paymentCard = metodo.getJSONObject("paymentCard");
								JSONObject maskedCardNumber = paymentCard.getJSONObject("maskedCardNumber");
								String mascara = maskedCardNumber.getString("value.string");
								this.bean.setMascaraTarjeta(mascara);
								
							}
						}
						break;
						
						case "airDocIssue":
						{
							this.bean.setServicioNDC("ADI");
							
							String[] cE = this.bean.getCodError();
							
							if (cE[0]!=null) {
							
								JSONObject kpi = source.getJSONObject("kpi");
								JSONObject parameters = kpi.getJSONObject("parameters");
								JSONObject parameter1 = parameters.getJSONObject("parameter1");
								JSONObject query = parameter1.getJSONObject("query");
								JSONArray tdilo = query.getJSONArray("ticketDocInfo.list.object");
								JSONObject tdiloCero = tdilo.getJSONObject(0);	
								JSONObject payments = tdiloCero.getJSONObject("payments");
								JSONArray plo = payments.getJSONArray("payment.list.object");
								JSONObject ploCero = plo.getJSONObject(0);
								
								JSONObject amount = ploCero.getJSONObject("amount");
								//Precio
								if (amount.has("value.int")) {
									Integer valorInteger = amount.getInt("value.int");
									this.bean.setTotalPrecio(valorInteger.doubleValue());
								} else if (amount.has("value.float")) {
									Double valorDouble = amount.getDouble("value.float");
									this.bean.setTotalPrecio(valorDouble);
								}
								//Moneda
								String moneda = amount.getString("code.string");
								this.bean.setMoneda(moneda);
								
								//Mascara tarjeta
								JSONObject paymentCard = ploCero.getJSONObject("paymentCard");
								JSONObject maskedCardNumber = paymentCard.getJSONObject("maskedCardNumber");
								String mascara = maskedCardNumber.getString("value.string");
								this.bean.setMascaraTarjeta(mascara);
								
							}
						}
						break;
						
						case "orderChange":
						{
							this.bean.setServicioNDC("OCH");
						}
						break;
					}	
				}
				else 
				{
					VentanaPrincipalIberiaPay.showInfo("Payment_IBPay.analizarNDC(): Array de resultados vacío en scriptAdiOcIberiaPay.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError("Payment_IBPay.analizarNDC() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
}
