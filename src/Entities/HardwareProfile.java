package Entities;

public class HardwareProfile {
	
	public int id;
	
	public String name;
	
	public int cores;
	
	public int ram;

	public HardwareProfile(String type) {
		switch (type) {
		case ("small"): {
			id=1;
			cores =1;
			ram = 1024;
		}
		case ("medium"):{
			id=2;
			cores =2;
			ram = 1024*2;
		}
		case ("large"):{
			id=3;
			cores =4;
			ram = 1024*4;
		}
		default: {
			id=1;
			cores =1;
			ram = 1024;
		}
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cores
	 */
	public int getCores() {
		return cores;
	}

	/**
	 * @param cores the cores to set
	 */
	public void setCores(int cores) {
		this.cores = cores;
	}

	/**
	 * @return the ram
	 */
	public int getRam() {
		return ram;
	}

	/**
	 * @param ram the ram to set
	 */
	public void setRam(int ram) {
		this.ram = ram;
	}

}
