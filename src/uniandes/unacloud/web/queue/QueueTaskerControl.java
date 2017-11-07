package uniandes.unacloud.web.queue;

import java.util.List;

import uniandes.unacloud.common.enums.TransmissionProtocolEnum;
import uniandes.unacloud.share.queue.QueueTaskerConnection;
import uniandes.unacloud.share.queue.messages.MessageAddInstances;
import uniandes.unacloud.share.queue.messages.MessageCreateCopyFromExecution;
import uniandes.unacloud.share.queue.messages.MessageDeployCluster;
import uniandes.unacloud.share.queue.messages.MessageIdOfImage;
import uniandes.unacloud.share.queue.messages.MessageStopExecutions;
import uniandes.unacloud.share.queue.messages.MessageTaskMachines;
import uniandes.unacloud.share.enums.QueueMessageType;
import uniandes.unacloud.common.enums.TaskEnum;

/**
 * Class used to put task in queue messaging service that will be read by Control project
 * @author CesarF
 *
 */
public class QueueTaskerControl {
	
	/**
	 * Represents class to connect to queue provider
	 */
	private static QueueTaskerConnection controlQueue;
	
	/**
	 * Sets the queue manager used to send task. This method should only be called one time.
	 * @param newQueue
	 */
	public static void setQueueConnection(QueueTaskerConnection newQueue) {
		controlQueue = newQueue;
	}	
		
	/**
	 * Puts a task to deploy a cluster
	 * @param deployment
	 * @param user
	 */
//	public static void deployCluster(Deployment deployment, User user, TransmissionProtocolEnum transmissionType) {
//		MessageDeployCluster message = new MessageDeployCluster("7", deployment.getDatabaseId(), transmissionType);
//		controlQueue.sendMessage(message);
//	}
}
