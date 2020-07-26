package beans;

public class BeanFormularioInformes {

	String fechaInicio = "";
	String fechaFin = "";
	String rutaExcel = "";

	public BeanFormularioInformes() {
		
	}
	
	public BeanFormularioInformes(String fchIni, String fchFin, String rE) {
		this.fechaInicio = fchIni;
		this.fechaFin = fchFin;
		this.rutaExcel = rE;
	}
	
	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getRutaExcel() {
		return rutaExcel;
	}

	public void setRutaExcel(String rutaExcel) {
		this.rutaExcel = rutaExcel;
	}
}
