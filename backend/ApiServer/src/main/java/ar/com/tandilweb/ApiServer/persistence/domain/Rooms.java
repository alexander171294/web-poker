package ar.com.tandilweb.ApiServer.persistence.domain;

public class Rooms {
	
	private long id_room;
	private String name;
	private String accessPassword;
	private String securityToken;
	private String gproto;
	private int max_players;
	private String description;
	private int minCoinForAccess;
	private String recoveryEmail;
	private int badLogins;
	
	private boolean nowConnected;
	private boolean isOfficial;
	
	public Rooms(long id_room, String name, String accessPassword, String securityToken, String gproto, int max_players,
			String description, int minCoinForAccess, String recoveryEmail, int badLogins, boolean nowConnected, boolean isOfficial) {
		super();
		this.id_room = id_room;
		this.name = name;
		this.accessPassword = accessPassword;
		this.securityToken = securityToken;
		this.gproto = gproto;
		this.max_players = max_players;
		this.description = description;
		this.minCoinForAccess = minCoinForAccess;
		this.recoveryEmail = recoveryEmail;
		this.badLogins = badLogins;
		this.nowConnected = nowConnected;
		this.isOfficial = isOfficial;
	}

	public Rooms() {
		
	}

	public long getId_room() {
		return id_room;
	}

	public void setId_room(long id_room) {
		this.id_room = id_room;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessPassword() {
		return accessPassword;
	}

	public void setAccessPassword(String accessPassword) {
		this.accessPassword = accessPassword;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public String getGproto() {
		return gproto;
	}

	public void setGproto(String gproto) {
		this.gproto = gproto;
	}

	public int getMax_players() {
		return max_players;
	}

	public void setMax_players(int max_players) {
		this.max_players = max_players;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinCoinForAccess() {
		return minCoinForAccess;
	}

	public void setMinCoinForAccess(int minCoinForAccess) {
		this.minCoinForAccess = minCoinForAccess;
	}

	public String getRecoveryEmail() {
		return recoveryEmail;
	}

	public void setRecoveryEmail(String recoveryEmail) {
		this.recoveryEmail = recoveryEmail;
	}

	public int getBadLogins() {
		return badLogins;
	}

	public void setBadLogins(int badLogins) {
		this.badLogins = badLogins;
	}

	public boolean isNowConnected() {
		return nowConnected;
	}

	public void setNowConnected(boolean nowConnected) {
		this.nowConnected = nowConnected;
	}

	public boolean isOfficial() {
		return isOfficial;
	}

	public void setOfficial(boolean isOfficial) {
		this.isOfficial = isOfficial;
	}
	
}
