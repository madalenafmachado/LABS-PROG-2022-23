package project;
/**
 * @author Madalena Machado fc59858
 */

public class Book {
	private String title;
	private String author;
	private int quantity;
	private double price;
	private double tax;
	private String ISBN;
	
	/**
	 * Creates a book object that represents a book specified by a title, its author, the stock quantity,
	 * its given price, a specific tax and its own ISBN 
	 * @param title       the book title
	 * @param author	  the book author
	 * @param quantity    the book quantity
	 * @param price 	  the book price
	 * @param tax		  the book tax
	 * @param ISBN		  the book ISBN
	 */
	public Book(String title, String author, int quantity, double price, double tax, String ISBN) {
		this.title = title;
		this.author = author;
		this.quantity = quantity;
		this.price = price;
		this.tax = tax;
		this.ISBN = ISBN;
	}
	
	/**
	 * Represents the given book title
	 * @return the book title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Represents the given book author
	 * @return the book author
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Represents the given book quantity
	 * @return the book quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Represents the given book price
	 * @return the book price
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Represents the given book tax
	 * @return the book tax
	 */
	public double getTax() {
		return tax;
	}
	
	/**
	 * Represents the given book ISBN
	 * @return the book ISBN
	 */
	public String getISBN() {
		return ISBN;
	}
	
	/**
	 * Defines a certain book quantity
	 * @param quantidade quantity to be set
	 */
	public void setQuantity(int quantidade) {
		this.quantity = quantidade;
	}
}
