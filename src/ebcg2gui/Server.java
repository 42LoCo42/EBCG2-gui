package ebcg2gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Server implements Runnable {
	private String hostname;
	private int port;
	
	public Server(String _hostname, int _port) {
		hostname = _hostname;
		port = _port;
	}
	public void run() {
		try (
			Socket socket = new Socket(hostname, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			
		) {
			String msg = in.readLine();
		} catch(UnknownHostException ex) {
			JOptionPane.showMessageDialog(null, "Unknown server!");
			return;
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "Harmless IO error ahm.. CRITICAL SOFTWARE INSTABILITY!!!!\n"
					+ "This shouldn't, so tell Leon!");
			return;
		}
	}
}
