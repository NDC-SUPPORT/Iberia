package beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class BeanSheetExcelIberiaPay {
	
	private String   timeStamp        = "";
	private Calendar timeLocal        = null;
	private String   fechaLocal       = "";
	private String   codRequest       = "";
	private String[] codError         = new String[2];
	private String[] descripcionError = new String[2];
	private String   mercado          = "";
	private String   usuario          = "";
	private String   codAgencia       = "";
	private String   agencia          = "";
	private String   tipoPago         = "";
	private String   razonFraude      = "";
	private String   decisionFraude   = "";
	private String   transaccion      = "";
	private String   estadoDelPago    = "";
	private String   resultadoDelPago = "";
	private String   servicioNDC      = "";
	private Double   totalPrecio      = null;
	private String   moneda           = "";
	private String   mascaraTarjeta   = "";
	private String   pnr              = "";

	public BeanSheetExcelIberiaPay() {
		
	}
	
	public BeanSheetExcelIberiaPay(String timeStamp, String codRequest, String mercado, String usuario, String codAgencia, String agencia, String tipoPago, String[] cE, String[] dE, Double totalPrecio, String moneda, String mascaraTarjeta, String pnr) 
	{
		this.timeStamp = timeStamp;
		this.codRequest = codRequest;
		this.mercado = mercado;
		this.usuario = usuario;
		this.codAgencia = codAgencia;
		this.agencia = agencia;
		this.tipoPago = tipoPago;
		this.codError[0] = cE[0];
		this.codError[1] = cE[1];
		this.descripcionError[0] = dE[0];
		this.descripcionError[1] = dE[1];
		this.totalPrecio = totalPrecio;
		this.moneda = moneda;
		this.mascaraTarjeta = mascaraTarjeta;
		this.pnr = pnr;

		//UTC = GMT = Zulu
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar c = GregorianCalendar.getInstance(timeZone);
		//tS = YYYY-MM-DDTHH:MM:SS.SSSZ
		//-> Advertencia !!! En este campo perdemos los milisegundos
		c.set(Integer.parseInt(timeStamp.substring(0,4)),    //Año
              Integer.parseInt(timeStamp.substring(5,7))-1,  //Mes
              Integer.parseInt(timeStamp.substring(8,10)),   //Dia
              Integer.parseInt(timeStamp.substring(11,13)),  //Hora
              Integer.parseInt(timeStamp.substring(14,16)),  //Minutos
              Integer.parseInt(timeStamp.substring(17,19))); //Segundos
		this.timeLocal  = c;
	
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
		this.fechaLocal = sdf.format(c.getTime()).substring(0,23);
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getCodRequest() {
		return codRequest;
	}
	
	public void setCodRequest(String codRequest) {
		this.codRequest = codRequest;
	}

	public Calendar getTimeLocal() {
		return timeLocal;
	}

	public void setTimeLocal(Calendar timeLocal) {
		this.timeLocal = timeLocal;
	}

	public String getFechaLocal() {
		return fechaLocal;
	}

	public void setFechaLocal(String fechaLocal) {
		this.fechaLocal = fechaLocal;
	}

	public String[] getCodError() {
		return codError;
	}

	public void setCodError(String[] codError) {
		this.codError = codError;
	}

	public String[] getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String[] descripcionError) {
		this.descripcionError = descripcionError;
	}
	
	public String getMercado() {
		return mercado;
	}

	public void setMercado(String mercado) {
		this.mercado = mercado;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getCodAgencia() {
		return codAgencia;
	}

	public void setCodAgencia(String codAgencia) {
		this.codAgencia = codAgencia;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getTipoPago() {
		return tipoPago;
	}

	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	
	public String getRazonFraude() {
		return razonFraude;
	}

	public void setRazonFraude(String razonFraude) {
		this.razonFraude = razonFraude;
	}

	public String getDecisionFraude() {
		return decisionFraude;
	}

	public void setDecisionFraude(String decisionFraude) {
		this.decisionFraude = decisionFraude;
	}
	
	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	
	public String getEstadoDelPago() {
		return estadoDelPago;
	}

	public void setEstadoDelPago(String estadoDelPago) {
		this.estadoDelPago = estadoDelPago;
	}

	public String getResultadoDelPago() {
		return resultadoDelPago;
	}

	public void setResultadoDelPago(String resultadoDelPago) {
		this.resultadoDelPago = resultadoDelPago;
	}
	
	public String getServicioNDC() {
		return servicioNDC;
	}

	public void setServicioNDC(String servicioNDC) {
		this.servicioNDC = servicioNDC;
	}
	
	public Double getTotalPrecio() {
		return totalPrecio;
	}

	public void setTotalPrecio(Double totalPrecio) {
		this.totalPrecio = totalPrecio;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMascaraTarjeta() {
		return mascaraTarjeta;
	}

	public void setMascaraTarjeta(String mascaraTarjeta) {
		this.mascaraTarjeta = mascaraTarjeta;
	}

	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

}
