package brightedge;

/**
 * Query1: Get total number of results
 * @param doc webpage content
 * @return result for Query1
 * @throws PageNotExistException 
 */

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TotalResultQuery {
	
	
	public static String getTotalResultNumber(Document doc) throws PageNotExistException
	{
    	/* Handle the case that page doesn't exists */
    	Element noMatch = doc.getElementsByClass("nomatch").first();
    	if(noMatch != null) {
    		throw new PageNotExistException("No Result Found on This Page"); 
    	}
		
		String totalNumText = doc.getElementsByClass("numTotalResults")
				.first().text();
		/*Get the text after "of ", which is the count of total result*/ 
		return totalNumText.split("of ")[1];
	}

}
