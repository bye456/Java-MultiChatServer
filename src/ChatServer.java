
/* ChatServer starts a Server on Port 5001
 * Accepts all Clients
 * 
 * Structure:
 * "ChatServer" creates "ChatRooms"
 * "ChatRooms" contains an ArrayList with "Room"s
 * "Room" has an ArrayList of "User"
 * "User" then contains the Socket
 * 
 * by Tobias Lapper
 * 08.12.2018
 */


import socketio.*;

public class ChatServer {
	private int port;
	private ServerSocket serverSocket;
	private ChatRooms chatRooms;
	private Room standard;

	public ChatServer(int port) {
		try {
			this.port = port;
			this.serverSocket = new ServerSocket(port);
			this.chatRooms=new ChatRooms();
			standard=this.chatRooms.getRoom("default");
		} catch (Exception e) {
		}
		new Acceptor();
	}
	
	
	
	public class Acceptor extends Thread{
		public Acceptor() {
			this.start();
			
		}
		
		public void run() {
			int i = 1;
			System.out.println("Started Server on Port "+port); // ist eine Endlosschleife die alle Clients akzeptiert
			do {
				try {
					chatRooms.addUserToRoom(new User("User"+i,i,serverSocket.accept(),standard,chatRooms), "default");
					System.out.println("User"+i+" accepted");
					i++;
				} catch (Exception e) {
				}
			} while (true);
		}
	}

	public static void main(String[] args) {
		ChatServer c = new ChatServer(5001);
	}

}
