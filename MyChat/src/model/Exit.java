package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the exits database table.
 * 
 */
@Entity
@Table(name="exits")
@NamedQuery(name="Exit.findAll", query="SELECT e FROM Exit e")
public class Exit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="exit_id")
	private int exitId;

	private String name;

	//bi-directional many-to-one association to Room
	@ManyToOne
	@JoinColumn(name="room_id")
	private Room origin;

	//uni-directional one-to-one association to Room
	@OneToOne
	@JoinColumn(name="destination")
	private Room destination;

	public Exit() {
	}

	public int getExitId() {
		return this.exitId;
	}

	public void setExitId(int exitId) {
		this.exitId = exitId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Room getOrigin() {
		return this.origin;
	}

	public void setOrigin(Room room1) {
		this.origin = room1;
	}

	public Room getDestination() {
		return this.destination;
	}

	public void setDestination(Room room2) {
		this.destination = room2;
	}

}