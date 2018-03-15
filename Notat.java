package treningsdagbok;

import java.sql.*;
import java.util.Scanner;

public class Notat {

	private Connection conn;
	private String formal;
	private int oktId;
	private String opplevelse;

	public Notat(Connection conn, int oktId) {
		this.conn = conn;
	}

	public String getFormal() {
		return formal;
	}

	public int getOktId() {
		return oktId;
	}

	public String getOpplevelse() {
		return opplevelse;
	}

	public void addNotat(Scanner scanner) throws SQLException {
		this.oktId = getOktIdFromDB(conn);
		System.out.println("Formalet med okten?");
		formal = scanner.nextLine();
		System.out.println("Opplevelsen av okten?");
		opplevelse = scanner.nextLine();

		String notatSql = String.format("INSERT INTO notat('%d','%s','%s')", getOktId(), getFormal(), getOpplevelse());
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(notatSql);

	}

	public int getOktIdFromDB(Connection conn) {
		String query = "SELECT oktID FROM treningsokt ORDER BY oktID DESC LIMIT 1";
		try {
			ResultSet rs = getResultSet(conn, query);
			if (rs.next()) {
				int øktID = rs.getInt("øktID");
				return øktID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static ResultSet getResultSet(Connection conn, String query) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(query);
	}

	public static void main(String[] args) {

	}
}
