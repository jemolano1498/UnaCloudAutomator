package Run;

import java.sql.Connection;

import Entities.Cluster;
import Entities.Node;
import Entities.NodeGroup;
import Services.ControlManager;
import Services.GroupConfigurer;
import Services.JSONReader;
import Services.QueryFactory;

public class InitialPoint {

	public static void main(String[] args) {
		
//		ControlManager controlManager = ControlManager.getInstance();
		
		Connection connection = ControlManager.getInstance().getDBConnection();
		
		new QueryFactory(connection);
		
		JSONReader jsonReader = new JSONReader();
		
		GroupConfigurer config = new GroupConfigurer(jsonReader.readJson());
		
		config.start();
		
	}
	
//	public static void main(String[] args) {
//		
//		Cluster c = new Cluster ("prueba");
//		c.addNodeGroup(new NodeGroup("0", 2, "DB", "", "small", "DebianTemplate"));
//		c.getNodeGroupById("0").addNode(new Node ("0","HOLA1"));
//		c.getNodeGroupById("0").addNode(new Node ("1","HOLA2"));
//		c.addNodeGroup(new NodeGroup("1", 1, "Normal", "ndsaefnin $DEP_0_0$ emoreoimrmo $DEP_0_1$ mwemfewf", "small", "DebianTemplate"));
//		c.getNodeGroupById("1").addDependency("0");
//		NodeGroup pruebita = c.getNodeGroupById("1");
//		System.out.println(pruebita.getScript());
//		
//		for (String dep:  pruebita.getDependencies()) {
//			NodeGroup actual = c.getNodeGroupById(dep);
//			System.out.println(actual.getgHostName());
//			String toChange = pruebita.getScript();
//			System.out.println(toChange);
//			for (Node nodo: actual.getNodes()) {
//				System.out.println("$DEP_"+actual.getId()+"_"+nodo.getId()+"$ "+nodo.getIp());
//				toChange = toChange.replace("$DEP_"+actual.getId()+"_"+nodo.getId()+"$", nodo.getIp());
//			}
//			System.out.println(toChange);
//			
//		}
//	}
	

}
