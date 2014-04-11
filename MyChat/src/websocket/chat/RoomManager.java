package websocket.chat;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RoomManager {
	private static ConcurrentHashMap<Integer, Room> rooms;
	private static Random random;
	public static final int MASTERROOM = 1;
	
	static {

//		 = Persistence.createEntityManagerFactory("MyProject").createEntityManager();
//		List<Room> resultList = entityManager.createQuery("select r from Room r", Room.class).getResultList();
		rooms = new ConcurrentHashMap<Integer, Room>();
		
//		for(Room room:resultList) {
//			rooms.put(room.getRoomId(), room);
//		}
		
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
		room.setRoomId(roomID);
		putRoom(roomID, room);
		return room;
	}
	
	public static void saveRoom(Integer roomID) {
		// TODO persist the room. 
	}
	
	public static void main(String[] args) {
		EntityManager entityManager = Persistence.createEntityManagerFactory("MyProject").createEntityManager();
		List<Room> resultList = entityManager.createQuery("select r from Room r", Room.class).getResultList();
		
		for(Room room:resultList) {
		rooms.put(room.getRoomId(), room);
		System.out.println(room.getDescription());
		}
//		EntityManager entityManager = Persistence.createEntityManagerFactory("MyProject").createEntityManager();
//		List<City> resultList = entityManager.createQuery("select c from City c", City.class).getResultList();
//		for(City city:resultList) {
//			System.out.println(city.name);
		}
}
