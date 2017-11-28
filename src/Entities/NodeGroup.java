package Entities;

import java.util.ArrayList;

public class NodeGroup {
	
	public String id;
	
	public String dBId;
	
	public int quantity;
	
	public String gHostName;
	
	public String script;
	
	public String hwp;
	
	public String imageTemplate;
	
	public String fileRoute;
	
	public int deployedImageId;
	
	public ArrayList<Node> nodes;
	
	public ArrayList<String> dependencies;
	
	public Image image;

	public NodeGroup(String id, int quantity, String gHostName, String script, String hwp, String imageTemplate) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.gHostName = gHostName;
		this.script = script;
		this.hwp = hwp;
		this.imageTemplate = imageTemplate;
		this.nodes = new ArrayList<Node>();
		this.dependencies = new ArrayList<String>();
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

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the gHostName
	 */
	public String getgHostName() {
		return gHostName;
	}

	/**
	 * @param gHostName the gHostName to set
	 */
	public void setgHostName(String gHostName) {
		this.gHostName = gHostName;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the nodes
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void addNode (Node node) {
		this.nodes.add(node);
	}
	
	public void addDependency (String dependency) {
		this.dependencies.add(dependency);
	}

	/**
	 * @return the hwp
	 */
	public String getHwp() {
		return hwp;
	}

	/**
	 * @param hwp the hwp to set
	 */
	public void setHwp(String hwp) {
		this.hwp = hwp;
	}

	/**
	 * @return the imageTemplate
	 */
	public String getImageTemplate() {
		return imageTemplate;
	}

	/**
	 * @param imageTemplate the imageTemplate to set
	 */
	public void setImageTemplate(String imageTemplate) {
		this.imageTemplate = imageTemplate;
	}

	/**
	 * @return the dependencies
	 */
	public ArrayList<String> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(ArrayList<String> dependencies) {
		this.dependencies = dependencies;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	

}
