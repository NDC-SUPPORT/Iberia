package beans;

public class BeanFormularioMailsTimeOut {

	String fecha = "";
	String horaInicio = "";
	String horaFin = "";
	boolean analizar = false;
	String rutaExcel = "";

	public BeanFormularioMailsTimeOut() {
		
	}
	
	public BeanFormularioMailsTimeOut(String fch, String hIni, String hFin, String rE, boolean analizar) {
		this.fecha = fch;
		this.horaInicio = hIni;
		this.horaFin = hFin;
		this.rutaExcel = rE;
		this.analizar = analizar;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public boolean isAnalizar() {
		return analizar;
	}

	public void setAnalizar(boolean analizar) {
		this.analizar = analizar;
	}
	
	public String getRutaExcel() {
		return rutaExcel;
	}

	public void setRutaExcel(String rutaExcel) {
		this.rutaExcel = rutaExcel;
	}
	
}
