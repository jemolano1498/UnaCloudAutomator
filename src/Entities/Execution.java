package Entities;

import java.util.Date;


public class Execution {
	
	/**
	 * Database execution id
	 */	
	public int id;
	
	
	public NetInterface interfaces;
    
    /**
     * Image copy to be executed
     */
	public DeployedImage image;

    /**
	 * Execution hostname
	 */
	public String name;
	
	/**
	 * Execution hardware profile assigned
	 */
	public HardwareProfile hardwareProfile;
	
	/**
	 * Date when the node was started
	 */
	public Date startTime;
	
	/**
	 * Date when the node was or should be stopped
	 */
	public Date stopTime;
	
	/**
	 * Actual state , by default is requested
	 */
	public String state;
	
	/**
	 * Execution last message
	 */
	public String message;
	
	/**
	 * Physical machine where the node is or was deployed
	 */
	public PhysicalMachine executionNode;
			
	/**
	 * Last report of execution
	 */
	public Date lastReport = new Date();
		
	/**
	 * Duration of execution in milliseconds
	 */
	public long duration;
	
	/**
	 * Image id where files of this instance will be saved
	 * This id is optional
	 */
	public long copyTo;	
    
    /**
     * Class constructor
     */
    public Execution() {	
    	
    }

	public Execution(DeployedImage depImage, String hostname, String string, HardwareProfile hp, long time,
			String string2) {
		image = depImage;
		name = hostname;
		message = string;
		hardwareProfile = hp;
		duration = time;
		state = string2;
	}

	public NetInterface getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(NetInterface interfaces) {
		this.interfaces = interfaces;
	}

	public DeployedImage getDeployedImage() {
		// TODO Auto-generated method stub
		return image;
	}

	public PhysicalMachine getExecutionNode() {
		return executionNode;
	}

	public HardwareProfile getHardwareProfile() {
		return hardwareProfile;
	}

	public void setHardwareProfile(HardwareProfile hardwareProfile) {
		this.hardwareProfile = hardwareProfile;
	}

	public void setExecutionNode(PhysicalMachine executionNode) {
		this.executionNode = executionNode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
    
  
    

}
