import java.io.File;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class CountWithPageQuery {
	String keyWord;
	int pageNumber;
	
	public CountWithPageQuery(String keyword, int pgNumber){
		this.keyWord = keyword;
		this.pageNumber = pgNumber;
	}

	public void getDetailsForItemsOnThatPage(){
		try {	
		
		//home page
		String baseURL ="http://www.shopping.com/";
		
		//generate the URL with the given keyword
		String urlWithKeyword = baseURL+keyWord+"/products";
//		System.out.println(urlWithKeyword);
		
		Document keywordDoc = Jsoup.connect(urlWithKeyword).get();
		
		Elements paginators = keywordDoc.getElementsByClass("paginationNew");
		
		Elements listOfPaginatedAnchors = paginators.select("a");
		int numberOfPages = Integer.parseInt(listOfPaginatedAnchors.get(listOfPaginatedAnchors.size()-2).text());
		
		if(pageNumber > numberOfPages){
			System.err.println("The page number requested for the current product is incorrect."
								+ "\nCheck if it exceeds the maximum number of pages for this product.");
			System.err.println("The program will terminate here.");
			System.exit(0);
		}
		
		/* The home page shows certain number of pages at the bottom to jump directly to.
		 * After that it shows ellipses (...) for the pages that are not shown there.
		 * The only way to reach those pages is to first click on the last observable link before ellipses and keep doing so
		 * until the page number you wish to jump to shows up.
		 * 
		 * To handle this situation programmatically, I have assumed that each keyword has atleast page number 2.
		 * Using this assumption, I generate the URL to the specific page number whose value is given as the input parameter.
		 * 
		 * */
		String paginate = paginators.select("a[name=PL2").attr("href");
//		System.out.println("paginate: "+paginate);
		String split1[] = paginate.split("\\?");
		String finalPaginateString = split1[0].replace("2", String.valueOf(pageNumber));
		String urlOfRequestedPage = baseURL.substring(0, baseURL.length()-1)+finalPaginateString+"?"+split1[1];
		System.out.println("URL Of RequestedPage: "+urlOfRequestedPage+"\n");
		
		//get the document for the requested page
		Document requestedPage = Jsoup.connect(urlOfRequestedPage).get();
		File requestedFile = new File("requestedFileOutput");
		PrintWriter pw = new PrintWriter(requestedFile);
		pw.write(requestedPage.html());
		pw.close();
		
		String totalResultText = requestedPage.getElementsByClass("numTotalResults").text();
		String partsOfTotalResultText[] = totalResultText.split("\\s+");
		NumberFormat usFormat = NumberFormat.getNumberInstance(Locale.US);
		System.out.println("Total number of results for "+keyWord+" = "+(partsOfTotalResultText[5]));
		int numberOfResultsOnAPage = (usFormat.parse(partsOfTotalResultText[3]).intValue() - usFormat.parse(partsOfTotalResultText[1]).intValue()) + 1;
		System.out.println("Number Of Results On This Page = "+numberOfResultsOnAPage+"\n");
		
		/*Now that we have the requested page, pick the needed details*/			
			Elements formElements = requestedPage.getElementsByAttributeValue("name", "compareprd");
			
			if(formElements!=null && formElements.size()>0)
			{
				Element formElement = formElements.get(0);
				List<Element> lstOuterDivisions = formElement.children();
				
				for(int count=0;count<lstOuterDivisions.size();++count)
				{
					Element divElement = lstOuterDivisions.get(count);
					
					//some pages have an additional div element in them with the class 'clearAll' which is not needed here
					if(divElement.nodeName().equalsIgnoreCase("div") && !divElement.attr("class").equalsIgnoreCase("clearAll"))
					{
						Elements gridItemDiv = divElement.getElementsByClass("gridItemBtm");
						
						if(gridItemDiv!=null && gridItemDiv.size()>0)
						{
							Element itemDiv = gridItemDiv.get(0);
							
							//title information
							getTitleInformation(itemDiv,count+1);
							
							//product price
							getProductPrice(itemDiv, count+1);
							
							//Shipping information
							getShippingInformation(itemDiv,count+1);
							
							//vendor information
							getVendorName(itemDiv,count+1);
							
							System.out.println();
							
										
						}
					}
					
				}
				
			}
			
			System.out.println();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void getProductPrice(Element itemDiv, int count) {
		if(itemDiv!=null)
		{
			Elements anchorTag = itemDiv.getElementsByClass("productPrice");
			if(anchorTag!=null && anchorTag.size()>0)
			{
				System.out.println("\tProduct Price : " + anchorTag.get(0).text());
			}
		}
	}

	private void getVendorName(Element itemDiv, int count) {
		if(itemDiv!=null)
		{
			Elements anchorTag = itemDiv.getElementsByClass("newMerchantName");
			if(anchorTag!=null && anchorTag.size()>0)
			{
				System.out.println("\tVendor Name : " + anchorTag.get(0).text());
			}
		}
		
	}

	private void getTitleInformation(Element itemDiv,int count) 
	{
		if(itemDiv!=null)
		{
			Elements anchorTag = itemDiv.getElementsByClass("productName");
			
			if(anchorTag!=null && anchorTag.size()>0)
			{
				System.out.println(count + ") \tTitle : " + anchorTag.get(0).text());
			}
			
		}
		
	}

	
	/* The content for shipping details was put up in quite a few tags.
	 * Some had them directly under tag with class name 'taxShippingArea', some others had them in a tag nested inside taxShippingArea with a class 'calc' or 'missCalc'.
	 * Whereas the items for which shipping is free, had the information inside a tag with class 'freeShip'
	 * 
	 * Thus, the if else ladder that is written in this method is to check each of the above cases.
	 * In situations where none of the above satisfy, the value supplied is "N/A"
	 * 
	 * */
	private void getShippingInformation(Element shippingDiv,int x) 
	{
		
		if(shippingDiv!=null)
		{
			Elements taxShippingAreaDivElements = shippingDiv.getElementsByClass("taxShippingArea");
			
			String cost = "";
			if(taxShippingAreaDivElements!=null && taxShippingAreaDivElements.size()>0)
			{
				Element taxShippingAreaDiv = taxShippingAreaDivElements.get(0);
				
				Element shippingCost = (taxShippingAreaDiv.getElementsByClass("calc")!=null && (taxShippingAreaDiv.getElementsByClass("calc").size()>0)? taxShippingAreaDiv.getElementsByClass("calc").get(0):null);
				
				
				if(shippingCost!=null)
				{
					cost = shippingCost.text();
					System.out.println("\tShipping Cost :" +cost);
				}
				else 
				{
					Element freeShippingCost = (taxShippingAreaDiv.getElementsByClass("freeShip")!=null && (taxShippingAreaDiv.getElementsByClass("freeShip").size()>0)? taxShippingAreaDiv.getElementsByClass("freeShip").get(0):null);
					if(freeShippingCost!=null)
					{
						cost = freeShippingCost.text();
						System.out.println("\tShipping Cost :" +cost);
					}
					else
					{
						Element noShippingCost = (taxShippingAreaDiv.getElementsByClass("missCalc")!=null && (taxShippingAreaDiv.getElementsByClass("missCalc").size()>0)? taxShippingAreaDiv.getElementsByClass("missCalc").get(0):null);
						if(noShippingCost!=null)
						{
							cost = noShippingCost.text();
							System.out.println("\tShipping Cost :" +cost);
						}else
						{
							System.out.println("\tShipping Cost : N/A");
						}
					}
				}	
			}
			else
			{
				Element freeShippingCost = (shippingDiv.getElementsByClass("freeShip")!=null && (shippingDiv.getElementsByClass("freeShip").size()>0)? shippingDiv.getElementsByClass("freeShip").get(0):null);
				if(freeShippingCost!=null)
				{
					cost = freeShippingCost.text();
					System.out.println("\tShipping Cost :" +cost);
				}
				else
				{
					System.out.println("\tShipping Cost : N/A");
				}
			}
			
		}

	}

}