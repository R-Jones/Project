package websocket.chat;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class EnvironmentMessageEncoder implements Encoder.Text<EnvironmentMessage> {
	
	@Override
	public String encode(EnvironmentMessage message) throws EncodeException {
		
		/*
		JsonArrayBuilder encodedPClist = Json.createArrayBuilder(); 
		JsonArrayBuilder encodedNPClist = Json.createArrayBuilder(); 
		JsonArrayBuilder encodedExitList = Json.createArrayBuilder(); 
		
		if(message.getPcList() == null) {
			encodedPClist.addNull();
		}
		else {
			for(String pcName:message.getPcList()) {
				encodedPClist.add(pcName);
			}
		}
		
		if(message.getNpcList() == null) {
			encodedNPClist.addNull();
		}
		else {
			for(String npcName:message.getNpcList()) {
				encodedNPClist.add(npcName);
			}
		}
		
		if(message.getExitList() == null) {
			encodedExitList.addNull(); 
		}
		else {
			for(String exit:message.getExitList()) {
				encodedExitList.add(exit);
			}
		}
		
		JsonObject encodedMessage = Json.createObjectBuilder()
				.add("type",message.getType())
				.add("roomDesc", message.getRoomDesc())
				.add("pcList", encodedPClist)
				.add("npcList", encodedNPClist)
				.add("exitList", encodedExitList)
				.build();
		*/
		StringBuilder encodedMessage = new StringBuilder("{");
		
		encodedMessage.append("\"type\":" + message.getType() + ",");
		
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
		
		firstPass = true;
		for(String s:message.getNpcList()) {
			if(!firstPass) 
				encodedMessage.append(",");
			encodedMessage.append("\"" + s + "\"");
			firstPass = false;
		}
		
		encodedMessage.append("],\"exitList\":[");
		
		firstPass = true;
		for(Exit s:message.getExitList()) {
			if(!firstPass)
				encodedMessage.append(",");
			encodedMessage.append("\"" + s.getName() + "\"");
			firstPass = false;
		}
		
		return encodedMessage.append("]}").toString();
	}

	
	
/*
	public String toJSON(Object... fields) {
		StringBuilder jsonString = new StringBuilder("{");
		
		for(int i = 0;i < fields.length;i++) {
			Object o = fields[i];
			if(o instanceof Collection) {
				jsonString.append("[");
				for(Object e:(Collection) o) {
					jsonString.append(e.toString() + )
				}
			}
		}
		
		return jsonString.append("}").toString();
	}
*/
	
	@Override
	public void destroy() {
		System.out.println("EnvironmentMessage destroyed");
	}

	@Override
	public void init(EndpointConfig arg0) {
		System.out.println("EnvironmentMessage initialized");	
	}
}
