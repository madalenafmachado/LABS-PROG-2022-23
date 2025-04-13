package project;

import java.util.Arrays;

/**
 * 
 * @author fc59858 Madalena Machado
 *
 */
public class Box {

	public static final int CAPACITY = 5;
	private int weight;
	private int numMatrioskas;
	private MatrioskaStack[] content;

	/**
	 * Verifies if one box is heavier than another
	 * 
	 * @param peek 1st box
	 * @param box  box
	 * @return true if peek box is heavier than box and false otherwise
	 * @requires peek != null && box != null
	 */
	public static boolean heavierThan(Box peek, Box box) {
		return peek.getWeight() > box.getWeight();
	}
	
	/**
	 * Creates a new Box
	 */
	public Box() {
		this.weight = 0;
		this.numMatrioskas = 0;
		this.content = new MatrioskaStack[CAPACITY];

	}

	/**
	 * Returns the number of matrioskas stacks inside a box
	 * 
	 * @return number of stacks
	 */
	public int getNumMatrioskas() {
		return this.numMatrioskas;
	}

	/**
	 * Adds the given matrioska stack to the set of matrioska stacks
	 * 
	 * @param matrioska
	 * @requires {@code getNumMatrioskas() < Box.CAPACITY && matrioska != null}
	 */
	public void add(MatrioskaStack matrioska) {
		this.content[getNumMatrioskas()] = matrioska.copy();
		this.weight += this.content[getNumMatrioskas()].getWeight();
		this.numMatrioskas++;
	}
	
	/**
	 * Copies the given box
	 * 
	 * @return a copied box
	 */
	public Box copy() {
		Box copy = new Box();
		copy.weight = this.weight;
		copy.numMatrioskas = this.numMatrioskas;
		copy.content = Arrays.copyOf(content, CAPACITY);
		return copy;
	}

	/**
	 * Returns the matrioska stack in given position 
	 * 
	 * @param pos      given position inside the set of matrioska stacks
	 * @return given matrioska stack
	 * @requires {@code pos >= 1 && pos <= getNumMatrioskas()}
	 */
	public MatrioskaStack getMatrioska(int pos) {
		// MatrioskaStack[] copy = Arrays.copyOf(this.content, CAPACITY);
		return this.content[pos - 1].copy();
	}

	/**
	 * Removes the last matrioska stack on the box
	 * 
	 * @requires {@code getNumMatrioskas > 0}
	 */
	public void removeLast() {
		// MatrioskaStack[] copy = Arrays.copyOf(this.content, CAPACITY);
		this.weight -= this.content[getNumMatrioskas() - 1].getWeight();
		this.content[getNumMatrioskas() - 1] = null;
		this.numMatrioskas--;
	}
	 /**
	  * Verifies if the box is empty
	  * 
	  * @return true in case box is empty and false otherwise
	  */
	public boolean isEmpty() {
		return getNumMatrioskas() == 0 && getWeight() == 0;
	}
	
	/**
	 * Verifies if the box is full
	 * 
	 * @return true in case the box is full and false otherwise
	 */
	public boolean isFull() {
		return getNumMatrioskas() == CAPACITY;
	}
	
	/**
	 * Returns the weight of the box
	 * 
	 * @return box weight
	 */
	public int getWeight() {
		return this.weight;
	}

	/**
	 * Compacts the box to reduce the number of stacks
	 * 
	 * @requires {@code getNumMatrioskas() > 1}
	 */
	public void compact() {
		int n = 0;
		int m = 1;
		while(m < CAPACITY 
				&& this.content[m] != null ) {
			if(this.content[n].merge(this.content[m])) {
				for(int i = m; i < CAPACITY && this.content[i] != null; i++) {
					if(i == CAPACITY -1)
						this.content[i] = null;
					else
						this.content[i] = this.content[i+1];
				}
				if(m == CAPACITY-1) {
					n++;
					m = n+1;
				}
				this.numMatrioskas--;
			}
			else {
 				if(m == getNumMatrioskas()-1) {
					n++;
					m = n+1;
				}else
					m++;
			}
		}
		
	}

	/**
	 * Constroi uma representacao de string do atual da instancia de Box. A string
	 * começa com o peso da caixa e o numero de MatrioskaStack dentro da caixa, e
	 * entao inclui uma representacao de string de cada MatrioskasStack aninhada.
	 * 
	 * @return uma representacao textual da instancia de Box
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("   ↳BOX: w: ").append(getWeight());
		sb.append(" #itens: ").append(getNumMatrioskas()).append(System.lineSeparator());
		for (int i = 0; i < getNumMatrioskas(); i++) {
			sb.append(content[i].toString()).append(System.lineSeparator());
		}
		return sb.toString();
	}

}
