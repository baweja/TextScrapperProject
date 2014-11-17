import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class TotalCountQuery {
	String keyWord;
	
	public String getTotalNumberOfResultsForKeyword(String inputKeyword){
		String totalCount = "";
		this.keyWord = inputKeyword;
		
		try {
			
			//this is the file which contains the HMTL for the home page
			File file = new File("initialOutput");
			PrintWriter pw = new PrintWriter(file);
			
			//home page URL
			String baseURL ="http://www.shopping.com/";
			
			//get the HTML source for the home page
			Document homeDoc = Jsoup.connect(baseURL).get();
			pw.write(homeDoc.html());
			pw.flush();
			pw.close();
			
			//Use the keyword to generate the URL
			String urlWithKeyword = baseURL+keyWord+"/products";
//			System.out.println(urlWithKeyword);
			
			//get the HTML source for the keyword result page
			Document keywordDoc = Jsoup.connect(urlWithKeyword).get();
			File keywordFile = new File("keywordFileOutput");
			pw = new PrintWriter(keywordFile);
			pw.write(keywordDoc.html());
			pw.close();
			
//			System.out.println(keywordDoc.getElementsByClass("numTotalResults").text());
			
			String totalResultText = keywordDoc.getElementsByClass("numTotalResults").text();
			String partsOfTotalResultText[] = totalResultText.split("\\s+");
			
			totalCount = partsOfTotalResultText[5];
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return totalCount;
	}
}