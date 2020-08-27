package com;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class VentanaPrincipal implements ActionListener { 

	private JFrame frame;
	private JButton btnYerros;
	private JButton btnPagosIberiaPay;
	private JButton btnNewButton;
	
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
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 275);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		btnYerros = new JButton("Yerros");
		btnYerros.setBounds(50, 46, 200, 35);
		btnYerros.setVisible(true);
		btnYerros.addActionListener(this);
		frame.getContentPane().add(btnYerros);
		
		btnPagosIberiaPay = new JButton("Informe IberiaPay");
		btnPagosIberiaPay.setBounds(50, 100, 200, 35);
		btnPagosIberiaPay.setVisible(true);
		btnPagosIberiaPay.addActionListener(this);
		frame.getContentPane().add(btnPagosIberiaPay);
		
		btnNewButton = new JButton("¿¿¿ --- ???");
		btnNewButton.setBounds(50, 155, 200, 35);
		btnNewButton.setVisible(true);
		btnNewButton.addActionListener(this);
		frame.getContentPane().add(btnNewButton);	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource().equals(btnYerros)) {
			frame.setVisible(false);
			VentanaPrincipalYerros.main(null);
		}
		
		if (e.getSource().equals(btnPagosIberiaPay)) {
			frame.setVisible(false);
			VentanaPrincipalIberiaPay.main(null);
		}
		
		if (e.getSource().equals(btnNewButton)) {
			
		}
	}
}
