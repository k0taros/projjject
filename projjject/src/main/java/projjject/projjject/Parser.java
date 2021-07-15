package projjject.projjject;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;


public class Parser {

	private static Document getPage() throws IOException {
		String url = "https://poezdato.net/raspisanie-po-stancyi/samara/elektrichki/";
		Document page = Jsoup.parse(new URL(url), 10000);
		
		return page;
	}
	
	public static void main(String[] args) throws IOException {
		Document page = getPage();
		Element tableRasp = page.select("table[ class=schedule_table stacktable desktop]").first();
		
		Elements allLinks = tableRasp.select("td > a");
		
		Elements allSpan = tableRasp.select("span[class=_time]");
		
		String arrives[] = new String[allSpan.size()];
		String departure[] = new String[allSpan.size()];
		
		for (int i = 0; i < allSpan.size(); i++) {
			if (i % 2 == 0)
				arrives[i] = allSpan.get(i).text();
			else 
				departure[i] = allSpan.get(i).text();
		}
		
		String numbers[] = new String[allLinks.size()];
		
		String hrefs[] = new String[allLinks.size()];
		
		for (int i = 0; i < allLinks.size(); i++) {
			hrefs[i] = allLinks.get(i).attr("href");
		}
		
		int index[] = new int[allLinks.size()];
		
		for (int i = 0; i < index.length; i++) {
			index[i] = 0;
		}
		
		for (int i = 0; i < allLinks.size(); i++) {
			index[i] = hrefs[i].indexOf("raspisanie-elektrichki");
		}
		
		for (int i = 0; i < index.length; i++) {
			if (index[i] != -1)
				numbers[i] = allLinks.get(i).text();
		}
		
		String routs[] = new String[allLinks.size()];
		
		for (int i = 0; i < allLinks.size(); i++) {
			hrefs[i] = allLinks.get(i).attr("href");
		}
		
		for (int i = 0; i < index.length; i++) {
			index[i] = 0;
		}
		
		for (int i = 0; i < allLinks.size(); i++) {
			index[i] = hrefs[i].indexOf("raspisanie-po-stancyi");
		}
		
		for (int i = 0; i < index.length; i++) {
			if (index[i] != -1) {
				routs[i] = allLinks.get(i).text();
			}
		}
		
		// Delete null
		
		int ii = 0;
		int jj = 0;
		
		String tmpr[] = new String[allLinks.size()];
		
		while (ii < index.length) {
			if (routs[ii] != null) {
				tmpr[jj] = routs[ii];
				jj++;
			}
			ii++;
		}
		
		routs = tmpr;
		
		String tmpn[] = new String[allLinks.size()];
		
		ii = 0;
		jj = 0;
		
		while (ii < index.length) {
			if (numbers[ii] != null) {
				tmpn[jj] = numbers[ii];
				jj++;
			}
			ii++;
		}
		
		numbers = tmpn;
				
		for (int i = 0, j = 0, k = 0, l = 1; i < numbers.length && j < routs.length && k < arrives.length && l < departure.length; i++, j = j + 2, k = k + 2, l = l + 2) {
			if (numbers[i] != null) {
				System.out.print(numbers[i] + " ");
			}
			if (routs[j] != null && routs[j + 1] != null) {
				System.out.print(routs[j] + " - " + routs[j + 1] + " ");
			}
			if (arrives[k] != null) {
				System.out.print(arrives[k] + " - ");
			}
			if (departure[l] != null) {
				System.out.println(departure[l]);
			}
		}
		try {
			String url = "jdbc:mysql://localhost:3306/raspisanie";
			String username = "Sergey";
			String password = "drstoneforman";
			
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			try (Connection conn = DriverManager.getConnection(url, username, password)) {
				System.out.println("Connection with db " + url + " Username " + username);
				
				String sql = "INSERT INTO route(Numbers, Arrival, Departure) Value (?, ?, ?)";
				
				Statement statement = conn.createStatement();
				PreparedStatement pStatement = conn.prepareStatement(sql);
				
				statement.executeUpdate("DELETE FROM route");	
				
				// Ввод в БД
				for (int i = 0, j = 0; i < 17 && j < routs.length; i++, j = j + 2) {
					if (numbers[i] != null) {
						pStatement.setString(1, numbers[i]);
					}
					if (routs[j] != null && routs[j + 1] != null) {
						pStatement.setString(2, routs[j]);
						pStatement.setString(3, routs[j + 1]);
					}
					pStatement.executeUpdate();
				}
	
				ResultSet result = statement.executeQuery("SELECT * FROM route");
				while (result.next()) {
					int id = result.getInt("ID_route");
					String number = result.getString("Numbers");
					String arrival = result.getString("Arrival");
					String departuret = result.getString("Departure");
					
					System.out.println(id + " " + number + " " + arrival + " - " + departuret);
				}
				
				System.out.println("Work with db succecsful over!");
				
			}
		}
		catch(Exception ex) {
			System.out.println("Connection failed!");
		}
	}
}
		
