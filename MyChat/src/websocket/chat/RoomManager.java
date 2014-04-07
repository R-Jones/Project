package websocket.chat;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
	private static ConcurrentHashMap<Integer, Room> rooms;
	private static Random random;
	public static final int MASTERROOM = 1;
	
	static {
		rooms = new ConcurrentHashMap<Integer, Room>();
		random = new Random();
		makeRoom(MASTERROOM);
		getRoom(MASTERROOM).setDescription("One room to rule them all, One room to find them," +
										   "One room to bring them all and in the darkness bind them.");
		getRoom(MASTERROOM).setName("MASTERROOM");
	}
	
	public static void putRoom(Integer roomID, Room room) {
		rooms.put(roomID, room);
	}
	
	public static Room getRoom(Integer roomID) {
		return rooms.get(roomID);
	}
	
	public static Room makeRoom() {
		Integer roomID = random.nextInt(20000);
		while(rooms.containsKey(roomID)) {
			roomID = random.nextInt();
		}
		return makeRoom(roomID);
	}
	
	public static Room makeRoom(Integer roomID) {
		Room room = new Room();
		room.setRoomID(roomID);
		putRoom(roomID, room);
		return room;
	}
	
	public static void saveRoom(Integer roomID) {
		// TODO persist the room. 
	}
}
