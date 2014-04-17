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
    }

    @OnOpen
    public void start(Session session) {    	
        this.session = session;
    	character.setClient(this);    	
    }

    @OnClose
    public void end() {
        if(character.getName() != null) {
        	new Command().setSubject(character).setAction(Action.LOGOFF).execute();
        }
    }

    @OnMessage
    public void onMessages(String message) {

    	System.out.println("Message received");
    	Command command = new Command();
    	try{
    		//TODO HTMLFilter.filter(message.toString())
    		command.buildCommand(character, message).execute();
    	} catch(Exception e) {
    			message(e.getMessage());
    	}
    }
    
    @OnError
    public void onError(Throwable t) throws Throwable {
    	if(character.getName() != null) {
            new Command().setSubject(character).setAction(Action.LOGOFF).execute();
        }
    }

    public void message(Object message) {   	
    	try {
			session.getBasicRemote().sendObject(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void message(String message) {
    	try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
