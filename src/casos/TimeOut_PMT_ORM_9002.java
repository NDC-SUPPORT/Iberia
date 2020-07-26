package casos;

import com.VentanaPrincipalIberiaPay;

import beans.BeanSheetExcel;

public class TimeOut_PMT_ORM_9002 {

	private BeanSheetExcel bean = null;
	
	public void analizar(BeanSheetExcel bean) 
	{
		this.bean = bean;
		
		try
		{	
			switch(bean.getTipoPago()) 
			{
				case "CASH": {
					bean.setComentarios("Tipo de Pago = CASH");
					break;
				}
				
				case "CreditCard": {
					bean.setComentarios("Tipo de Pago = CREDIT_CARD");
					TrazaPagoIBPay tpibpay = new TrazaPagoIBPay();
					tpibpay.obtenerTrazasIBPay(bean);
					bean.setComentarios(bean.getComentarios() + "\n" + tpibpay.obtenerStringGeneral());
					break;
				}
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError("TimeOut_PMT_ORM_9002.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
	
}
