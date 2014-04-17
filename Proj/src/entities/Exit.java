package entities;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the exits database table.
 * 
 */
@Entity
@Table(name="exits")
@NamedQuery(name="Exit.findAll", query="SELECT e FROM Exit e")
//@SequenceGenerator(name="exitSeq", initialValue=1, allocationSize=100)
public class Exit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="exit_id")
	private long exitId;

	private long destination;

	private String name;

	@Column(name="room_id")
	private long roomId;

	public Exit() {
	}

	public long getExitId() {
		return this.exitId;
	}

	public void setExitId(long exitCount) {
		this.exitId = exitCount;
	}

	public long getDestination() {
		return this.destination;
	}

	public void setDestination(long l) {
		this.destination = l;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getRoomId() {
		return this.roomId;
	}

	public void setRoomId(long l) {
		this.roomId = l;
	}
}