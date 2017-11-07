package Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import uniandes.unacloud.agent.utils.SystemUtils;

public class QueryFactory {
	
	
	private Connection connection;
	
	private static QueryFactory queryFactory;
	
	public QueryFactory (Connection conn) {
		
		connection = conn;
		queryFactory = this;
		
	}
	
	public static QueryFactory getInstance() {
		
		return queryFactory;
		
	}
	
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
	
	public void registerImageInDB (String imageName, String imageRoute, int imageNumber) {
		try {
			SystemUtils.sleep(5000);
			String query = 	"INSERT INTO image (id, version,"
					+ " access_protocol, fixed_disk_size, "
					+ "image_version, last_update, main_file, "
					+ "name, operating_system_id, owner_id, "
					+ "password, platform_id, repository_id, "
					+ "state, token, user, is_public) "
					+ "VALUES ("+imageNumber+", 0, \"SSH\","
							+ " 2311069109, 1, \"2017-11-01 11:52:17\","
							+ " \""+ imageRoute+"\", \""+ imageName+"\","
									+ " 6, 7, \"root\", 1, 1, \"AVAILABLE\","
									+ " \"NULL\",\"root\", \"\");";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			SystemUtils.sleep(5000);
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

	public void createCluster(String clusterName,int imageNumber) {
		try {
			SystemUtils.sleep(5000);
			String query = 	"INSERT INTO cluster (version,"
					+ " name, state, user_id) "
					+ "VALUES ( 0,\""+ clusterName +"\", \"AVAILABLE\", 7);";
			query += "INSERT INTO cluster_image (cluster_images_id, image_id) values "
					+ "VALUES ( (select id from dbunacloud.cluster where name= \"\"+ clusterName +\"\"),"+imageNumber+"); /n";
			
			PreparedStatement ps = connection.prepareStatement(query);
			System.out.println(ps.toString());
			SystemUtils.sleep(5000);
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
	
	

}
