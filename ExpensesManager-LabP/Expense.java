package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class creates a new expense that has been paid and needs to be 
 * split between a group of people. It stores the description of the expense, 
 * the user who paid it, its value, and the people involved. It also allows 
 * for the expense to be split in different proportions among the involved people.
 *
 * @author Madalena Machado fc59858
 *
 */
public class Expense {

	private String description;
	private User userWhoPaid;
	private int value;
	private List<String> people = new ArrayList<>();
	private List<Double> split = new ArrayList<>();
	
	/**
	 * This constructor creates a new expense with the given parameters.
	 * 
	 * @param description what the expense is
	 * @param userWhoPaid the user in question
	 * @param value       the expense value
	 * @param peopleInvolved a list contaning all the usernames involved
	 * @requires {@code howToSplit.size() == peopleInvolved.size() && userWhoPaid != null
	 * 				&& peopleInvolved.contains(userWhoPaid.getUsername())}
	 */
	public Expense (String description, User userWhoPaid, Integer value, List<String> peopleInvolved) {
		this.description = description;
		this.userWhoPaid = userWhoPaid;
		this.value = value;
		this.people.addAll(peopleInvolved);
	}
	
	/**
	 * This constructor creates a new expense with the given parameters.
	 * 
	 * @param description what the expense is
	 * @param userWhoPaid the user in question
	 * @param value       the expense value
	 * @param peopleInvolved a list contaning all the usernames involved
	 * @param howToSplit  the list containing all proportions to be paid by each user
	 * @requires {@code howToSplit.size() == peopleInvolved.size() && userWhoPaid != null
	 * 				&& peopleInvolved.contains(userWhoPaid.getUsername())}
	 */
	public Expense (String description, User userWhoPaid, Integer value, List<String> peopleInvolved, 
			List<Double> howToSplit) {
		this.description = description;
		this.userWhoPaid = userWhoPaid;
		this.value = value;
		this.people.addAll(peopleInvolved);
		this.split.addAll(howToSplit);
	}
	
	/**
	 * Returns the value of the expense.
	 * 
	 * @return the value of the expense.
	 */
	public Integer getValue() {
		return this.value;
	}
	
	/**
	 * Returns the user in question, who paid for the expense
	 * 
	 * @return  the user who paid for the expense
	 */
	public User getPayer() {
		return this.userWhoPaid;
	}
	
	/**
	 * Returns the description of the expense.
	 * 
	 * @return description of the expense
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Returns the expense split for a specific user.
	 * 
	 * @param username user string 
	 * @return the expense split for the specified user
	 * @requires {@code people.contains(username)}
	 */
	public Integer getSplit(String username) {
		double part = 0;
		if(split.isEmpty()){
			part = value / people.size();
		}
		else
			part =(split.get(people.indexOf(username)) * value);
		
		//verificacao do resto e reparticao do mesmo
		/**
		double resto = value % people.size();
		if(resto != 0) {		
			for (double i = 0; i < resto; i++) {
				part+=1;
			}			
		}
		*/
		return (int) part;
	}
	
	/**
	 * Returns the expense balance for the given username in this expense.
	 * The expense balance represents the difference between the amount that 
	 * the user has paid for this expense and the amount that they should have 
	 * paid based on the expense splitting.
	 * 
	 * @param username user string
	 * @return integer representing the expense balance
	 * @requires {@code people.contains(username)}
	 */
	public Integer getExpenseBalance(String username) {
		return getPayer().getUsername().equals(username) ? value - getSplit(username) :
			-getSplit(username);
	}
	
}
