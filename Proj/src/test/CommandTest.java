package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import control.Command;
import control.Command.Action;
import entities.Player;
import entities.Room;

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
		Player bob = new Player("Bob");
		Command command = new Command();
		
		command.buildCommand(bob, "create bob pizza");
		assertTrue(command.getSubject().equals(bob));
		assertTrue(command.getObject().equals("pizza"));
		assertTrue(command.getIndirectObject().equals("bob"));
		assertTrue(command.getAction().equals(Action.CREATE));

		command.buildCommand(bob, "login bob pizza");
		assertTrue(command.getSubject().equals(bob));
		assertTrue(command.getObject().equals("pizza"));
		assertTrue(command.getIndirectObject().equals("bob"));
		assertTrue(command.getAction().equals(Action.LOGIN));
		
		bob.setRoom(new Room());
		
		command.buildCommand(bob, "get \"a box of crayons\" from box");
		assertTrue(command.getSubject().equals(bob));
		assertTrue(command.getAction().equals(Action.GET));
		assertTrue(command.getObject().equals("a box of crayons"));
		assertTrue(command.getIndirectObject().equals("box"));
		
		command.buildCommand(bob, "put 50 pennies in the \"vending machine\"");
		assertTrue(command.getAction().equals(Action.PUT));
		assertTrue(command.getCount() == 50);
		assertTrue(command.getObject().equals("pennies"));
		assertTrue(command.getIndirectObject().equals("vending machine"));
		
		command.buildCommand(bob, "Whisper \"Heeeeeeey Fonzie!\" to \"The Fonz\"");
		assertTrue(command.getAction().equals(Action.WHISPER));
		assertTrue(command.getObject().equals("Heeeeeeey Fonzie!"));
		assertTrue(command.getIndirectObject().equals("The Fonz"));
		
		command.buildCommand(bob, "Give a dog a bone");
		assertTrue(command.getAction().equals(Action.GIVE));
		assertTrue(command.getObject().equals("bone"));
		assertTrue(command.getIndirectObject().equals("dog"));
		
		command.buildCommand(bob, "give Arthur Excalibur");
		assertTrue(command.getObject().equals("Excalibur"));
		assertTrue(command.getIndirectObject().equals("Arthur"));
	}
}