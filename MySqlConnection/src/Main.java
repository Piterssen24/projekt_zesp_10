import java.io.*;
import java.sql.*;



public class Main {

	public static void main(String[] args) throws Exception
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String dbUrl = "jdbc:mysql://localhost:3306/bd_pz_2014";
			String user = "root";
			String password = "hac2Afe&";
			Connection con = DriverManager.getConnection(dbUrl,user,password);
			FileInputStream input = null;
			FileOutputStream output = null;
			ResultSet myRs = null;
			
				
			//ResultSet myRs = statement.executeQuery("select * from users");
		
			/*String uaktualnij = "update users"
					+ " set user_name = 'piotr'"
					+ "where id = 4";*/
			
			/*String wstaw = "insert into post_info " 
							+ " (post_id,user_id,post_content,photo,post_type,added_time,localization)"
							+ " values (5,5,'Tresc postu.','fotka','wydarzenie kulturalne','2014-11-24','Torun');";
			
			statement.executeUpdate(wstaw);
			System.out.println("Wstawianie zakonczone powodzeniem!");*/
			
			//statement.executeUpdate(uaktualnij);
			//System.out.println("Uaktualnienie zakonczone powodzeniem!");
			
			
			/*while(myRs.next())
			{
				System.out.println(myRs.getString(1) + " " + myRs.getString(2) + " " + myRs.getString(3) + " " + myRs.getString(4));
				//wypisanie konkretnej kolumny 
				//System.out.println(myRs.getString("user_name"));	
			}*/
		
			/*String usun = "delete from post_info where post_id = '4' ";
			int rowsAffected = statement.executeUpdate(usun);		
			
			System.out.println("Rows affected: "+ rowsAffected);
			System.out.println("Delete Complete.");*/
			
			//Wstawianie obrazka do bazy danych
			
			/*String wstaw = "update post_info set photo=? where post_id=1"; 
			
			PreparedStatement statement = con.prepareStatement(wstaw);
			
			File theFile = new File("foto.png");
			input = new FileInputStream(theFile);
			statement.setBinaryStream(1,input);
			
			
			
			System.out.println("Czytam obrazek: "+ theFile.getAbsolutePath());
			
			System.out.println("Zapisuje w bazie danych: " + theFile);
			System.out.println(wstaw);
			
			statement.executeUpdate();
			
			System.out.println("Operacje zakonczono powodzeniem! ");*/
			
			//Odczytywanie obrazka z bazy danych
			Statement statement = con.createStatement();
			String odczytaj = "select photo from post_info where post_id=1";
			myRs = statement.executeQuery(odczytaj);
			
			
			
			File theFile = new File("foto1.png");
			output = new FileOutputStream(theFile);
			
			if (myRs.next()){
				
				//nie dziala input = myRs.getBinaryStream("photo");
				System.out.println("czytam obrazek z bazy danych");
				System.out.println(odczytaj);
				
				byte[] buffer = new byte[1024];
				while(input.read(buffer)>0){output.write(buffer);}
				
				System.out.println("\n Zapisano do nowego pliku: " + theFile.getAbsolutePath());
				System.out.println("\n Zakonczono powodzeniem!");
				
				
			}
			
			
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		finally{
		//if (input != null){}
			
		}
		
		
		
		
		
		
	}
	
}
