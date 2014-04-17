package entities;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class PlayerManager {
	
	//Contains logged in players.
	private ConcurrentHashMap<String, Player> players;
	
	private Object playerWriteLock = new Object();
	
	@PersistenceUnit(unitName="Proj_PU")
	private EntityManagerFactory emf;
	
	public PlayerManager() {
		emf = Persistence.createEntityManagerFactory("Proj_PU");
		
		players = new ConcurrentHashMap<String, Player>();
	}
	
	public Player createPlayer(String name, String password) {
		EntityManager em = emf.createEntityManager();
		
		if(em.find(Password.class, name) != null) {
			throw new RuntimeException("That player already exists!");
		} 
		
		Player p = new Player(name);
		Password pw = new Password(name, password);
			
		synchronized(playerWriteLock) {
			try {
				em.getTransaction().begin();
				em.persist(p);
				em.persist(pw);
				em.getTransaction().commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		em.close();
		return p;
	}
	
	public Player getPlayer(String name) {
		return players.get(name);
	}
	
	public Player loadPlayer(String name, String password) {
		
		System.out.println("Player Count:" + players.size());
		
		EntityManager em = emf.createEntityManager();
		
		Password pw = em.find(Password.class, name);
		
		if(pw == null) {
			throw new RuntimeException("That player doesn't exist!");
		
		} else if(!pw.getPassword().equals(password)) {
			
			throw new RuntimeException("Password doesn't match!");
			
		}	
		
		Player p = em.find(Player.class, name);
		System.out.println(p.getDescription());
		players.put(name, p);
		
		return p;
	}
	
	public void removePlayer(String player) {
		players.remove(player);
	}

	public void savePlayer(Player subject) {

		synchronized(playerWriteLock) {
			try {
			EntityManager em = emf.createEntityManager();
		
			em.getTransaction().begin();
			em.merge(subject);
			em.getTransaction().commit();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
