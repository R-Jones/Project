package websocket.chat;

import java.util.Set;



public class EnvironmentMessage extends Message 
{
	private final int type = Message.ENVIRONMENT;
	

	private String roomName;
	
	
	private String roomDesc;

	
	private Set<String> pcList;

	
	private Set<String> npcList;
	

	private Set<Exit> exitList;


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

	public Set<Exit> getExitList() {
		return exitList;
	}

	public void setExitList(Set<Exit> exitList) {
		this.exitList = exitList;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getRoomID() {
		return roomID;
	}
}

