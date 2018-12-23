
/* User contains functions to recieve and manage Inputs
 * Is also used to write to client
 * 
 * by Tobias Lapper
 * 08.12.2018
 */



import socketio.*;
import java.io.*;

public class User {
	Socket socket;
	String name;
	int id;
	Room room;
	private ChatRooms chatRooms;
	boolean disconected = false;

	public User(String name, int id, Socket socket, Room room, ChatRooms chatRooms) {
		this.socket = socket;
		this.name = name;
		this.room = room;
		this.chatRooms = chatRooms;				//um funktionen der klasse ChatRooms zu nutzen
		this.id = id;

		try {
			socket.write("Connected to Server" + "\n");
			socket.write("You are in Room: " + room.getRoomName() + "\n");
			new Reciever(this).start();
		} catch (Exception e) {

		}
	}

//------------------------------------------------------------------------User functions--------------------------------------------------------
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}
//----------------------------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------Room functions--------------------------------------------------------
	public void setRoom(Room room) {
		this.room = room;
	}
//----------------------------------------------------------------------------------------------------------------------------------------------

	public Socket getSocket() {
		return this.socket;
	}

//------------------------------------------------------------------------Message functions-----------------------------------------------------
	public void msgHandler(String input) {										//wird benutzt um befehle zu verwalten und erweitern
		if (input.startsWith("/")) { 											//überprüft zuerst, ob ein Befehl ein command ist
			String[] inputSplit = input.split("\\s+");							//teilt die commands auf, um zu überprüfen welcher es ist. Trennt bei " "
			switch (inputSplit[0]) {

			case "/lu":															//listet alle nutzer auf dem Server in allen räumen
				String[] allNames = chatRooms.getAllUsers();
				for (int i = 0; i < allNames.length; i++) {
					sendMsg("--" + allNames[i]);
				}
				break;

			case "/wu":															//private nachrichten an user
				Pair p = chatRooms.getUser(inputSplit[1]);						//bekommt User und boolean (Pair) zurück
				if (!p.bool) {													//wenn user nicht gefunden
					sendMsg("Error while searching for user. Wrong name?");
				} else {
					String msg = "";
					for (String txt : inputSplit) {								//fügt die restlichen strings wider zusammen
						if (!txt.equals(inputSplit[0]) && !txt.equals(inputSplit[1]))
							msg += txt;
					}
					p.usr.sendMsg("«PM " + this.name + ":» " + msg);
					sendMsg("«PM " + this.name + ":» " + msg);
				}
				break;

			case "/cr":															//erstellt räume
				if (chatRooms.addRoom(inputSplit[1])) {
					sendMsg("created room: " + inputSplit[1]);
				} else
					sendMsg("failed to create room! check if already exists");

				break;

			case "/lr":															//listet räume auf
				String[] names = chatRooms.getRoomNames();
				for (int i = 0; i < names.length; i++) {
					if (names[i].equals(room.getRoomName())) {
						sendMsg("» " + names[i]);
					} else
						sendMsg("› " + names[i]);
				}
				break;

			case "/jr":															//tritt raum bei
				if (chatRooms.swtichRoom(this, room, inputSplit[1])) {			//prüft ob der beitritt erfolgreich war
					sendMsg("switched room to: " + room.getRoomName());
				} else
					sendMsg("failed to join: " + inputSplit[1] + " | still in room: " + room.getRoomName());
				break;

			case "/er":															//geht in den default room zurück
				if (chatRooms.swtichRoom(this, room, "default")) {
					sendMsg("switched room to: default");
				} else
					sendMsg("failed to join: default");
				break;

			case "/ec":															//beendet die verbindung zum client
				disconected = true;
				break;

			case "/rn":															//zum umbenennen des nutzers
				Pair pp = chatRooms.getUser(inputSplit[1]);
				if (!pp.bool) {
					name = inputSplit[1];
					sendMsg("changed name to: " + name);
				} else
					sendMsg("user " + inputSplit[1] + " already exists");
				break;

			case "/help":														//help command
				sendMsg("/lu -ListUsers");
				sendMsg("/rn <name> -Rename Yourself");
				sendMsg("/wu <name> <msg>-Wisper to User");
				sendMsg("/cr <name> -Create Room");
				sendMsg("/lr -List Rooms");
				sendMsg("/jr <name> -Join Room");
				sendMsg("/er -Exit Room (return to default)");
				sendMsg("/ec -Exits Server");
				sendMsg("/help -What do you think?");
				break;

			case "/cls":														//um alte nachrichten nach oben zu schieben
				for (int i = 0; i < 50; i++) {
					sendMsg("");
				}
				break;

			default:
				break;
			}

		} else {
			this.room.sendeAnAlle('<' + name + ":> " + input);
		}
	}

	public void sendMsg(String msg) {											//sendet nachrichten an user
		try {
			socket.write(msg + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//----------------------------------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------Reciever Thread-------------------------------------------------------
	public class Reciever extends Thread {										//empfänt nachrichten des clients
		User user;

		public Reciever(User user) {
			this.user = user;
		}

		public void run() {
			disconected = false;
			do {
				try {
					msgHandler(socket.readLine());
				} catch (Exception e) {
					disconected = true;
				}
			} while (!disconected);
			System.out.println("Disconected: " + name);
			room.removeUser(this.user);

		}
	}
}
