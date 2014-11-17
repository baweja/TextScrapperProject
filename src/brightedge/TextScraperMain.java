package brightedge;

	
import java.io.IOException;
import org.jsoup.*;
import org.jsoup.nodes.Document;

	
	public class TextScraperMain {
	    
		private static enum Query {QUERY1,QUERY2}; //Query Options
		private static Query command = Query.QUERY1; //Default command is Query1
		
	    public static void main(String[] args) throws IOException {
	    	
	    	try {
	    		
	    		/* Handle user request and create query*/
	    		String query = createQuery(args);
	    		
	    		/* Process query using Jsoup and get results */
	    		Document doc = Jsoup.connect(query).get();
	    		if(command == Query.QUERY1) {
	    			System.out.println("Total Number of Result Found: " 
	    					+ TotalResultQuery.getTotalResultNumber(doc));
	    		}
	    		else { //case for Query2 
	    			System.out.println(ResultWithPageQuery.getAllResultDetail(doc));
	    		}
	    	} 
	    	catch (InvalidPageNumberException e) {
	    		System.out.println(e.getMessage());
	    	} 
	    	catch (PageNotExistException e) {
	    		System.out.println(e.getMessage());
			}
	    	catch (InvalidArgumentException e) {
				System.out.println(e.getMessage());
				System.out.println("Usage:");
				System.out.println("Query1: java -jar Assignment.jar <keyword> "
						+ "(e.g. java -jar Assignment.jar baby strollers)");
				System.out.println("Query2: java -jar Assignment.jar <keyword> "
						+ "<page number> (e.g. java -jar Assignment.jar baby "
						+ "strollers 2)");
			}
	    	
	    	
	    	
		}
	    
		/**
	     * Take input from user, handle request to determine
	     * the query type and search key, finally generate query
	     * @param args user input 
	     * @return search query
	     * @throws InvalidPageNumberException
		 * @throws InvalidArgumentException 
	     */
		public static String createQuery(String[] args) 
				throws InvalidPageNumberException, InvalidArgumentException {
			
			/* Handle the case that no input argument */
			if(args.length == 0) {
				throw new InvalidArgumentException("No query is found"); 
			}
			
			/*Determine the last argument is an integer*/
			if(isInteger(args[args.length-1])) {
				command = Query.QUERY2;
			}
			
			/*Obtain search key */
			int keySize = args.length;//The default is keySize is arguments' length
			int pageNum = 0; 
			String searchKey = "";
			if(command == Query.QUERY2) {
				keySize = args.length-1; 
				pageNum = Integer.parseInt(args[args.length-1]);
				if(pageNum < 1) {
					throw new InvalidPageNumberException("Invalid Page Number");
				}
			}
			
			/*Generate search key */
			searchKey = args[0];
			for(int i = 1; i < keySize; i++) {
				searchKey += "%20" + args[i];
			}
			
			String host = "http://www.shopping.com";
			if(command == Query.QUERY1) {
				return host + "/products?CLT=SCH&KW=" + searchKey;
			}
			else {
				return host + "/products~PG-" + pageNum + "?KW=" + searchKey;
			}
		}

		/**
		 * Determine the argument is an integer
		 * @param arg argument
		 * @return true if argument is an integer, otherwise return false;
		 * @throws InvalidPageNumberException 
		 */
		public static boolean isInteger(String arg) 
				throws InvalidPageNumberException {
			for(int i = 0; i < arg.length(); i++) {
				
				/* if number is negative, throw invalid page number exception */
				if(i == 0 && arg.charAt(i) == '-') {
					throw new InvalidPageNumberException(
							"Page Number cannot be Negative");
				}
				
				/*Determine each character is digit */ 
				if(!Character.isDigit(arg.charAt(i))) {
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * A class to handle invalid page number exception
	 * @author Garima Baweja
	 */
	class InvalidPageNumberException extends Exception {

		private static final long serialVersionUID = 1L;
		private String message = null; 
		
		/**
		 * Constructor for creating InvalidPageNumberException
		 * @param message exception message
		 */
		public InvalidPageNumberException(String message) {
			super();
			this.message = message;
		}
		
		/**
		 * Get exception message 
		 * @return message text
		 */
		public String getMessage() {
			return message.toString();
		}
	}

	/**
	 * A class to handle page not exist exception
	 * @author Garima Baweja
	 */
	class PageNotExistException extends Exception {

		private static final long serialVersionUID = 1L;
		private String message = null; 
		
		/**
		 * Constructor for creating PageNotExistException
		 * @param message exception message
		 */
		public PageNotExistException(String message) {
			super();
			this.message = message;
		}
		
		/**
		 * Get exception message 
		 * @return message text
		 */
		public String getMessage() {
			return message.toString();
		}
	}

	/**
	 * An exception class to handle invalid arguments; 
	 * @author Garima Baweja
	 */
	class InvalidArgumentException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String message = null; 
		
		/**
		 * Constructor for creating InvalidArgumentException
		 * @param message exception message
		 */
		public InvalidArgumentException(String message) {
			super();
			this.message = message;
		}
		
		/**
		 * Get exception message 
		 * @return message text
		 */
		public String getMessage() {
			return message.toString();
		}


	

	}