package websocket.chat;
import java.util.Map;


public class Room extends MobileContainer
{	
	private int roomID;
	
	private String name;
	
	private String description;
	
	private Map<String, Room> exits;
	
	private Map<String, PlayerCharacter> PCs;
	
	private Map<String, NonPlayerCharacter> NPCs;
	
	public Room(){
		super();
	}
	
	public Room(String name) {
		this.setName(name);
	}
	
	public void addExit(String exitName, Room target) {
		exits.put(exitName, target);
	}

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
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
}

