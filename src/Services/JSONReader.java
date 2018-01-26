package Services;

import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Entities.Cluster;
import Entities.NodeGroup;
/**
 * 
 * @author juanes
 * Class that reads the JSON input
 */
public class JSONReader {
	
	/**
	 * JSON file path
	 */
	public static String JSON_SOURCE = "out.json";

	/**
	 * Method that reads the cluster
	 * @return mapped cluster
	 */
	public Cluster readJson() {
		
		Cluster clusterObj = null;
		
		JSONParser parser = new JSONParser();try {
 
            Object obj = parser.parse(new FileReader(JSON_SOURCE));
 
            JSONObject jsonObject = (JSONObject) obj;
 
            String name = (String) jsonObject.get("name");
            
            String time = (String) jsonObject.get("time");
                                   
            JSONObject cluster = (JSONObject) jsonObject.get("cluster");
            
            String clusterId = (String) cluster.get("id");
            
            clusterObj = new Cluster (clusterId);
            
            clusterObj.name = name;
            
            switch (time) {
            case "1 Hour": clusterObj.setTime( new Long (3600000));break;
            case "2 Hour": clusterObj.time = new Long (3600000*2);break;
            case "4 Hour": clusterObj.time = new Long (3600000*4);break;
            case "12 Hour": clusterObj.time = new Long (3600000*12);break;
            default : clusterObj.time = new Long (3600000);
                
            }
                        
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
