package edu.upc.eetac.dsa.amartin.ejercicio12;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

class Servidor implements Runnable {
	private volatile boolean isRunning = false;
	
	public void run() {
		isRunning = true;
		try {
			DatagramSocket serverSocket = new DatagramSocket(50001);
			byte[] recepcion = new byte[1024];
			byte[] transmision = new byte[1024];
			while (isRunning) {
				DatagramPacket receivePacket = new DatagramPacket(recepcion, recepcion.length);
				serverSocket.receive(receivePacket);
				System.out.println (receivePacket.getLength());
				String peticion = new String(receivePacket.getData()).substring(0,receivePacket.getLength());
				InetAddress ip = receivePacket.getAddress();
				int port = receivePacket.getPort();
				System.out.println ("SRV: Me ha llegado una peticion: |" + peticion + "|");
				System.out.println(peticion.equalsIgnoreCase("Que hora es?"));
				if (peticion.equals("Que hora es?")) {
					SimpleDateFormat df = new SimpleDateFormat ("dd/MM/yyyy hh:mm:ss");
					transmision = df.format(new Date()).getBytes();
				}
				else {
					transmision = "No te entiendo".getBytes();
				}
				DatagramPacket sendPacket = new DatagramPacket(transmision,transmision.length,ip,port);
				serverSocket.send(sendPacket);
			}
			System.out.println ("El servidor se muere");
			serverSocket.close();
		}
		catch (Exception ex) {
			System.out.println ("Error");
		}
	}
	
	public void Kill () {
		isRunning = false;
	}
}

/**
 * Hello world!
 *
 */
public class App
{
	
	
    public static void main( String[] args ) throws Exception
    {
    	Servidor s = new Servidor();
    	Thread ts = new Thread(s);
    	ts.start();
    	
    	Thread.sleep(100);
    	 
    	byte[] sendData = new byte[1024];
    	byte[] receiveData = new byte[1024];
    			
    	DatagramSocket clientSocket = new DatagramSocket();
    	InetAddress ip = InetAddress.getByName("localhost");
    	
    	String msg = "Que hora es?";
    	sendData = msg.getBytes();
    	System.out.println ("Num bytes =" + sendData.length);
    	DatagramPacket sendPacket = new DatagramPacket (sendData,sendData.length,ip,50001);
    	clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String respuesta = new String(receivePacket.getData());
		System.out.println("CLI: Me han respondido: " + respuesta);
		clientSocket.close();
		
		s.Kill();
    }
}
