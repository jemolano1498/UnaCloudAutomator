package Run;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TimesClass {
	
	private static TimesClass singletonTime = new TimesClass( );
	
	public long actualTime;
	public int maquina;
	public int endTime;
	 public Logger logger;
	
	public TimesClass (){
		maquina = 1;
		actualTime = 0;
		logger = Logger.getLogger("MyLog");  
	    FileHandler fh;  

	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("MyLogFile.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	        logger.info("My first log");  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  

	    logger.info("Inicia"); 
	}
	
	public void calcularTiempo (int i, long tiempo, String mensaje){
		if (i == 0){
			actualTime = tiempo;
		}
		if (i == 1){
			long measure = tiempo - actualTime;
			logger.info(maquina + " " + mensaje + " " + measure); 
			actualTime = 0;
		}
		
	}
	
	
	public void setEndTime (int tiempo){
		logger.info(maquina + " Termina ");
		maquina++;
	}
	
	public static TimesClass getInstance( ) {
	      return singletonTime;
	   }

}
