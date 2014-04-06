package websocket.chat;

public abstract class Mobile {

	private String name;
		
	private String description;
	
	private Room room;

	private Integer ID;
	
	


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


	public void setID(Integer ID) {
		this.ID = ID;
		// TODO Auto-generated method stub
		
	}
}
