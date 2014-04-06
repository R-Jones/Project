package websocket.chat;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MobileManager {
	
	private static ConcurrentHashMap<String, PlayerCharacter> playerCharacters;

	private static ConcurrentHashMap<Integer, NonPlayerCharacter> nonPlayerCharacters;
	
	private static Random random;
	
	static {
		playerCharacters = new ConcurrentHashMap<String, PlayerCharacter>();
		nonPlayerCharacters = new ConcurrentHashMap<Integer, NonPlayerCharacter>();
		random = new Random();
	}
	
	private MobileManager() {}
	
	public static PlayerCharacter getPC(String name) {
		return playerCharacters.get(name);
	}
	
	public static NonPlayerCharacter getNPC(String name) {
		return nonPlayerCharacters.get(name);
	}
	
	public static void putPC(String name, PlayerCharacter pc) {
		playerCharacters.put(name, pc);	
	}
	
	public static void putNPC(Integer npcID, NonPlayerCharacter npc) {
		nonPlayerCharacters.put(npcID, npc);
	}
	
	public static PlayerCharacter removePC(String name) {
		return playerCharacters.remove(name);
	}
	
	public static NonPlayerCharacter removeNPC(Integer npcID) {
		return nonPlayerCharacters.remove(npcID);
	}
	
	public static PlayerCharacter makePC(String name) {
		PlayerCharacter pc = new PlayerCharacter();
		putPC(name, pc);
		pc.setName(name);
		return pc;
	}
	
	public static NonPlayerCharacter makeNPC() {
		NonPlayerCharacter npc = new NonPlayerCharacter();
		Integer npcID = random.nextInt();
		putNPC(npcID,npc);
		npc.setID(npcID);
		return npc;
	}
}
