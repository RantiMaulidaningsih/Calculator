package Kalkulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MiniCalculator extends JFrame
		implements ActionListener { 
	private JPanel panelAtas = new JPanel();
	private JPanel panelTengah = new JPanel();
	private JPanel panelBawah = new JPanel();

	private JTextField tampilan = new JTextField ("");

	private Button[] tombolFungsi = 
	{ new Button ("Hapus"),new Button ("Reset"), new Button ("CE"), new Button ("^"),
	 new Button ("7"), new Button ("8"),
	 new Button ("9"), new Button ("*"),
	 new Button ("4"), new Button ("5"),
	 new Button ("6"), new Button ("/"),
	 new Button ("1"), new Button ("2"),
	 new Button ("3"), new Button ("-"),
	 new Button ("0"), new Button ("."),
	 new Button ("="), new Button ("+"), };
	
	private JLabel kelompok = 
			 new JLabel ("Kelompok 5 ");
	private char operator = ' ';
	private double dataPertama = 0.0;
	private double dataKedua = 0.0;
	private double dataHasil = 0.0;
	private boolean awalKetikan = true;
	private boolean entryPertama = true;
	private boolean entryDesimal = false;
	
	//---------------------
	// Constructor class
	//---------------------
	public MiniCalculator()
	{
		super ("Kalkulator Project Akhir");
	
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocation (100,100);
		setSize (400,300);
		setResizable (false);
		
		setPanelAtas();
		setPanelTengah();
		setPanelBawah();
		resetNilai();
		
		getContentPane().setLayout (new BorderLayout());
		getContentPane().add (panelAtas, 
				 BorderLayout.NORTH);
		getContentPane().add (panelTengah, 
				 BorderLayout.CENTER);
		getContentPane().add (panelBawah,
				 BorderLayout.SOUTH);
		
		show();
	}
	
	//-------------------------
	// Mereset seluruh nilai
	//-------------------------
	private void resetNilai()
	{
		operator = ' ';
		
		dataPertama = 0.0;
		dataKedua = 0.0;
		dataHasil = 0.0;
		
		awalKetikan = true;
		entryPertama = true;
		entryDesimal = false;
	}
	
	
	//-----------------------
	// Mengatur panel atas
	//-----------------------
	private void setPanelAtas()
	{
		
		tampilan.setEnabled (false);
		tampilan.setHorizontalAlignment (JTextField.RIGHT);
		tampilan.setFont (new Font ("Helvetica",Font.PLAIN,30));
		tampilan.setBackground(Color.BLACK);
		
		panelAtas.setLayout (new BorderLayout());
		panelAtas.add (tampilan, BorderLayout.CENTER);
	}
	
	//-------------------------
	// Mengatur panel tengah
	//-------------------------
	private void setPanelTengah()
	{
		panelTengah.setLayout (new GridLayout (5,4));
		
		for (int i=0; i<5*4; i++)
		{
			tombolFungsi[i].addActionListener (this);
			tombolFungsi[i].setFont (
					new Font ("Helvetica", Font.PLAIN, 15));
			tombolFungsi[i].setBackground(Color.BLACK);
			tombolFungsi[i].setForeground(Color.WHITE);
			panelTengah.add (tombolFungsi[i]);
			
		}
	}
	
	//------------------------
	// Mengatur panel bawah
	//------------------------
	private void setPanelBawah()
	{
		kelompok.setFont (
				new Font ("Helvetica", Font.PLAIN, 15));
		
	
		panelBawah.setLayout (new BorderLayout());
		panelBawah.add (kelompok, BorderLayout.EAST);
	}
	
	//--------------------------------
	// Proses pemisahan data ribuan (ngaruh buat samadengan)
	//--------------------------------
	private String pisahkan (StringBuffer data)
	{
		String temp = data.toString();
	
		if (data.length() > 3)
		{
			temp = data.substring (data.length()-3);
			data.delete (data.length()-3, data.length());
			
			temp = pisahkan (data) + ',' + temp.toString();
			}
		
		return (temp);
		}
	
	//------------------
	// Pemisah ribuan (ini juga ngaruh samadegan)
	//------------------
	private String pisahkanRibuan (double data)
	{
		String string = Double.toString (data);
		int titik = string.indexOf ('.');
		String pecahan = string.substring (titik);
		long bulat = new Double (dataHasil).longValue();
		
		string = Long.toString (bulat);
		string = pisahkan (new StringBuffer (string));
		
		return (string + pecahan);
		}

	//-------------------------------
	// Menghapus karakter terakhir
	//-------------------------------
	private void hapusKarakter()
	{
		if (tampilan.getText().length() > 0)
		{
			StringBuffer data = new StringBuffer (tampilan.getText());
			char terakhir = data.charAt (data.length()-1);
			
			if (terakhir == '.')
				entryDesimal = false;
			
			data.deleteCharAt (data.length()-1);
			tampilan.setText (data.toString());
			}
		}
	
	//-----------------------------
	// Membatalkan data terakhir
	//-----------------------------
	private void batalkanData()
	{
		if (entryPertama)
			dataPertama = 0.0;
		else
			dataKedua = 0.0;
		tampilan.setText ("");
		}
	
	//-------------------------
	// Mengupdate data angka
	//-------------------------
	private void updateData (int index)
	{
		if (awalKetikan)
			tampilan.setText ("");
		
		String label = tombolFungsi[index].getLabel();
		char karakter = label.charAt(0);
		StringBuffer data = new StringBuffer (tampilan.getText());
		tampilan.setText (data.toString() + karakter);
		
		awalKetikan = false;
		}
	
	//----------------------------
	// Mengupdate data operator
	//----------------------------
	private void updateOperator (int index)
	{
		if (entryPertama)
		{
			StringBuffer data = new StringBuffer (tampilan.getText());
			dataPertama = Double.parseDouble (data.toString());
			}
		
		String label = tombolFungsi[index].getLabel();
		operator = label.charAt(0);
		entryPertama = false;
		awalKetikan = true;
		}
	
	//--------------------------------
	// Melakukan proses perhitungan
	//--------------------------------
	private void prosesPerhitungan()
	{
		StringBuffer data = new StringBuffer (tampilan.getText());
		dataKedua = Double.parseDouble (data.toString());
		switch (operator)
		{
		case '+' : dataHasil = dataPertama + dataKedua;
		break;
		case '-' : dataHasil = dataPertama - dataKedua;
		break;
		case '*' : dataHasil = dataPertama * dataKedua;
		break;
		case '/' : dataHasil = dataPertama / dataKedua;
		break;
		case '^' : dataHasil = Math.pow (dataPertama,dataKedua);
		}
		
		//buat samadengan
			tampilan.setText (pisahkanRibuan (dataHasil));
			tampilan.setText (Double.toString (dataHasil));
		
		entryPertama = true;
		awalKetikan = true;
		}
	
	//--------------------------
	// Menambah tanda desimal
	//--------------------------
	private void tambahTandaDesimal()
	{
		if (!entryDesimal && !awalKetikan)
		{
			entryDesimal = true;
			
			StringBuffer data = new StringBuffer (tampilan.getText());
			tampilan.setText (data.toString() + '.');
			}
		}
	
	//-------------------------------
	// Action terhadap respon user
	//-------------------------------
	public void actionPerformed (ActionEvent event)
	{
		Object objek = event.getSource();
		int lokasi = 0;
		
		
			for ( ; lokasi<20; lokasi++)
				if (objek == tombolFungsi[lokasi])
					break;
			switch (lokasi)
			{
			case 0 : hapusKarakter();
					 break;
			case 2 : batalkanData();
					 break;
			case 1 : resetNilai();
					 tampilan.setText ("");
					 break;
			case 16 : ;
			case 12 : ;
			case 13 : ;
			case 14 : ;
			case 8 : ;
			case 9 : ;
			case 10 : ;
			case 4 : ;
			case 5 : ;
			case 6 : updateData (lokasi);
					 break;
			case 3 : ;
			case 7 : ;
			case 11 : ;
			case 15 : ;
			case 19 : updateOperator (lokasi);
					  break;
			case 18 : prosesPerhitungan();
					  break;
			case 17 : tambahTandaDesimal();
					  break;
					  }
			
		}
}