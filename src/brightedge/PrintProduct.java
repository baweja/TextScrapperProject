package brightedge;

import java.util.ArrayList;

public class PrintProduct {
    	   
    /**
     * Create a string of all product info for printing
     * @param productList list of products
     * @return string of all product info 
     */
	public static String printAllProduct(ArrayList<ProductDetails> productList)
	{
		String results = "";
		Integer count=1;
		for(ProductDetails product : productList) {
			results+=count++ +". ";
			results += "Product Title:  " + product.getTitle() + "\n";
			results += "Product Price:  " + product.getPrice() + "\n";
			results += "Shipping Price: " + product.getShippingPrice() + "\n";
			results += "Vendor Name:    " + product.getVendor() + "\n";
			results += "\n";
		}
		return results;
	}

}
