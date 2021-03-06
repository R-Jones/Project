package control;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import entities.Exit;
import entities.Player;
import entities.PlayerManager;
import entities.Room;
import entities.RoomManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import client.Client;
import client.EnvironmentMessage;

public class Command implements Runnable {

	public enum Action { CONNECT, LOGIN, GIVE, GET, ATTACK, USE, RECALL, PUT, WHISPER, 
		DIG, DESC, DESCRIBE, MOVE, L, LOOK, CREATE, LOGOFF, SAY, TELL, NAME, GO }
	
//	final static int INTRANSITIVE = 0b0001;
//	final static int TRANSITIVE = 0b0010;
//	final static int DITRANSITIVE = 0b0100;

	private static RoomManager roomManager = new RoomManager();
	private static PlayerManager playerManager = new PlayerManager();
	
	private static final Logger log = Logger.getLogger(Client.class.getName());
	
//	private static final ExecutorService executor = Executors.newFixedThreadPool(10, command -> new Thread(command));

	private static final ExecutorService executor = Executors.newFixedThreadPool(10, new ThreadFactory() {

		@Override
		public Thread newThread(Runnable command) {
			return new Thread(command);
		}
		
	});
	
	public static void initialize() {
		roomManager = new RoomManager();
		playerManager = new PlayerManager();
    	BasicConfigurator.configure(); 
	}

	private Action action;
	
	private Player subject;
	
	private String object;

	private String indirectObject;
	
	private int count = 1;
	
	private int valency = 1;
	
	private Room setting;

	public Command(Player subject) {
		this.subject = subject;
	}

	public Command buildCommand(String input) throws IllegalArgumentException {
		
			clear();
			
			setSetting(subject.getRoom());
			
			String[] parsedInput = input.split("\\s");
			
			int inputLength = parsedInput.length;
			
			boolean preposed = false;
			
			if(subject.getName() == null) {
				if(inputLength < 3 || !(parsedInput[0].toUpperCase().equals("LOGIN") || parsedInput[0].toUpperCase().equals("CREATE") || parsedInput[0].toUpperCase().equals("CONNECT"))) {
					//TODO Implement create
					throw new IllegalArgumentException("Not logged in! Please use login 'character' 'password' or create 'character' 'password'");
				} 
			}
			
			if(inputLength == 1) {
				setObject(parsedInput[0]);
				return this;
			}
	
			
			try {
				setAction(Action.valueOf(parsedInput[0].toUpperCase()));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Syntax Error: That command doesn't exist.");
			}
	
			int index = 0;
			
			//The first word in the input is always going to be the action(the verb) or one word.
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
						 valency++;
					}
					else if(object != null) {
						setIndirectObject(object);
						object = null;
						setObject(sb.toString());
					} else {
						setObject(sb.toString());
						valency++;
					}
					
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
						if(preposed) {
							setIndirectObject(s);
							valency++;
							preposed = false;
						} else if(object != null) {
							
							setIndirectObject(object);
							object = null;
							setObject(s.toString());
							
						} else {
							setObject(s);
							valency++;
						}
				}	
			}
	//		log.info(command.getAction());
			return this;
		}

	public Action getAction() {
		return action;
	}

	public Player getSubject() {
		return subject;
	}

	public String getObject() {
		return object;
	}

	public String getIndirectObject() {
		return indirectObject;
	}

	public int getCount() {
		return count;
	}

	public Room getSetting() {
		return setting;
	}

	public Command setAction(Action action) {
		this.action = action;
		return this;
	}

	/**
	 * @param count the count to set
	 * @return 
	 */
	public Command setCount(int count) {
		this.count = count;
		return this;
	}

	public Command setIndirectObject(String indirectObject) throws IllegalArgumentException {
		if(this.indirectObject != null) {
			throw new IllegalArgumentException("Syntax Error: Multiple indirect objects");
		}
		this.indirectObject = indirectObject;
		return this;
	}

	public Command setObject(String object) throws IllegalArgumentException {
		if(this.object != null){
			throw new IllegalArgumentException("Syntax Error: Multiple direct objects");
		}
		this.object = object;
		return this;
	}

	public Command setSetting(Room room) {
		this.setting = room;
		return this;
	}

	public Command setSubject(Player subject) {
		this.subject = subject;
		return this;
	}

	//clears everything except subject
	public void clear() {
		object = null;
		indirectObject = null;
		action = null;
		setting = null;
		valency = 1;
		setCount(1);
	}

	public void execute() {
		executor.execute(this);
	}
	
	@Override
	public void run() throws RuntimeException {
		
		log.info("Object: " + object);
		log.info("Subject: " + subject.getName());
		log.info("Indirect Object: " + indirectObject);
		log.info("Action: " + action);
		log.info("Setting: " + setting);
		
		
		//If action is null, then the player has entered a command that doesn't exist.
		//However, if they are, for example, typing the name of an exit, then we still want to go there.
		//TODO It seems that an action like 'l' isn't being recognized as such and is being made an object.
		//It all works out in the end, but...Conceiveably someone could name an exit "attack" or something and then the
		//player won't be able to use an "attack" command.
		if(action == null) {
			
			if(subject.getRoom().hasExit(object)){
				
				move(); //moves to the room indicated by Exit's destination variable.
				
				return;
				
			} else {
				try {
					setAction(Action.valueOf(object.toUpperCase()));
				} catch (Exception e) {
					subject.getClient().message("That command doesn't exist!");
					return;
				}
			}
		}

		switch(action) {
		
			case DIG: dig(); break; 
			case ATTACK: break;
			case GET: break;
			case GIVE: break;
			case PUT: break;
			case RECALL: break;
			case USE: break;
			case NAME: name(); break;
			case WHISPER: case TELL: whisper(); break;
			case DESC: case DESCRIBE: desc(); break;
			case MOVE: case GO: move(); break;
			case SAY: broadcast(subject.getName() + " says, \\\"" + object + "\\\""); break;
			case L: case LOOK: look(); break;
			case CONNECT: case LOGIN: login(); break;
			case LOGOFF: logoff(); break;
			case CREATE: create(); break;
			default: break;
		}
	}

	
	
private void name() {
		// TODO Auto-generated method stub
	if(indirectObject.toUpperCase().equals("HERE")) {
		subject.getRoom().setName(object);
		roomManager.saveRoom(subject.getRoom());
		subject.getClient().message("Room description saved!");
	} 
	else {
		
		try{
			setting.getExits().get(indirectObject).setName(object);
			roomManager.saveExit(setting.getExits().get(indirectObject));
			setting.getExits().put(object, setting.getExits().get(indirectObject));
			setting.getExits().remove(indirectObject);
		} catch (Exception e) {
			subject.getClient().message("Exit doesn't exist!");
		}
	}
	
	look();
	
	
	}

//	private static final Log log = LogFactory.getLog(Connection.class.getName());
	
	private void move() {
		
		Room origin = subject.getRoom();
		Room destination = roomManager.getRoom(origin.getExitDestinationId(object));
		
		origin.removePlayer(subject);
		
		broadcast(subject.getName() + " has left the room.");
		
		setSetting(destination);
		
		broadcast(subject.getName() + " has entered the room.");
		
		destination.addPlayer(subject);
		
		subject.getClient().message("You have entered \\\"" + destination.getName() + "\\\"!");
		subject.setRoom(destination);
		
		look();
	}

	private void create() {
		//TODO Don't store the passwords as plain text in the database!
		//TODO It would probably be better to have an Https servlet handling password based stuff.
		
		try {
			playerManager.createPlayer(indirectObject, object);//Exception is thrown if it fails
		} catch (Exception e) {
			e.printStackTrace();
			subject.getClient().message(e.getMessage());
			return;
		}
		
		subject.getClient().message("New character created! Use LOGIN to now log into your new character");
	}

	private void desc() {
		if(indirectObject.toUpperCase().equals("HERE")) {
			subject.getRoom().setDescription(object);
			roomManager.saveRoom(subject.getRoom());
			subject.getClient().message("Room description saved!");
		}
		else if(indirectObject.toUpperCase().equals("ME")) {
			subject.setDescription(object);
			playerManager.savePlayer(subject);
			subject.getClient().message("Description saved.");
		}
		else {
			try{
				setting.getExits().get(indirectObject).setName(object);
				roomManager.saveExit(setting.getExits().get(indirectObject));
				setting.getExits().put(object, setting.getExits().get(indirectObject));
				setting.getExits().remove(indirectObject);
			} catch (Exception e) {
				subject.getClient().message("Exit doesn't exist!");
			}
		}
		
		look();
		
	}

	private void dig() {
		
		Room newRoom = roomManager.addRoom(); 
		roomManager.addExit(setting, newRoom);
		roomManager.addExit(newRoom, setting);
		
		look();
		
	}

	private void login() {
		
		Player p;
		try {
			p = playerManager.loadPlayer(indirectObject, object);
			p.setClient(subject.getClient());
			subject.getClient().setCharacter(p);
			
			//TODO Putting new logins in the master room for now...
			p.setRoom(roomManager.getRoom(1L));
			setSetting(p.getRoom());
			broadcast(p.getName() + " has entered the room.");
			p.getClient().message("Welcome," + p.getName() + "!");
			p.getRoom().addPlayer(p);
			look();
						
		} catch (Exception e) {
			e.printStackTrace();
			subject.getClient().message(e.toString());
		}
		
		
	}

	private void broadcast(String string) {
		
		for(Player p:setting.getPlayerList().values()) {
			p.getClient().message(string);
		}
		
	}

	private void logoff() {
		subject.getRoom().removePlayer(subject);
		playerManager.removePlayer(subject.getName());
	}

	//TODO Could rename this to something more descriptive.
	private void look() {
				
		subject.getClient().message(
				
				new EnvironmentMessage().setRoomID(setting.getRoomId())
										.setExitList(setting.getExits().keySet())
										.setPcList(setting.getPlayerList().keySet())
										.setRoomDesc(setting.getDescription())
										.setRoomName(setting.getName())				
				);
		
	}

	private void whisper() {
		
		try {
			playerManager.getPlayer(indirectObject).getClient().message(subject.getName() + "��: " + object);
			subject.getClient().message("��" + indirectObject + ": " + object);
		} catch(NullPointerException e) {
			subject.getClient().message(indirectObject + " is not online!");
		}
		
	}
	
}