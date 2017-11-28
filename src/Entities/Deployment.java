package Entities;

import java.util.Date;

public class Deployment {
	
	public int id;
	
	/**
	 * Deployed cluster representation 
	 */
	public NodeGroup cluster;
	
	/**
	 * start time of the deployment
	 */
	public Date startTime = new Date();
	
	/**
	 * Duration of execution
	 */
	public long duration;
	
	/**
	 * represent status of the deployment (ACTIVE, FINISHED)
	 */
	public String status = "ACTIVE";
	
	/**
	 * list of deployed images present in the deployment
	 */
	public  DeployedImage images;

	public Deployment(NodeGroup cluster, long duration, String status) {
		super();
		this.cluster = cluster;
		this.duration = duration;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NodeGroup getCluster() {
		return cluster;
	}

	public void setCluster(NodeGroup cluster) {
		this.cluster = cluster;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DeployedImage getImages() {
		return images;
	}

	public void setImages(DeployedImage images) {
		this.images = images;
	}
	
	

}
