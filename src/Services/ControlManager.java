package Services;

import uniandes.unacloud.common.utils.UnaCloudConstants;
import uniandes.unacloud.share.db.DatabaseConnection;
import uniandes.unacloud.share.manager.ProjectManager;
import uniandes.unacloud.share.queue.QueueMessageReceiver;
import uniandes.unacloud.share.queue.QueueRabbitManager;
import uniandes.unacloud.share.utils.EnvironmentManager;
import uniandes.unacloud.web.queue.QueueTaskerControl;

/**
 *  Initializes and control all services in project. It class extends from Project Manager class and works as a Singleton class.
 * @author CesarF
 *
 */
public class ControlManager extends ProjectManager {
	
	/**
	 * instance of control manager
	 */
	private static ControlManager control;
	
	/**
	 * Number of initial and minimum active connections to database
	 */
	private static final int POOL_SIZE = 5;

	/**
	 * Number of concurrent threads to process messages from physical machines
	 */
	private static final int CONCURRENT_THREADS_PM = 50;
	
	/**
	 * Number of concurrent threads to process messages from virtual machines status
	 */
	private static final int CONCURRENT_THREADS_VM = 30;
	
	/**
	 * Number of concurrent threads to process messages from queue
	 */
	private static final int CONCURRENT_THREADS_QUEUE = 50;
	
	/**
	 * Number of tasks in each thread in queue processor
	 */
	private static final int TASK_BY_THREAD_QUEUE = 5;
	
	/**
	 * Creates a project manager with all services
	 */
	public ControlManager() {
		super();
	}
	
	/**
	 * Returns control  manager instance
	 * @return instance
	 */
	public static ControlManager getInstance() {
		try {
			if (control == null)
				control = new ControlManager();
			return control;
		} catch (Exception e) {
			return null;
		}		
	}	
	
	/**
	 * Returns the configured communication agent port
	 * @return agent port
	 * @throws Exception 
	 */
	public int getPort() throws Exception {
		return reader.getIntegerVariable(UnaCloudConstants.AGENT_PORT);
	}
	

	@Override
	protected String getPropetiesFileName() {
		System.out.println("Load file: " + EnvironmentManager.getConfigPath() + UnaCloudConstants.FILE_CONFIG);
		return EnvironmentManager.getConfigPath() + UnaCloudConstants.FILE_CONFIG;
	}

	/**
	 * Returns the list of variables required by services
	 * @return string array
	 */
	@Override
	protected String[] getVariableList() {
		return new String[]{
				UnaCloudConstants.CONTROL_MANAGE_VM_PORT,
				UnaCloudConstants.CONTROL_MANAGE_PM_PORT,
				UnaCloudConstants.QUEUE_USER,
				UnaCloudConstants.QUEUE_PASS,
				UnaCloudConstants.QUEUE_IP,
				UnaCloudConstants.QUEUE_PORT,
				UnaCloudConstants.DB_NAME,
				UnaCloudConstants.DB_PASS,
				UnaCloudConstants.DB_PORT,
				UnaCloudConstants.DB_IP,
				UnaCloudConstants.DB_USERNAME,
				UnaCloudConstants.AGENT_PORT};
    }

	/**
	 * Initial configuration for Database connection pool
	 * @throws Exception
	 */
	@Override
	protected void startDatabaseService() throws Exception {
		System.out.println("Start database service");
		connection = new DatabaseConnection();
		connection.connect(
				"dbunacloud", 
				3306,
				"157.253.236.113", 
				"root", 
				"adminUnaCloud",
				POOL_SIZE);
		connection.getConnection().close();		
	}

	/**
	 * Starts the queue connection service to receive messages
	 * @throws Exception
	 */
	@Override
	protected void startQueueService() throws Exception {
//		System.out.println("Start queue service " + "157.253.236.113" + ":" + "5672");
//		QueueRabbitManager rabbitManager = new QueueRabbitManager(
//				"admin",
//				"adminUnaCloud", 
//				"157.253.236.113", 
//				5672, 
//				UnaCloudConstants.QUEUE_CONTROL);
//		queueReceiver = new QueueMessageReceiver();
//		queueReceiver.createConnection(rabbitManager);
//		processor = new QueueMessageProcessor(CONCURRENT_THREADS_QUEUE, TASK_BY_THREAD_QUEUE);
//		queueReceiver.startReceiver(processor);		
		QueueRabbitManager queueControl = new QueueRabbitManager(
				"admin", 
				"adminUnaCloud",
				"157.253.236.113", 
				5672, 
				UnaCloudConstants.QUEUE_CONTROL);
		System.out.println("Start queue service " + "157.253.236.113" + ":" + "5672");
		QueueTaskerControl.setQueueConnection(queueControl);
	}
	

//	/**
//	 * Starts the communication socket readers 
//	 */
//	@Override
//	protected void startCommunicationService() throws Exception {
//		System.out.println("Start communication service");
//		new PmMessageReceiver(reader.getIntegerVariable(UnaCloudConstants.CONTROL_MANAGE_PM_PORT), CONCURRENT_THREADS_PM).start();
//		new VmMessageReceiver(reader.getIntegerVariable(UnaCloudConstants.CONTROL_MANAGE_VM_PORT), CONCURRENT_THREADS_VM).start();
//	}	
	
	/**
	 * Sends message to agents not have to put message in queue
	 * @param ids
	 */
	public void sendStopMessageExecutions(Long[] ids) {
//		processor.remoteStopDeploy(ids);
	}

	@Override
	protected void startCommunicationService() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
