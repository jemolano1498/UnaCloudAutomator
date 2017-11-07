package Entities;

public class HardwareProfile {
	
	public String name;
	
	public int cores;
	
	public int ram;

	public HardwareProfile(String name, int cores, int ram) {
		super();
		this.name = name;
		this.cores = cores;
		this.ram = ram;
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
