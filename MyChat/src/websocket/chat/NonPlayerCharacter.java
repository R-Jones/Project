package websocket.chat;

public class NonPlayerCharacter {

	private Integer ID;
	
	private String name;
	
	private Room room;

	public void setID(Integer npcID) {
		// TODO Auto-generated method stub
		this.ID = npcID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getID() {
		return ID;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}
