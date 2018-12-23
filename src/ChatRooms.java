
/* "ChatRoom" is a Manager of the "Room"
 * is used to:
 * 		-create rooms
 * 		-delete rooms
 * 		-add user to room
 * 		-remove user from room
 * 		-gets user
 * 
 * by Tobias Lapper
 * 08.12.2018
 */

import java.util.ArrayList;

public class ChatRooms {

	ArrayList<Room> roomList;
	int roomId=1;

	public ChatRooms() {
		roomList = new ArrayList<Room>();
		roomList.add(new Room("default",roomId));
	}

	
//------------------------------------------------------------------------User functions--------------------------------------------------------
	public void addUserToRoom(User user, String name) {		//Only for ChatServer
		getRoom(name).addUser(user);
	}
	
	public Pair getUser(String name) {						//trys to return user by name
		int count = 0;
		User tempUser = null;
		for (int j = 0; j < roomList.size(); j++) {
			for (int i = 0; i < roomList.get(j).getUserCount(); i++) {
				if(roomList.get(j).getUserByIndex(i).getName().equals(name)) {
					count++;
					tempUser=roomList.get(j).getUserByIndex(i);
				}
			}
		}
		if(count==1) {
			return new Pair(true,tempUser);
		} else if(count==0) {
			return new Pair(false,null);
		} else return new Pair(false,tempUser);
		
	}
	
	public Pair getUserById(int id) {
		int count = 0;
		User tempUser = null;
		for (int j = 0; j < roomList.size(); j++) {
			for (int i = 0; i < roomList.get(j).getUserCount(); i++) {
				if(roomList.get(j).getUserByIndex(i).getId()==id) {
					count++;
					tempUser=roomList.get(j).getUserByIndex(i);
				}
			}
		}
		if(count==1) {
			return new Pair(true,tempUser);
		} else if(count==0) {
			return new Pair(false,null);
		} else return new Pair(false,tempUser);
		
	}

	public String[] getAllUsers() {							//return string[] of all names
		ArrayList<String> allNames = new ArrayList<String>();

		for (int j = 0; j < roomList.size(); j++) {
			for (int i = 0; i < roomList.get(j).getUserCount(); i++) {
				allNames.add(roomList.get(j).getUserByIndex(i).getName()+" |room: "+roomList.get(j).getRoomName());
			}
		}
		String[] names = new String[allNames.size()];
		for(int i = 0;i<allNames.size();i++) {
			names[i]=allNames.get(i);
		}
		return names;
	}
	
	public boolean swtichRoom(User user, Room isIn, String wantsToGo) {	//Switches Rooms
		System.out.println(user.getName() + " wants to change from channel:" + isIn.getRoomName() + " to:" + wantsToGo);
		if (doesRoomExist(wantsToGo)) {
			Room newRoom = getRoom(wantsToGo);
			newRoom.addUser(user);
			user.setRoom(newRoom);
			isIn.removeUser(user);
			return true;
		} else
			return false;

	}
//----------------------------------------------------------------------------------------------------------------------------------------------	
	
	
//------------------------------------------------------------------------Room functions--------------------------------------------------------
	public boolean addRoom(String name) {					//adds room
		boolean nameAlreadyExists = false;
		for (Room room : roomList) {
			if (room.getRoomName().equals(name))
				nameAlreadyExists = true;
		}
		if (!nameAlreadyExists) {
			roomList.add(new Room(name,roomId));
			roomId++;
			return true;
		} else
			return false;
	}
	
	public void removeRoom(String name) {					//removes room
		if (name != "default") {
			for (Room room : roomList) {
				if (room.getRoomName().equals(name))
					roomList.remove(room);
			}
		}
	}
	
	public Room getRoom(String name) {						//return room

		Room found = roomList.get(0);
		for (Room room : roomList) {
			if (room.getRoomName().equals(name))
				found = room;
		}
		return found;
	}
	
	public boolean doesRoomExist(String name) {				//checks if room exists
		boolean found = false;
		for (Room room : roomList) {
			if (room.getRoomName().equals(name))
				found = true;
		}
		return found;
	}
	
	public String[] getRoomNames() {						//return all roomnames
		String[] names = new String[roomList.size()];
		for (int i = 0; i < roomList.size(); i++) {
			names[i] = roomList.get(i).getRoomName();
		}
		return names;
	}
//----------------------------------------------------------------------------------------------------------------------------------------------	
}
