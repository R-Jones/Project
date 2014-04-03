package websocket.chat;

import java.util.Arrays;

public class Command implements Runnable {

	final static int INTRANSITIVE = 0b0001;
	final static int TRANSITIVE = 0b0010;
	final static int DITRANSITIVE = 0b0100;
	
	public enum Action { GIVE, GET, ATTACK, USE, RECALL, PUT, WHISPER }
	
	private Action action;
	
	private String subject;
	
	private String object;
	
	private String indirectObject;

	private int count = 1;
	
	public Command(String subject) {
		this.subject = subject;
	}
	
	//This still needs some work.
	public void buildCommand(String input) throws IllegalArgumentException {
		String[] parsedInput = input.split("\\s");
		
		int inputLength = parsedInput.length;
		
		this.clear();
		
		boolean preposed = false;
		

		System.out.println(Arrays.deepToString(parsedInput));
		System.out.println(inputLength);
		
		try {
			action = Action.valueOf(parsedInput[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Syntax Error: That command doesn't exist.");
		}
		System.out.println(action);

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
					 indirectObject = sb.toString();
				}
				else object = sb.toString();
			}
			
			
			//Take 50 dollars from safe. This will store the 50 part.
			else if(s.matches("\\d+")) {
				this.setCount(Integer.valueOf(s));
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
					System.out.println(s);
					if(preposed == true) {
						this.setIndirectObject(s);
						preposed = false;
					}
					else {
						this.setObject(s);
					}
			}
			
		}
		
	}

	private void clear() {
		object = null;
		indirectObject = null;
		action = null;
		setCount(1);
	}

	public Action getAction() {
		return action;
	}
	
	public String getSubject() {
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
	public void run() {
		
	}
	
}
