package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The ExpensesGroup class represents a group of users sharing expenses. It
 * keeps track of each user's debt and credits according to the expenses that
 * have been added to the group. It also allows the users to settle their debts
 * by creating transactions between them.
 * 
 * @author Madalena Machado fc59858
 *
 */
public class ExpensesGroup {

	private Map<String, Map<String, Integer>> debtGraph; // QuemÉdevido -> QuemDeve, QtDeve
	private User creator;
	private String name;
	private List<Expense> expenses;
	private List<String> users;

	/**
	 * Creates a new ExpensesGroup object
	 * 
	 * @param groupName  the group name
	 * @param user       The user who creates the group
	 * @requires {@code user != null}
	 */
	public ExpensesGroup(String groupName, User user) {
		this.creator = user;
		this.name = groupName;
		debtGraph = new HashMap<>();
		expenses = new ArrayList<>();
		users = new ArrayList<>();
		addUser(user);
	}
	
	/**
	 * Returns the user who created the group.
	 * 
	 * @return the user who created the group.
	 */
	public User getCreator() {
		return creator;
	}
	
	/**
	 * Returns the name of the group.
	 * 
	 * @return  the name of the group.
	 */
	public String getGroupName() {
		return name;
	}
	
	/**
	 * Returns the list of users in the group.
	 * 
	 * @return the list of users in the group.
	 */
	public List<String> usersInGroup() {
		return users;
	}
	
	/**
	 * Returns the list of expenses in the group.
	 * 
	 * @return the list of expenses in the group.
	 */
	public List<Expense> getExpenses() {
		return expenses;
	}

	/**
	 * Adds a new user to the group, updating its active groups
	 * 
	 * @param user the user to be added
	 * @requires {@code !usersInGroup().contains(user)}
	 */
	public void addUser(User user) {
		users.add(user.getUsername());
		user.addGroup(this);
		debtGraph.put(user.getUsername(), new HashMap<String, Integer>());
	}

	/**
	 * Adds a new expense, with its value to be splited equally by every user
	 * 
	 * @param description the expense description
	 * @param userWhoPaid who paid for the expense
	 * @param value       how much the expense costs
	 * @requires {@code userWhoPaid != null && value > 0 && usersInGroup().contains(userWhoPaid)}
	 */
	public void addExpense(String description, User userWhoPaid, Integer value) {
		Expense newExpense = new Expense(description, userWhoPaid, value, usersInGroup());
		expenses.add(newExpense);
		Map<String, Integer> temp = debtGraph.get(userWhoPaid.getUsername()); // guarda os devedores do userWhoPaid

		for (String user : users) {
			temp.put(user, newExpense.getSplit(user));

			if (!user.equals(userWhoPaid.getUsername()) && debtGraph.get(user).get(userWhoPaid.getUsername()) != null) {
				if (debtGraph.get(userWhoPaid.getUsername()).containsKey(user) &&
						debtGraph.get(user).containsKey(userWhoPaid.getUsername())) {
					int debt = debtGraph.get(user).get(userWhoPaid.getUsername()); // dinheiro que o user que pagou deve

					if (debt > newExpense.getSplit(user)) { // o userWhoPaid esta a dever a user
						debtGraph.get(user).put(userWhoPaid.getUsername(), newExpense.getSplit(user) + debt);
					} else if (debt == newExpense.getSplit(user)) { // o userWhoPaid ja tinha divida com o user igual a													// despesa
						temp.remove(user, newExpense.getSplit(user));
					} else if (value > newExpense.getSplit(user)) { // o user ja estava a dever ao userWhoPaid
						temp.put(userWhoPaid.getUsername(), newExpense.getSplit(user) + debt);
					}
				}
				
				else
					debtGraph.put(userWhoPaid.getUsername(), temp);
			}
		}
		//debtGraph.put(userWhoPaid.getUsername(), temp);
	}
	
	/**
	 * Adds a new expense, with its value to be splited with an algorithm by every user
	 * 
	 * @param description the expense description
	 * @param userWhoPaid who paid for the expense
	 * @param value       how much the expense costs
	 * @param howToSplit  the proportions
	 * @requires {@code userWhoPaid != null && value > 0 && usersInGroup().contains(userWhoPaid)}
	 */
	public void addExpense(String description, User userWhoPaid, Integer value, List<Double> howToSplit) {
		Expense newExpense = new Expense(description, userWhoPaid, value, usersInGroup(), howToSplit);
		expenses.add(newExpense);

		Map<String, Integer> temp = debtGraph.get(userWhoPaid.getUsername());
		for (String user : users) {
			temp.put(user, newExpense.getSplit(user));
		}
	}

	/**
	 * Returns the balance of a given user.
	 * 
	 * @param user the user whose balance is to be calculated
	 * @return the balance of a given user.
	 * @requires {@code user!=null && usersInGroup().contains(user)}
	 */
	public Integer getBalance(User user) {
		int moneyPaid = 0;
		int debt = 0;
		Map<String, Integer> userDebtors = getUserDebtors(user);
		if (userDebtors != null) {
			for (String payer : userDebtors.keySet()) {
				moneyPaid += userDebtors.get(payer);
			}
		}
		Map<String, Integer> userDebts = getUserDebts(user);
		if (userDebts != null) {
			for (String debts : userDebts.keySet()) {
				debt += userDebts.get(debts);
			}
		}
		return moneyPaid - debt;
	}

	/**
	 * Returns the users who owe this user and how much
	 * 
	 * @param user given user
	 * @return map containing the debtors of this user
	 */
	public Map<String, Integer> getUserDebtors(User user) {
		return debtGraph.get(user.getUsername());
	}

	/**
	 * Returns those whose this user is in debt with and how much
	 * 
	 * @param user the given debtor
	 * @return map containing people who paid this user and how much user is oweing
	 *         them
	 */
	public Map<String, Integer> getUserDebts(User user) {
		Map<String, Integer> userDebts = new HashMap<>();
		for (String person : debtGraph.keySet()) {
			if (debtGraph.get(person).containsKey(user.getUsername())) {
				userDebts.put(person, debtGraph.get(person).get(user.getUsername()));
			}
		}
		return userDebts;
	}

	/**
	 * Removes all debts owed by the specified user from the debt graph.
	 * 
	 * @param userPayer the user whose debts will be settled
	 * @requires {@code user!=null && usersInGroup().contains(user)}
	 */
	public void settleUp(User userPayer) {
		for (String key : debtGraph.keySet()) {
			if (debtGraph.get(key).containsKey(userPayer.getUsername())) {
				debtGraph.get(key).remove(userPayer.getUsername());
			}
		}
	}
	
	/**
	 * Removes any debt owed by userPayer to userReceiver in the debt graph.
	 * 
	 * @param userPayer     the user who owes money to userReceiver
	 * @param userReceiver  the user who is owed money by userPayer
	 * @requires {@code userPayer != null && userReceiver != null && 
	 * !userPayer.equals(userReceiver) && usersInGroup().contains(userPayer) 
	 * && usersInGroup().contains(userReceiver)}
	 */
	public void settleUp(User userPayer, User userReceiver) {
		for (String key : debtGraph.keySet()) {
			if (key.equals(userReceiver.getUsername())) {
				if (debtGraph.get(key).containsKey(userPayer.getUsername())) {
					debtGraph.get(key).remove(userPayer.getUsername());
				}
			}
		}
	}

}
