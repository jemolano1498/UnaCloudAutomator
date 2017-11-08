package uniandes.unacloud.web.pmallocators;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Entities.DeployedImage;
import Entities.Execution;
import Entities.Image;
import Entities.PhysicalMachine;


/**
 * Abstract class with main methods to allocate deployments. Validates enough resources in physical machine and enough IPs in lab
 * The purpose of this class is to be extended to code allocator algorithms 
 * @author Clouder and CesarF
 *
 */
public abstract class ExecutionAllocator{
	
	/**
	 * Required IPs for deployment
	 */
	private TreeMap<Long, Integer> ipsNeeded;
	
	/**
	 * Creates an execution allocator
	 */
	public ExecutionAllocator() {
		ipsNeeded = new TreeMap<Long, Integer>();
	}
	
	/**
	 * Start the allocation process
	 * @param executionList
	 * @param physicalMachines
	 * @param physicalMachineDescriptions
	 * @throws AllocatorException
	 */
	public synchronized void startAllocation(List<Execution> executionList, List<PhysicalMachine> physicalMachines, Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions)throws AllocatorException{
		ipsNeeded = new TreeMap<Long, Integer>();
		allocateExecutions(executionList, physicalMachines, physicalMachineDescriptions);
	}

	/**
	 * Method to match physical machines with executions.
	 * @param executionList
	 * @param physicalMachines
	 * @param physicalMachineDescriptions
	 * @throws AllocatorException
	 */
	protected abstract void allocateExecutions(List<Execution> executionList, List<PhysicalMachine> physicalMachines, Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions)throws AllocatorException;
	
}
