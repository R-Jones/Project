package client;

import java.util.List;
import java.util.Set;



public class EnvironmentMessage
{
	public final int ENVIRONMENTMESSAGE = 1;
	

	private String roomName;
	
	
	private String roomDesc;

	
	private Set<String> pcList;

	
	private Set<String> npcList;
	

	private Set<String> exitList;


	private long roomID;
	

	public EnvironmentMessage(){
		super();
	}

	public String getRoomName() {
		return roomName;
	}

	public EnvironmentMessage setRoomName(String roomName) {
		this.roomName = roomName;
		return this;
	}
	
	public String getRoomDesc() {
		return roomDesc;
	}

	public EnvironmentMessage setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
		return this;
	}

	public Set<String> getPcList() {
		return pcList;
	}

	public EnvironmentMessage setPcList(Set<String> pcList) {
		this.pcList = pcList;
		return this;
	}

	public Set<String> getNpcList() {
		return npcList;
	}

	public EnvironmentMessage setNpcList(Set<String> npcList) {
		this.npcList = npcList;
		return this;
	}

	public Set<String> getExitList() {
		return exitList;
	}

	public EnvironmentMessage setExitList(Set<String> set) {
		this.exitList = set;
		return this;
	}

	public EnvironmentMessage setRoomID(long l) {
		this.roomID = l;
		return this;
	}

	public long getRoomID() {
		return roomID;
	}
}

