TextScrapperProject
===================

This java webscrapper is robust text scraper that will connect to a page on 
www.shopping.com, and return a result for a given keyword.This scrapper utilize
Jsoup HTML scrapper to obtain pages from www.shopping com. Two query can be 
perform using this program. The first query is getting the total number of 
queries by a given search keywords. The second query is find all results detail 
information by a given keywords. This program also include handle exceptions on 
invalid arguments, invalid page number, and no page found by using some keywords
or exceeding maximum number of search pages.
Queries: 
    Query 1: (requires a single argument)
    java -jar Assignment.jar <keyword> 
    (e.g. java -jar Assignment.jar "baby strollers")
    
    Query 2: (requires two arguments)
    java -jar Assignment.jar <keyword> <page number> 
    (e.g. java -jar Assignment.jar "baby strollers" 2)


Approach
--------
The program can be divided into two sections. The first section is handling user
input. When program starts, the default query is Query1. If user provides one 
argument, it means user wants to perform Query1.The program parse user's 
argument and find the keyword. The keyword will append to the specific URL for 
searching. After calling Jsoup's connect method, the program obtains Document 
Object, which is the returned webpage by offering keyword. Then, by calling 
getTotalResultNumber() method, the program find the location of total number of 
searching results.Finally,print results on screen.

If user provides two arguments, the second arugment will be page number. Page 
number must be non-zero positive number. If a negative or 0 is provided by user,
the program will be interrupted by a InvalidPageNumber exception. If user 
provides a number that exceed the maximum of page number. Then a no-match 
webpage is returned. The program will detect the no-match page and exit by 
create a PageNotFound exception. If user provides proper search keywords and 
page number, program will execute Query2 option, and Jsoup connect shopping.com
with another URL including both keywords and page nubmers. Then program will 
scrap the webpage to find each product's title, price, shipping price and 
vendor. After gathering all the required information, a Product Object is 
created. All product Objects will be stored into a ArrayList.Finally, the 
progarm print out all products in list. 

Command Line Argument Example
-----------------------------
Query 1: java -jar Assignment.jar "digital camera"
Query 2: java -jar Assignment.jar "digital camera" 2
