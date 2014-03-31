package websocket.chat;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class Room
{
	
	private List<String> exits;
	
	
	private String name;
	
	
	private List<PC> pcList;
	
	
	public Room(){
		super();
	}
	
	public Room(String name) {
		this.name = name;
	}
	
	public void addExit(String exit) {
		exits.add(exit);
	}
	
	
}

