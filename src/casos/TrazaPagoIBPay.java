package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipalYerros;
import com.VentanaPrincipalIberiaPay;

import beans.BeanSheetExcel;

public class TrazaPagoIBPay {

	private BeanSheetExcel bean = null;
	private String request = "";
	private String fchIni  = "";
	private String fchFin  = "";
	private String pnr     = "";
	private String resultFraude = "| Fraude - WARNING !!!";
	private String resultTransactionID = "| TransactionID - WARNING !!!";
	private String resultPayment = "| Payment - WARNING !!!";
	private String errorPayment = "";
	private String errorTransaccion = "";
	private String errorVault = "";
	private boolean tieneLlamadasIBPay = false;

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

	public boolean isTieneLlamadasIBPay() {
		return tieneLlamadasIBPay;
	}

	public void setTieneLlamadasIBPay(boolean tieneLlamadasIBPay) {
		this.tieneLlamadasIBPay = tieneLlamadasIBPay;
	}
	
	private void datosDelPagoIBPay(BeanSheetExcel bean) 
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
					this.setTieneLlamadasIBPay(true);
					
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
									this.resultFraude = " | " + "FRAUDE: " + codigoRazon + " - " + decision;
									VentanaPrincipalYerros.showInfo("FRAUDE: " + codigoRazon + " - " + decision);
								}
							
								//Transacci�n
								if (jsonPayload.has("transactionId")) {
									String transaccion = jsonPayload.getString("transactionId");
									this.resultTransactionID = " | " + "TRANSACCION: " + transaccion;
									VentanaPrincipalYerros.showInfo("TRANSACCION: " + transaccion);
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

											this.resultPayment = " | " + "PAGO: " + status + " (" + code + " - " + desc + ")";
											VentanaPrincipalYerros.showInfo("PAGO: " + status + "(" + code + " - " + desc + ")");
										}
									}
									else if (item.has("transactionPath") && item.isNull("transactionPath"))
									{
										String estado = item.getString("progressStatus");
										if ("PSP_UNAVAILABLE".equals(estado)) {
											resultPayment = "| Payment - WARNING !!! PSP_UNAVAILABLE"; 
										}
									}
								}
						    }
						    else
						    {
						    	VentanaPrincipalYerros.showWarning("WARNING !!! -> payload vac�o.");
						    }
						}
					}
				}
				else 
				{
					VentanaPrincipalYerros.showInfo("TrazaPagoIBPay.datosDelPagoIBPay(): Array de resultados vac�o en scriptIberiaPay.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("TrazaPagoIBPay.datosDelPagoIBPay() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
	private void obtenerErrorPago(BeanSheetExcel bean) 
	{
		try
		{			
			//Obtenemos los datos de pmt-ppm.iberiapay.payments-in.1.debug
			String responseKibana = MyUtil.ejecutarShellScript("scriptIBPayPaymentError.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
							
						} else {
							this.errorPayment = "exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}";
							VentanaPrincipalYerros.showInfo(bean.getComentarios());
						}
					}
				}
				else 
				{
					VentanaPrincipalYerros.showInfo("TrazaPagoIBPay.obtenerErrorPago(): Array de resultados vac�o en scriptIBPayPaymentError.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("TrazaPagoIBPay.obtenerErrorPago() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
	private void obtenerErrorTransaccion(BeanSheetExcel bean) 
	{
		try
		{			
			//Obtenemos los datos de pmt-ppm.iberiapay.payments-in.1.debug
			String responseKibana = MyUtil.ejecutarShellScript("scriptIBPayTransactionError.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
							
						} else {
							this.errorTransaccion = "exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}";
							VentanaPrincipalYerros.showInfo(bean.getComentarios());
						}
					}
				}
				else 
				{
					VentanaPrincipalYerros.showInfo("TrazaPagoIBPay.obtenerErrorTransaccion(): Array de resultados vac�o en scriptIBPayPaymentError.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("TrazaPagoIBPay.obtenerErrorTransaccion() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
	private void obtenerErrorVault(BeanSheetExcel bean) 
	{
		try
		{			
			//Obtenemos los datos de pmt-ppm.iberiapay.payments-in.1.debug
			String responseKibana = MyUtil.ejecutarShellScript("scriptIBPayVaultError.sh" + " " + this.request + " " + this.fchIni + " " + this.fchFin);
			
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
							
						} else {
							this.errorVault = "exception: {" + "\n" + "     stackTrace: " + (stackTrace.split("\n"))[0].toString() + "\n" + "     errorCode: " + errorCode + "\n" + "     errorDescription: " + errorDescription + "\n" + "}";
							VentanaPrincipalYerros.showInfo(bean.getComentarios());
						}
					}
				}
				else 
				{
					VentanaPrincipalYerros.showInfo("TrazaPagoIBPay.obtenerErrorVault(): Array de resultados vac�o en scriptIBPayPaymentError.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("TrazaPagoIBPay.obtenerErrorTransaccion() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
	public String obtenerStringGeneral()
	{			
		String parteUno  = "PNR = " + this.pnr + " " +  this.resultFraude + " " + this.resultTransactionID + " " + this.resultPayment;
		String parteDos  = "";
		if (!this.errorVault.isEmpty()) {
			parteDos = "\n" + this.errorVault;
		} else if (!this.errorTransaccion.isEmpty()) {
			parteDos = "\n" + this.errorTransaccion;
		} else if (!this.errorPayment.isEmpty()) {
			parteDos = "\n" + this.errorPayment;
		}
		return parteUno + parteDos;
	}
	
	public void obtenerTrazasIBPay(BeanSheetExcel bean) 
	{
		this.bean = bean;
		this.request = bean.getCodRequest();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = bean.getTimeLocal();
		this.fchIni = sdf.format(c.getTime());
		this.fchFin = sdf.format(c.getTime());
		
		try
		{
			this.pnr = MyUtil.obtenerPNR(bean);
			//Es importante primero realizar este m�todo para informar si tiene llamadas a IBPay o no... "tieneLlamadasIBPay"
			datosDelPagoIBPay(bean);
			
			obtenerErrorVault(bean);
			if (this.errorVault.isEmpty()) 
			{
				obtenerErrorTransaccion(bean);
				if (this.errorTransaccion.isEmpty()) {
					obtenerErrorPago(bean);
				}
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError("TrazaPagoIBPay.obtenerTrazasIBPay() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
	
}
