package Entities;

public class PhysicalMachine {
	
	public int id;
	/**
	 * Physical machine name
	 */
    public String name;
	
	/**
	 * indicates if this machine is being used by an user
	 */
    public boolean withUser = false;
	
	/**
	 * quantity of core processors
	 */
    public int cores;
    
    public String laboratory;
	
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

	public boolean isWithUser() {
		return withUser;
	}

	public void setWithUser(boolean withUser) {
		this.withUser = withUser;
	}

	public int getCores() {
		return cores;
	}

	public void setCores(int cores) {
		this.cores = cores;
	}

	public int getpCores() {
		return pCores;
	}

	public void setpCores(int pCores) {
		this.pCores = pCores;
	}

	public int getRam() {
		return ram;
	}

	public void setRam(int ram) {
		this.ram = ram;
	}

	public boolean isHighAvailability() {
		return highAvailability;
	}

	public void setHighAvailability(boolean highAvailability) {
		this.highAvailability = highAvailability;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public long getDataSpace() {
		return dataSpace;
	}

	public void setDataSpace(long dataSpace) {
		this.dataSpace = dataSpace;
	}

	public long getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(long freeSpace) {
		this.freeSpace = freeSpace;
	}

	public String getAgentVersion() {
		return agentVersion;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
	}

	/**
	 * quantity of physical core processors
	 */
    public int pCores;
	
	/**
	 * quantity of RAM memory in MB
	 */
    public int ram;
	
	/**
	 * Indicates if this machine has high availability
	 */
    public boolean highAvailability;

	/**
	 * physical machine IP address
	 */
    public String ip;
	
	/**
	 * physical machine MAC address
	 */
    public String mac;
	
	/**
	 * Total space in bytes in data directory: current image directory
	 */
    public long dataSpace = 0;
	
	/**
	 * Used space in bytes in data directory: current image directory
	 */
    public long freeSpace = 0;
	
	/**
	 * Current agent version
	 */
    public String agentVersion;

	public PhysicalMachine(int id, String name, int cores, int pCores, int ram, String ip, String laboratory) {
		super();
		this.id = id;
		this.name = name;
		this.cores = cores;
		this.pCores = pCores;
		this.ram = ram;
		this.ip = ip;
		this.laboratory = laboratory;
	}

	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}
		
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------

}
