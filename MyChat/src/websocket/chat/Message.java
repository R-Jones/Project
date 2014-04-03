package websocket.chat;


public abstract class Message 
{
	public static final int PRIVATE = 0;
	public static final int ENVIRONMENT = 1;

	public Message(){
		super();
	}
	
	abstract protected int getType();
	
}

