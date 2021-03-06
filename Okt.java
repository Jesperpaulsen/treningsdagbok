package treningsdagbok;

import java.sql.*;
import java.util.Scanner;

public class Okt {

	private Connection conn;
	private int oktId;
	private String dato, varighet, tidspunkt, informasjon;

	public Okt(Connection conn) {
		this.conn = conn;
		this.oktId = getOktIdFromDB(conn);

	}

	public int getOktId() {
		return oktId;
	}

	public String getVarighet() {
		return varighet;
	}

	public String getDato() {
		return dato;
	}

	public String getTidspunkt() {
		return tidspunkt;
	}

	public String getInformasjon() {
		return informasjon;
	}

	public void addOkt(Scanner scanner) throws SQLException {
		System.out.println("Legg til treningsokt, skriv inn dato og tid i formatet yyyy-mm-dd");
		dato = scanner.nextLine();
		System.out.println("Hvilket tidspunkt trente du?");
		tidspunkt = scanner.nextLine();
		System.out.println("Hvor lenge varte okten");
		varighet = scanner.nextLine();
		System.out.println("Informasjon om okten");
		informasjon = scanner.nextLine();
		String oktSql = String.format(
				"INSERT INTO okt(dato,tidspunkt,varighet,informasjon) VALUES('%s', '%s', '%s', '%s')", getDato(),
				getTidspunkt(), getVarighet(), getInformasjon());
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(oktSql);
		//Legge til øvelser
		System.out.println("Ønsker du å legge til øvelse?(ja/nei)");
		while(scanner.nextLine().toLowerCase().equals("ja")) {
			Ovelse ovelse=new Ovelse(conn);
			ovelse.addOvelse(scanner);
			System.out.println("Ønsker du å legge til øvelse?(ja/nei)");
			
			}
	
		//Legge til notat
		System.out.println("Ønsker du å legge til notat?");
		String notatSvar = scanner.nextLine();
		if (notatSvar.toLowerCase().equals("ja")) {
			Notat notat = new Notat(conn, getOktIdFromDB(conn));
			notat.addNotat(scanner);	
		}

	}

	public int getOktIdFromDB(Connection conn) {
		String query = "SELECT oktId FROM okt ORDER BY oktID DESC LIMIT 1";
		try {
			ResultSet rs = getResultSet(conn, query);

			if (rs.next()) {
				int oktID = rs.getInt("oktId");
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
