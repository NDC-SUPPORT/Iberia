package beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class BeanSheetExcelMailsTimeOut {
	
	private String   timeStamp        = "";
	private Calendar timeLocal        = null;
	private String   fechaLocal       = "";
	private String   codRequest       = "";
	private String   usuario          = "";
	private String   pnr              = "";

	public BeanSheetExcelMailsTimeOut() {
		
	}
	
	public BeanSheetExcelMailsTimeOut(String timeStamp, String codRequest, String usuario, String pnr) 
	{
		this.timeStamp = timeStamp;
		this.codRequest = codRequest;
		this.usuario = usuario;
		this.pnr = pnr;

		//UTC = GMT = Zulu
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar c = GregorianCalendar.getInstance(timeZone);
		//tS = YYYY-MM-DDTHH:MM:SS.SSSZ
		//-> Advertencia !!! En este campo perdemos los milisegundos
		c.set(Integer.parseInt(timeStamp.substring(0,4)),    //A�o
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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getPnr() {
		return pnr;
	}

	public void setPnr(String pnr) {
		this.pnr = pnr;
	}

}
