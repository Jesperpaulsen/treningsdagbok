package treningsdagbok;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ViseOvelserIgruppe {
	public Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;

	public ViseOvelserIgruppe(Connection conn) {
		this.conn = conn;
	}

	public void toString(Scanner scanner) {
		System.out.println("Hvilken øvelsesgruppe ønsker du å se øvelser fra?");
		String ovelsesgruppe = scanner.nextLine();
		
		try {
			stmt = conn.createStatement();
			String query = "SELECT navn \r\n"+ "FROM\r\n"
					+ " ovelsesgruppe\r\n" + "INNER JOIN ovelse ON ovelsesgruppe.ovelsesGruppeId=ovelse.ovelsesGruppeId\r\n" + "WHERE\r\n"
					+ "	gruppeType= "+"'"+ovelsesgruppe+"'";
			if (stmt.execute(query)) {
				rs = stmt.getResultSet();
				
			}
			while (rs.next()) {
			
				String ovelsesnavn = rs.getString(1);
				System.out.println("Øvelse i "+ovelsesgruppe+ ": "+ovelsesnavn);
			}

		} catch (SQLException ex) {

		}
	}

}
