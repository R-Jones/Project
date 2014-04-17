package client;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class EnvironmentMessageEncoder implements Encoder.Text<EnvironmentMessage> {
	
	@Override
	public String encode(EnvironmentMessage message) throws EncodeException {

		StringBuilder encodedMessage = new StringBuilder("{");
		
		encodedMessage.append("\"type\":" + message.ENVIRONMENTMESSAGE + ",");
		
		encodedMessage.append("\"roomID\":" + message.getRoomID() + ",");
		
		encodedMessage.append("\"roomName\":\"" + message.getRoomName() + "\",");
		
		encodedMessage.append("\"roomDesc\":\"" + message.getRoomDesc() + "\",");
		
		encodedMessage.append("\"pcList\"" + ":[");

		boolean firstPass = true;
		for(String s:message.getPcList()) {
			if(!firstPass) 
				encodedMessage.append(",");
			encodedMessage.append("\"" + s + "\"");
			firstPass = false;
		}
		
		encodedMessage.append("],\"npcList\":[");
		
		
/*	TODO For now, no NPCs...I'll figure out what to do about that later.	
 * 		firstPass = true;
		for(String s:message.getNpcList()) {
			if(!firstPass) 
				encodedMessage.append(",");
			encodedMessage.append("\"" + s + "\"");
			firstPass = false;
		}*/
		
		encodedMessage.append("],\"exitList\":[");
		
		firstPass = true;
		for(String s:message.getExitList()) {
			if(!firstPass)
				encodedMessage.append(",");
			encodedMessage.append("\"" + s + "\"");
			firstPass = false;
		}
		
		return encodedMessage.append("]}").toString();
	}

	
	@Override
	public void destroy() {
		System.out.println("EnvironmentMessage destroyed");
	}

	@Override
	public void init(EndpointConfig arg0) {
		System.out.println("EnvironmentMessage initialized");	
	}
}
