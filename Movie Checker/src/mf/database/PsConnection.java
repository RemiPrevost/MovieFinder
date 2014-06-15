package mf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PsConnection {
	private String url = "jdbc:postgresql://localhost:5432/Films";
	private String user = "postgres";
	private String passwd = "remi050393";
	private static Connection connect;
	
	private PsConnection(){
		try {
			connect = DriverManager.getConnection(this.url,this.user,this.passwd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getInstance(){
		if (connect == null)
			new PsConnection();
		
		return connect;
	}
}
