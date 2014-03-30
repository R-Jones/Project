package websocket.chat;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class EnvironmentMessageEncoder implements Encoder.Text<EnvironmentMessage> {
	
	@Override
	public String encode(EnvironmentMessage message) throws EncodeException {
		
		JsonArrayBuilder encodedPClist = Json.createArrayBuilder(); 
		JsonArrayBuilder encodedNPClist = Json.createArrayBuilder(); 
		JsonArrayBuilder encodedExitList = Json.createArrayBuilder(); 
		
		for(String pcName:message.getPcList()) {
			encodedPClist.add(pcName);
		}
		
		for(String npcName:message.getNpcList()) {
			encodedNPClist.add(npcName);
		}
		
		for(String exit:message.getExitList()) {
			encodedExitList.add(exit);
		}
		
		JsonObject encodedMessage = Json.createObjectBuilder()
				.add("type",message.getType())
				.add("roomDesc", message.getRoomDesc())
				.add("pcList", encodedPClist)
				.add("npcList", encodedNPClist)
				.add("exitList", encodedExitList)
				.build();
		
		return encodedMessage.toString();
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
