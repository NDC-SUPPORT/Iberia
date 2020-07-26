package com;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import beans.BeanFormulario;

public class VentanaPrincipal implements ActionListener {

	private JFrame  frame;
	private JButton btnEjecutar;
	private JButton btnSalir;
	private JTextField txfFecha;
	private JTextField txfIdExcel;
	private JCheckBox chckbxADI;
	private JCheckBox chckbxOC;
	private JCheckBox chckbxFP;
	private JCheckBox chckbxCA;
	private JCheckBox chckbxSE;
	private JCheckBox chckbxBA;
	private JCheckBox chckbxOCH;
	private JCheckBox chckbxIR;
	private JCheckBox chckbxOR;
	private JCheckBox chckbxAS;
	private JCheckBox checkBoxAgruparADI;
	private JCheckBox checkBoxAgruparAS;
	private JCheckBox checkBoxAgruparFP;
	private JCheckBox checkBoxAgruparOC;
	private JCheckBox checkBoxAgruparCA;
	private JCheckBox checkBoxAgruparSE;
	private JCheckBox checkBoxAgruparBA;
	private JCheckBox checkBoxAgruparOCH;
	private JCheckBox checkBoxAgruparIR;
	private JCheckBox checkBoxAgruparOR;
	private JButton btnMas;
	private JButton btnMenos;
	private Date hoy;
	private SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
	private JTextField txtHoraInicio;
	private JTextField txtHoraFin;
	private JCheckBox chckbxAnotarTotalErrores;
	private JButton btnReset;
	private JCheckBox chckbxAnalizar;
	private JButton btnAbrirExcel;
	private static JTextPane textPane;
	private static JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal window = new VentanaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaPrincipal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1152, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		btnEjecutar = new JButton("Ejecutar");
		btnEjecutar.setBounds(283, 502, 89, 23);
		btnEjecutar.addActionListener(this);
		frame.getContentPane().add(btnEjecutar);
		
		JLabel lblYerros = new JLabel("Yerros");
		lblYerros.setFont(new Font("Segoe Print", Font.BOLD, 25));
		lblYerros.setBounds(10, 11, 89, 33);
		frame.getContentPane().add(lblYerros);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 42, 456, 2);
		frame.getContentPane().add(separator);
		
		btnSalir = new JButton("Salir");
		btnSalir.setBounds(377, 502, 89, 23);
		btnSalir.addActionListener(this);
		frame.getContentPane().add(btnSalir);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 154, 456, 301);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		JCheckBox chckbxNewCheckBox_8 = new JCheckBox("Seleccionar todos");
		chckbxNewCheckBox_8.setBounds(13, 5, 147, 25);
		chckbxNewCheckBox_8.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxNewCheckBox_8.setEnabled(false);
		panel.add(chckbxNewCheckBox_8);
		
		JCheckBox chckbxAgruparTodos = new JCheckBox("Agrupar todos");
		chckbxAgruparTodos.setBounds(233, 5, 127, 25);
		chckbxAgruparTodos.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxAgruparTodos.setEnabled(false);
		panel.add(chckbxAgruparTodos);
		
		chckbxAS = new JCheckBox("Availability (AirShopping)");
		chckbxAS.setBounds(13, 61, 173, 23);
		chckbxAS.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxAS);
		
		chckbxFP = new JCheckBox("Fare (FlightPrice)");
		chckbxFP.setBounds(13, 87, 127, 23);
		chckbxFP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxFP);
		
		chckbxADI = new JCheckBox("AirDocIssue");
		chckbxADI.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxADI.setBounds(13, 33, 97, 23);
		panel.add(chckbxADI);
		
		checkBoxAgruparADI = new JCheckBox("Agrupar");
		checkBoxAgruparADI.setBounds(233, 35, 97, 23);
		checkBoxAgruparADI.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(checkBoxAgruparADI);
		
		checkBoxAgruparAS = new JCheckBox("Agrupar");
		checkBoxAgruparAS.setBounds(233, 61, 97, 23);
		checkBoxAgruparAS.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparAS.setSelected(true);
		checkBoxAgruparAS.setEnabled(false);
		panel.add(checkBoxAgruparAS);
		
		checkBoxAgruparFP = new JCheckBox("Agrupar");
		checkBoxAgruparFP.setBounds(233, 87, 97, 23);
		checkBoxAgruparFP.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparFP.setSelected(true);
		panel.add(checkBoxAgruparFP);
		
		chckbxOC = new JCheckBox("OrderCreate");
		chckbxOC.setBounds(13, 113, 107, 23);
		chckbxOC.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxOC);
		
		checkBoxAgruparOC = new JCheckBox("Agrupar");
		checkBoxAgruparOC.setBounds(233, 113, 97, 23);
		checkBoxAgruparOC.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(checkBoxAgruparOC);
		
		chckbxCA = new JCheckBox("OrderCancel");
		chckbxCA.setBounds(13, 139, 107, 23);
		chckbxCA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxCA);
		
		chckbxSE = new JCheckBox("Seat");
		chckbxSE.setBounds(13, 165, 97, 23);
		chckbxSE.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxSE);
		
		chckbxBA = new JCheckBox("Baggage");
		chckbxBA.setBounds(13, 191, 97, 23);
		chckbxBA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxBA);
		
		checkBoxAgruparCA = new JCheckBox("Agrupar");
		checkBoxAgruparCA.setBounds(233, 139, 97, 23);
		checkBoxAgruparCA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparCA.setSelected(false);
		panel.add(checkBoxAgruparCA);
		
		checkBoxAgruparSE = new JCheckBox("Agrupar");
		checkBoxAgruparSE.setBounds(233, 165, 97, 23);
		checkBoxAgruparSE.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparSE.setSelected(true);
		panel.add(checkBoxAgruparSE);
		
		checkBoxAgruparBA = new JCheckBox("Agrupar");
		checkBoxAgruparBA.setBounds(233, 191, 97, 23);
		checkBoxAgruparBA.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparBA.setSelected(true);
		panel.add(checkBoxAgruparBA);
		
		chckbxOCH = new JCheckBox("OrderChange");
		chckbxOCH.setBounds(13, 217, 112, 23);
		chckbxOCH.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxOCH);
		
		chckbxIR = new JCheckBox("ItinReshop");
		chckbxIR.setBounds(13, 243, 97, 23);
		chckbxIR.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxIR);
		
		checkBoxAgruparOCH = new JCheckBox("Agrupar");
		checkBoxAgruparOCH.setBounds(233, 217, 97, 23);
		checkBoxAgruparOCH.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparOCH.setSelected(false);
		panel.add(checkBoxAgruparOCH);
		
		checkBoxAgruparIR = new JCheckBox("Agrupar");
		checkBoxAgruparIR.setBounds(233, 243, 97, 23);
		checkBoxAgruparIR.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparIR.setSelected(true);
		panel.add(checkBoxAgruparIR);
		
		chckbxOR = new JCheckBox("OrderRetrieve");
		chckbxOR.setBounds(13, 269, 146, 23);
		chckbxOR.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(chckbxOR);
		
		checkBoxAgruparOR = new JCheckBox("Agrupar");
		checkBoxAgruparOR.setBounds(233, 269, 97, 23);
		checkBoxAgruparOR.setFont(new Font("Tahoma", Font.PLAIN, 14));
		checkBoxAgruparOR.setSelected(true);
		panel.add(checkBoxAgruparOR);
		
		txfFecha = new JTextField();
		txfFecha.setHorizontalAlignment(SwingConstants.CENTER);
		txfFecha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txfFecha.setBounds(66, 85, 90, 25);
		frame.getContentPane().add(txfFecha);
		txfFecha.setColumns(10);
		hoy= new Date(); 
		txfFecha.setText(sdf.format(hoy));
		
		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFecha.setBounds(10, 91, 46, 14);
		frame.getContentPane().add(lblFecha);
		
		JLabel lblIdExcel = new JLabel("Id Excel:");
		lblIdExcel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblIdExcel.setBounds(10, 57, 59, 14);
		frame.getContentPane().add(lblIdExcel);
		
		txfIdExcel = new JTextField();
		txfIdExcel.setHorizontalAlignment(SwingConstants.LEFT);
		txfIdExcel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txfIdExcel.setColumns(10);
		txfIdExcel.setBounds(67, 53, 399, 24);
		frame.getContentPane().add(txfIdExcel);
		
		btnMas = new JButton("+");
		btnMas.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnMas.setBounds(207, 85, 43, 25);
		btnMas.addActionListener(this);
		frame.getContentPane().add(btnMas);
		
		btnMenos = new JButton("-");
		btnMenos.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnMenos.setBounds(160, 85, 43, 25);
		btnMenos.addActionListener(this);
		frame.getContentPane().add(btnMenos);
		
		JLabel lblV = new JLabel("vs Kibana v5.6.11");
		lblV.setBounds(100, 26, 103, 14);
		frame.getContentPane().add(lblV);
		
		chckbxAnotarTotalErrores = new JCheckBox("Anotar total");
		chckbxAnotarTotalErrores.setFont(new Font("Tahoma", Font.BOLD, 14));
		chckbxAnotarTotalErrores.setBounds(10, 466, 119, 23);
		chckbxAnotarTotalErrores.setSelected(false);
		frame.getContentPane().add(chckbxAnotarTotalErrores);
		
		txtHoraInicio = new JTextField();
		txtHoraInicio.setHorizontalAlignment(SwingConstants.CENTER);
		txtHoraInicio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtHoraInicio.setText("00:00:00");
		txtHoraInicio.setBounds(66, 118, 90, 25);
		frame.getContentPane().add(txtHoraInicio);
		txtHoraInicio.setColumns(10);
		
		txtHoraFin = new JTextField();
		txtHoraFin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtHoraFin.setHorizontalAlignment(SwingConstants.CENTER);
		txtHoraFin.setText("23:59:59");
		txtHoraFin.setColumns(10);
		txtHoraFin.setBounds(160, 118, 90, 25);
		frame.getContentPane().add(txtHoraFin);
		
		JLabel lblHora = new JLabel("Hora:");
		lblHora.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblHora.setBounds(10, 123, 46, 14);
		frame.getContentPane().add(lblHora);
		
		btnReset = new JButton("Reset");
		btnReset.setBounds(253, 118, 68, 24);
		btnReset.addActionListener(this);
		frame.getContentPane().add(btnReset);
		
		chckbxAnalizar = new JCheckBox("Analizar");
		chckbxAnalizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chckbxAnalizar.setBounds(131, 466, 97, 23);
		chckbxAnalizar.setSelected(true);
		frame.getContentPane().add(chckbxAnalizar);
		
		textPane = new JTextPane() {
		    @Override
		    public boolean getScrollableTracksViewportWidth() {
		        return getUI().getPreferredSize(this).width <= getParent().getSize().width;
		    }
		};
		textPane.setForeground(Color.WHITE);
		textPane.setEditable(false);
		textPane.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
		textPane.setBackground(Color.DARK_GRAY);
		textPane.setBounds(135, 42, 1000, 479);
		frame.getContentPane().add(textPane);
		//DefaultCaret caret = (DefaultCaret) textPane.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(477, 11, 659, 514);
		scrollPane.setViewportView(textPane);
		frame.getContentPane().add(scrollPane);
		
		btnAbrirExcel = new JButton("Abrir Excel");
		btnAbrirExcel.addActionListener(this);
		btnAbrirExcel.setBounds(283, 468, 183, 23);
		frame.getContentPane().add(btnAbrirExcel);
	}
	
	public static void showInfo(String msg) {
	    SimpleAttributeSet attrs = new SimpleAttributeSet();
	    StyleConstants.setForeground(attrs, Color.white);
	    showMsg(msg, attrs);
	}
	
	public static void showWarning(String msg) {
	    SimpleAttributeSet attrs = new SimpleAttributeSet();
	    StyleConstants.setForeground(attrs, Color.orange);
	    showMsg(msg, attrs);
	}
	
	public static void showError(String msg) {
	    SimpleAttributeSet attrs = new SimpleAttributeSet();
	    StyleConstants.setForeground(attrs, Color.red);
	    msg =   "- - - ERROR - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" 
	            + "\n"  + msg;    
	    showMsg(msg, attrs);
	}
	
	private static void showMsg(String msg, AttributeSet attrs) {
		System.out.println(msg);
	    Document doc = textPane.getDocument();
	    msg += "\n";
	    try {
	      //String[] ary = msg.split("");
	      //for (int i=0; i<ary.length; i++) {
	      //	  doc.insertString(doc.getLength(), ary[i], attrs);
	      //	  textPane.update(textPane.getGraphics());
	      //}
	      doc.insertString(doc.getLength(), msg, attrs);
	      textPane.setDocument(doc);
	      //textPane.setText(textPane.getText() + msg);
	      //int length = doc.getLength();
	      //DefaultCaret caret = (DefaultCaret) textPane.getCaret();
	      //textPane.setCaretPosition(textPane.getDocument().getLength());
	      //textPane.revalidate();
	      textPane.update(textPane.getGraphics());
	      //scrollPane.getVerticalScrollBar().setValue(0);
	      
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    //catch (BadLocationException ex) { 
	    //	ex.printStackTrace(); 
	    //}
	  }
	
	private Date sumarDia() {
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(hoy); 
        cal.add(Calendar.DATE, 1);
        hoy = cal.getTime();
		return hoy;
	}
	
	private Date restarDia() {
		Calendar cal = Calendar.getInstance(); 
        cal.setTime(hoy); 
        cal.add(Calendar.DATE, -1);
        hoy = cal.getTime();
		return hoy;
	}
	
	/**
	 * Captura evento
	 */
	//prueba
	public void actionPerformed(ActionEvent e) 
	{
		List<BeanFormulario> listaCheckServ = new ArrayList<BeanFormulario>();
		String fchFormulario = txfFecha.getText();
		String fchParaScript = fchFormulario.substring(6) + "-" + fchFormulario.substring(3,5) + "-" + fchFormulario.substring(0,2);
		
		if (e.getSource().equals(btnMas)) {
			Date fchMas = sumarDia();
			txfFecha.setText(sdf.format(fchMas));
		}
		
		if (e.getSource().equals(btnMenos)) {
			Date fchMenos = restarDia();
			txfFecha.setText(sdf.format(fchMenos));
		}
		
		if (e.getSource().equals(btnReset)) {
			txtHoraInicio.setText("00:00:00");
			txtHoraFin.setText("23:59:59");
		}
		
		String os = System.getProperty("os.name").toLowerCase();
		if (e.getSource().equals(btnAbrirExcel)) 
		{
			if (os.contains("mac")) {
				//macOS
				try 
				{
					String textPath = txfIdExcel.getText().replace("\\", "\\\\");
					if (!textPath.isEmpty()) {
						File file = new File(textPath);
						Desktop desktop = Desktop.getDesktop(); 
						if(file.exists()) {desktop.open(file);}
					} else {
						showError("Falta informar \\ruta\\fichero.xls !!!");
					}
				} catch (Exception exMac) {exMac.printStackTrace();}
				
			} else {
				//Windows
				try {				
					String textPath = txfIdExcel.getText().replace("\\", "\\\\");
					if (!textPath.isEmpty()) {
						Runtime.getRuntime().exec("cmd /c start " + textPath);
					} else {
						showError("Falta informar c:\\ruta\\fichero.xls !!!");
					}
				} catch (Exception exWin) {exWin.printStackTrace();}
				
			}
		}
		
		if (e.getSource().equals(btnEjecutar)) 
		{
			boolean bTotal = chckbxAnotarTotalErrores.isSelected();
			boolean bAnalizar = chckbxAnalizar.isSelected();
			String hIni = txtHoraInicio.getText();
			String hFin = txtHoraFin.getText();
		
			textPane.setText(null);
			//textPane.setCaretPosition(0);
			
			//AirDocIssue
			if (chckbxADI.isSelected()) {
				BeanFormulario bF = new BeanFormulario("ADI", checkBoxAgruparADI.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//OrderCreate
		    if (chckbxOC.isSelected()) {
		    	BeanFormulario bF = new BeanFormulario("OC", checkBoxAgruparOC.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
		    }
		    //FlightPrice
			if (chckbxFP.isSelected()) {
				BeanFormulario bF = new BeanFormulario("FP", checkBoxAgruparFP.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//Cancel
			if (chckbxCA.isSelected()) {
				BeanFormulario bF = new BeanFormulario("CA", checkBoxAgruparCA.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//Seat
			if (chckbxSE.isSelected()) {
				BeanFormulario bF = new BeanFormulario("SE", checkBoxAgruparSE.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//Baggage
			if (chckbxBA.isSelected()) {
				BeanFormulario bF = new BeanFormulario("BA", checkBoxAgruparBA.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//OrderChange
			if (chckbxOCH.isSelected()) {
				BeanFormulario bF = new BeanFormulario("OCH", checkBoxAgruparOCH.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//ItinReshop
			if (chckbxIR.isSelected()) {
				BeanFormulario bF = new BeanFormulario("IR", checkBoxAgruparIR.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//OrderRetrieve
			if (chckbxOR.isSelected()) {
				BeanFormulario bF = new BeanFormulario("OR", checkBoxAgruparOR.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			//AirShopping
			if (chckbxAS.isSelected()) {
				BeanFormulario bF = new BeanFormulario("AS", checkBoxAgruparAS.isSelected(), fchParaScript, hIni, hFin, txfIdExcel.getText(), bTotal, bAnalizar);
				listaCheckServ.add(bF);
			}
			
			Proceso p = new Proceso();
			p.ejecutar(listaCheckServ);
		}
		
		if (e.getSource().equals(btnSalir)) {
			System.exit(0);
		}
	}
}
