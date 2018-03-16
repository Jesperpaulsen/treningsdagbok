	package treningsdagbok;

import java.sql.*;
import java.util.Scanner;

public class Ovelse {
	 private Connection conn;
	 private Statement stmt=null;
	 private ResultSet rs=null;
	 
	 private String navn, type, beskrivelse,ovelseId;//,gruppeId;
	 private int apparatId,reps, sett, kilo, oktId;
	 
	 //Konstruktør
	 public Ovelse(Connection conn)
	 {
		 this.conn=conn;
	 }
	 
	 //Gettere
	 public String getNavn(){return this.navn;}
	 public String getType(){return this.type;}
	 public String getBeskrivelse() {return this.beskrivelse;}
	 public String getOvelseId() {return this.ovelseId;}
	 public int getOktId() {return this.oktId;}
	 public int getApparatId() {return this.apparatId;}
	 public int getReps() {return this.reps;}
	 public int getSett() {return this.sett;}
	 public int getKilo() {return this.kilo;}
	 //Legger til ny øvelse i økt
	 public void addOvelseIOkt(Connection conn,Scanner scanner,int oktId) throws SQLException
	 {
		 System.out.println("Hvilken øvelse ønsker du å legge til?");
		 navn=scanner.nextLine();
		 System.out.println(""+navn);
		 this.oktId=oktId;
		 if(doesOvelseExist(conn,navn))
		 {
			 addSettRepsKilo(conn,navn,scanner,oktId);
		 }
		 else
		 {
			 addOvelse(conn,scanner,navn);
		 }

	 }
	 //Legger til sett,reps,kilo
	 public void addSettRepsKilo(Connection conn,String navn, Scanner scanner,int oktId)
	 {
		 System.out.println("Hvor mange sett tok du?");
		 String sett = scanner.nextLine();
		 System.out.println("Hvor mange reps tok du?");
		 String reps = scanner.nextLine();
		 System.out.println("Hvor mange kilo tok du?");
		 String kilo = scanner.nextLine();
		 String ovelseId=getOldOvelseId(conn,navn);
		 String ovelseIOktQuerry=String.format("INSERT INTO ovelseprokt VALUES('%d','%s','%s','%s','%s'",oktId,ovelseId,sett,reps,kilo);
		 insertQuerry(conn,ovelseIOktQuerry);
	 }
	 //Sjekker om øvelsen eksisterer
	 public boolean doesOvelseExist(Connection conn,String navn)
	 {
		 String SQLquerryExists="SELECT \r\n" + 
						 		"	navn\r\n" + 
						 		"FROM\r\n" + 
						 		"	ovelse\r\n" + 
						 		"WHERE EXISTS\r\n" + 
						 		"	(SELECT navn FROM ovelse WHERE navn='"+navn+"');";
		 try {
				stmt=conn.createStatement();
				if(stmt.execute(SQLquerryExists)){rs=stmt.getResultSet();}
				/*while(rs.next())
				{
					String kolonne1=rs.getString(1);
					System.out.println(kolonne1+"");
				}*/
				if(rs.next()){return true;}	
			}
			catch(SQLException ex) {
				System.out.println("SQLException: "+ex.getMessage());
			}
		 return false;
	 }
	 //Sjekker om øvelsesgruppen eksisterer
	 public boolean doesOvelseGruppeExist(Connection conn,String navn)
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
	 public void addOvelse(Connection conn,Scanner scanner,String navn) throws SQLException
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
				 addIkkeFastmontert(conn,navn,scanner);
				 gruppeId=ovelsesGruppe(conn,scanner);
				 addSettRepsKilo(conn,navn,scanner,oktId);
			 }
			 else if(valgtType.toLowerCase().equals("f"))
			 {
				 Apparat apparat = new Apparat(conn);
				 apparat.addApparat(scanner);
				 int apparatId=apparat.getApparatId();
				 String apparatNavn=apparat.getNavn();
				 ovelseId=getOvelseId(conn);
				 gruppeId=ovelsesGruppe(conn,scanner);
				 String querryStringOvelse=String.format("INSERT INTO ovelse VALUES('%s','%s','%s')",ovelseId,gruppeId,apparatNavn);
				 insertQuerry(conn,querryStringOvelse);
				 String querryStringFastmontert=String.format("INSERT INTO fastmontert('apparatId','ovelseId' VALUES('%d','%s'",ovelseId,apparatId);
				 insertQuerry(conn,querryStringFastmontert);
			 }
		 }
	 }
	 //Legger til ikke fastmontert
	 public void addIkkeFastmontert(Connection conn,String navn,Scanner scanner) throws SQLException
	 {
		 this.navn=navn;
		 System.out.println("Har du en beskrivelse av "+navn+"?");
		 this.beskrivelse=scanner.nextLine();
		 String ovelseId=getOvelseId(conn);
		 String gruppeId=ovelsesGruppe(conn,scanner);
		 String querryStringOvelse=String.format("INSERT INTO ovelse VALUES('%s','%s','%s')",ovelseId,gruppeId,navn);
		 insertQuerry(conn,querryStringOvelse);
		 String ikkeFastMontertQuerry=String.format("INSERT INTO ikkefastmontert('ovelseId','beskrivelse') VALUES ('%s','%s'",ovelseId,beskrivelse);
		 insertQuerry(conn,ikkeFastMontertQuerry);
	 }
	 //Metode som legger inn elementer i databasen
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
	 //Returnerer gruppeId, og sjekker om den eksisterer allerede
	 public String ovelsesGruppe(Connection conn,Scanner scanner) throws SQLException
	 {
		 String gruppeId=null;
		 System.out.println("Hvilken øvelsesgruppe tilhører øvelsen?");
		 String valgtGruppe = scanner.nextLine();
		 if(doesOvelseGruppeExist(conn,valgtGruppe))
		 {
			 gruppeId = getGruppeId(conn,valgtGruppe);
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
	 //returnerer en ny id
	 public String getGruppeId(Connection conn,String valgtGruppe)
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
	 //returner en ny id
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
	 //Finner iden til en eksisterende øvelse
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
