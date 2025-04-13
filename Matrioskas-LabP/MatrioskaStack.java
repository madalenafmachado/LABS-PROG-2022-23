package project;

import backbone.ArrayStack;
import backbone.Stack;
import backbone.Utils;

/**
 * 
 * @author Madalena Machado fc59858
 *
 */
public class MatrioskaStack implements Stack<Matrioska> {

	private Stack<Matrioska> stack;
	private int weight;

	/**
	 * Creates a Matrioska Stack object
	 */
	public MatrioskaStack() {
		stack = new ArrayStack<Matrioska>();
		this.weight = 0;
	}

	/**
	 * Returns the weight value of the matrioskas stack
	 * 
	 * @return weight of the pile of matrioskas
	 */
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Tries to merge the current stack of matrioskas with the one received as a
	 * parameter, while checking if that is a possibility. In case thats possible,
	 * the matrioskas should be organized, along with the biggest matrioska on top
	 * of the stack
	 * 
	 * @param other stack of matrioskas
	 * @return true if it is possible to merge both stacks and false otherwise
	 * @requires {@code other != null}
	 */
	public boolean merge(MatrioskaStack other) {
		boolean doesMerge = true;
		MatrioskaStack temp = this.copy();
		MatrioskaStack tempOther = other.copy();
		MatrioskaStack matrioskas = new MatrioskaStack();
		
		while (!temp.isEmpty() && !tempOther.isEmpty() && doesMerge) {
			if(temp.peek().getSize() != tempOther.peek().getSize()) {
				if(temp.peek().getSize() > tempOther.peek().getSize()) {
					matrioskas.push(temp.peek());
					temp.pop();
				}
				else if(temp.peek().getSize() < tempOther.peek().getSize()) {
					matrioskas.push(tempOther.peek());
					tempOther.pop();
				}
			}
			else
				doesMerge = false;
		}
			if(doesMerge) {
				while(temp.isEmpty() && !tempOther.isEmpty()) {
						matrioskas.push(tempOther.peek());
						tempOther.pop();
				}
				while(!temp.isEmpty() && tempOther.isEmpty()) {
						matrioskas.push(temp.peek());
						temp.pop();					
				}
				this.stack = Utils.invertStack(matrioskas);
				this.weight = matrioskas.getWeight();
			}	
		return doesMerge;
	}
	
	@Override
	public MatrioskaStack copy() {
		MatrioskaStack tempCopy = new MatrioskaStack();
		tempCopy.weight = this.weight;
		tempCopy.stack = this.stack.copy();

		return tempCopy;
	}

	@Override
	public void push(Matrioska matrioska) {
		stack.push(matrioska);
		this.weight += (int) matrioska.getSize();
	}

	/**
	 * Takes off the last element in the stack
	 * 
	 * @requires !isEmpty()
	 */
	@Override
	public void pop() {
		this.weight -= (int) stack.peek().getSize();
		;
		stack.pop();
	}

	@Override
	public Matrioska peek() {
		return stack.peek();
	}

	@Override
	public boolean isEmpty() {
		return this.weight == 0;
	}

	/**
	 * 
	 * Retorna uma representacao de string do estado atual de uma instancia
	 * MatrioskaStack. A string inclui o peso de toda a MatrioskaStack e uma
	 * representação de string de cada Matrioska dentro da pilha, listados na ordem
	 * inversa a que foram adicionados à pilha.
	 * 
	 * @return uma representação de string do MatrioskaStack
	 * 
	 */
	@Override
	public String toString() {
		MatrioskaStack toPrint = this.copy();
		StringBuilder sb = new StringBuilder("      ");
		int i = 0;
		for (; !toPrint.isEmpty(); i++) {
			sb.append(Utils.peekPop(toPrint));
			sb.append("[");
		}
		sb.setLength(sb.length() - 1);
		for (; i > 1; i--)
			sb.append("]");
		sb.append(" w: ").append(getWeight());
		return sb.toString();
	}

}
