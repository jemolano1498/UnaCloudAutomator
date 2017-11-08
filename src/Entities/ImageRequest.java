package Entities;

public class ImageRequest {
	
	public HardwareProfile hp;
	
	public int instances; 
	
	public Image image;
	
	public String hostname;
	
	public boolean high;
	
	public int time;
	
	public ImageRequest (Image image, HardwareProfile hp, int instances, String hostname, boolean high) {
		
		boolean throwEx = false;
		
		if (image != null)
			this.image = image; 
		else throwEx = true;
		
		if (hp != null)
			this.hp = hp; 
		else throwEx = true;
		
		if (instances > 0) 
			this.instances = instances; 
		else throwEx = true;
		
		if (hostname != null)
			this.hostname = hostname; 
		else throwEx = true;
		
		this.high = high;
		
//		if (throwEx)
//			throw new Exception("Field values are not valid");
	}
	
	public ImageRequest() {
		
	}
	
	@Override
	public String toString() {
		return "ImageRequestOptions [ram=" + hp.ram + ", cores=" + hp.cores+", instances=" + instances + ", Image= "+image+", HighA= "+high+", Host= "+hostname+"]";
	}
	

}
