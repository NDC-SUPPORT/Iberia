package com;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import beans.BeanSheetExcel;

public class MyUtil {
	
	public static String ventanaEnEjecucion = "VentanaPrincipal";

	public static void escribirTraza(String tipo, String mensaje) 
	{
		if ("VentanaPrincipal".equals(ventanaEnEjecucion))
		{
			if ("INFO".equals(tipo)) {
				VentanaPrincipal.showInfo(mensaje);
			} else if ("WARNING".equals(tipo)) {
				VentanaPrincipal.showWarning(mensaje);
			} else if ("ERROR".equals(tipo)) {
				VentanaPrincipal.showError(mensaje);
			}
		}
		else if ("VentanaPrincipalIberiaPay".equals(ventanaEnEjecucion))
		{
			if ("INFO".equals(tipo)) {
				VentanaPrincipalIberiaPay.showInfo(mensaje);
			} else if ("WARNING".equals(tipo)) {
				VentanaPrincipalIberiaPay.showWarning(mensaje);
			} else if ("ERROR".equals(tipo)) {
				VentanaPrincipalIberiaPay.showError(mensaje);
			}
		}
		else if ("VentanaPrincipalInformes".equals(ventanaEnEjecucion))
		{
			if ("INFO".equals(tipo)) {
				VentanaPrincipalInformes.showInfo(mensaje);
			} else if ("WARNING".equals(tipo)) {
				VentanaPrincipalInformes.showWarning(mensaje);
			} else if ("ERROR".equals(tipo)) {
				VentanaPrincipalInformes.showError(mensaje);
			}
		}
	}

	//Nombre que figuran en las pestañas
	public static String getNombrePestaniaExcel(String codigo) 
	{
		String nombre = null;
		
		switch(codigo) {
			//AirDocIssue         
			case "ADI": nombre = "AirDocIssue";   break;
			//CreateOrder
			case "OC":  nombre = "OrderCreate";   break;
			//FlightPrice
			case "FP":  nombre = "Fare";  	      break;
			//OrderCancel
			case "CA":	nombre = "OrderCancel";   break;
			//Seats
			case "SE":	nombre = "Seat"; 		  break;
			//Baggages
			case "BA":  nombre = "Baggage";       break;
			//OrderChange
			case "OCH":	nombre = "OrderChange";   break;
			//ItinReshop
			case "IR":	nombre = "ItinReshop";    break;
			//OrderRetrieve
			case "OR":	nombre = "OrderRetrieve"; break;
			//AirShopping
			case "AS":	nombre = "Availability_nube"; break;
			
			default:
				escribirTraza("ERROR", "ERROR - getNombreLargoServicio(" + codigo + "): Nombre de servicio no encontrado");
		}
		
		return nombre;
	}
	
	public static String getNombreScript_AA(String codigo) 
	{
		String nombre = null;
		
		switch(codigo) {
			//AirDocIssue
			case "ADI": nombre = "AA_scriptAirDocIssue.sh";   break;
			//CreateOrder
			case "OC":  nombre = "AA_scriptOrderCreate.sh";   break;
			//FlightPrice
			case "FP":  nombre = "AA_scriptFlightPrice.sh";   break;
			//OrderCancel
			case "CA":	nombre = "AA_scriptOrderCancel.sh";   break;
			//Seats
			case "SE":	nombre = "AA_scriptSeat.sh"; 		  break;
			//Baggages
			case "BA":  nombre = "AA_scriptBaggage.sh";       break;
			//OrderChange
			case "OCH":	nombre = "AA_scriptOrderChange.sh";   break;
			//ItinReshop
			case "IR":	nombre = "AA_scriptItinReshop.sh";    break;
			//OrderRetrieve
			case "OR":	nombre = "AA_scriptOrderRetrieve.sh"; break;
			//AirShopping
			case "AS":	nombre = "AA_scriptAirShopping.sh"; break;
			
			default:
				escribirTraza("ERROR", "getNombreScript(" + codigo + "): Nombre de servicio no encontrado");
		}
		
		return nombre;
	}
	
	public static String getNombreScript_BB(String codigo) 
	{
		String nombre = null;
		
		switch(codigo) {
			//AirDocIssue
			case "ADI": nombre = "BB_scriptAirDocIssue.sh";   break;
			//CreateOrder
			case "OC":  nombre = "BB_scriptOrderCreate.sh";   break;
			//FlightPrice
			case "FP":  nombre = "BB_scriptFlightPrice.sh";   break;
			//OrderCancel
			case "CA":	nombre = "BB_scriptOrderCancel.sh";   break;
			//Seats
			case "SE":	nombre = "BB_scriptSeat.sh"; 		  break;
			//Baggages
			case "BA":  nombre = "BB_scriptBaggage.sh";       break;
			//OrderChange
			case "OCH":	nombre = "BB_scriptOrderChange.sh";   break;
			//ItinReshop
			case "IR":	nombre = "BB_scriptItinReshop.sh";    break;
			//OrderRetrieve
			case "OR":	nombre = "BB_scriptOrderRetrieve.sh"; break;
			
			default:
				escribirTraza("ERROR", "getNombreScript(" + codigo + "): Nombre de servicio no encontrado");
		}
		
		return nombre;
	}	
	
	/*
	 * la columna se identificará por el servicio
	 * la fila se identificará por el día de la semana
	 * 4-> lunes
	 * 5-> martes
	 * ...
	 */
	public static String getUbicacionPestaniaCero(String codServ, String fch)
	{
		String columna = null;
		String fila = null;
		
		switch(codServ) {
			//AirDocIssue
			case "ADI": columna = "B"; break;
			//CreateOrder
			case "OC":  columna = "E"; break;
			//FlightPrice
			case "FP":  columna = "D"; break;
			//OrderCancel
			case "CA":	columna = "F"; break;
			//Seats
			case "SE":	columna = "G"; break;
			//Baggages
			case "BA":  columna = "H"; break;
			//OrderChange
			case "OCH":	columna = "I"; break;
			//ItinReshop
			case "IR":	columna = "J"; break;
			//OrderRetrieve
			case "OR":	columna = "K"; break;
			
			default:
				escribirTraza("ERROR", "getUbicacionPestaniaCero(" + codServ + "): Nombre de servicio no encontrado");
		}
		
		Calendar c = GregorianCalendar.getInstance();
	    c.set(Integer.parseInt(fch.substring(6,10)), Integer.parseInt(fch.substring(3,5))-1, Integer.parseInt(fch.substring(0,2)));
	    int nD=c.get(Calendar.DAY_OF_WEEK); 
	    switch (nD) {
	        case 1: fila = "10"; break;
	        case 2: fila =  "4"; break;
	        case 3: fila =  "5"; break;
	        case 4: fila =  "6"; break;
	        case 5: fila =  "7"; break;
	        case 6: fila =  "8"; break;
	        case 7: fila =  "9"; break;
	    }
	    		
		return columna + fila;
	}
	
	public static Integer getUbicacionPestaniaCero_NumFila(String codServ, String fch)
	{
		Integer fila = null;
		
		Calendar c = GregorianCalendar.getInstance();
	    c.set(Integer.parseInt(fch.substring(0,4)), Integer.parseInt(fch.substring(5,7))-1, Integer.parseInt(fch.substring(8,10)));
	    int nD=c.get(Calendar.DAY_OF_WEEK); 
	    switch (nD) {
	        case 1: fila = 9; break;
	        case 2: fila = 3; break;
	        case 3: fila = 4; break;
	        case 4: fila = 5; break;
	        case 5: fila = 6; break;
	        case 6: fila = 7; break;
	        case 7: fila = 8; break;
	    }
	    		
		return fila;
	}
	
	public static Integer getUbicacionPestaniaCero_NumColumna(String codServ, String fch)
	{
		Integer columna = null;

		switch(codServ) {
			//AirDocIssue
			case "ADI": columna =  1; break;
			//CreateOrder
			case "OC":  columna =  4; break;
			//FlightPrice
			case "FP":  columna =  3; break;
			//OrderCancel
			case "CA":	columna =  5; break;
			//Seats
			case "SE":	columna =  6; break;
			//Baggages
			case "BA":  columna =  7; break;
			//OrderChange
			case "OCH":	columna =  8; break;
			//ItinReshop
			case "IR":	columna =  9; break;
			//OrderRetrieve
			case "OR":	columna = 10; break;
			//AirShopping
			case "AS":  columna =  2; break;
			
			default:
				escribirTraza("ERROR", "getUbicacionPestaniaCero(" + codServ + "): Nombre de servicio no encontrado");
		}
	
		return columna;
	}
	
	public static Document convertStringToXMLDocument(String xmlString)
    {
        try
        {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();  
            Document doc = documentBuilder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e) {
        	e.printStackTrace();
        	escribirTraza("ERROR", e.getMessage());
        }
        return null;
    }
	
	public static String ejecutarShellScript(String scriptMasParametros)
	{
		try
		{
			//String path = "\\src\\scripts\\";
			String path = new File (".").getAbsolutePath() + "//src//scripts//";
		
			Process p = null;
			
			escribirTraza("INFO", "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
			escribirTraza("INFO", "MyUtil.ejecutarShellScript(): " + scriptMasParametros);

			p = Runtime.getRuntime().exec("sh " + path + scriptMasParametros);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF8"));
			BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			String line;
			StringBuilder sbResponse = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sbResponse.append(line);
			}
			br.close();
			while ((line = brError.readLine()) != null) {
				escribirTraza("INFO", line);
			}
			brError.close();
			int resultCode = p.waitFor();
			if (resultCode == 0) 
			{
				//Hay respuesta, correcta o no !!!
				return sbResponse.toString();
			}
			else
			{
				escribirTraza("INFO", "MyUtil.ejecutarShellScript(): ResultCode = " + resultCode);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
		}
		return null;
	}
	
	public static String obtenerCodigoDeXML(Document doc)
	{
		try
		{
			//http://www.latascadexela.es/2008/07/java-y-xml-dom-ii.html
			NodeList nlCode = doc.getElementsByTagName("code");
			Node nodoCode = nlCode.item(0);
			String s = nodoCode!=null?nodoCode.getTextContent():null;
			
			//Puede estar en castellano !!!
			if (s == null) {
				NodeList nlCodigo = doc.getElementsByTagName("codigo");
				Node nodoCodigo = nlCodigo.item(0);
				s = nodoCodigo!=null?nodoCodigo.getTextContent():null;
			}
			
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	public static String obtenerDescripcionDeXML(Document doc)
	{
		try
		{
			NodeList nlDescription = doc.getElementsByTagName("description");
			Node nodoDesc = nlDescription.item(0);
			String s = nodoDesc!=null?nodoDesc.getTextContent():null;
			
			//Puede estar en castellano !!!
			if (s == null) {
				NodeList nlDescripcion = doc.getElementsByTagName("descripcion");
				Node nodoDescripcion = nlDescripcion.item(0);
				s = nodoDescripcion!=null?nodoDescripcion.getTextContent():null;
			}
			
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	public static String obtenerDetallesDeXML(Document doc)
	{
		try
		{
			NodeList nlDetalles = doc.getElementsByTagName("Details");
			Node nodoDetalles = nlDetalles.item(0);
			String s = nodoDetalles!=null?nodoDetalles.getTextContent():null;
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	public static String obtenerTKTsDeXML(Document doc)
	{
		String s = "";
		
		try
		{
			NodeList nlTKT = doc.getElementsByTagName("TKT");
			for (int i=0; i<nlTKT.getLength(); i++)
			{
				Node nodoTKT = nlTKT.item(i);
				s = s + (nodoTKT!=null?(nodoTKT.getTextContent() + " | "):"");
			}
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	public static String obtenerLocalizadorDeXML(Document doc)
	{
		try
		{
			NodeList nlLocalizador = doc.getElementsByTagName("localizador_resiber");
			Node nodoLocalizador = nlLocalizador.item(0);
			String s = nodoLocalizador!=null?nodoLocalizador.getTextContent():null;
			return s;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return null;
		}
	}
	
	public static String obtenerPNR(BeanSheetExcel bean)
	{
		String pnr = "¿?";
		
		try
		{	
			String request = bean.getCodRequest();
			String fchIni  = "";
			String fchFin  = "";
				
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = bean.getTimeLocal();
			fchIni = sdf.format(c.getTime());
			fchFin = sdf.format(c.getTime());
			
			String responseKibana = ejecutarShellScript("scriptReserve.sh " + request + " " + fchIni + " " + fchFin);
			
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
					JSONObject kpi = source.getJSONObject("kpi");
					JSONObject parameters = kpi.getJSONObject("parameters");
					JSONObject parameter1 = parameters.getJSONObject("parameter1");
					pnr = parameter1.getString("locator.string");
				}
	        } 
			
			escribirTraza("INFO", "MyUtil.obtenerPNR(): PNR = " + pnr);
			return pnr;
		}
		catch (Exception e) {
			e.printStackTrace();
			escribirTraza("ERROR", e.getMessage());
			return pnr;
		}
	}
		
}
