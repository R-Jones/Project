package websocket.chat;



public class EnvironmentMessage extends Message 
{
	private final int type = Message.ENVIRONMENT;
	

	private String roomName;
	
	
	private String roomDesc;

	
	private String[] pcList;

	
	private String[] npcList;
	

	private String[] exitList;
	

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

	public String[] getPcList() {
		return pcList;
	}

	public void setPcList(String[] pcList) {
		this.pcList = pcList;
	}

	public String[] getNpcList() {
		return npcList;
	}

	public void setNpcList(String[] npcList) {
		this.npcList = npcList;
	}

	public String[] getExitList() {
		return exitList;
	}

	public void setExitList(String[] exitList) {
		this.exitList = exitList;
	}
}

