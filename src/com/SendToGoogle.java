package com;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import beans.BeanFormulario;
import beans.BeanSheetExcel;

public class SendToGoogle 
{
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Google Errors NDC";
	
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	
	private static Credential authorize() throws IOException, GeneralSecurityException	 
	{
		InputStream in = SendToGoogle.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();
        
        Credential credential = new AuthorizationCodeInstalledApp(
        		flow, new LocalServerReceiver())
        		.authorize("user");
        		
        return credential;
	}
	
	private static Sheets getSheetService() throws IOException, GeneralSecurityException
	{
		Credential credential = authorize();
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),JacksonFactory.getDefaultInstance(),credential).setApplicationName(APPLICATION_NAME).build();		
	}
	
	public static void toSheet(Object o, BeanFormulario bF) throws IOException, GeneralSecurityException 
	{
		sheetsService = getSheetService();
		String codServ = bF.getCodServicio();
		String hoja = MyUtil.getNombrePestaniaExcel(codServ);
		String SPREADSHEET_ID = bF.getIdExcel();
		boolean agrupar = bF.isRespuestaAgrupada();
		boolean total = bF.isTotal();
		
		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, hoja+"!A:A").execute();
		List<List<Object>> values = response.getValues();
		//List<Object> cells = values.get(0);
		int sizeColumna = values.size();
		int primeraFilaVacia = sizeColumna==1?1:sizeColumna+1;
		
		/* 
		List<BeanSheetExcel> lista = (List<BeanSheetExcel>) o;
		int i = primeraFilaVacia;
		for (BeanSheetExcel bean: lista) {
			i++;
			ValueRange valorCelda = new ValueRange().setValues(Arrays.asList(Arrays.asList(group?bean.getFechaLocal().substring(0,10):bean.getFechaLocal(), group?"":bean.getCodRequest(), bean.getCodError(), bean.getDescripcionError(), bean.getNumDeCasos())));
			UpdateValuesResponse actualizar = sheetsService.spreadsheets().values().update(SPREADSHEET_ID, hoja+"!A"+i, valorCelda).setValueInputOption("RAW").execute();
		}
		*/

        List<BeanSheetExcel> lista = (List<BeanSheetExcel>) o;
        List<ValueRange> data = new ArrayList<ValueRange>();
		int i = primeraFilaVacia;
		int numCasos = 0;
		for (BeanSheetExcel bean: lista) {
			i++;
			String impCodError = bean.getCodError()[0] + (bean.getCodError()[1]!=null?"\n" + bean.getCodError()[1]:"");
			String impDesError = bean.getDescripcionError()[0] + (bean.getDescripcionError()[1]!=null?"\n" + bean.getDescripcionError()[1]:"");
			ValueRange valoresFila = new ValueRange().setRange(hoja+"!A"+i).setValues(Arrays.asList(Arrays.asList(agrupar?bean.getFechaLocal().substring(0,10):bean.getFechaLocal(), agrupar?"":bean.getCodRequest(), impCodError, impDesError, bean.getNumDeCasos(), bean.getTipoError(), bean.getComentarios())));
			data.add(valoresFila);
			numCasos = numCasos + bean.getNumDeCasos();
		}
		BatchUpdateValuesRequest body = new BatchUpdateValuesRequest().setValueInputOption("RAW").setData(data);
        BatchUpdateValuesResponse result = sheetsService.spreadsheets().values().batchUpdate(SPREADSHEET_ID, body).execute();
        
        if (total) {
        	String celda = MyUtil.getUbicacionPestaniaCero(codServ, lista.get(0).getFechaLocal().substring(0, 10));
        	ValueRange valorCelda = new ValueRange().setValues(Arrays.asList(Arrays.asList(numCasos)));
        	UpdateValuesResponse actualizar = sheetsService.spreadsheets().values().update(SPREADSHEET_ID, "0!"+celda, valorCelda).setValueInputOption("RAW").execute();
        }
	}

}
	