package Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import Entities.DeployedImage;
import Entities.Deployment;
import Entities.Execution;
import Entities.NetInterface;
import Entities.NodeGroup;
import Entities.PhysicalMachine;
import Run.FinalStaticsVals;
import uniandes.unacloud.agent.system.OperatingSystem;
import uniandes.unacloud.agent.utils.SystemUtils;
import uniandes.unacloud.web.pmallocators.PhysicalMachineAllocationDescription;

/**
 * @author juanes
 * Class responsible for the SQL Queries
 */
public class QueryFactory {
	
	/**
	 * DB Connection
	 */
	private Connection connection;
	
	/**
	 * Singleton attribute
	 */
	private static QueryFactory queryFactory;
	
	/**
	 * Initialize the class
	 * @param conn 
	 */
	public QueryFactory (Connection conn) {
		
		connection = conn;
		queryFactory = this;
		
	}
	
	/**
	 * Method that returns the singleton attribute
	 * @return
	 */
	public static QueryFactory getInstance() {
		
		return queryFactory;
		
	}
	
	/**
	 * Method that checks in the database the position of the image to be registered
	 * @return next Image ID
	 */
	public int getNextImage () {
		int result =0;
		try {
			String query = 	"SELECT * FROM image;";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				result = rs.getInt(1);
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return result+1;
	}
	
	/**
	 * Method that register a new image in the DB
	 * @param imageName Name of the image to be deployed
	 * @param imageRoute Path of the image to be deployed
	 * @param imageNumber ID of the image being deployed
	 */
	public void registerImageInDB (String imageName, String imageRoute, int imageNumber) {
		try {
			String query = 	"INSERT INTO image (id, version,"
					+ " access_protocol, fixed_disk_size, "
					+ "image_version, last_update, main_file, "
					+ "name, operating_system_id, owner_id, "
					+ "password, platform_id, repository_id, "
					+ "state, token, user, is_public) "
					+ "VALUES ("+imageNumber+", 0, \"SSH\","
					+ " 2311069109, 1, \"2017-11-01 11:52:17\","
					+ " \""+ "D:\\\\GRID\\\\repo\\"+ OperatingSystem.PATH_SEPARATOR + imageName + "\\"+ OperatingSystem.PATH_SEPARATOR + imageName + ".vbox"+"\", \""+ imageName+"\","
							+ " 6, "+FinalStaticsVals.USERNAME_ID+", \""+FinalStaticsVals.IMAGE_PASSWORD+"\", 1, 1, \"AVAILABLE\","
							+ " \"NULL\",\""+FinalStaticsVals.IMAGE_USERNAME+"\", \"\");";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}

	/**
	 * Method that registers a cluster in the database
	 * @param clusterName nodegroup being deployed
	 * @param imageNumber Image ID of the nodegroup being deployed
	 */
	public void createCluster(String clusterName,int imageNumber) {
		try {
			String query = 	"INSERT INTO cluster (version,"
					+ " name, state, user_id) "
					+ "VALUES ( 0,\""+ clusterName +"\", \"AVAILABLE\", "+FinalStaticsVals.USERNAME_ID+");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		asociateCluster(clusterName, imageNumber);
	}
	
	/**
	 * Create the association betwixt a cluster and its image
	 * @param clusterName nodegroup id
	 * @param imageNumber deployed image id
	 */
	public void asociateCluster(String clusterName,int imageNumber) {
		try {
			String query = 	 "INSERT INTO cluster_image (cluster_images_id, image_id) "
					+ "VALUES ( (select id from cluster where name= \""+ clusterName +"\"),"+imageNumber+");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}
	
	/**
	 * Method that registers a nodegroup in the DB
	 * @param nodegroup
	 */
	public void addClusterDbId(NodeGroup nodegroup) {
		try {
			String query = 	"select id from cluster "
					+ "where cluster.name = \""+nodegroup.gHostName+"\""
							+ " and cluster.user_id = \""+FinalStaticsVals.USERNAME_ID+"\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				nodegroup.dBId=rs.getString(1);
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}

	/**
	 * Method that check in the DB if the deployment has been done
	 * @param deployedImageId deployed image id
	 * @param deployedInstances number of deployments
	 * @throws Exception in case any deployment fails
	 */
	public void verifyDeployment(int deployedImageId, int deployedInstances) throws Exception {
		boolean deployed =false;
		int iterations = 0;
		while (!deployed) {
			
			SystemUtils.sleep(10000);
			try {
				String query = 	"select count(*) from execution where deploy_image_id = "+deployedImageId+" and message = \"Execution is running\";";
				PreparedStatement ps = connection.prepareStatement(query);
				System.out.println(ps.toString());
				ResultSet rs = ps.executeQuery();
				
				while(rs.next()){
					if (rs.getInt(1)==deployedInstances){
						deployed = true;
					}
				}
				
				try {
					rs.close();
					ps.close();
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
				
			} catch (Exception e) {
				e.printStackTrace();			
			}
			if (iterations > 1000) {
				throw new Exception("All instances could not be deployed");
			}
			iterations ++;
		}
		
	}

	/**
	 * Method that returns a list of a nodegroup IP's
	 * @param deployedImageId
	 * @return NodeGroup IP's list
	 */
	public List<String> getIPs(int deployedImageId) {
		
		List<String> IPs = new ArrayList<String>();
		
		try {
			String query = 	"select ip from execution "
					+ "join net_interface on "
					+ "execution.id = net_interface.execution_id "
					+ "join ip on net_interface.ip_id = ip.id "
					+ "where execution.deploy_image_id = "+deployedImageId+";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				IPs.add(rs.getString(1));
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return IPs;
	}

	/**
	 * Method that return the available physical machines
	 * @return a list of the available physical machines
	 */
	public List<PhysicalMachine> getAllowedMachines() {
		List<PhysicalMachine> physicalMachines = new ArrayList<PhysicalMachine>();
		
		try {
			String query = 	"select physical_machine.id, physical_machine.name"
					+ ",physical_machine.cores, physical_machine.p_cores"
					+ ", physical_machine.ram, ip.ip, laboratory.name  "
					+ "from physical_machine "
					+ "join laboratory on physical_machine.laboratory_id = laboratory.id "
					+ "join user_restriction on laboratory.name = user_restriction.value "
					+ "join user_user_restriction on user_restriction.id = user_user_restriction.user_restriction_id "
					+ "join ip on physical_machine.ip_id=ip.id "
					+ "where user_user_restriction.user_restrictions_id ="+FinalStaticsVals.USERNAME_ID+" "
					+ "and physical_machine.state = \"ON\" "
					+ "order by physical_machine.name asc;";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				physicalMachines.add(new PhysicalMachine(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getString(7)));
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return physicalMachines;
	}

	/**
	 * Method that checks in the DB if there are available physical machines
	 * @param pmDescriptions
	 * @param listId
	 * @return map of physical machines and its id
	 */
	public Map<Long, PhysicalMachineAllocationDescription> getUnavailableMachines(Map<Long, PhysicalMachineAllocationDescription> pmDescriptions, String listId) {
		
		try {
			String query = 	"SELECT execution_node_id, count(*) AS vms, sum(ram) AS ram, sum(cores) AS cores " + 
					"FROM execution JOIN hardware_profile ON execution.hardware_profile_id = hardware_profile.id " +
					"JOIN execution_state ON execution_state.id = execution.state_id " +
					"WHERE execution_state.state != \"FINISHED\" AND execution_node_id in (" + listId + ") group by execution_node_id order by execution.name desc;";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				pmDescriptions.put(new Long(rs.getInt(1)), new PhysicalMachineAllocationDescription(rs.getInt(1), rs.getInt(4), rs.getInt(3), rs.getInt(2)));
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return pmDescriptions;
		
	}

	/**
	 * Check in the DB the available IP's
	 * @param laboratory
	 * @return list of the available IP's
	 */
	public List<String> getAvailableIps(String laboratory) {
		List<String> IPs = new ArrayList<String>();
		
		try {
			String query = 	"select ip.ip from ip "
					+ "join ippool on ip.ip_pool_id = ippool.id "
					+ "join laboratory on laboratory.id = ippool.laboratory_id "
					+ "where laboratory.name = \""+laboratory+"\" "
					+ "and ip.state = \"AVAILABLE\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				IPs.add(rs.getString(1));
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return IPs;
	}

	/**
	 * Method that mark an IP as USED
	 * @param ip ip to be reserved
	 */
	public void reserveIp(String ip) {
		try {
			String query = 	"update ip "
					+ "set state = \"USED\" "
					+ "where ip.ip = \""
					+ ip
					+ "\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}

	/**
	 * Method that sets an IP as available
	 * @param ip IP to set free
	 */
	public void freeIp(String ip) {
		try {
			String query = 	"update ip "
					+ "set state = \"AVAILABLE\" "
					+ "where ip.ip = \""
					+ ip
					+ "\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		
	}

	/**
	 * Method that checks the ID of the actual user
	 * @param username
	 * @return id of the actual user
	 */
	public String getUserId(String username) {
		int result = 0;
		try {
			String query = 	"select id from user where user.username = \""+username+"\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				result = rs.getInt(1);
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return result+"";
	}

	/**
	 * Method that register a Deployment un the DB
	 * @param dep
	 */
	public void insertDeploy(Deployment dep) {
		
		try {
			String query = 	"INSERT INTO deployment (version,"
					+ "cluster_id,"
					+ "duration,"
					+ "start_time,"
					+ "status,"
					+ "user_id) "
					+ "VALUES ( 1,"
							+ dep.cluster.dBId+","
									+ FinalStaticsVals.TWELVE_HOUR +", \""
											+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"\","
													+ "\"ACTIVE\", "
													+ FinalStaticsVals.USERNAME_ID + ");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		addDeployId (dep);
	}
	
	/**
	 * Method add the DB id of a deployment to the respective class
	 * @param dep
	 */
	public void addDeployId(Deployment dep) {
		
		try {
			String query = 	"select id from deployment "
					+ "where deployment.user_id = \""+FinalStaticsVals.USERNAME_ID+"\""
							+ " and deployment.status = \"ACTIVE\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				dep.setId(rs.getInt(1));
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}

	/**
	 * Method that register a Deployed image
	 * @param depImage
	 * @param dep
	 */
	public void insertDeployedImage(DeployedImage depImage, Deployment dep) {
		try {
			String query = 	"INSERT INTO deployed_image (version,"
					+ "deployment_id,"
					+ "high_avaliavility,"
					+ "image_id) "
					+ "VALUES ( 0,"
					+ dep.id+", \"\","
							+ depImage.getImage().getId()+");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		addDeployedImageId (depImage, dep);
		
	}
	
	/**
	 * Method that adds the DB id of a deployed image to its respective class
	 * @param depImage
	 * @param dep
	 */
	public void addDeployedImageId(DeployedImage depImage, Deployment dep) {
		
		try {
			String query = 	"select id from deployed_image "
					+ "where deployed_image.deployment_id = "+dep.getId()+""
							+ " and deployed_image.image_id = "+depImage.getImage().getId()+";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				depImage.setId(rs.getInt(1));
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}

	/**
	 * Method that registers an execution in the DB
	 * @param execution
	 */
	public void insertExecution(Execution execution) {
		try {
			String query = 	"INSERT INTO execution (version,"
					+ "copy_to,"
					+ "deploy_image_id,"
					+ "duration,"
					+ "execution_node_id,"
					+ "hardware_profile_id,"
					+ "last_report,"
					+ "message,"
					+ "name,"
					+ "start_time,"
					+ "state_id,"
					+ "stop_time) "
					+ "VALUES (1, 0, "
					+ execution.getDeployedImage().getId()+","
							+ execution.duration +","
									+ execution.executionNode.getId()+","
											+ execution.getHardwareProfile().id+", \""
													+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +"\" , \""
															+ execution.message +"\" ,\""
																	+ execution.name + "\" , \""
																	+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\" , "
																			+ "11, \""
																			+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()+execution.duration)) + "\" "
																					+ ");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		addExecutionId (execution);
		
	}

	/**
	 * Method that adds the DB id of an execution to its respective class
	 * @param execution
	 */
	private void addExecutionId(Execution execution) {
		try {
			String query = 	"select id from execution "
					+ "where execution.deploy_image_id = "+execution.getDeployedImage().getId()+""
							+ " and execution.name = \""+execution.name+"\";";
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				execution.id = rs.getInt(1);
			}
			
			try {
				rs.close();
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}

	/**
	 * Method that registers a Net Interface in the DB
	 * @param interfaces
	 */
	public void insertNetInterface(NetInterface interfaces) {
		try {
			String query = 	"INSERT INTO net_interface (version,"
					+ "execution_id,"
					+ "ip_id,"
					+ "name) "
					+ "VALUES (1,"
					+ interfaces.execution.id+" , (select ip.id from ip where ip.ip = \""
							+ interfaces.getIp()+"\") , \"eth0\");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			System.out.println("\t change " + ps.executeUpdate() + " lines");
			
			try {
				ps.close();
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
	}

	
	
	

}
