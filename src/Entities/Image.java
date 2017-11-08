package Entities;

import java.util.Date;


public class Image {
	
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	private int id;
	
	/**
	 * Image name
	 */
	private String name;
	
	/**
	 * indicates if the image is public or not
	 */
	private boolean isPublic;
	
	/**
	 * Size of files in bytes
	 */
	private long fixedDiskSize;
	
	public String owner;
	
	/**
	 * username used to access the image
	 */
	private String user;
	
	/**
	 * password used to access the image
	 */
	private String password;
	
	/**
	 * Image operating system 
	 */
	private String operatingSystem;
	
	/**
	 * access protocol (SSH, RDP)
	 */
	private String accessProtocol;
	
	/**
	 * Main file path (File that can be executed by platform in order to
	 * deploy the machine)
	 */
	private String mainFile;
	
	/**
	 * Indicates how many times the image files had been edited
	 */
	private int imageVersion = 1;
	
	/**
	 * token to validate image message sent by client
	 */	
	private String token = null;
	
	/**
	 *Image state (UNAVAILABLE,DISABLE,AVAILABLE,REMOVING_CACHE,COPYING,IN_QUEUE) 
	 */
	private String state = "AVAILABLE";
	
	/**
	 * Last update date
	 */
	private Date lastUpdate;
	
	/**
	 * Platform where image could be executed
	 */
	private String platform;

	public Image(int id,String name, boolean isPublic, long fixedDiskSize, String owner, String password, String user,
			String operatingSystem, String accessProtocol, String mainFile, int imageVersion, String token,
			String state, Date lastUpdate, String platform) {
		super();
		this.id = id;
		this.name = name;
		this.isPublic = isPublic;
		this.fixedDiskSize = fixedDiskSize;
		this.owner = owner;
		this.password = password;
		this.operatingSystem = operatingSystem;
		this.accessProtocol = accessProtocol;
		this.mainFile = mainFile;
		this.imageVersion = imageVersion;
		this.token = token;
		this.state = state;
		this.lastUpdate = lastUpdate;
		this.platform = platform;
		this.user = user;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public long getFixedDiskSize() {
		return fixedDiskSize;
	}

	public void setFixedDiskSize(long fixedDiskSize) {
		this.fixedDiskSize = fixedDiskSize;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getAccessProtocol() {
		return accessProtocol;
	}

	public void setAccessProtocol(String accessProtocol) {
		this.accessProtocol = accessProtocol;
	}

	public String getMainFile() {
		return mainFile;
	}

	public void setMainFile(String mainFile) {
		this.mainFile = mainFile;
	}

	public int getImageVersion() {
		return imageVersion;
	}

	public void setImageVersion(int imageVersion) {
		this.imageVersion = imageVersion;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
		
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------	
	

}
