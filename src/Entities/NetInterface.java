package Entities;

public class NetInterface {
	
	/**
	 * IP assign in this interface
	 */
	public String ip;
	
	/**
	 * name of the interface
	 */
	public String name;
	
	public NetInterface( String name,String ip, Execution execution) {
		super();
		this.ip = ip;
		this.name = name;
		this.execution = execution;
	}

	public Execution execution;

	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
