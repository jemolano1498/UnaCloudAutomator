package Entities;

public class ImageTemplate {
	
	public String name;
	
	public String OS;
	
	public String user;
	
	public String password;

	public ImageTemplate(String name) {
		super();
		this.name = name;
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
	 * @return the oS
	 */
	public String getOS() {
		return OS;
	}

	/**
	 * @param oS the oS to set
	 */
	public void setOS(String oS) {
		OS = oS;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
