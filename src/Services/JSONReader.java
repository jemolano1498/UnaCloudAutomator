package Services;

import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Entities.Cluster;
import Entities.NodeGroup;

public class JSONReader {
	
	public static String JSON_SOURCE = "out.json";
	
	public static String name;
	
	public static String time;

	public Cluster readJson() {
		
		Cluster clusterObj = null;
		
		JSONParser parser = new JSONParser();try {
 
            Object obj = parser.parse(new FileReader(JSON_SOURCE));
 
            JSONObject jsonObject = (JSONObject) obj;
 
            String name = (String) jsonObject.get("name");
            this.name = name;
            String time = (String) jsonObject.get("time");
            this.time = time;
                                   
            JSONObject cluster = (JSONObject) jsonObject.get("cluster");
            String clusterId = (String) cluster.get("id");
            
            clusterObj = new Cluster (clusterId);
            
            clusterObj.name = name;
            
            JSONArray clusterNodes = (JSONArray) cluster.get("nodes");
            
            Iterator<JSONObject> iterator = clusterNodes.iterator();
            
            while (iterator.hasNext()) {
            	
            	JSONObject node = (JSONObject) iterator.next();
            	
            	NodeGroup nodegroup = new NodeGroup((String) node.get("id"), Integer.parseInt((String)node.get("quantity")), (String) node.get("gHostName"),
            			(String) node.get("script"), (String) node.get("hwp"), (String) node.get("imageTemplate"));
            	
            	JSONArray nodeGroupDependencies = (JSONArray) node.get("dependencies");
            	if (!nodeGroupDependencies.isEmpty()) {
            		Iterator<String> depIterator = nodeGroupDependencies.iterator();
                	
                	
                	while (depIterator.hasNext()) {
                		
                		String response = depIterator.next().toString();
                		if (!response.equals("")) {
                			nodegroup.addDependency( response);
                		}
                	}
            	}
            	
            	clusterObj.addNodeGroup(nodegroup);
            	
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return clusterObj;
		
		
	}
	
	

}
