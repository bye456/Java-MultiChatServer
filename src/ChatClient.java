
/*ChatClient connects to ChatServer
 * 
 * by Tobias Lapper
 * 08.12.2018
 */

import socketio.*;
import java.io.*;

public class ChatClient {
	private String host;
	private int port;
	private Socket socket = null;
	private boolean disconected=false;

	ChatClient(String host, int port) {
		this.port = port;
		this.host = host;
	}

	public boolean starteClient() { // Startet den Client
		System.out.println("Connecting to: "+host+":"+port);
		try {
			do {
				System.out.println("Starting...");
				socket = new Socket(host, port);

			} while (!socket.connect());
			new ReaderThread().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void sendeNachricht(String line) { // Sendet eine Nachricht
		try {
			socket.write(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void beendeClient() { // beendet client
		try {
			socket.close();
			socket=null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatClient chatClient = new ChatClient("durchrasten.de", 25565);
		boolean gestartet = chatClient.starteClient();
		do {
			String msg = Tastatur.leseText();
			chatClient.sendeNachricht(msg);
			if(msg=="/exit") {
				chatClient.beendeClient();
			}
			
		} while (true);

	}

	private class ReaderThread extends Thread { // innere Klasse zum Empfangen von Nachrichten

		public void run() {
			do {
				try {

					System.out.println(socket.readLine());
				} catch (IOException e) {
					disconected=true;
				}
			} while (!disconected);
			System.out.println("Disconected");
		}

	}
}
