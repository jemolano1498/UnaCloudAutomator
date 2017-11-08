package Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Entities.Cluster;
import Entities.DeployedImage;
import Entities.Deployment;
import Entities.Execution;
import Entities.ImageRequest;
import Entities.NetInterface;
import Entities.PhysicalMachine;
import uniandes.unacloud.common.enums.TransmissionProtocolEnum;
import uniandes.unacloud.web.pmallocators.AllocatorEnum;
import uniandes.unacloud.web.pmallocators.AllocatorException;
import uniandes.unacloud.web.pmallocators.PhysicalMachineAllocationDescription;
import uniandes.unacloud.web.queue.QueueTaskerControl;

public class DeploymentService {
	
	QueryFactory qf ;
	
	private static DeploymentService deploymentservice;
	
	public static DeploymentService getInstance() {
		
		if (deploymentservice != null) {
			return deploymentservice;
		}
		else {
			new DeploymentService();
			return deploymentservice;
		}
		
		
	}
	
	public DeploymentService() {
		super();
		this.qf = QueryFactory.getInstance();
		deploymentservice = this;
	}

	public void deploy (Cluster cluster, String user, long time, ImageRequest request ) {
		List<PhysicalMachine> pms = new ArrayList<>();
		pms.addAll(qf.getAllowedMachines());
		Map<Long, PhysicalMachineAllocationDescription> pmDescriptions = getPhysicalMachineUsage(pms);
		
		DeployedImage depImage= new DeployedImage(request.image);
		ArrayList<Execution> executions = new ArrayList<Execution>();
		for (int j = 0 ; j < request.instances; j++) {		
			executions.add( new Execution( depImage, 
				request.hostname, 
				"Initializing", 
				request.hp, 
				time,		
				"REQUESTED"));
		}
		depImage.executions = executions;	
		
		allocatePhysicalMachines(executions, pms, pmDescriptions);
		try {
			allocateIPAddresses(depImage.executions);
		} catch (AllocatorException e) {
			e.printStackTrace();
		}
		
		Deployment dep = new Deployment(cluster, time, "ACTIVE");
		qf.insertDeploy(dep); //deployment
		qf.insertDeployedImage(depImage, dep);	//deployed_image
		for (Execution execution : depImage.executions) {
			execution.image = depImage;
			qf.insertExecution(execution);	//execution
			qf.insertNetInterface(execution.interfaces);	//net_interface
		}
		
		QueueTaskerControl.deployCluster(dep,TransmissionProtocolEnum.getEnum( "TCP"));		
		
//		return dep;
		
	}
	
	private void allocateIPAddresses(ArrayList<Execution> executions) throws AllocatorException {
		for (Execution vme : executions) {
			if (vme.state.equals("REQUESTED")) {
				List <String> ips = qf.getAvailableIps(vme.executionNode.laboratory);
				for (String ip : ips) {
					NetInterface netInterface = new NetInterface("eth0", ip, vme);
					vme.interfaces=netInterface;
					qf.reserveIp(ip);
					String[] subname = ip.split("\\.");
					vme.setName(vme.name + subname[2] + subname[3]);
					break;
				}
//				if (vme.interfaces == null) { 
//					for (Execution vm : executions)
//						if (vme.state.equals("REQUESTED"))
//							qf.freeIp(vme.interfaces.ip);
//													
//					throw new AllocatorException("Not enough IPs for this deployment");
//				}
			}
		}
		
	}

	public void allocatePhysicalMachines(List<Execution> vms,List<PhysicalMachine> pms, Map<Long, PhysicalMachineAllocationDescription> pmDescriptions) {
		AllocatorEnum allocator = AllocatorEnum.getAllocatorByName("Round Robin");
		try {
			allocator.getAllocator().startAllocation(vms, pms, pmDescriptions);
		} catch (AllocatorException e) {
			e.printStackTrace();
		}
	}
	
	public Map<Long, PhysicalMachineAllocationDescription> getPhysicalMachineUsage(List<PhysicalMachine> pms) {
		Map<Long,PhysicalMachineAllocationDescription> pmDescriptions = new TreeMap<>();
		
		if (pms.size() == 0)
			return pmDescriptions;
		
		String listId = "";
		for (int i = 0; i < pms.size(); i++) {
			listId += pms.get(i).id;
			if (i != pms.size() - 1)
				listId += ",";
		}
		
		pmDescriptions = qf.getUnavailableMachines(pmDescriptions,listId);
		
		return pmDescriptions;
	}

}
