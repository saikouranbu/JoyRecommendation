package joy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class JoyDAO {
	final private static String dbname = "joy";
	final private static String user = "joymanager";
	final private static String password = "joyjoy";
	final private static String driverClassName = "org.postgresql.Driver";
	final private static String url = "jdbc:postgresql://localhost/" + dbname;
	private static JoyDAO dao = new JoyDAO();

	Connection connection;

	PreparedStatement prepStmt_S; // SELECT
	PreparedStatement prepStmt_I; // INSERT

	String strPrepSQL_S = "SELECT * FROM joymusic WHERE id = ?";
	String strPrepSQL_I = "INSERT INTO joymusic VALUES(?,?,?,?)";

	private JoyDAO() {
		try {
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, user, password);

			prepStmt_S = connection.prepareStatement(strPrepSQL_S);
			prepStmt_I = connection.prepareStatement(strPrepSQL_I);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JoyDAO getInstance() {
		return dao;
	}

	public void exit() {
		try {
			prepStmt_S.close();
			prepStmt_I.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(String id, String name, String artist, String relation) {
		try {
			prepStmt_I.setString(1, id);
			prepStmt_I.setString(2, name);
			prepStmt_I.setString(3, artist);
			prepStmt_I.setString(4, relation);
			prepStmt_I.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vector<Music> select(String id) {
		try {
			prepStmt_S.setString(1, id);
			ResultSet r = prepStmt_S.executeQuery();
			Vector<Music> v = new Vector<Music>();
			while (r.next()) {
				v.add(new Music(r.getString(1), r.getString(2), r.getString(3),
						r.getString(4)));
			}
			return v;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
