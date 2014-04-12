package websocket.chat;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Command implements Runnable {

	public enum Action { LOGIN, GIVE, GET, ATTACK, USE, RECALL, PUT, WHISPER, DIG, DESC, MOVE, LOOK, TEST }
	
	final static int INTRANSITIVE = 0b0001;
	final static int TRANSITIVE = 0b0010;
	final static int DITRANSITIVE = 0b0100;

	private static RoomManager roomManager = new RoomManager();
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(10, command -> new Thread(command));
	
	public static RoomManager getRoomManager() {
		return roomManager;
	}
	
	private Action action;
	
	private Mobile subject;
	
	private String object;

	private String indirectObject;
	
	private int count = 1;
	
	//This still needs some work.

	public Command() {
		// TODO Auto-generated constructor stub
	}

	public Command(Mobile subject) {
		this.subject = subject;
	}

	public void buildCommand(Mobile subject, String input) throws IllegalArgumentException {
		setSubject(subject);
		
		String[] parsedInput = input.split("\\s");
		
		int inputLength = parsedInput.length;
		
		boolean preposed = false;

		
		try {
			setAction(Action.valueOf(parsedInput[0].toUpperCase()));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Syntax Error: That command doesn't exist.");
		}

		int index = 0;
		
		//The first word in the input is always going to be the action(the verb)
		//At the beginning of every pass, we increment index.
		while(++index < inputLength) {
			
			String s = parsedInput[index];
			
			//Handles quotations, i.e. whisper "Did you get my message?" to John
			if(s.startsWith("\"")){
				StringBuilder sb = new StringBuilder(s.substring(1));//clip off the open quote
				boolean endQuote = false;
				
				while(++index < inputLength){
					s = parsedInput[index];
					sb.append(" " + s);
					if(s.endsWith("\"")){
						sb.setLength(sb.length() - 1);//clip off the end quote
						endQuote = true;
						break;
					}
				}
				
				if(endQuote == false) {
					throw new IllegalArgumentException("Syntax Error: No ending quotation mark found!");
				}
				
				if(preposed) {
					 setIndirectObject(sb.toString());
				}
				else setObject(sb.toString());
			}
			
			
			//Take 50 dollars from safe. command will store the 50 part.
			else if(s.matches("\\d+")) {
				setCount(Integer.valueOf(s));
			}
			
			
			//Now we filter out reserved keywords before storing the first indirect/direct object we come across.
			else switch(s.toUpperCase()) {
				
				case "TO":
					preposed = true;
					break;
				
				case "FROM":
					preposed = true;
					break;
				
				case "A": break;
				
				case "THE": break;
				
				case "UP": break;
				
				case "DOWN": break;
				
				case "IN": 
					preposed = true;
					break;
				
				default: 
					if(preposed == true) {
						setIndirectObject(s);
						preposed = false;
					}
					else {
						setObject(s);
					}
			}	
		}
//		log.info(command.getAction());
	}

	public void clear() {
		object = null;
		indirectObject = null;
		action = null;
		setCount(1);
	}

	private void dig() throws IllegalArgumentException {
		Room newRoom = roomManager.makeRoom();
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

	public void execute() {
		executor.execute(this);
	}
	
	public Action getAction() {
		return action;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	public String getIndirectObject() {
		return indirectObject;
	}

	public String getObject() {
		return object;
	}

	public Mobile getSubject() {
		return subject;
	}

	@Override
	public void run() throws IllegalArgumentException {
	
		switch(action) {
		
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
			
			case TEST: roomManager.testPersistence(); break;
			
			default: break;
		
		
		}
	}

	public void setAction(Action action) {
		this.action = action;
	}


	
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
//	private static final Log log = LogFactory.getLog(Connection.class.getName());
	
	public void setIndirectObject(String indirectObject) throws IllegalArgumentException {
		if(this.indirectObject != null) {
			throw new IllegalArgumentException("Syntax Error: Multiple indirect objects");
		}
		this.indirectObject = indirectObject;
	}

	public void setObject(String object) throws IllegalArgumentException {
		if(this.object != null){
			throw new IllegalArgumentException("Syntax Error: Multiple direct objects");
		}
		this.object = object;
	}

	public void setSubject(Mobile subject) {
		this.subject = subject;
	}
	
}