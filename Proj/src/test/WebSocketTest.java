package test;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class WebSocketTest {
	private EntityManager myEntityManager;

  @OnMessage
  public void onMessage(String message, Session session) 
    throws IOException, InterruptedException {
  
	  
	  
	List<TestRoom> resultList = myEntityManager.createQuery("SELECT r FROM TestRoom r", TestRoom.class).getResultList();
	
	for(TestRoom room:resultList) {
		System.out.println(room.getDescription());
		session.getBasicRemote().sendText(room.getDescription());
	}
	  
    // Print the client message for testing purposes
    System.out.println("Received: " + message);
  
    // Send the first message to the client
    session.getBasicRemote().sendText("This is the first server message");
  
    // Send 3 messages to the client every 5 seconds
    int sentMessages = 0;
    while(sentMessages < 3){
      Thread.sleep(5000);
      session.getBasicRemote().
        sendText("This is an intermediate server message. Count: " 
          + sentMessages);
      sentMessages++;
    }
  
    // Send a final message to the client
    session.getBasicRemote().sendText("This is the last server message");
  }
  
  @OnOpen
  public void onOpen() {
    System.out.println("Client connected");
    myEntityManager = Persistence.createEntityManagerFactory("Proj").createEntityManager();
  }

  @OnClose
  public void onClose() {
    System.out.println("Connection closed");
  }
}
