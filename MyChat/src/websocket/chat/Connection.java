

/*
*  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package websocket.chat;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;




import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import util.HTMLFilter;

@ServerEndpoint(value = "/chat", encoders = {EnvironmentMessageEncoder.class})
public class Connection {

    private static final Logger log;
    
    static { 
    	log = Logger.getLogger(Connection.class.getName());
    	BasicConfigurator.configure(); 
    	}

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<String, Connection>();

    private final String name;
    private final PlayerCharacter character;
    private Session session;

    public Connection() {
        name = GUEST_PREFIX + connectionIds.getAndIncrement();
        character = MobileManager.makePC(name);
        character.setConnection(this);
        connections.put(name, this);
    }


    @OnOpen
    public void start(Session session) {
    	
        this.session = session;
        character.move(RoomManager.getRoom(RoomManager.MASTERROOM));
        
        String message = String.format("* %s %s", name, "has joined.");

        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(name);
        character.logoff();
        String message = String.format("* %s %s", name, "has disconnected.");
        broadcast(message);
    }


    @OnMessage
    public void onMessages(String message) {
        // Never trust the client

    	Command command;
        String filteredMessage = String.format("%s: %s", name, HTMLFilter.filter(message.toString()));
        broadcast(filteredMessage);
    	try{
    		command = CommandExecutor.buildCommand(character, message);
    		CommandExecutor.executeCommand(command);
    	} catch(IllegalArgumentException e) {
    			broadcast(e.toString());
    	}
    }

    
    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("Chat Error: " + t.toString(), t);
    }

    public void message(Object message) {

    	log.error((session == null) + "Object message.");
//    	System.out.println((session == null) + "Object message.");
    	try {
			session.getBasicRemote().sendObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void message(String message) {
    	System.out.println((session == null) + "String message.");
    	try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			System.out.println("Message exception");
			e.printStackTrace();
		}
    }
    
    private static void broadcast(final String msg) {
    	
    	connections.forEachValue(Long.MAX_VALUE, (client) -> {
    		try {
    			log.info("info test");
    			log.error("Broadcast message.");
    	    	System.out.println((client.session == null) + "Broadcast message.");
				client.session.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				System.out.println("whoa");
				e.printStackTrace();
			}
    	});
    	
    	/*
    	connections.forEachValue(Long.MAX_VALUE, new Consumer<Connection>() {

			@Override
			public void accept(Connection client) {
				  try {
		               client.session.getBasicRemote().sendText(msg);
		              
		            } catch (IOException e) {
		                log.debug("Chat Error: Failed to send message to client", e);
		                connections.remove(client);
		                try {
		                    client.session.close();
		                } catch (IOException e1) {
		                    // Ignore
		                }
		                String message = String.format("* %s %s", client.name, "has been disconnected.");
		                broadcast(message);
		            }
			}
    		
    	});
    	*/
    	
    	
    	
    	/*
        for (Connection client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                log.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s", client.name, "has been disconnected.");
                broadcast(message);
            }
        }
        */
    }
}
