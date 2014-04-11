package websocket.chat;

import java.util.List;
import java.util.Set;



public class EnvironmentMessage extends Message 
{
	private final int type = Message.ENVIRONMENT;
	

	private String roomName;
	
	
	private String roomDesc;

	
	private Set<String> pcList;

	
	private Set<String> npcList;
	

	private List<Exit> exitList;


	private int roomID;
	

	public EnvironmentMessage(){
		super();
	}

	@Override
	protected int getType() {
		return type;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomDesc() {
		return roomDesc;
	}

	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}

	public Set<String> getPcList() {
		return pcList;
	}

	public void setPcList(Set<String> pcList) {
		this.pcList = pcList;
	}

	public Set<String> getNpcList() {
		return npcList;
	}

	public void setNpcList(Set<String> npcList) {
		this.npcList = npcList;
	}

	public List<Exit> getExitList() {
		return exitList;
	}

	public void setExitList(List<Exit> exits) {
		this.exitList = exits;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getRoomID() {
		return roomID;
	}
}

