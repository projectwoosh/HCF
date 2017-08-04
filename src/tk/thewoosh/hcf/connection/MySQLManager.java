package tk.thewoosh.hcf.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.huskehhh.mysql.mysql.MySQL;

import tk.thewoosh.hcf.Settings;

public class MySQLManager {

	private static final MySQL MYSQL = new MySQL(Settings.MYSQL_HOST, Settings.MYSQL_PORT, Settings.MYSQL_DATABASE, Settings.MYSQL_USER, Settings.MYSQL_PASSWORD);
	private static final MySQL MYSQL_SERVER = new MySQL(Settings.MYSQL_HOST, Settings.MYSQL_PORT, Settings.MYSQL_SERVER_DATABASE, Settings.MYSQL_USER, Settings.MYSQL_PASSWORD);
	
	private static Connection connection, connection_server;
	
	public static void openConnection() {
		try {
			connection = MYSQL.openConnection();
			connection_server = MYSQL_SERVER.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}

	public static Connection getServerConnection() {
		return connection_server;
	}

	public static void closeConnection() {
		try {
			MYSQL.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}