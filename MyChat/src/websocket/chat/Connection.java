

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
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import util.HTMLFilter;

@ServerEndpoint(value = "/chat", encoders = {EnvironmentMessageEncoder.class})
public class Connection {

    private static final Log log = LogFactory.getLog(Connection.class);

    private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<Connection> connections = new CopyOnWriteArraySet<Connection>();

    private final String name;
    private Session session;

    public Connection() {
        name = GUEST_PREFIX + connectionIds.getAndIncrement();
    }


    @OnOpen
    public void start(Session session) {
    	
        this.session = session;
        connections.add(this);
        
        
        System.out.println("boo");
        String message = String.format("* %s %s", name, "has joined.");
        EnvironmentMessage enviMessage = new EnvironmentMessage();
        enviMessage.setExitList(new String[]{"South", "West"});
        enviMessage.setRoomDesc("A dark stormy path");
        enviMessage.setNpcList(new String[]{"Monkey","Cheetah"});
        enviMessage.setPcList(new String[]{"bob", "charlie"});
        enviMessage.setRoomName("Path");
        try {
			session.getBasicRemote().sendObject(enviMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EncodeException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
        String message = String.format("* %s %s", name, "has disconnected.");
        broadcast(message);
    }


    @OnMessage
    public void onMessages(String message) {
        // Never trust the client

        String filteredMessage = String.format("%s: %s", name, HTMLFilter.filter(message.toString()));
        broadcast(filteredMessage);
    	Command command = new Command(name);
    	try{
    		command.buildCommand(message);
    	} catch(IllegalArgumentException e) {
    			broadcast(e.toString());
    	}
    }

    
    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("Chat Error: " + t.toString(), t);
    }


    private static void broadcast(String msg) {
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
    }
}
