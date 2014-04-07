package websocket.chat;

import java.util.List;

public class PlayerCharacter extends Mobile
{
	private Connection connection;
	
	List<InventoryEntry> inventory;
	
	
	private String name;
		
	
	private String description;
	
	
	private Room room;
	
	
	public PlayerCharacter(){
		super();
	}


	public PlayerCharacter(String name) {

		super();
		this.name=name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Room getRoom() {
		return room;
	}


	public void setRoom(Room room) {
		this.room = room;
	}
	
	public void move(Room room) {
		if(getRoom() != null){
			message("You have left " + this.getRoom().getName());
			getRoom().removePlayer(this);
		}
//		this.message(room.getEnvironmentMessage());
		room.enterPlayer(this);
	}
	
	public void roomLook() {
		connection.message(this.getRoom().getEnvironmentMessage());
	}
	
	public void message(String message) {
		System.out.println(message);
		connection.message(message);
	}
	
	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	public void message(Object message) {
		System.out.println(message);
		connection.message(message);
	}


	@Override
	public void look() {
		connection.message(getRoom().getEnvironmentMessage());
	}


	public void logoff() {

		System.out.println(name + "logging off now");
		getRoom().removePlayer(this);
		MobileManager.removePC(name);
	}

}

