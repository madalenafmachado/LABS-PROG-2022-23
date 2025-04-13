package project;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;

/**
 * @author Madalena Machado fc59858
 */

public class Bookshop {
	
	private Book [] bookshelf; 
 	private int numberOfBooks;
	private int availableBooks;
	private double totalSales;
	private double totalProfit;
	
	/**
	 * Creates a library object that represents a file specified by its name and a 
	 * certain number of books
	 * @param fileName         the file name
	 * @param numberOfBooks    the number of books
	 * @requires {@code numberOfBooks > 0}
	 * @throws FileNotFoundException
	 */
	public Bookshop(String fileName, int numberOfBooks) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner sc = new Scanner (file);
		sc.nextLine();
		
		this.numberOfBooks = numberOfBooks;
		
		int lines = 0;
		while(sc.hasNextLine()) {
			lines++;
			sc.nextLine();
		}
		if(numberOfBooks > lines) {
			numberOfBooks = lines;
		}
		sc.close();
		
		Scanner scan = new Scanner (file);
		scan.nextLine();
		this.bookshelf = new Book [numberOfBooks];
		this.totalSales = 0;
		this.totalProfit = 0;
		int i = 0;
		
		while(scan.hasNextLine() && i < this.numberOfBooks) {
			String [] data = scan.nextLine().split(",");
	    	String title = data[0];
	    	String ISBN = data[1];
	    	String author = data[2];
	    	String quantityString = data[3];
	    	int quantity = Integer.parseInt(quantityString);
	    	String priceString = data[4];
	    	double price = Double.parseDouble(priceString);
	    	String taxString = data[5].substring(0, data[5].length()-1);
	    	double tax = Double.parseDouble(taxString);
			this.bookshelf[i] = new Book(title, author, quantity, price, tax, ISBN);
	    	i++;
		}
		scan.close();
	}
	
	/**
	 * Represents the number of books in the file
	 * 
	 * @return the number of books in the file 
	 */
	public int getNumberOfBooks(){
		return this.numberOfBooks;
	}
	
	/**
	 * Represents the number of books available in stock
	 * 
	 * @return the number of books in the bookshelf
	 */
	public int availableBooks() {
		int books = 0;
		for(int i = 0; i < bookshelf.length; i++) {
			if(this.bookshelf[i].getQuantity() > 0) {
				books++;
			}
		}
		this.availableBooks = books;
		return this.availableBooks;
	}
	
	/**
	 * Returns a book in the given position 
	 * @param i  random position in bookshelf
	 * @return   the book in position i
	 * @requires {@code 1 <= i && i <= getNumberOfBooks()}
	 */
	public Book getBook(int i) {
		return this.bookshelf[i-1];
	}
	
	/**
	 * Returns the total daily revenue
	 * 
	 * @return total of sales 
	 */
	public double getTotalRevenue() {
		return this.totalSales;
	}
	
	/**
	 * Returns the total daily profit
	 * 
	 * @return total of money profited
	 */
	public double getTotalProfit() {
		return this.totalProfit;
	}
	
	/**
	 * Represents the books written by given author
	 * @param author   book author string to be filtered
	 * @return the books written by said author
	 */
	public String filterByAuthor(String author) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bookshelf.length; i++) {
			if(bookshelf[i].getAuthor().equals(author)) {
				sb.append("Title:");
				sb.append(bookshelf[i].getTitle());
				sb.append(",Price:$"); 
				sb.append(bookshelf[i].getPrice());
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}
	
	/**
	 * Represents the books with price lower than the one given
	 * @param price   book price to be filtered
	 * @return the books with a lower price than the one given
	 */
	public String filterByPrice(double price) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < numberOfBooks; i++) {
			if(bookshelf[i].getPrice() < price) {
				sb.append("Title:");
				sb.append(bookshelf[i].getTitle());
				sb.append(",Author:"); 
				sb.append(bookshelf[i].getAuthor());
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}
	
	/**
	 * Reads CSV file identified by filename and returns a string with the info about
	 * said purchases (successful, book out of stock or book not found)
	 * 
	 * @param fileName  input file name
	 * @return the purchases status
	 * @throws FileNotFoundException
	 */
	public String readPurchase(String fileName) throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		File file = new File(fileName);
		Scanner scan = new Scanner (file);
		scan.nextLine();
		double dailySales = 0;
		double dailyProfit = 0;
    	while(scan.hasNextLine()) {
    		String [] purchase = scan.nextLine().split(",");
        	String title = purchase[0];
        	String buyer = purchase[1];
        	int i = 0;
        	boolean exists = false;
    		while(i < numberOfBooks && !exists) {
    			boolean isEqual = bookshelf[i].getTitle().equals(title) ||(bookshelf[i].getISBN()).equals(title);
    			if(isEqual) {
    				
    				if(bookshelf[i].getQuantity() > 0) {
    					int quant = bookshelf[i].getQuantity();
    					purchaseSuccessful(i, purchase, sb);
    					dailySales += calculateSales(i);
    					dailyProfit += calculateProfit(i);
    	    			bookshelf[i].setQuantity(quant-1);
    	    		}
    	    		else {
    					bookOutOfStock(i, purchase, sb);
    	    		}
					exists = true;
    			}
    		
    			if(i==numberOfBooks-1 && !exists) {
					bookNotFound(i, purchase, sb);
    			}
    		i++;
    		}
    	}
    	sb.append(String.format("Total: $%.2f ",dailySales));
		sb.append((String.format("[$%.2f",dailyProfit)+"]"));
		scan.close();
		return sb.toString();
	}
	/**
	 * Calculates the daily profit	
	 * @param i book position in bookshelf
	 * @return daily profit calculated
	 */
	private double calculateProfit(int i) {
		double perProf = 1 - (bookshelf[i].getTax())/100;
		this.totalProfit += perProf * bookshelf[i].getPrice();
		double dailyProfit = perProf * bookshelf[i].getPrice();
		
		return dailyProfit;
	}
	/**
	 * Calculates the total amount of sales
	 * @param i book position in bookshelf
	 * @return  total sales calculated
	 */
	private double calculateSales(int i) {
		totalSales += bookshelf[i].getPrice();
		double dailySales = bookshelf[i].getPrice();
		
		return dailySales;
		
	}
	/**
	 * Represents the status of a successful purchase
	 * @param i        book position in bookshelf
	 * @param purchase purchase data in input file
	 * @param sb       StringBuilder used to append
	 */
	private void purchaseSuccessful(int i, String[] purchase, StringBuilder sb) {
		sb.append("Purchase successful: ");
		sb.append(purchase[1] + " bought ");
		sb.append(bookshelf[i].getTitle());
		sb.append(" by " + bookshelf[i].getAuthor());
		sb.append(", price: $");
		sb.append(bookshelf[i].getPrice());
		sb.append(System.lineSeparator());
	}
	/**
	 * Represents the status of a purchase trying to be made with a 
	 * book out of stock
	 * @param i        book position in bookshelf
	 * @param purchase purchase data in input file
	 * @param sb       StringBuilder used to append
	 */
	private void bookOutOfStock(int i, String[] purchase, StringBuilder sb) {
		sb.append("Book out of stock: ");
		sb.append(purchase[1] + " asked for ");
		sb.append(bookshelf[i].getTitle());
		sb.append(" by " + bookshelf[i].getAuthor());
		sb.append(", price: $");
		sb.append(bookshelf[i].getPrice());
		sb.append(System.lineSeparator());
	}
	/**
	 * Represents the status of a purchase trying to be made with
	 * a book that does not exist 
	 * @param i        book position in bookshelf
	 * @param purchase purchase data in input file
	 * @param sb       StringBuilder used to append
	 */
	private void bookNotFound(int i, String[] purchase, StringBuilder sb) {
		sb.append("Book not found: ");
	    sb.append(purchase[1] + " asked for ");
	    sb.append(purchase[0]);
		sb.append(System.lineSeparator());
	}
	
	/**
	 * Updates the stock to represent the daily sales and saves it on a CSV file
	 * @param fileName  output file
	 * @throws FileNotFoundException
	 */
	public void updateStock(String fileName) throws FileNotFoundException {
		PrintWriter write = new PrintWriter(fileName);
		write.println("Title,ISBN,Author,Quantity,Price,Tax");
		for(int i = 0; i < bookshelf.length; i++) {
			write.println(bookshelf[i].getTitle()+
					","+bookshelf[i].getISBN()+
					","+bookshelf[i].getAuthor()+
					","+Integer.toString(bookshelf[i].getQuantity())+
					","+Double.toString(bookshelf[i].getPrice())+
					","+Double.toString(bookshelf[i].getTax())+"%");
		}
		write.close();
	}
	
	

}
