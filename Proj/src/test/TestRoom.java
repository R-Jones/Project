package test;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the rooms database table.
 * 
 */
@Entity
@Table(name="rooms")
@NamedQuery(name="TestRoom.findAll", query="SELECT r FROM TestRoom r")
public class TestRoom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="room_id")
	private int roomId;

	private String description;

	private String name;

	public TestRoom() {
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}