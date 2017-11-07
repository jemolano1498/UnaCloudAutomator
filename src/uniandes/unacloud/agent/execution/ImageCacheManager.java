package uniandes.unacloud.agent.execution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uniandes.unacloud.agent.exceptions.ExecutionException;
import uniandes.unacloud.agent.execution.domain.Execution;
import uniandes.unacloud.agent.execution.domain.Image;
import uniandes.unacloud.agent.execution.domain.ImageCopy;
import uniandes.unacloud.agent.execution.domain.ImageStatus;
import uniandes.unacloud.agent.net.send.ServerMessageSender;
import uniandes.unacloud.agent.platform.PlatformFactory;
import uniandes.unacloud.agent.system.OperatingSystem;
import uniandes.unacloud.agent.utils.SystemUtils;
import uniandes.unacloud.agent.utils.VariableManager;
import uniandes.unacloud.common.enums.ExecutionProcessEnum;
import uniandes.unacloud.common.enums.TransmissionProtocolEnum;
import uniandes.unacloud.common.net.tcp.message.UnaCloudResponse;
import uniandes.unacloud.utils.security.HashGenerator;
import static uniandes.unacloud.common.utils.UnaCloudConstants.*;

/**
 * Responsible to manage list of images in cache
 * @author Clouder
 */
public class ImageCacheManager {
	
	/**
	 * Path of current image repository
	 */
	private static final String machineRepository = "~/VirtualBox\\ VMs/";
	
	/**
	 * Represents file where image list is stored
	 */
	private static final File imageListFile = new File("imageList");
	
	/**
	 * Represents list of images currently stored in repository
	 */
	private static Map<Long, Image> imageList = null;
	
	/**
	 * Returns a free copy of the image
	 * @param imageId image Id 
	 * @return image available copy
	 * @throws Exception 
	 */
	public static ImageCopy getFreeImageCopy(Execution execution, TransmissionProtocolEnum type) throws Exception {
		System.out.println("\tgetFreeImageCopy " + execution.getImageId());
		Image vmi = new Image ();
		vmi.setUsername("root");
		vmi.setPassword("root");
		ImageCopy source;
		ImageCopy dest;
		System.out.println("\t No copy is free");
		source = vmi.getImageCopies().get(0);
		final String vmName = "v" + HashGenerator.randomString(9);
		dest = new ImageCopy();
		dest.setImage(vmi);
		vmi.getImageCopies().add(dest);
		File root = new File(machineRepository + OperatingSystem.PATH_SEPARATOR + vmi.getId() + OperatingSystem.PATH_SEPARATOR + vmName);
		if (source.getMainFile().getExecutableFile().getName().contains(".")) {
			String[] fileParts = source.getMainFile().getExecutableFile().getName().split("\\.");
			dest.setMainFile(new File(root, vmName + "." + fileParts[fileParts.length-1]));
		}
		else
			dest.setMainFile(new File(root, vmName));
		dest.setStatus(ImageStatus.LOCK);
		saveImages();
		ServerMessageSender.reportExecutionState(execution.getId(), ExecutionProcessEnum.SUCCESS, "Start configuring");
		SystemUtils.sleep(2000);
		System.out.println("\tclonning");
		return source.cloneCopy(dest);
	}

		
	/**
	 * Removes a directory from physical machine disk
	 * @param f file or directory to be deleted
	 */
	public static void cleanDir(File f) {
		if (f.isDirectory()) 
			for (File r : f.listFiles())
				cleanDir(r);
		System.out.println("\t\t" + f + ": " + f.delete());
	}
	
	
	
	
	
	/**
	 * Saves the images data in a file
	 */
	private static synchronized void saveImages() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(imageListFile))) {
			oos.writeObject(imageList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	

}
