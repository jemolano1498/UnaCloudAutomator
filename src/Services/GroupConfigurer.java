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

import Entities.Cluster;
import Entities.Node;
import Entities.NodeGroup;
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
//			// TODO Auto-generated catch block
//			e.printStackTrace();
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
					
					configureGroup(a);
					
				}
				else if (deploymentsList.get(a.getId())==0 && dependenciesDeployed(a.getId())) {
					
					fixScript (a);
					configureGroup(a);
				}
			}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doGroupThings (NodeGroup a) throws Exception {
		
		configureGroup(a);
		
		//FALTA LLAMAR AL ALLOCATOR y guardar el deploy_image_id
		deployGroup();
		
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
	
	public void configureGroup (NodeGroup nodeGroup) {
				
		String copyName = cluster.name+"_"+nodeGroup.getId();
		int imageNumber = qf.getNextImage();
		System.out.println("Register platforms");
    	PlatformFactory.registerplatforms();
    	Platform platform = PlatformFactory.getPlatform("VBox5");
		platform.cloneImage("DebianTemplate",copyName);
		
		try {
			platform.startExecution(copyName +"_Je.molano1498");
			
			platform.copyFileOnExecution(copyName +"_Je.molano1498", "/home/user/", generateGroupScript(nodeGroup));
			
			platform.executeCommandOnExecution(copyName +"_Je.molano1498", "/bin/bash", "/home/user/"+cluster.getId()+"_"+nodeGroup.getId()+".sh");
			
			platform.stopExecution(copyName +"_Je.molano1498");
			
			platform.unregisterImage(copyName +"_Je.molano1498");
			
			String folderPath = "/opt/unacloud/repo/"+copyName +"_Je.molano1498/"+copyName +"_Je.molano1498";
//			String folderPath = "/home/juanes/VirtualBox VMs/"+copyName +"_Je.molano1498/"+copyName +"_Je.molano1498";
						
			zipFile(folderPath+".vbox",folderPath+".vdi",folderPath+".zip");
			
			nodeGroup.fileRoute = folderPath+".zip";
			
			qf.registerImageInDB(copyName +"_Je.molano1498", "/opt/unacloud/repo/"+copyName +"_Je.molano1498/", imageNumber);
			
			qf.createCluster(copyName +"_Je.molano1498", imageNumber);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deployGroup () {
		
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
