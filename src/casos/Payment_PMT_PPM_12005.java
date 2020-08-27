package casos;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import com.MyUtil;
import com.VentanaPrincipalYerros;

import beans.BeanSheetExcel;

public class Payment_PMT_PPM_12005 {

	private BeanSheetExcel bean = null;
	private String request      = "";
	private String fchIni       = "";
	private String fchFin       = "";
		
	public void analizar(BeanSheetExcel bean) 
	{
		try
		{
			this.bean = bean;
			this.request = bean.getResponseId();
						
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			this.fchIni = sdf.format(c.getTime());
			this.fchFin = sdf.format(c.getTime());
			
			String responseKibana = MyUtil.ejecutarShellScript("scriptNetPlusToken.sh " + this.request + " " + this.fchIni + " " + this.fchFin);
			
			if (responseKibana != null) 
			{
				//Hay respuesta (correcta o no) !!!
				JSONObject jsonResponse = new JSONObject(responseKibana);
				JSONObject hitsObj = jsonResponse.getJSONObject("hits");
				JSONArray hitsArray = hitsObj.getJSONArray("hits");
				
				if (hitsArray.length() != 0) 
				{
					String[] address = new String[3];
					String[] payload = new String[3];
					String[] responseCode = new String[3];
					for(int i=0; i<hitsArray.length(); i++)
					{
						JSONObject objArray = hitsArray.getJSONObject(i);
												
						JSONObject source = objArray.getJSONObject("_source");
						JSONObject kpi = source.getJSONObject("kpi");
						JSONObject parameters = kpi.getJSONObject("parameters");
						JSONObject message = parameters.getJSONObject("message");

						address[i] = message.getString("address.string"); //-> Te dice quién ha cascado !!!
						payload[i] = message.getString("payload.string");
						responseCode[i] = message.getString("responseCode.string");
					}
					
					String comentarios = "";
					for(int j=0; j<hitsArray.length(); j++)
					{
						if("https://hub-iberia-pci.indra-netplus.com:4443/payments/pcp/transactionId/".equals(address[j])) 
						{
							if(payload[j].isEmpty() && responseCode[j].isEmpty()) {
								comentarios = comentarios + "netp_tokenizedPayment.TRANSACTIONID -> Sin respuesta !!!";
							} 
						}
						else if("https://hub-iberia-pci.indra-netplus.com:4443/payments/pcp/transaction/paymentTokenNetPlus".equals(address[j]))
						{
							if(payload[j].isEmpty() && responseCode[j].isEmpty()) {
								comentarios = comentarios + "3) netp_tokenizedPayment.VOID_TRANSACTION -> " + payload[0];
								comentarios = comentarios + "\n" + "2) netp_tokenizedPayment.TOKEN -> Sin respuesta !!!";
								comentarios = comentarios + "\n" + "1) netp_tokenizedPayment.TRANSACTION_ID -> " + payload[2];
							}
						}
					}
					
					this.bean.setTipoError("NETPLUS");
					this.bean.setComentarios(comentarios);
					VentanaPrincipalYerros.showInfo(comentarios);
				}
				else 
				{
					VentanaPrincipalYerros.showInfo("Payment_PMT_PPM_12005.analizar(): Array de resultados vacío en scriptNetPlusToken.sh");
				}
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalYerros.showError("Payment_PMT_PPM_12005.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalYerros.showError(e.getMessage());
		}
	}
	
}
