package treningsdagbok;

import java.sql.*;
import java.util.Scanner;

public class Notat {

	private Connection conn;
	private String formal;
	private String oktId;
	private String opplevelse;

	public Notat(Connection conn, int oktId) {
		this.conn = conn;
	}

	public String getFormal() {
		return formal;
	}

	public String getOktId() {
		return oktId;
	}

	public String getOpplevelse() {
		return opplevelse;
	}

	public void addNotat(Scanner scanner) throws SQLException {
		oktId = getOktIdFromDB(conn);
		System.out.println("Formalet med okten?");
		formal = scanner.nextLine();
		System.out.println("Opplevelsen av okten?");
		opplevelse = scanner.nextLine();

		String notatSql = String.format("INSERT INTO notat VALUES('%s','%s','%s')", getOktId(), getFormal(), getOpplevelse());
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(notatSql);

	}

	public String getOktIdFromDB(Connection conn) {
		String query = "SELECT oktId FROM okt ORDER BY oktID DESC LIMIT 1";
		try {
			ResultSet rs = getResultSet(conn, query);
			if (rs.next()) {
				this.oktId = rs.getString("oktId");
				return oktId;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static ResultSet getResultSet(Connection conn, String query) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(query);
	}

	public static void main(String[] args) {

	}
}
