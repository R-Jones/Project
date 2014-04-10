package model;

import java.io.Serializable;

import javax.persistence.*;

import websocket.chat.EnvironmentMessage;
import websocket.chat.NonPlayerCharacter;
import websocket.chat.PlayerCharacter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The persistent class for the rooms database table.
 * 
 */
@Entity
@Table(name="rooms")
@NamedQuery(name="Room.findAll", query="SELECT r FROM Room r")
public class Room implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="room_id")
	private int roomId;

	private String name;
	
	@Column(name = "description")
	private String description;

	//bi-directional many-to-one association to Exit
	@OneToMany(mappedBy="origin")
	private Set<Exit> exits;
	
	@Transient
	private ConcurrentHashMap<String, PlayerCharacter> PCs = new ConcurrentHashMap<>();
	
	@Transient
	private ConcurrentHashMap<String, NonPlayerCharacter> NPCs = new ConcurrentHashMap<>();

	public Room() {
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Exit> getExits() {
		return this.exits;
	}

	public void setExits(Set<Exit> exits) {
		this.exits = exits;
	}

	public Exit addExit(Exit exit) {
		getExits().add(exit);
		exit.setOrigin(this);

		return exit;
	}

	public Exit removeExit(Exit exit) {
		getExits().remove(exit);
		exit.setOrigin(null);

		return exit;
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
        enviMessage.setRoomID(this.getRoomId());
        enviMessage.setRoomName(this.getName());
        
        System.out.println(PCs.keySet().toString());
        return enviMessage;
	}

}