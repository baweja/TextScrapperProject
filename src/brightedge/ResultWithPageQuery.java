package brightedge;

import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ResultWithPageQuery {
	
	 /**
     * Query2: create result object and print all result on screen
     * @return result for Query2
     * @throws PageNotExistException 
     */
    public static String getAllResultDetail(Document doc) 
    		throws PageNotExistException
	{
    	
    	/* Handle the case that page doesn't exists */
    	
    	Element noMatch = doc.getElementsByClass("nomatch").first();
    	if(noMatch != null) {
    		throw new PageNotExistException("No Result Found on This Page"); 
    	}
    	
		/* Determine the number of results in one page */
		int size = doc.getElementsByClass("gridBox").size();
		System.out.println("The total number of results on this website are "+TotalResultQuery.getTotalResultNumber(doc));
		System.out.println("Total results on this page are "+ size+"\n");
		
		/* Create Product Object and save into list */
		ArrayList<ProductDetails> productList = new ArrayList<ProductDetails>();
		for(int i = 1; i <= size; i++) {
			
			/* Get product title */
			String itemID = "nameQA" + i;
			Element item = doc.getElementById(itemID);
			String title = item.attr("title");
			
			/* Get product price */
			Elements itemPrices = doc.getElementsByClass("productPrice");
			String price = itemPrices.get(i-1).text();
			
			/* Get vendor name */
			Elements vendorNames = doc.getElementsByClass("newMerchantName");
			String vendor = vendorNames.get(i-1).text();
			
			/* Get shipping price information */
			String qLItemID = "quickLookItem-" + i;
			Element qLItem = doc.getElementById(qLItemID);
			String shipping = "";
			Element shipPrice = qLItem.getElementsByClass("freeShip").first();
			if(shipPrice == null) { 
				// shipping info of item is not in "freeShip" class
				shipPrice 
					= qLItem.getElementsByClass("taxShippingArea").first();
				if(shipPrice == null) { 
					//shipping info is not available on grid of search result
					shipping = "Multiple Options, click stores for detail";
				}
				else { 
					//shipping info of item is available in "taxShippingArea"
					shipping = shipPrice.text();
				}
			}
			else { 
				// item is free shipping
				shipping = shipPrice.text();
			}
			
			/* Create Product object */
			ProductDetails product = new ProductDetails(title, price, shipping, vendor); 
			productList.add(product);
		} // end of for loop
		
		return PrintProduct.printAllProduct(productList);
	}
    

}
