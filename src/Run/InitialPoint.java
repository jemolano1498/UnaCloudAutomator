package Run;

import java.sql.Connection;
import Services.ControlManager;
import Services.GroupConfigurer;
import Services.JSONReader;
import Services.QueryFactory;

public class InitialPoint {

	public static void main(String[] args) {
		
		/**
		 * The DB connection initialization
		 */
		Connection connection = ControlManager.getInstance().getDBConnection();
		
		/**
		 * The class of SQL Queries is initialized
		 */
		new QueryFactory(connection);
		
		/**
		 * The User ID is assigned as a constant
		 */
		FinalStaticsVals.USERNAME_ID = QueryFactory.getInstance().getUserId(FinalStaticsVals.USERNAME);
		
		/**
		 * The JSON reader is initialized
		 */
		JSONReader jsonReader = new JSONReader();
		
		/**
		 * The class that makes the configurations is initialized
		 */
		GroupConfigurer config = new GroupConfigurer(jsonReader.readJson());
		
		/**
		 * The deployment process starts
		 */
		config.start();
		
	}

}
