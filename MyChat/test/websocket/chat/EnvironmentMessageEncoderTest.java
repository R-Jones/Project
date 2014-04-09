package websocket.chat;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import javax.websocket.EncodeException;

import org.junit.BeforeClass;
import org.junit.Test;

public class EnvironmentMessageEncoderTest {

	private static EnvironmentMessage message;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		message = new EnvironmentMessage();
		message.setRoomName("In the forest");
		message.setRoomDesc("There are trees....trees everywhere! Oh god, the humanity!");
		message.setPcList(new HashSet<String>(Arrays.asList(new String[]{"Aerith", "Bob", "Roger", "Jeremy"})));
		message.setNpcList(new HashSet<String>(Arrays.asList(new String[]{"A gopher", "A gnat", "A barber"})));
		message.setExitList(new HashSet<String>(Arrays.asList(new String[]{"south", "west", "north", "east"})));
	}

	@Test
	public void testEncode() {
		EnvironmentMessageEncoder encoder = new EnvironmentMessageEncoder();
		String encodedMessage = "";
		try {
			encodedMessage= encoder.encode(message);
		} catch (EncodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals("{\"type\":1,\"roomName\":\"In the forest\",\"roomDesc\":\"There are trees....trees everywhere! Oh god, the humanity!\",\"pcList\":[\"Aerith\",\"Bob\",\"Roger\",\"Jeremy\"],\"npcList\":[\"A gopher\",\"A gnat\",\"A barber\"],\"exitList\":[\"south\",\"west\",\"north\",\"east\"]}", encodedMessage);
	}

}
