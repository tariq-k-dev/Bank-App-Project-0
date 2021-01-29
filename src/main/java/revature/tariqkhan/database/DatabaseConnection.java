package revature.tariqkhan.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	public static Connection dbConnection() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/BankApp", "", "");
			// System.out.println("Database connection established...");
			
			return conn;
		}  catch (SQLException e) {
	        System.out.println("Connection failure.");
	        e.printStackTrace();
	    }
		return null;
	}
}

