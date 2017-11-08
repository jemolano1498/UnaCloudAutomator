package Entities;

import java.util.ArrayList;

public class DeployedImage {
	
	public int id;
	/**
	 * representation of the image
	 */
	public Image image;
	
	public ArrayList<Execution> executions;
	
	public ArrayList<Execution> getExecutions() {
		return executions;
	}

	public void setExecutions(ArrayList<Execution> executions) {
		this.executions = executions;
	}

	/**
	 * Represents if image is configured to deploy in high availability machines
	 */
	public boolean highAvaliavility;

	public DeployedImage(Image image) {
		super();
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isHighAvaliavility() {
		return highAvaliavility;
	}

	public void setHighAvaliavility(boolean highAvaliavility) {
		this.highAvaliavility = highAvaliavility;
	}

}
