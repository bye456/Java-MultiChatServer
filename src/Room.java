
/* Room contains function to manage Users in this Room
 *
 * by Tobias Lapper
 * 08.12.2018
 */

import java.util.ArrayList;

public class Room {
	String name;
	ArrayList<User> users;
	int roomId;

	public Room(String name, int roomId) {
		this.name = name;
		this.roomId=roomId;
		users = new ArrayList<User>();
	}

//------------------------------------------------------------------------User functions--------------------------------------------------------
	public void addUser(User user) {
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
	
	public User getUserByIndex(int index) {
		return users.get(index);
	}
	
	public int getUserCount() {
		return users.size();
	}
	
	public String[] getUserNames() {
		String[] names = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			names[i] = users.get(i).getName();
		}
		return names;
	}

	public void sendeAnAlle(String msg) {
		for (User user : users) {
			user.sendMsg(msg);
		}
	}
//----------------------------------------------------------------------------------------------------------------------------------------------	
	
//------------------------------------------------------------------------Room functions--------------------------------------------------------
	public String getRoomName() {
		return this.name;
	}

	public int getRoomId() {
		return this.roomId;
	}
//----------------------------------------------------------------------------------------------------------------------------------------------	
}