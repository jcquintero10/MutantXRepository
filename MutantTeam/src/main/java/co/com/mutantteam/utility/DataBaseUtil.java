package co.com.mutantteam.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * @author juquintero
 *
 */
public class DataBaseUtil {
	
	private static String url = "jdbc:h2:file:~/mutant-h2-db;DB_CLOSE_ON_EXIT=FALSE;IFEXISTS=TRUE;DB_CLOSE_DELAY=-1;AUTO_RECONNECT=TRUE;";
	private static String user = "sa";
    private static String pass = "";
    
    private DataBaseUtil() {
       throw new IllegalStateException("DataBaseUtil class");
    }

    public static Connection getConnection() throws MutantException {
        Connection connection = null;
        try {
        	Properties props = new Properties();
        	props.setProperty("user", user);
        	props.setProperty("password", pass);
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
        	throw new MutantException(Constant.MESSAGE_ERROR_DATA_BASE +  e.getMessage());
        }
        return connection;
    }

}
