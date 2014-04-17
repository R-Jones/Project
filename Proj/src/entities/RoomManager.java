package entities;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class RoomManager {
	
	//Represents current rooms(right now ALL rooms)
	private ConcurrentHashMap<Long, Room> rooms;
	public final Long MASTERROOM = 0L;
	
	@PersistenceUnit(unitName="Proj_PU")
	private EntityManagerFactory emf;
	Long roomNextId = 1L;
	Long exitNextId = 1L;
	
	//Precondition: This assumes that the database is automatically sorted by key in ascending order.
	//If it doesn't, bad things happen...
	public RoomManager() {
		
		rooms = new ConcurrentHashMap<Long, Room>();

		emf = Persistence.createEntityManagerFactory("Proj_PU");
		
		EntityManager em = emf.createEntityManager();
		
		List<Room> roomList = em.createQuery("SELECT r FROM Room r", Room.class).getResultList();

		if(!roomList.isEmpty()){
			
			roomNextId = roomList.get(roomList.size() - 1).getRoomId() + 1;

			for(Room r:roomList) {
				rooms.put(r.getRoomId(), r);
			}
		
			List<Exit> exitList = em.createQuery("SELECT e FROM Exit e", Exit.class).getResultList();
			
			
			if(!exitList.isEmpty()) {
			
				exitNextId = exitList.get(exitList.size() - 1).getExitId() + 1;
				
				/*TODO This doesn't error-check the supposed roomIDs and so has null pointer potential.
				//There are two ways of fixing this...
				//1. Put in some error-checking
				//2. Take advantage of JPA's features to make sure that all these keys cascade correctly.  
				*/
				for(Exit exit:exitList) {
					rooms.get(exit.getRoomId()).addExit(exit);
				}
			}
			
		}		
	}
	
	public Room getRoom(Long roomID) {
		return rooms.get(roomID);
	}
	
	public Room addRoom() {
		
		System.err.println(roomNextId);
		Room r = new Room();
		EntityManager em = emf.createEntityManager();
		synchronized(roomNextId) {
			try {

				em.getTransaction().begin();
				r.setRoomId(roomNextId);
				em.persist(r);
				em.getTransaction().commit();
				rooms.put(roomNextId, r);
				roomNextId++;
			} 
			catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("ruh roh");
			}
		}
		return r;
	}

	public void addExit(Room origin, Room destination) {
		
		Exit exit = new Exit();
		exit.setRoomId(origin.getRoomId());
		exit.setDestination(destination.getRoomId());
		exit.setName("Room" + String.valueOf(exit.getDestination()));
		
		EntityManager em = emf.createEntityManager();

		
		synchronized(exitNextId) {
			try {		
				exit.setExitId(exitNextId);
				em.getTransaction().begin();
				em.persist(exit);
				em.getTransaction().commit();
				exitNextId++;
				origin.addExit(exit);
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	public void saveRoom(Room room) {

		synchronized(roomNextId) {
			
			try {
				EntityManager em = emf.createEntityManager();
		
				em.getTransaction().begin();
				em.merge(room);
				em.getTransaction().commit();
		
				em.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveExit(Exit exit) {
		
		synchronized(exitNextId) {
			
			try {
				EntityManager em = emf.createEntityManager();
		
				em.getTransaction().begin();
				em.merge(exit);
				em.getTransaction().commit();
		
				em.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
