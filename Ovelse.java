package treningsdagbok;

import java.sql.*;
import java.util.Scanner;

public class Ovelse {
	 private Connection conn;
	 private Statement stmt=null;
	 private ResultSet rs=null;
	 
	 private String navn, type, beskrivelse,ovelseId, oktId,gruppeId;
	 private int apparatId,reps, sett, kilo;
	 
	 //Konstruktør
	 public Ovelse(Connection conn,String gruppeId)
	 {
		 this.conn=conn;
		 this.gruppeId=gruppeId;
	 }
	 
	 //Gettere
	 public String getNavn(){return this.navn;}
	 public String getType(){return this.type;}
	 public String getBeskrivelse() {return this.beskrivelse;}
	 public String getOvelseId() {return this.ovelseId;}
	 public String getOktId() {return this.oktId;}
	 public int getApparatId() {return this.apparatId;}
	 public int getReps() {return this.reps;}
	 public int getSett() {return this.sett;}
	 public int getKilo() {return this.kilo;}
	 
	 public void addOvelseIOkt(Scanner scanner,String oktId) throws SQLException
	 {
		 System.out.println("Hvilken øvelse ønsker du å legge til?");
		 navn=scanner.nextLine();
		 if(doesOvelseExist(navn))
		 {
			 System.out.println("Hvor mange sett tok du?");
			 String sett = scanner.nextLine();
			 System.out.println("Hvor mange reps tok du?");
			 String reps = scanner.nextLine();
			 System.out.println("Hvor mange kilo tok du?");
			 String kilo = scanner.nextLine();
			 String ovelseId=getOldOvelseId(conn,navn);
			 String ovelseIOktQuerry=String.format("INSERT INTO ovelseprokt VALUES('%s','%s','%s','%s','%s'",oktId,ovelseId,sett,reps,kilo);
			 insertQuerry(conn,ovelseIOktQuerry);
		 }
		 else
		 {
			 addOvelse(scanner,navn);
		 }

	 }
	 
	 public boolean doesOvelseExist(String navn)
	 {
		 String SQLquerryExists="SELECT \r\n" + 
			 		"	navn\r\n" + 
			 		"FROM\r\n" + 
			 		"	ovelse\r\n" + 
			 		"WHERE EXISTS\r\n" + 
			 		"	(SELECT navn FROM ovelse WHERE navn='"+navn+"';";
		 try {
				stmt=conn.createStatement();
				if(stmt.execute(SQLquerryExists)){rs=stmt.getResultSet();}
				if(rs.getString(1).equals("true") ){return true;}	
			}
			catch(SQLException ex) {
				System.out.println("SQLException: "+ex.getMessage());
			}
		 return false;
	 }
	 
	 public boolean doesOvelseGruppeExist(String navn)
	 {
		 String SQLquerryExists="SELECT \r\n" + 
			 		"	COUNT(navn)\r\n" + 
			 		"FROM\r\n" + 
			 		"	ovelsesgruppe\r\n" + 
			 		"WHERE\r\n" + 
			 		"	navn='"+navn+"';";
		 try {
				stmt=conn.createStatement();
				if(stmt.execute(SQLquerryExists)){rs=stmt.getResultSet();}
				if(rs.next()){return true;}	
			}
			catch(SQLException ex) {
				System.out.println("SQLException: "+ex.getMessage());
			}
		 return false;
	 }

	 //Må få tak i øvelsesgruppe
	 public void addOvelse(Scanner scanner,String navn) throws SQLException
	 {
		 System.out.println("Vi fant ikke denne øvelsen i databasen. Ønsker du å legge til "+navn+"?");
		 String valg=scanner.nextLine();
		 if(valg.toLowerCase().equals("ja"))
		 {
			 String gruppeId;
			 String ovelseId;
			 System.out.println("Er øvelsen (f)astmontert eller (i)kke fastmontert?");
			 String valgtType=scanner.nextLine();
			 if(valgtType.toLowerCase().equals("i"))
			 {
				 addIkkeFastmontert(navn,scanner);
				 gruppeId=ovelsesGruppe(scanner);
			 }
			 else if(valgtType.toLowerCase().equals("f"))
			 {
				 Apparat apparat = new Apparat(conn);
				 apparat.addApparat(scanner);
				 int apparatId=apparat.getApparatId();
				 String apparatNavn=apparat.getNavn();
				 ovelseId=getOvelseId(conn);
				 gruppeId=ovelsesGruppe(scanner);
				 String querryStringOvelse=String.format("INSERT INTO ovelse VALUES('%s','%s','%s')",ovelseId,gruppeId,apparatNavn);
				 insertQuerry(conn,querryStringOvelse);
				 String querryStringFastmontert=String.format("INSERT INTO fastmontert('apparatId','ovelseId' VALUES('%d','%s'",ovelseId,apparatId);
				 insertQuerry(conn,querryStringFastmontert);
			 }
		 }
	 }
	 
	 public void addIkkeFastmontert(String navn,Scanner scanner) throws SQLException
	 {
		 this.navn=navn;
		 System.out.println("Har du en beskrivelse av "+navn+"?");
		 this.beskrivelse=scanner.nextLine();
		 String ovelseId=getOvelseId(conn);
		 String gruppeId=ovelsesGruppe(scanner);
		 String querryStringOvelse=String.format("INSERT INTO ovelse VALUES('%s','%s','%s')",ovelseId,gruppeId,navn);
		 insertQuerry(conn,querryStringOvelse);
		 String ikkeFastMontertQuerry=String.format("INSERT INTO ikkefastmontert('ovelseId','beskrivelse') VALUES ('%s','%s'",ovelseId,beskrivelse);
		 insertQuerry(conn,ikkeFastMontertQuerry);
	 }
	 
	 public void insertQuerry(Connection conn, String query)
	 {
			try {
				stmt=conn.createStatement();
				stmt.executeUpdate(query);
			}
			catch(SQLException ex){
				System.out.println("SQLException: "+ex.getMessage());
			}
	 }
	 
	 public String ovelsesGruppe(Scanner scanner) throws SQLException
	 {
		 String gruppeId=null;
		 System.out.println("Hvilken øvelsesgruppe tilhører øvelsen?");
		 String valgtGruppe = scanner.nextLine();
		 if(doesOvelseGruppeExist(valgtGruppe))
		 {
			 gruppeId = getGruppeId(valgtGruppe);
		 }
		 else
		 {
			 System.out.println("Vi fant ikke denne gruppen. Ønsker du å legge den til?");
			 String valg=scanner.nextLine();
			 if(valg.toLowerCase().equals("ja"))
			 {
				 Ovelsesgruppe gruppe = new Ovelsesgruppe(conn);
				 gruppe.addOvelsesgruppe(scanner);
				 gruppeId= Integer.toString(gruppe.getGruppeId());
			 }
			 }
		 return gruppeId;
		 } 
	 
	 public String getGruppeId(String valgtGruppe)
	 {
		String gruppeId=null;
		 try {
				stmt=conn.createStatement();
				String query= "SELECT \r\n" + 
						"	ovelsesGruppeId\r\n" + 
						"FROM\r\n" + 
						"	ovelsesgruppe\r\n" + 
						"WHERE\r\n" + 
						"	gruppeType='"+valgtGruppe+"';";
				if(stmt.execute(query))
				{
					rs=stmt.getResultSet();
				}
				if(rs!=null)
				{
					gruppeId= rs.getString(1);
				}
				
			}
			catch(SQLException ex) {
				System.out.println("SQLException: "+ex.getMessage());
			}
		 return gruppeId;
	 }
	 
	 public String getOvelseId(Connection conn)
	 {
		 try {
				stmt=conn.createStatement();
				String query= "SELECT ovelseId FROM ovelse ORDER BY ovelseId DESC LIMIT 1";
				if(stmt.execute(query))
				{
					rs=stmt.getResultSet();
				}
				if(rs.next())
				{
					String oktID = rs.getString(1) + 1;
					return oktID;
				}
				
			}
			catch(SQLException ex) {
				System.out.println("SQLException: "+ex.getMessage());
			}
		 return "1";
	 }
	 
	 public String getOldOvelseId(Connection conn,String navn)
	 {
		 try {
				stmt=conn.createStatement();
				String query= "SELECT ovelseId FROM ovelse WHERE navn='"+navn+"'";
				if(stmt.execute(query))
				{
					rs=stmt.getResultSet();
				}
				if(rs.next())
				{
					String oktID = rs.getString(1);
					return oktID;
				}
				
			}
			catch(SQLException ex) {
				System.out.println("SQLException: "+ex.getMessage());
			}
		 return null;
	 }
}
