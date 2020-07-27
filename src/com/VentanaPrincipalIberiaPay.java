package com;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import beans.BeanFormularioIberiaPay;

public class VentanaPrincipalIberiaPay implements ActionListener {

	private JFrame  frame;
	private JButton btnEjecutar;
	private JButton btnSalir;
	private JTextField txfFecha;
	private JTextField txfIdExcel;
	private JButton btnMas;
	private JButton btnMenos;
	private Date hoy;
	private SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
	private JTextField txtHoraInicio;
	private JTextField txtHoraFin;
	private JButton btnReset;
	private static JTextPane textPane;
	private static JScrollPane scrollPane;
	private JButton btnAbrirExcel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipalIberiaPay window = new VentanaPrincipalIberiaPay();
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
	public VentanaPrincipalIberiaPay() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1152, 529);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		btnEjecutar = new JButton("Ejecutar");
		btnEjecutar.setBounds(283, 466, 89, 23);
		btnEjecutar.addActionListener(this);
		frame.getContentPane().add(btnEjecutar);
		
		JLabel lblYerros = new JLabel("IberiaPay");
		lblYerros.setFont(new Font("Segoe Print", Font.BOLD, 25));
		lblYerros.setBounds(10, 11, 119, 33);
		frame.getContentPane().add(lblYerros);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 42, 456, 2);
		frame.getContentPane().add(separator);
		
		btnSalir = new JButton("Salir");
		btnSalir.setBounds(377, 466, 89, 23);
		btnSalir.addActionListener(this);
		frame.getContentPane().add(btnSalir);
		
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
		lblV.setBounds(136, 26, 103, 14);
		frame.getContentPane().add(lblV);
		
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
		textPane.setBounds(1, 1, 484, 477);
		frame.getContentPane().add(textPane);	

		scrollPane = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(477, 11, 659, 479);
		scrollPane.setViewportView(textPane);
		frame.getContentPane().add(scrollPane);
		
		btnAbrirExcel = new JButton("Abrir Excel");
		btnAbrirExcel.setBounds(283, 432, 183, 23);
		btnAbrirExcel.addActionListener(this);
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
	      doc.insertString(doc.getLength(), msg, attrs);
	      textPane.setDocument(doc);
	      textPane.update(textPane.getGraphics());
	    } 
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
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
	public void actionPerformed(ActionEvent e) 
	{
		boolean bAnalizar = true;
		BeanFormularioIberiaPay bF_IBPay = null;
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
			String hIni = txtHoraInicio.getText();
			String hFin = txtHoraFin.getText();
		
			textPane.setText(null);
			
			bF_IBPay = new BeanFormularioIberiaPay(fchParaScript, hIni, hFin, txfIdExcel.getText(), bAnalizar);
			
			ProcesoIberiaPay p = new ProcesoIberiaPay();
			p.ejecutar(bF_IBPay);
		}
		
		if (e.getSource().equals(btnSalir)) {
			System.exit(0);
		}
	}
}
