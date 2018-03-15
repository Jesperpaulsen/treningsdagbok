package treningsdagbok;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Okt {

	private Connection conn;
	private int oktId, varighet;
	private java.sql.Date dato;
	private String informasjon;

	public Okt(Connection conn) {
		this.conn = conn;
		this.oktId = getOktIdFromDB(conn);

	}
	public int getOktId() {return oktId;}
	public int getVarighet(){return varighet;}
	public java.sql.Date getDato() {return dato;}
	public String getInformasjon() {return informasjon;}
	
	@SuppressWarnings("deprecation")
	public void addOkt(Scanner scanner) throws SQLException {
		System.out.println("Legg til treningsokt, skriv inn dato og tid i formatet yyyymmdd");
		String date=scanner.nextLine();
		dato = java.sql.Date.valueOf( date );
		System.out.println("Hvor lenge varte okten i minutter?");
		varighet=Integer.parseInt(scanner.nextLine());
		System.out.println("Informasjon om okten");
		informasjon=scanner.nextLine();
		
	String oktSql = String.format("INSERT INTO okt VALUES(%d, %d, %d, '%s', '%s')", getOktId(),getDato(), getVarighet(), getInformasjon());
	
	Statement stmt=conn.createStatement();
	stmt.executeUpdate(oktSql);
	
	}
	
	public int getOktIdFromDB(Connection conn) {
		String query = "SELECT oktId FROM okt ORDER BY oktID DESC LIMIT 1";
		try {
			ResultSet rs = getResultSet(conn, query);
			
			if (rs.next()) {
				int oktID = rs.getInt("oktID") + 1;
				return oktID;
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
}
