package mf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PsConnection {
	private static String url = "jdbc:postgresql://localhost:5432/Films";
	private static String user = "postgres";
	private String passwd = "remi050393";
	private static Connection connect;
	
	private PsConnection() throws SQLException{
		connect = DriverManager.getConnection(url,user,passwd);
	}
	
	public static Connection getInstance() throws SQLException{
		if (connect == null)
			new PsConnection();
		
		return connect;
	}

	public static String getUrl() {
		return url;
	}

	public static String getUser() {
		return user;
	}
}
