package treningsdagbok;

import java.sql.*;
import java.util.Scanner;





public class Gruppe {
	
//Variabler
	
	private String type, beskrivelse;
    private int GruppeID;
    private int OvelseID;
	
	private Connection forbindelse;

    
    
//Metoder
    
	public String getType() { 
		return type; 
		}
    
	public String getBeskrivelse() { 
		return beskrivelse; 
		}
	
	public int getGruppeID() { 
		 return GruppeID; 
		 }
    
    public int getOvelseID() { 
    	return OvelseID; 
    	}
    
    
    
    
     
    public Gruppe(Connection n) {
        forbindelse = n;
        GruppeID = GruppeID_DB(n);
    }

    
    
     public void lagOvelsesGruppe(Scanner s)  {
        System.out.println("Ny ovelsesgruppe");
        System.out.println("Navn på gruppen:");
        type = s.nextLine();
        System.out.println("Fastmontert eller ikke?");
        beskrivelse = s.nextLine();
        System.out.println("Legg til ovelse:?");
        ovelser(s);

        String SQLgruppe = String.format("INSERT INTO Ovelsesgruppe VALUES(%d, '%s', %d)", getGruppeID(), getType(), getOvelseID());
        System.out.println("Legge til gruppe (Nei/Ja):");
        String avslutt = s.nextLine();
        
        if (avslutt == "ja"){
            Statement m = forbindelse.createStatement();
            m.executeUpdate(SQLgruppe);
            antallGrupper(s);     //!!!!!!!!!!!!!!!!!!!
        } else {
            System.out.println("Databasen ble ikke oppdatert med en ny gruppe");
        }
    }


    private void antallGrupper(Scanner s) throws SQLException{
        
    	System.out.println("Legge til flere grupper(Nei/Ja):");
    	String avslutt = s.nextLine();
    	while (avslutt =="ja"){
    		lagOvelsesGruppe(s);
        }
        System.out.println("Avsluttet");
        return;
    }


    public void ovelser(Scanner s) throws SQLException{
        
    	System.out.println("Velg en ovelse");
        String n = "SELECT OvelseID, Navn FROM Ovelse";
        try {
            ResultSet m = getResultSet(forbindelse, n);
            while (m.next()){
                System.out.println(m.getInt("OvelseID") + ", " + m.getString("Navn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("OvelseID:");
        OvelseID = Integer.parseInt(s.nextLine());
    }
    
    /**
     * Henter gruppeID for bruk i programmet
     * @param conn
     * @return gruppeID eller 0
     */
    public int GruppeID_DB(Connection conn){
        String query = "SELECT gruppeID FROM gruppe ORDER BY gruppeID DESC LIMIT 1";
        try {
            ResultSet rs = getResultSet(conn, query);
            if (rs.next()){
                int gruppeID = rs.getInt("gruppeID") + 1;
                return gruppeID;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private static ResultSet getResultSet(Connection n, String m) throws SQLException{
        Statement s = n.createStatement();
        return s.executeQuery(m);
    }

}
