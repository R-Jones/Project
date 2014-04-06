package websocket.chat;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
	private static ConcurrentHashMap<Integer, Room> rooms;
	private static Random random;
	private static final int MASTERROOM = 1;
	
	static {
		rooms = new ConcurrentHashMap<Integer, Room>();
		random = new Random();
		makeRoom(MASTERROOM);
	}
	
	public static void putRoom(Integer roomID, Room room) {
		rooms.put(roomID, room);
	}
	
	public static Room getRoom(Integer roomID) {
		return rooms.get(roomID);
	}
	
	public static Room makeRoom() {
		Integer roomID = random.nextInt();
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
}
