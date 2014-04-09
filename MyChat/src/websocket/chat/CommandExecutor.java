package websocket.chat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import org.apache.juli.logging.Log;
//import org.apache.juli.logging.LogFactory;

import websocket.chat.Command.Action;

public class CommandExecutor {
	
	private static final ExecutorService executor = Executors.newFixedThreadPool(10, command -> new Thread(command));
//	private static final Log log = LogFactory.getLog(Connection.class.getName());
	
	public static Command buildCommand(Mobile subject, String input) throws IllegalArgumentException {
		Command command = new Command();
		
		command.setSubject(subject);
		
		String[] parsedInput = input.split("\\s");
		
		int inputLength = parsedInput.length;
		
		boolean preposed = false;

		
		try {
			command.setAction(Action.valueOf(parsedInput[0].toUpperCase()));
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
					 command.setIndirectObject(sb.toString());
				}
				else command.setObject(sb.toString());
			}
			
			
			//Take 50 dollars from safe. command will store the 50 part.
			else if(s.matches("\\d+")) {
				command.setCount(Integer.valueOf(s));
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
						command.setIndirectObject(s);
						preposed = false;
					}
					else {
						command.setObject(s);
					}
			}	
		}
//		log.info(command.getAction());
		return command;
	}
	
	public static void executeCommand(Command command) {
		executor.execute(command);
	}

	
}
