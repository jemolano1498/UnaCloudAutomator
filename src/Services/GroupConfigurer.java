package Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import Services.DeploymentService;
import Entities.Cluster;
import Entities.DeployedImage;
import Entities.HardwareProfile;
import Entities.Image;
import Entities.ImageRequest;
import Entities.Node;
import Entities.NodeGroup;
import Run.FinalStaticsVals;
import uniandes.unacloud.agent.platform.Platform;
import uniandes.unacloud.agent.platform.PlatformFactory;
import uniandes.unacloud.agent.system.OperatingSystem;
import uniandes.unacloud.agent.utils.SystemUtils;

/**
 * @author juanes
 *
 */
public class GroupConfigurer {
	
	/**
	 * The Global cluster
	 */
	public Cluster cluster;
	
	/**
	 * List of nodegroups and if it has been deployed
	 */
	private Map<String, Integer> deploymentsList;
	
	/**
	 * List of nodegroups and its dependencies
	 */
	private Map<String, List<String>> dependencies;
	
	/**
	 * Instance of the DB class
	 */
	private QueryFactory qf;

	/**
	 * Method that checks if the deployments has been deployed and if it has dependencies
	 * @param cluster mapped cluster
	 */
	public GroupConfigurer(Cluster cluster) {
		
		this.cluster = cluster;
		
		deploymentsList = new HashMap<String, Integer>();
		
		dependencies = new HashMap<String, List<String>>();
		
		for (NodeGroup a : cluster.getNodeGroups()) {
			
			deploymentsList.put(a.getId(), 0);
			dependencies.put(a.getId(), a.getDependencies());
		}
		
		qf = QueryFactory.getInstance();
		
	}

	/**
	 * Method that start the process of provisioning and deployment
	 */
	public void start() {
		
		try {
		while (!allDeployed()) {
			for (NodeGroup a : cluster.getNodeGroups()) {
				if (deploymentsList.get(a.getId())==0 && a.getDependencies().isEmpty()) {
					
					DeployGroup(a);
					
				}
				else if (deploymentsList.get(a.getId())==0 && dependenciesDeployed(a.getId())) {
					
					fixScript (a);
					DeployGroup(a);
				}
			}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method that call the deployment services and checks if it has already been deployed
	 * @param a Group to be deployed
	 * @throws Exception In case the deployment fails
	 */
	public void DeployGroup (NodeGroup a) throws Exception {
		
		DeployedImage depIm = DeploymentService.getInstance().deploy (a, FinalStaticsVals.USERNAME_ID, cluster.getTime(), configureGroup(a) );
		
		qf.verifyDeployment(depIm.id, a.getQuantity());
		
		List<String> IPs = qf.getIPs(depIm.id);
		
		for (int i =0;i < a.getQuantity(); i++ ) {
			a.addNode(new Node (i+"",IPs.get(i)));
		}
		
		deploymentsList.put(a.getId(), 1);
	}
	
	

	/**
	 * Method that checks if all nodegroups has been marked as deployed
	 * @return true if all nodegroups are marked as deployed
	 */
	public boolean allDeployed () {
		for (NodeGroup a : cluster.getNodeGroups()) {
			if (deploymentsList.get(a.getId())==0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that checks if the previous dependencies has been deployed
	 * @param group Actual nodegroup being deployed
	 * @return true if all dependencies has been deployed
	 */
	public boolean dependenciesDeployed (String group) {
		
		for (String a : dependencies.get(group)) {
			if (deploymentsList.get(a) == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method that provides the Virtual Machine Image of the nodeGroup being deployed
	 * @param nodeGroup nodegroup being deployed
	 * @return Image request of the VMI
	 */
	public ImageRequest configureGroup (NodeGroup nodeGroup) {
		
		ImageRequest resp = null;
				
		String copyName = cluster.name+"_"+nodeGroup.getId();
		
		int imageNumber = qf.getNextImage();
		
		System.out.println("Register platforms");
		
    	PlatformFactory.registerplatforms();
    	
    	Platform platform = PlatformFactory.getPlatform(FinalStaticsVals.PLATFORM);
    	
		platform.cloneImage(FinalStaticsVals.IMAGE_NAME,copyName);
		
		try {
			
			String imageName = copyName +"_" + FinalStaticsVals.USERNAME;
			
			platform.startExecution(imageName);
			
			platform.copyFileOnExecution(imageName, FinalStaticsVals.IMAGE_FOLDER_PATH, generateGroupScript(nodeGroup));
			
			platform.executeCommandOnExecution(imageName, "/bin/bash", FinalStaticsVals.IMAGE_FOLDER_PATH + cluster.getId()+"_"+nodeGroup.getId()+".sh");
			
			platform.stopExecution(imageName);
			
			platform.unregisterImage(imageName);
			
			String folderPath = FinalStaticsVals.FOLDER_PATH+imageName + OperatingSystem.PATH_SEPARATOR +imageName;
						
			zipFile(folderPath+".vbox",folderPath+".vdi",folderPath+".vbox.zip");
			
			nodeGroup.fileRoute = folderPath+".zip";
			
			qf.registerImageInDB(imageName, FinalStaticsVals.FOLDER_PATH + imageName + OperatingSystem.PATH_SEPARATOR , imageNumber);
			
			qf.createCluster(nodeGroup.gHostName, imageNumber);
			
			qf.addClusterDbId(nodeGroup);
			
			Image image = new Image(imageNumber, imageName, false, new Long("2311069109"), FinalStaticsVals.USERNAME_ID, FinalStaticsVals.IMAGE_PASSWORD, FinalStaticsVals.IMAGE_USERNAME, "6", "SSH", folderPath, 0, null, "AVAILABLE", null, FinalStaticsVals.PLATFORM_ID);
			
			nodeGroup.setImage(image);
				
			resp = new ImageRequest (image, new HardwareProfile(nodeGroup.getHwp()), nodeGroup.getQuantity(), nodeGroup.getgHostName(), false);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resp;
	}
	
	/**
	 * Method that modifies script with the IP's of the dependent groups
	 * @param a
	 */
	private void fixScript(NodeGroup a) {
		
		for (String dep:  a.getDependencies()) {
			NodeGroup actual = cluster.getNodeGroupById(dep);
			for (Node nodo: actual.getNodes()) {
				a.script = a.getScript().replace("$DEP_"+actual.getId()+"_"+nodo.getId()+"$", nodo.getIp());
			}
		}
		
	}
	
	/**
	 * method that zip two input files
	 * @param Route1 route of the first file
	 * @param Route2 route of the second file
	 * @param dest destination path
	 */
	public void zipFile (String Route1, String Route2, String dest) {
		
		String zipFile = dest;
		
		System.out.println("creating file");
		
		String[] srcFiles = { Route1, Route2};
		
		try {
			
			// create byte buffer
			byte[] buffer = new byte[1024];

			FileOutputStream fos = new FileOutputStream(zipFile);

			ZipOutputStream zos = new ZipOutputStream(fos);
			
			for (int i=0; i < srcFiles.length; i++) {
				
				File srcFile = new File(srcFiles[i]);

				FileInputStream fis = new FileInputStream(srcFile);

				// begin writing a new ZIP entry, positions the stream to the start of the entry data
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				
				int length;

				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}

				zos.closeEntry();

				// close the InputStream
				fis.close();
				
			}

			// close the ZipOutputStream
			zos.close();
			
		}
		catch (IOException ioe) {
			System.out.println("Error creating zip file: " + ioe);
		}
		
	}
	
	/**
	 * Method that generates the script file to be run in the VMI
	 * @param nodeGroup NodeGroup being deployed
	 * @return Path of the script file
	 */
	public File generateGroupScript (NodeGroup nodeGroup) {
		String route = "";
				
		FileOutputStream fop = null;
		File file = null;
		String content = nodeGroup.getScript();

		try {

			route = cluster.getId()+"_"+nodeGroup.getId()+".sh";
			file = new File(route);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
	

}
