package websocket.chat;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


public class Room extends MobileContainer
{	

	private int roomID;
	

	private String name;
	
	private String description;
	

	private Map<String, Room> exits = new ConcurrentHashMap<>();
	

	private ConcurrentHashMap<String, PlayerCharacter> PCs = new ConcurrentHashMap<>();
	

	private ConcurrentHashMap<String, NonPlayerCharacter> NPCs = new ConcurrentHashMap<>();
	
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
	
	public void enterPlayer(PlayerCharacter pc) {
		pc.message(getEnvironmentMessage());
		broadcast(pc.getName() + " has entered the room.");
		PCs.put(pc.getName(), pc);
		pc.message("You have entered " + this.getName());
		pc.setRoom(this);
	}
	
	public void enterNPC(NonPlayerCharacter npc) {
		broadcast(npc.getName() + " has entered the room.");
		NPCs.put(npc.getName(), npc);
		npc.setRoom(this);
	}
	
	public void removePlayer(PlayerCharacter pc) {
		PCs.remove(pc.getName());
		broadcast(pc.getName() + " has left the room.");
	}
	
	public void removeNPC(NonPlayerCharacter npc) {
		NPCs.remove(npc);
		broadcast(npc.getName() + " has left the room.");
	}
	
	public void broadcast(String message) {
		PCs.forEachValue(Long.MAX_VALUE, (client) -> client.message(message));
	}
	
	public EnvironmentMessage getEnvironmentMessage() {
		EnvironmentMessage enviMessage = new EnvironmentMessage();
		enviMessage.setExitList(exits.keySet());
        enviMessage.setRoomDesc(this.getDescription());
        enviMessage.setNpcList(NPCs.keySet());
        enviMessage.setPcList(PCs.keySet());
        enviMessage.setRoomID(this.getRoomID());
        enviMessage.setRoomName(this.getName());
        
        System.out.println(PCs.keySet().toString());
        return enviMessage;
	}
}

