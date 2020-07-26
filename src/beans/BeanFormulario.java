package beans;

public class BeanFormulario {

	String codServicio = "";
	boolean respuestaAgrupada = false;
	String fecha = "";
	String idExcel = "";
	boolean total = false;
	String horaInicio = "";
	String horaFin = "";
	boolean analizar = false;
	String rutaExcel = "";

	public BeanFormulario() {
		
	}
	
	public BeanFormulario(String nS, boolean rA, String fch, String hIni, String hFin,String idE, boolean total, boolean analizar) {
		this.codServicio = nS;
		this.respuestaAgrupada = rA;
		this.fecha = fch;
		this.horaInicio = hIni;
		this.horaFin = hFin;
		//this.idExcel = idE;
		this.rutaExcel = idE;
		this.total = total;
		this.analizar = analizar;
	}
	
	public String getCodServicio() {
		return codServicio;
	}
	
	public void setCodServicio(String nombreServicio) {
		this.codServicio = nombreServicio;
	}
	
	public boolean isRespuestaAgrupada() {
		return respuestaAgrupada;
	}
	
	public void setRespuestaAgrupada(boolean respuestaAgrupada) {
		this.respuestaAgrupada = respuestaAgrupada;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getIdExcel() {
		return idExcel;
	}

	public void setIdExcel(String idExcel) {
		this.idExcel = idExcel;
	}

	public boolean isTotal() {
		return total;
	}

	public void setTotal(boolean total) {
		this.total = total;
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
