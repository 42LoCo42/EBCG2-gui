package ebcg2gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class ServerConnection implements Runnable {
	private String hostname;
	private int port;
	private GUI instance;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private boolean gracefulExit;
	
	public ServerConnection(String _hostname, int _port, GUI _instance) {
		hostname = _hostname;
		port = _port;
		instance = _instance;
	}
	
	public void openConnection() {
		try {
			socket = new Socket(hostname, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			// attempt connection
			String msg = in.readLine();
			if(msg != null && msg.split(" ")[0].equals("connect")) {
				JOptionPane.showMessageDialog(null, String.format("Connection established! Client id is %s!\nAttempting to start game...", msg.split(" ")[1]));
				
				// check if a game is running
				send("getgame ");
				msg = in.readLine();
				
				if(msg.split(" ")[0].equals("error")) { // no game is running
					send("newgame ");
					
					instance.getLblServerInfo().setText(String.format("Server: %s, Port: %d", hostname, port));
					instance.startGame();
				}
				else { // a game is running
					JOptionPane.showMessageDialog(null, "A game is already running!\nCheck for connected clients!");
					int opt = JOptionPane.showOptionDialog(null, "What to do?", "EBCG2", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] {
						"Load game",
						"Restart game",
						"Cancel"
						
					}, "Proceed");
					
					if(opt == JOptionPane.YES_OPTION) { // Load game
						send("getgame "); // get game data to be processed by the main loop
					}
					else if(opt == JOptionPane.NO_OPTION) { // Restart game
						send("quitgame ");
						send("newgame ");
					}
					else if(opt == JOptionPane.CANCEL_OPTION) { // Cancel
						closeConnection();
						return;
					}

					instance.getLblServerInfo().setText(String.format("Server: %s, Port: %d", hostname, port));
					instance.startGame();
				}
			}
		} catch(UnknownHostException ex) {
			JOptionPane.showMessageDialog(null, "Unknown server!");
			return;
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "Connection not possible or I/O error whilst connecting!");
			return;
		}
	}
	
	protected void closeConnection() {
		try {
			if(!socket.isConnected()) return;
			
			out.close();
			in.close();
			socket.close();
		} catch(IOException ex) {
			JOptionPane.showMessageDialog(null, "I/O error whilst closing connection!");
		}
	}
	
	public void run() {
		try {
			String msg;
			String[] parts;
			
			while(true) {
				msg = in.readLine();
				if(msg == null) {
					break;
				}
				parts = msg.split(" ");
				
				// message handling
				if(parts[0].equals("error")) {
					if(parts[1].equals("WrongMode")) {
						JOptionPane.showMessageDialog(null, "Operation not allowed in current mode!\n");
					}
					else if(parts[1].equals("YouLose")) {
						JOptionPane.showMessageDialog(null, String.format("You lose!%nScore: %d, max %d", instance.getSSM().getScore(), instance.getSSM().getMaxScore()));
						instance.showServerMenu();
						gracefulExit();
					}
					else if(parts[1].equals("NotEnoughPoints")) {
						JOptionPane.showMessageDialog(null, "You need more points to buy that!");
					}
				}
				else if(parts[0].equals("savestate")) {
					// display new savestate and wait
					instance.getSSM().display(parts[1]);
					Thread.sleep(125);
				}
				else {
					System.out.println(msg);
				}
			}	
		} catch(IOException ex) {
			if(!gracefulExit) { // this will be triggered on closeConnection(), so disable warnings if we know the origin
				JOptionPane.showMessageDialog(null, "I/O error in main loop!");
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public void send(String msg) {
		if(socket.isConnected()) {
			out.println(msg);
			out.flush();
		}
	}
	
	public void gracefulExit() {
		gracefulExit = true;
		closeConnection();
	}
}
