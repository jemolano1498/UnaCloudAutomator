package uniandes.unacloud.web.pmallocators;

/**
 * represents types of allocator algorithms 
 * @author Clouder
 *
 */
public enum AllocatorEnum {
	
	/**
	 * Assigns an for each physical machine order by physical machine id
	 */
	ROUND_ROBIN(new RoundRobinAllocator(), "Round Robin");
	
	/**
	 * Unused
	 */
	//GREEN(null,"Green"),
	
	/**
	 * allocator class to execute algorithm
	 */
	public ExecutionAllocator allocator;
	
	/**
	 * Name of allocator
	 */
	private String name;
	
	/**
	 * Creates an allocator enum
	 * @param allocator enum
	 * @param name for enum
	 */
	private AllocatorEnum(ExecutionAllocator allocator, String name) {
		this.allocator = allocator;
		this.name = name;
	}
	
	/**
	 * Returns allocator class to execute algorithm
	 * @return allocator class 
	 */
	public ExecutionAllocator getAllocator() {
		return allocator;
	}
	
	/**
	 * Returns name of allocator
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return a list of current types of allocator
	 * @return
	 */
	public static String[] getList() {
		return new String[]{
				ROUND_ROBIN.name};
	}
	
	/**
	 * Searches and returns an allocator class based in string name
	 * @param name to search
	 * @return allocator type
	 */
	public static AllocatorEnum getAllocatorByName(String name) {
		if (name.equals(ROUND_ROBIN.name)) return ROUND_ROBIN;
		return null;
	}
}
