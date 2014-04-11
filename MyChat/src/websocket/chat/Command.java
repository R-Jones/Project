package websocket.chat;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class Command implements Runnable {

	final static int INTRANSITIVE = 0b0001;
	final static int TRANSITIVE = 0b0010;
	final static int DITRANSITIVE = 0b0100;
	
	public enum Action { GIVE, GET, ATTACK, USE, RECALL, PUT, WHISPER, DIG, DESC, MOVE, LOOK }
	
	private Action action;
	
	private Mobile subject;
	
	private String object;
	
	private String indirectObject;

	private int count = 1;
	
	public Command(Mobile subject) {
		this.subject = subject;
	}
	
	//This still needs some work.

	public void setAction(Action action) {
		this.action = action;
	}

	public void setSubject(Mobile subject) {
		this.subject = subject;
	}

	public Command() {
		// TODO Auto-generated constructor stub
	}

	public void clear() {
		object = null;
		indirectObject = null;
		action = null;
		setCount(1);
	}

	public Action getAction() {
		return action;
	}
	
	public Mobile getSubject() {
		return subject;
	}

	public String getObject() {
		return object;
	}

	public String getIndirectObject() {
		return indirectObject;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public void setObject(String object) throws IllegalArgumentException {
		if(this.object != null){
			throw new IllegalArgumentException("Syntax Error: Multiple direct objects");
		}
		this.object = object;
	}

	public void setIndirectObject(String indirectObject) throws IllegalArgumentException {
		if(this.indirectObject != null) {
			throw new IllegalArgumentException("Syntax Error: Multiple indirect objects");
		}
		this.indirectObject = indirectObject;
	}


	
	@Override
	public void run() throws IllegalArgumentException {
	
		switch(this.action) {
		
			case DIG: dig(); break;
			
			case ATTACK: break;
		
			case GET: break;
		
			case GIVE: break;
		
			case PUT: break;
		
			case RECALL: break;
		
			case USE: break;
		
			case WHISPER: break;
			
			case DESC: break;
			
			case MOVE: break;
		
			case LOOK: {
				if(object == null) {
					subject.look();
				}
				break;
			}
			
			default: break;
		
		
		}
	}

	public void dig() throws IllegalArgumentException {
		Room newRoom = RoomManager.makeRoom();
		newRoom.setName(this.getObject());
		Exit newExit = new Exit();
		newExit.setName(String.valueOf(subject.getRoom().getRoomId()));
		newExit.setDestination(newRoom);
		newExit.setOrigin(subject.getRoom());
		subject.getRoom().addExit(newExit);
		
//		subject.getRoom().addExit(String.valueOf(newRoom.getRoomId()), newRoom);
//		newRoom.addExit(String.valueOf(subject.getRoom().getRoomId()), subject.getRoom());
		
		subject.move(newRoom);
	}
	
}