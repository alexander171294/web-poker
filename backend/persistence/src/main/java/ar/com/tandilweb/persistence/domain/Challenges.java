package ar.com.tandilweb.persistence.domain;

public class Challenges {
	
	private long challengeID;
	private long id_user;
	private long id_room;
	private String challenge;
	
	public Challenges(long challengeID, long id_user, long id_room, String challenge) {
		super();
		this.challengeID = challengeID;
		this.id_user = id_user;
		this.id_room = id_room;
		this.challenge = challenge;
	}
	
	public Challenges() {
		
	}

	public long getChallengeID() {
		return challengeID;
	}

	public void setChallengeID(long challengeID) {
		this.challengeID = challengeID;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public long getId_room() {
		return id_room;
	}

	public void setId_room(long id_room) {
		this.id_room = id_room;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

}
