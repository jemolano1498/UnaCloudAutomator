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
import Entities.HardwareProfile;
import Entities.Image;
import Entities.ImageRequest;
import Entities.Node;
import Entities.NodeGroup;
import Run.FinalStaticsVals;
import uniandes.unacloud.agent.platform.Platform;
import uniandes.unacloud.agent.platform.PlatformFactory;
import uniandes.unacloud.agent.utils.SystemUtils;

public class GroupConfigurer {
	
	public Cluster cluster;
	
	private Map<String, Integer> deploymentsList;
	
	private Map<String, List<String>> dependencies;
	
	private QueryFactory qf;

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

	public void start() {
		
//		try {
//			qf.verifyDeployment(325,5);
//		} catch (Exception e) {
//		}
//		
//
//		SystemUtils.sleep(5000);
//		
//		List<String> IPs = qf.getIPs(325);
//		
//		for (String a : IPs) {
//			System.out.println(a);
//		}
		
//		configureGroupWithNoDependencies(cluster.nodeGroups.get(0));
//		zipFile();
//		System.out.println(generateGroupScript(cluster.nodeGroups.get(0)).getAbsolutePath());
		
		try {
		while (!allDeployed()) {
			for (NodeGroup a : cluster.getNodeGroups()) {
				if (deploymentsList.get(a.getId())==0 && a.getDependencies().isEmpty()) {
					
					doGroupThings(a);
					
				}
				else if (deploymentsList.get(a.getId())==0 && dependenciesDeployed(a.getId())) {
					
					fixScript (a);
					doGroupThings(a);
				}
			}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doGroupThings (NodeGroup a) throws Exception {
		
		//FALTA LLAMAR AL ALLOCATOR y guardar el deploy_image_id
		DeploymentService.getInstance().deploy (cluster, FinalStaticsVals.USERNAME_ID, new Long (3600000), configureGroup(a) );
		
		qf.verifyDeployment(a.deployedImageId, a.getQuantity());
		
		List<String> IPs = qf.getIPs(a.deployedImageId);
		
		for (int i =0;i < a.getQuantity(); i++ ) {
			a.addNode(new Node (i+"",IPs.get(i)));
		}
		
		deploymentsList.put(a.getId(), 1);
	}
	
	

	public boolean allDeployed () {
		for (NodeGroup a : cluster.getNodeGroups()) {
			if (deploymentsList.get(a.getId())==0){
				return false;
			}
		}
		return true;
	}
	
	public boolean dependenciesDeployed (String group) {
		
		for (String a : dependencies.get(group)) {
			if (deploymentsList.get(a) == 0){
				return false;
			}
		}
		return true;
	}
	
	public ImageRequest configureGroup (NodeGroup nodeGroup) {
		
		ImageRequest resp = null;
				
		String copyName = cluster.name+"_"+nodeGroup.getId();
		
		int imageNumber = qf.getNextImage();
		
		System.out.println("Register platforms");
		
    	PlatformFactory.registerplatforms();
    	
    	Platform platform = PlatformFactory.getPlatform(FinalStaticsVals.PLATFORM);
    	
//		platform.cloneImage(FinalStaticsVals.IMAGE_NAME,copyName);
		
		try {
			
			String imageName = copyName +"_" + FinalStaticsVals.USERNAME;
			
//			platform.startExecution(imageName);
//			
//			platform.copyFileOnExecution(imageName, FinalStaticsVals.IMAGE_FOLDER_PATH, generateGroupScript(nodeGroup));
//			
//			platform.executeCommandOnExecution(imageName, "/bin/bash", FinalStaticsVals.IMAGE_FOLDER_PATH+cluster.getId()+"_"+nodeGroup.getId()+".sh");
//			
//			platform.stopExecution(imageName);
//			
//			platform.unregisterImage(imageName);
			
			String folderPath = FinalStaticsVals.FOLDER_PATH+imageName +"/"+imageName;
//			String folderPath = "/home/juanes/VirtualBox VMs/"+copyName +"_Je.molano1498/"+copyName +"_Je.molano1498";
						
//			zipFile(folderPath+".vbox",folderPath+".vdi",folderPath+".zip");
			
			nodeGroup.fileRoute = folderPath+".zip";
			
			qf.registerImageInDB(imageName, FinalStaticsVals.FOLDER_PATH + imageName +"/", imageNumber);
			
			qf.createCluster(cluster.name, imageNumber);
			
			qf.addClusterDbId(cluster);
			
			Image image = new Image(imageNumber, imageName, false, new Long("2311069109"), FinalStaticsVals.USERNAME_ID, FinalStaticsVals.IMAGE_PASSWORD, FinalStaticsVals.IMAGE_USERNAME, "6", "SSH", folderPath, 0, null, "AVAILABLE", null, FinalStaticsVals.PLATFORM_ID);
			
			cluster.setImage(image);
				
			resp = new ImageRequest (image, new HardwareProfile(nodeGroup.getHwp()), nodeGroup.getQuantity(), nodeGroup.getgHostName(), false);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resp;
	}
	
	private void fixScript(NodeGroup a) {
		
		for (String dep:  a.getDependencies()) {
			NodeGroup actual = cluster.getNodeGroupById(dep);
			for (Node nodo: actual.getNodes()) {
				a.script = a.getScript().replace("$DEP_"+actual.getId()+"_"+nodo.getId()+"$", nodo.getIp());
			}
		}
		
	}
	
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
