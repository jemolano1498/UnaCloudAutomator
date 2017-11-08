package uniandes.unacloud.web.pmallocators;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import Entities.PhysicalMachine;
import Entities.Execution;


/**
 * Class to execute Round Robin allocator algorithms
 * Assigns an execution for each physical machine order by physical machine id
 * @author Clouder
 *
 */
public class RoundRobinAllocator extends ExecutionAllocator {

	/**
	 * Assigns an execution for each physical machine order by physical machine id
	 */
	@Override
	protected void allocateExecutions(List<Execution> executionList, List<PhysicalMachine> physicalMachines, Map<Long, PhysicalMachineAllocationDescription> physicalMachineDescriptions) throws AllocatorException {
		Collections.sort(physicalMachines, new Comparator<PhysicalMachine>() {
			public int compare(PhysicalMachine p1, PhysicalMachine p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
		ciclo1 : for (int nextVm = 0, lastNextVm = 0; nextVm < executionList.size();) {
			for (PhysicalMachine pm : physicalMachines) {
				if (nextVm >= executionList.size())
					break ciclo1;
				PhysicalMachineAllocationDescription pmad = physicalMachineDescriptions.get(new Long(pm.getId()));
				Execution nextExecution = executionList.get(nextVm);
				nextExecution.setExecutionNode(pm);
				if (pmad == null) {
					pmad = new PhysicalMachineAllocationDescription(pm.getId(), 0, 0, 0);
					physicalMachineDescriptions.put(pmad.getNodeId(), pmad);
				}
				pmad.addResources(nextExecution.getHardwareProfile().getCores(), nextExecution.getHardwareProfile().getRam(), 1);
				nextVm++;
				
			}
			if (lastNextVm == nextVm) {
				throw new AllocatorException("Cannot allocate all Executions on available insfrastructure");
			}
			lastNextVm = nextVm;
		}
	}

}
