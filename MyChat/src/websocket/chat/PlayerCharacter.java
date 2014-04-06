package websocket.chat;

import java.util.List;

public class PlayerCharacter
{
	Connection connection;
	
	List<InventoryEntry> inventory;
	
	
	private String name;
		
	
	private String description;
	
	
	private Room room;
	
	
	public PlayerCharacter(){
		super();
		System.out.println("boo");
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

}

