package entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.*;

import client.EnvironmentMessage;


/**
 * The persistent class for the rooms database table.
 * 
 */
@Entity(name = "Room")
@Table(name="rooms")
@NamedQuery(name="Room.findAll", query="SELECT r FROM Room r")
//@SequenceGenerator(name="roomSeq", initialValue=1, allocationSize=100)
public class Room implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="room_id")
	private long roomId;

	private String description = "Describe me!";

	private String name = "Name me!";
	
	//TODO This really IS transient...I just need to learn how to use JPA properly.
	@Transient
	private Map<String, Exit> exits = new ConcurrentHashMap<String, Exit>();
	
	public Map<String, Exit> getExits() {
		return exits;
	}

	public void setExits(Map<String, Exit> exits) {
		this.exits = exits;
	}

	@Transient
	private ConcurrentHashMap<String, Player> playerList = new ConcurrentHashMap<>();

	public ConcurrentHashMap<String, Player> getPlayerList() {
		return playerList;
	}

	public Room() {
	}

	public long getRoomId() {
		return this.roomId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addPlayer(Player pc) {
		playerList.put(pc.getName(), pc);
	}

	public void removePlayer(Player pc) {
		playerList.remove(pc.getName());
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public void addExit(Exit exit) {
		exits.put(exit.getName(), exit);
	}

	public boolean hasExit(String exitName) {
		return exits.containsKey(exitName);
	}
	
	public Long getExitDestinationId(String exitName) {
		return exits.get(exitName).getDestination();	
	}
}