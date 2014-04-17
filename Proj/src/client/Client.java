

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
package client;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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

import control.Command;
import control.Command.Action;
import entities.Player;

@ServerEndpoint(value = "/socket", encoders = {EnvironmentMessageEncoder.class})
public class Client {

    private static final Logger log;
    
    static { 
    	log = Logger.getLogger(Client.class.getName());
    	BasicConfigurator.configure(); 
    	}

    private static final Set<Client> connections = new HashSet<Client>();

    
    private Player character;
    
    public Player getCharacter() {
		return character;
	}

	public void setCharacter(Player character) {
		this.character = character;
	}

	private Session session;

    public Client() {
    	
    	character = new Player();//A temporary dummy player object.
    	connections.add(this);
    	
    }


    @OnOpen
    public void start(Session session) {
        this.session = session;

    	character.setClient(this);
        
    }


    @OnClose
    public void end() {
    	System.out.println("boo");
        connections.remove(this);
        if(character.getName() != null) {
        	new Command().setSubject(character).setAction(Action.LOGOFF).execute();
        }
    }


    @OnMessage
    public void onMessages(String message) {
        // Never trust the client

    	System.out.println("Message received");
    	Command command = new Command();
    	try{
    		//TODO HTMLFilter.filter(message.toString())
    		command.buildCommand(character, message);
    		command.execute();
    	} catch(Exception e) {
 //   			e.printStackTrace();
    			message(e.toString());
    	}
    }

    
    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("Chat Error: " + t.toString(), t);
        connections.remove(this);
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
}
