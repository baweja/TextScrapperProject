	/**
	 * This is the main class. It simply creates objects of the other 2 classes according the number of input arguments to it
	 * Then it calls the corresponding method defined in the respective classes with the arguments entered
	 * */

public class Parser {
	
	public static void main(String args[]){
		/*If there is only 1 argument provided, then it is the first query*/
		if(args.length == 1){
			TotalCountQuery fQuery = new TotalCountQuery();
			
			String keyWord = args[0];
			String totalCountOfProducts = fQuery.getTotalNumberOfResultsForKeyword(keyWord);

			System.out.println("Total number of results for "+keyWord+" = "+totalCountOfProducts);
		}
		
		/*If there is only 1 argument provided, then it is the second query*/
		if(args.length == 2){
			CountWithPageQuery sQuery = new CountWithPageQuery(args[0], Integer.parseInt(args[1]));
			sQuery.getDetailsForItemsOnThatPage();
			
		}
	}
}