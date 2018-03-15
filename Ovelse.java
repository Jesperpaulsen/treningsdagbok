package treningsdagbok;

import java.sql.*;
import java.util.Scanner;

public class Ovelse {
	
	
	// ovelse:
		private String navn, type;
		private int OvelseID, OktID, ApparatID, reps, sett;
		
		
	// Fastmontert:
		
	// Ikke Fastmontert:
		private String beskrivelse;
		private Connection forb;
		
		
// Metoder:	
		
		public Ovelse(Connection n) { forb = n;}
		public String getNavn() {return navn;}
		public String getType() {return type;}
		public String getBeskrivelse() {return beskrivelse;}
		public int getOvelseID() {return OvelseID;}
		public int getApparatID() {return ApparatID;}
		public int getOktID() {return OktID; }
		public int getReps() {return reps;}
		public int getSett() {return sett;}
		
		
	//Finner de tilgjengelige og ønskelige øvelsene i databasen
	   
	    public int OvelseID_DB(Connection n){
	        String query = "SELECT OvelseID FROM Ovelse ORDER BY OvelseID DESC LIMIT 1";
	        try {
	            ResultSet rs = getResultSet(n, query);
	            if (rs.next()){
	                int ovelseID = rs.getInt("ovelseID") + 1;
	                return ovelseID;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return 0;
	    }
	    
	    
	    
	     
	//Finner tilgjengelige og ønskelige øktene i databasen, 1 om gangen?
	    
	    public int OktID_DB(Connection n){
	        String query = "SELECT OktID FROM Okt ORDER BY OktID DESC LIMIT 1";
	        try {
	            ResultSet rs = getResultSet(n, query);
	            if (rs.next()){
	                int OktID = rs.getInt("oktID");
	                return OktID;
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
		
	    
	     
	 
	    
	    
	    //Legger til de øvelsene vi har brukt iløpet av økten, i treningsdagboken
	     
	     
	     public void addOvelse(Scanner s) throws SQLException {
	    	    this.OktID = OktID_DB(forb);
	        this.OvelseID = OvelseID_DB(forb);
	        System.out.println("Legge til en fastmontert eller ikke-fastmontert ovelse: \n" +
	                "(f)astmontert eller (i)kke-fastmontert");
	        String n = s.nextLine();
	        switch (n){
	            case ("f"):
	                addFastmontert(s, OktID);
	                break;
	            case ("i"):
	                addIkkeFast(s, OktID);
	                break;
	            default:
	                System.out.println("Ikke valid type øvelse");
	                break;
	        }
	    }
		
		
	    
	    
	     
	    // Fastmontert har ingen attributter, men arver ID og navn fra Ovelse
	    // Legger til en fastmontert øvelse:
	      
	     private void addFastmontert(Scanner s, int okt) throws SQLException {
	         
	         System.out.println("Hvilken fastmontert øvelse vil du legge til: ");
	         navn = s.nextLine();
	         System.out.println("Hva er navnet på øvelsen?");
	         navn = s.nextLine();
	         																									//nødvendig?:
	         String ovelseSQL = String.format("INSERT INTO Ovelse VALUES(%d, '%s')", getOvelseID(), getNavn());
	         String fastSQL = String.format("INSERT INTO fastmontert VALUES(%d)", getOvelseID(), getApparatID());

	         System.out.println("Skal valgt øvelse legges til: (ja / nei)");
	         String m =  s.nextLine();
	         if (m =="ja"){
	             Statement a = forb.createStatement();
	             a.executeUpdate(ovelseSQL);
	             a.executeUpdate(fastSQL);
	             nyOvelse(s);
	         } else {
	             System.out.println("Avsluttet, ingen øvelser ble lagt til");
	         }
	     }
	     
	     // Ikkefastmontert har 1 attributt: Beskrivelse, i tillegg til ID og navn
	     // Legger til en ikke fastmontert øvelse:
	     
	     private void addIkkeFast(Scanner s, int okt) throws SQLException{
	         System.out.println("Hvilken fastmontert øvelse vil du legge til:");
	         System.out.println("Navn på øvelsen?");
	         navn = s.nextLine();
	         System.out.println("En beskrivelse av øvelsen:");
	         beskrivelse = s.nextLine();
	         
	      //Her er jobben, hvor mange attributter skal vi ha med, ta hensyn til på hver av de? samme for fastmontert
	         String ovelseSQL = String.format("INSERT INTO ovelse VALUES('%d', '%s')", getOvelseID(), getNavn());
	         String ikkefastSQL = String.format("INSERT INTO ikkeFastmontert('ovelseId','beskrivelse' VALUES('%d','%s')", getOvelseID(),getBeskrivelse());

	         System.out.println("Skal valgt øvelse legges til: (ja / nei)");
	         String m = s.nextLine();
	         if (m == "ja"){
	             Statement a = forb.createStatement();
	             a.executeUpdate(ovelseSQL);
	             a.executeUpdate(ikkefastSQL);
	             nyOvelse(s);
	         } else {
	             System.out.println("Avsluttet, ingen øvelser ble lagt til");
	         }
	     }
	     
	     
	     
	     
	     
	     //Ekstra øvelser //         er denne nødvendig??????????
	     private void nyOvelse(Scanner s) throws SQLException{
	         
	    	     System.out.println("Legge til en ny øvelse: ");
	         String m = s.nextLine();
	         if (m == "ja"){
	             addOvelse(s);
	         } else {
	         	System.out.println("Avsluttet");
	         	return;
	         }
	     }
	   
	     
	     
	    //
	     
	     /*
	      * Henter ResultSet, eller data fra databasen for bruk i diverse get-operasjoner
	      
	      * Koblingen til databasen
	      * @param query
	      * SQL-queryen som brukes for å hente data fra databasen
	      * @return ResultSet
	      * @throws SQLException
	      */
	     
	     
	     private static ResultSet getResultSet(Connection conn, String query) throws SQLException {
	         Statement stmt = forb.createStatement();
	         return stmt.executeQuery(query);
	     }

	     
	    
		

}
