package websocket.chat;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import websocket.chat.Command.Action;

public class CommandTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildCommand() {
		PlayerCharacter bob = new PlayerCharacter("Bob");
		Command command = CommandExecutor.buildCommand(bob, "get \"a box of crayons\" from box");
		assertTrue(command.getSubject().equals(bob));
		assertTrue(command.getAction().equals(Action.GET));
		assertTrue(command.getObject().equals("a box of crayons"));
		assertTrue(command.getIndirectObject().equals("box"));
		
		command = CommandExecutor.buildCommand(new PlayerCharacter("Ben"), "put 50 pennies in the \"vending machine\"");
		assertTrue(command.getAction().equals(Action.PUT));
		assertTrue(command.getCount() == 50);
		assertTrue(command.getObject().equals("pennies"));
		assertTrue(command.getIndirectObject().equals("vending machine"));
		
		command = CommandExecutor.buildCommand(new PlayerCharacter("Jeffrey"), "Whisper \"Heeeeeeey Fonzie!\" to \"The Fonz\"");
		assertTrue(command.getAction().equals(Action.WHISPER));
		assertTrue(command.getObject().equals("Heeeeeeey Fonzie!"));
		assertTrue(command.getIndirectObject().equals("The Fonz"));
		
//		command.buildCommand("Give a dog a bone");
//		assertTrue(command.getAction().equals(Action.GIVE));
//		assertTrue(command.getObject().equals("bone"));
//		assertTrue(command.getIndirectObject().equals("dog"));
		
		
	}

}
