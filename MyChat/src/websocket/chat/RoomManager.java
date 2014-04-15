package websocket.chat;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class RoomManager {
	private ConcurrentHashMap<Integer, Room> rooms;
	private static Random random;
	public static final int MASTERROOM = 1;
	private EntityManager entityManager;
	
	public RoomManager() {
		rooms = new ConcurrentHashMap<Integer, Room>();
		
		random = new Random();
		makeRoom(MASTERROOM);
		getRoom(MASTERROOM).setDescription("One room to rule them all, One room to find them," +
										   "One room to bring them all and in the darkness bind them.");
		getRoom(MASTERROOM).setName("MASTERROOM");
		
		entityManager = Persistence.createEntityManagerFactory("MyProject").createEntityManager();
	}
	
	public void putRoom(Integer roomID, Room room) {
		rooms.put(roomID, room);
	}
	
	public Room getRoom(Integer roomID) {
		return rooms.get(roomID);
	}
	
	public Room makeRoom() {
		Integer roomID = random.nextInt(20000);
		while(rooms.containsKey(roomID)) {
			roomID = random.nextInt();
		}
		return makeRoom(roomID);
	}
	
	public Room makeRoom(Integer roomID) {
		Room room = new Room();
		room.setRoomId(roomID);
		putRoom(roomID, room);
		return room;
	}
	
	public void persistRoom(Integer roomID) {
		// TODO persist the room. 
	}
	
	public void testPersistence() {
		List<Room> resultList = entityManager.createQuery("select r from Room r", Room.class).getResultList();
		for(Room room:resultList) {
			rooms.put(room.getRoomId(), room);
			System.out.println(room.getDescription());
			}
	}
	
	static public void main(String[] args) {
		EntityManager myEntityManager = Persistence.createEntityManagerFactory("MyProject").createEntityManager();

//		Query query = myEntityManager.createNamedQuery("Room.findAll");
	
		Query query = myEntityManager.createQuery("select r from Room r", Room.class);
		
//		List<Room> resultList = myEntityManager.createQuery("SELECT r FROM Room r", Room.class).getResultList();
		
//		for(Room room:resultList) {
		//rooms.put(room.getRoomId(), room);
//		System.out.println(room.getDescription());
//		}
//		EntityManager entityManager = Persistence.createEntityManagerFactory("MyProject").createEntityManager();
//		List<City> resultList = entityManager.createQuery("select c from City c", City.class).getResultList();
//		for(City city:resultList) {
//			System.out.println(city.name);
		}
}
