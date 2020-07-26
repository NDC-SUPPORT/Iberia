package casos;

import com.VentanaPrincipalIberiaPay;

import beans.BeanSheetExcel;

public class InternalError_PMTPPM9017_PMTORM9004 {

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
					
					if (tpibpay.isTieneLlamadasIBPay()) {
						bean.setTipoError("INTERNAL ERROR");
						bean.setComentarios(bean.getComentarios() + "\n" + tpibpay.obtenerStringGeneral());
					} else {
						//Aunque no tenga llamadas de IBPay se ha obtenido el PNR en la clase TrazaPagoIBPay... (ver clase)
						bean.setTipoError("INTERNAL ERROR");
						bean.setComentarios(tpibpay.getPnr() + " | " + "(WCS - CircuitBreaker) PAC_UTILS_E0006 - CONTENTS_ERROR");
					}
					break;
				}
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			VentanaPrincipalIberiaPay.showError("InternalError_PMTPPM9017_PMTORM9004.analizar() -> " + "fecha: " + this.bean.getFechaLocal() + " - request: " + this.bean.getCodRequest());
			VentanaPrincipalIberiaPay.showError(e.getMessage());
		}
	}
	
}
