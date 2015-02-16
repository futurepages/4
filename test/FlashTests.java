
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.futurepages.util.The;
import org.futurepages.util.brazil.PersonNamesUtil;

public class FlashTests {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	//  Database credentials
	static final String USER = "root";
	static final String PASS = "";
	

	//need mysql-jdbc-driver.jar
	public static void jdbcUpdate(String[] args) {
		// JDBC driver name and database URL
		Connection conn = null;
		Statement stmt = null;
		try {
			//STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			//STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			System.out.println("Connected database successfully...");

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();

			// Now you can extract all the records
			// to see the updated records
			String sqlSelect = "SELECT email, nomeCompleto from contato where nomeCompleto like '% _ %'";
			ResultSet rs = stmt.executeQuery(sqlSelect);
			ArrayList<String> updatesSQL = new ArrayList();

			while (rs.next()) {
				//Retrieve by column name
				String email = rs.getString("email");
				System.out.println(email);
				String nome = rs.getString("nomeCompleto");
				updatesSQL.add("UPDATE contato SET nomeCompleto = '"+PersonNamesUtil.correctFullName(nome)+"' WHERE email = '"+email+"'");
			}
			rs.close();

			for(String str : updatesSQL){
				stmt.executeUpdate(str);
				System.out.println(str);
			}
		} catch (SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			//finally block used to close resources
			try {
				if (stmt != null) {
					conn.close();
				}
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}//end finally try
		}//end try

		System.out.println("Goodbye!");
	}//end main

	//Need jxl.jar
	public static void main(String[] args) throws IOException, BiffException {

		InputStream is = new FileInputStream("e:/amatras_e_tjpi.xls");
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("pt", "BR"));
		ws.setEncoding("ISO-8859-1");
		Workbook workbook = Workbook.getWorkbook(is, ws);
		FileOutputStream output = new FileOutputStream("E:/magistrados.sql");
		PrintWriter pw = new PrintWriter(output);

		for (Sheet planilha : workbook.getSheets()) {
//			planilha.getRows();Column(0);
//			planilha.getColumn(1);
			if (!planilha.getName().startsWith("#")) {
//				pw.println("\n-- "+planilha.getName());
				for (int i = 1; i < planilha.getRows(); i++) { //planilha.getRows()
					String nome = "";
					String email1 = "";
					String email2 = "";
					String obs = "";
					String listaId = "null";


					try {
						nome = PersonNamesUtil.correctFullName(planilha.getCell(0, i).getContents());


					} catch (Exception ex) {
						ex.printStackTrace();


					}
					try {
						email1 = planilha.getCell(1, i).getContents().replaceAll("\n", " ").replaceAll("'", " ");


					} catch (Exception ex) {
						ex.printStackTrace();
					}
					try {
						email2 = planilha.getCell(2, i).getContents().replaceAll("\n", " ").replaceAll("'", " ");
					} catch (Exception ex) {
						if (!(ex instanceof ArrayIndexOutOfBoundsException)) {
							ex.printStackTrace();
						}
					}
					try {
						obs = planilha.getCell(3, i).getContents();


					} catch (Exception ex) {
						if (!(ex instanceof ArrayIndexOutOfBoundsException)) {
							ex.printStackTrace();


						}
					}
					try {
						listaId = planilha.getName().split("_")[0];


					} catch (Exception ex) {
						ex.printStackTrace();


					}
					if (nome.equals("") && email1.equals("") && email2.equals("")) {
					} else //					if(listaId.equals("15"))
					{
//					pw.println(The.concat(
						System.out.println(The.concat(
								"INSERT INTO contatos_originais (nome,emails,obs,lista_id) VALUES ",
								"('", nome, "','", email1.trim(), ";", email2.trim(), "','", obs.trim(), "',", listaId, ");"));
						output.flush();
						pw.flush();


					}
				}
			}
		}
		output.close();
		pw.close();

//		SELECT
//		c.nome, CAST(email AS CHAR(255)), CAST(obs AS CHAR(255) ) AS 'observações', l.nome AS 'Lista Origem'
//		FROM contato c
//		INNER JOIN lista_original l ON c.lista_id = l.id
//		ORDER BY c.nome, c.email, c.lista_id;


//		SELECT c.id, c.nome, CAST(emails AS CHAR(255)) AS emails, CAST(obs AS CHAR(255) ) AS 'observações', l.nome AS 'Lista Origem'
//		FROM contatos_originais c
//		INNER JOIN lista_bruta l ON c.lista_id = l.id
//		ORDER BY l.id, c.nome, c.emails;


	}

	public static void main2(String[] args) throws IOException, BiffException {

		InputStream is = new FileInputStream("e:/associados.xls");
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("pt", "BR"));
		ws.setEncoding("ISO-8859-1");
		Workbook workbook = Workbook.getWorkbook(is, ws);
		FileOutputStream output = new FileOutputStream("E:/magistrados.sql");
		PrintWriter pw = new PrintWriter(output);

		for (Sheet planilha : workbook.getSheets()) {
//			planilha.getRows();Column(0);
//			planilha.getColumn(1);
			if (!planilha.getName().startsWith("#")) {
//				pw.println("\n-- "+planilha.getName());
				for (int i = 1; i < planilha.getRows(); i++) { //planilha.getRows()
					String nome = "";
					String email1 = "";
					String email2 = "";
					String obs = "";
					String listaId = "null";


					if (nome.equals("") && email1.equals("") && email2.equals("")) {
					} else //					if(listaId.equals("15"))
					{
//					pw.println(The.concat(
						System.out.println(The.concat(
								"INSERT INTO contatos_originais (nome,emails,obs,lista_id) VALUES ",
								"('", nome, "','", email1.trim(), ";", email2.trim(), "','", obs.trim(), "',", listaId, ");"));
						output.flush();
						pw.flush();


					}
				}
			}
		}
		output.close();
		pw.close();

		//SELECT 0, COUNT(1) FROM contatos_originais WHERE emails = ''
		//UNION
		//SELECT 1, COUNT(1) FROM contatos_originais WHERE emails NOT LIKE '%;%' AND emails <>''
		//UNION
		//SELECT 2, COUNT(1) FROM contatos_originais WHERE emails LIKE '%;%' AND emails NOT LIKE '%;%;%'
		//UNION
		//SELECT 3, COUNT(1) FROM contatos_originais WHERE emails LIKE '%;%;%' AND emails NOT LIKE '%;%;%;%'
		//UNION
		//SELECT 4, COUNT(1) FROM contatos_originais WHERE emails LIKE '%;%;%;%' AND emails NOT LIKE '%;%;%;%;%'
		//UNION
		//SELECT 5, COUNT(1) FROM contatos_originais WHERE emails LIKE '%;%;%;%;%' AND emails NOT LIKE '%;%;%;%;%;%'
		//;
		//
		//SELECT c.id, c.nome, CAST(emails AS CHAR(255)) AS emails, CAST(obs AS CHAR(255) ) AS 'observações', l.nome AS 'Lista Origem'
		//FROM contatos_originais c
		//INNER JOIN lista_bruta l ON c.lista_id = l.id
		//ORDER BY l.id, c.nome, c.emails;


	}

}
