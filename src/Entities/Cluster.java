package Entities;

import java.util.ArrayList;

public class Cluster {
	
	public String id;
	
	public String name;
	
	public ArrayList<NodeGroup> nodeGroups;

	public Cluster(String id) {
		super();
		this.nodeGroups = new ArrayList<NodeGroup>();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	public void addNodeGroup (NodeGroup add) {
		nodeGroups.add(add);
	}
	
	public NodeGroup getNodeGroupById (String id) {
		
		for (NodeGroup a : nodeGroups) {
			if (a.getId().equals(id)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * @return the nodeGroups
	 */
	public ArrayList<NodeGroup> getNodeGroups() {
		return nodeGroups;
	}

	/**
	 * @param nodeGroups the nodeGroups to set
	 */
	public void setNodeGroups(ArrayList<NodeGroup> nodeGroups) {
		this.nodeGroups = nodeGroups;
	}

}
